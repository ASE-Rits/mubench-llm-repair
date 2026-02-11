#!/bin/bash

# Daikon不変条件抽出スクリプト
# 対象: 動的テストのみ（静的テストはスキップ）
# JUnitテストをChicoryで計測してテストクラスの不変条件も取得
# toString()関連の行は自動的に除外される
#
# Usage: ./run_daikon.sh [project case_num variant]
#   No arguments: process all projects
#   With arguments: process single variant

set -e

# ============================================
# 静的テストリスト（スキップ対象）
# Daikonで有意な不変条件が得られないため除外
# ============================================
STATIC_TESTS=(
    "adempiere/_1"
    "adempiere/_2"
    "alibaba_druid/_1"
    "alibaba_druid/_2"
    "android_rcs_rcsjta/_1"
    "androiduil/_1"
    "asterisk_java/_194"
    "jmrtd/_1"
    "jmrtd/_2"
    "logblock_logblock_2/_15"
    "mqtt/_389"
    "onosendai/_1"
    "pawotag/_1"
    "rhino/_1"
    "wordpressa/_3"
    "testng/_17"
    "testng/_18"
    "testng/_21"
    "testng/_22"
)

# ============================================
# 問題のあるテストリスト（スキップ対象）
# タイムアウト、無限ループ等の問題があるため除外
# ============================================
SKIP_TESTS=(
    "testng/_16"  # 並行テストのため実行が終わらない
)

# 静的テストかどうかをチェック
is_static_test() {
    local project=$1
    local case_num=$2
    local key="${project}/${case_num}"
    for static in "${STATIC_TESTS[@]}"; do
        if [[ "$static" == "$key" ]]; then
            return 0  # true
        fi
    done
    return 1  # false
}

# スキップ対象かどうかをチェック
should_skip() {
    local project=$1
    local case_num=$2
    local key="${project}/${case_num}"
    for skip in "${SKIP_TESTS[@]}"; do
        if [[ "$skip" == "$key" ]]; then
            return 0  # true
        fi
    done
    return 1  # false
}

DAIKONDIR=${DAIKONDIR:-/home/moriwaki-y/tools/daikon-5.8.22}
PROJECT_ROOT=$(pwd)
BUILD_DIR="$PROJECT_ROOT/build/classes/java/main"
TEST_BUILD_DIR="$PROJECT_ROOT/build/classes/java/test"
INVARIANT_DIR="$PROJECT_ROOT/invariant"

LIBS_DIR="$PROJECT_ROOT/libs"
JUNIT_JAR="$LIBS_DIR/junit-4.13.2.jar"
HAMCREST_JAR="$LIBS_DIR/hamcrest-core-1.3.jar"
CLASSPATH="$BUILD_DIR:$TEST_BUILD_DIR:$DAIKONDIR/daikon.jar:$LIBS_DIR/*:$JUNIT_JAR:$HAMCREST_JAR"

# JDK 17を使用（JDK21よりDaikonとの互換性が良い）
JAVA_CMD="${JAVA_17:-/usr/lib/jvm/java-17-openjdk-amd64/bin/java}"

# JDK 9+用の--add-opensオプション（Daikonが動作に必要）
JAVA_OPTS="--add-opens java.base/java.lang=ALL-UNNAMED \
    --add-opens java.base/java.lang.reflect=ALL-UNNAMED \
    --add-opens java.base/java.util=ALL-UNNAMED \
    --add-opens java.base/java.io=ALL-UNNAMED"

# プロジェクトとケースのリストを取得
get_projects() {
    ls -d "$BUILD_DIR"/*/ 2>/dev/null | xargs -n1 basename | grep -v daikon_runner
}

get_cases() {
    local project=$1
    ls -d "$BUILD_DIR/$project"/_*/ 2>/dev/null | xargs -n1 basename
}

# テストクラスの完全修飾名を実際のファイルから取得
get_full_test_class() {
    local project=$1
    local case_num=$2
    local test_dir="$PROJECT_ROOT/src/test/java/$project"
    
    # テストファイルを検索（例: *Test_1.java, *Test_56_1.java）
    local test_file=$(find "$test_dir" -maxdepth 1 -name "*Test${case_num}.java" 2>/dev/null | head -1)
    
    if [[ -n "$test_file" ]]; then
        # ファイルからパッケージ宣言を読み取る
        local package_name=$(grep "^package " "$test_file" | sed 's/package //; s/;//')
        local class_name=$(basename "$test_file" .java)
        echo "${package_name}.${class_name}"
    else
        # フォールバック: プロジェクト名.キャメルケースクラス名
        local result=""
        IFS='_' read -ra parts <<< "$project"
        for part in "${parts[@]}"; do
            result+="${part^}"
        done
        echo "${project}.${result}Test${case_num}"
    fi
}

# Daikonを実行する関数（JUnitテストを計測）
run_daikon_for_variant() {
    local project=$1
    local case_num=$2
    local variant=$3  # original, misuse, fixed
    
    local class_dir="$BUILD_DIR/$project/$case_num/$variant"
    local output_dir="$INVARIANT_DIR/$project/$case_num/$variant"
    
    # クラスディレクトリが存在しない場合はスキップ
    if [[ ! -d "$class_dir" ]]; then
        echo "  [SKIP] $variant: ディレクトリなし"
        return 0
    fi
    
    # 出力ディレクトリ作成
    mkdir -p "$output_dir"
    
    # テストクラスの完全修飾名を実際のファイルから取得
    local full_class=$(get_full_test_class "$project" "$case_num")
    local variant_class="${variant^}"  # original -> Original
    local test_class="${full_class}\$${variant_class}"
    
    # 計測対象パターン: 全てを計測（標準ライブラリ/jdkを除外）
    # テストクラスとドライバークラス両方を含める
    local ppt_omit_pattern="java\\..*|javax\\..*|org\\..*|sun\\..*|jdk\\..*|com\\.sun\\..*"
    
    echo "  [$variant] Processing..."
    echo "    Test class: $test_class"
    
    # 作業ディレクトリをプロジェクトルートに変更（ソースファイルパスを解決するため）
    pushd "$PROJECT_ROOT" > /dev/null
    
    # Step 1: DynComp (比較可能性分析) - 標準ライブラリを除外
    echo "    Step 1: DynComp..."
    $JAVA_CMD $JAVA_OPTS -cp "$CLASSPATH" daikon.DynComp \
        --ppt-omit-pattern="$ppt_omit_pattern" \
        --output-dir="$output_dir" \
        org.junit.runner.JUnitCore "$test_class" 2>&1 | head -5 || true
    
    local decls_file=$(ls "$output_dir"/*.decls-DynComp 2>/dev/null | head -1)
    
    # Step 2: Chicory (トレース取得) - 標準ライブラリを除外
    echo "    Step 2: Chicory (trace)..."
    if [[ -f "$decls_file" ]]; then
        $JAVA_CMD $JAVA_OPTS -cp "$CLASSPATH" daikon.Chicory \
            --ppt-omit-pattern="$ppt_omit_pattern" \
            --comparability-file="$decls_file" \
            --output-dir="$output_dir" \
            org.junit.runner.JUnitCore "$test_class" 2>&1 | head -5 || true
    else
        $JAVA_CMD $JAVA_OPTS -cp "$CLASSPATH" daikon.Chicory \
            --ppt-omit-pattern="$ppt_omit_pattern" \
            --output-dir="$output_dir" \
            org.junit.runner.JUnitCore "$test_class" 2>&1 | head -5 || true
    fi
    
    local dtrace_file=$(ls "$output_dir"/*.dtrace.gz 2>/dev/null | head -1)
    
    # Step 3: Daikon + --suppress_redundant (不変条件推論)
    if [[ -f "$dtrace_file" ]]; then
        echo "    Step 3: Daikon --suppress_redundant..."
        $JAVA_CMD -cp "$DAIKONDIR/daikon.jar" daikon.Daikon \
            --suppress_redundant \
            -o "$output_dir/invariants.inv.gz" \
            "$dtrace_file" 2>&1 | tail -3 || true
    else
        echo "    [WARN] No dtrace file generated"
        popd > /dev/null
        return 0
    fi
    
    popd > /dev/null
    
    # Step 4: inv.gz を txt に変換（toString()関連の行を除外）
    local inv_file="$output_dir/invariants.inv.gz"
    if [[ -f "$inv_file" ]]; then
        echo "    Step 4: PrintInvariants -> txt..."
        $JAVA_CMD -cp "$DAIKONDIR/daikon.jar" daikon.PrintInvariants "$inv_file" 2>/dev/null \
            | grep -v "\.toString" \
            | grep -v "\.class\$" \
            > "$output_dir/invariants.txt" || true
        echo "    [OK] Generated: $output_dir/invariants.txt"
    else
        echo "    [WARN] No invariants generated"
    fi
}

# メイン処理
main() {
    echo "=========================================="
    echo "Daikon Invariant Extraction (Dynamic Tests Only)"
    echo "=========================================="
    echo "DAIKONDIR: $DAIKONDIR"
    echo "BUILD_DIR: $BUILD_DIR"
    echo "OUTPUT_DIR: $INVARIANT_DIR"
    echo "Skipping ${#STATIC_TESTS[@]} static tests, ${#SKIP_TESTS[@]} problematic tests"
    echo ""
    
    # invariantディレクトリをクリア
    rm -rf "$INVARIANT_DIR"
    mkdir -p "$INVARIANT_DIR"
    
    # 各プロジェクトを処理
    for project in $(get_projects); do
        echo ""
        echo "=== Project: $project ==="
        
        for case_num in $(get_cases "$project"); do
            # 静的テストはスキップ
            if is_static_test "$project" "$case_num"; then
                echo "  [SKIP] $case_num (static test)"
                continue
            fi
            
            # 問題のあるテストはスキップ
            if should_skip "$project" "$case_num"; then
                echo "  [SKIP] $case_num (problematic test)"
                continue
            fi
            
            echo ""
            echo "--- Case: $case_num ---"
            
            # original, misuse, fixed の3バリアントを処理
            for variant in original misuse fixed; do
                run_daikon_for_variant "$project" "$case_num" "$variant"
            done
        done
    done
    
    echo ""
    echo "=========================================="
    echo "Complete! Results in: $INVARIANT_DIR"
    echo "=========================================="
}

# 単一プロジェクト/ケース/バリアントのみ実行（テスト用）
if [[ $# -eq 3 ]]; then
    project=$1
    case_num=$2
    variant=$3
    
    # 静的テストの場合は警告
    if is_static_test "$project" "$case_num"; then
        echo "[WARN] $project/$case_num is a static test (Daikon may not produce useful invariants)"
    fi
    
    # 問題のあるテストの場合は警告
    if should_skip "$project" "$case_num"; then
        echo "[WARN] $project/$case_num is a problematic test (may hang or timeout)"
    fi
    
    mkdir -p "$INVARIANT_DIR"
    run_daikon_for_variant "$project" "$case_num" "$variant"
elif [[ $# -eq 0 ]]; then
    main
else
    echo "Usage: $0 [project case_num variant]"
    echo "  No arguments: process all dynamic tests"
    echo "  With arguments: process single variant"
    exit 1
fi
