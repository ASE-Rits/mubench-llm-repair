#!/bin/bash

# Daikon不変条件抽出スクリプト
# 対象: original, misuse, fixed (CommonCasesは除外)

set -e

DAIKONDIR=${DAIKONDIR:-/home/moriwaki-y/tools/daikon-5.8.22}
PROJECT_ROOT=$(pwd)
BUILD_DIR="$PROJECT_ROOT/build/classes/java/main"
TEST_BUILD_DIR="$PROJECT_ROOT/build/classes/java/test"
INVARIANT_DIR="$PROJECT_ROOT/invariant"
LIBS_DIR="$PROJECT_ROOT/libs"
CLASSPATH="$BUILD_DIR:$TEST_BUILD_DIR:$DAIKONDIR/daikon.jar:$LIBS_DIR/*"

# プロジェクトとケースのリストを取得
get_projects() {
    ls -d "$BUILD_DIR"/*/ 2>/dev/null | xargs -n1 basename | grep -v daikon_runner
}

get_cases() {
    local project=$1
    ls -d "$BUILD_DIR/$project"/_*/ 2>/dev/null | xargs -n1 basename
}

# Daikonを実行する関数
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
    
    # DaikonRunnerを使用
    local runner_class="daikon_runner.DaikonRunner"
    local project_case="${project}.${case_num}"
    
    echo "  [$variant] Processing..."
    
    # 作業ディレクトリを変更
    pushd "$BUILD_DIR" > /dev/null
    
    # Step 1: DynComp (比較可能性分析)
    echo "    Step 1: DynComp..."
    java -cp "$CLASSPATH" daikon.DynComp \
        --output-dir="$output_dir" \
        "$runner_class" "$project_case" "$variant" 2>&1 | head -3 || true
    
    local decls_file=$(ls "$output_dir"/*.decls-DynComp 2>/dev/null | head -1)
    
    # Step 2: Chicory (トレース取得)
    echo "    Step 2: Chicory (trace)..."
    if [[ -f "$decls_file" ]]; then
        java -cp "$CLASSPATH" daikon.Chicory \
            --comparability-file="$decls_file" \
            --output-dir="$output_dir" \
            "$runner_class" "$project_case" "$variant" 2>&1 | head -3 || true
    else
        java -cp "$CLASSPATH" daikon.Chicory \
            --output-dir="$output_dir" \
            "$runner_class" "$project_case" "$variant" 2>&1 | head -3 || true
    fi
    
    local dtrace_file=$(ls "$output_dir"/*.dtrace.gz 2>/dev/null | head -1)
    
    # Step 3: Daikon + --suppress_redundant (不変条件推論)
    if [[ -f "$dtrace_file" ]]; then
        echo "    Step 3: Daikon --suppress_redundant..."
        java -cp "$DAIKONDIR/daikon.jar" daikon.Daikon \
            --suppress_redundant \
            -o "$output_dir/invariants.inv.gz" \
            "$dtrace_file" 2>&1 | tail -3 || true
    else
        echo "    [WARN] No dtrace file generated"
        popd > /dev/null
        return 0
    fi
    
    popd > /dev/null
    
    # Step 4: inv.gz を txt に変換
    local inv_file="$output_dir/invariants.inv.gz"
    if [[ -f "$inv_file" ]]; then
        echo "    Step 4: PrintInvariants -> txt..."
        java -cp "$DAIKONDIR/daikon.jar" daikon.PrintInvariants "$inv_file" > "$output_dir/invariants.txt" 2>/dev/null || true
        echo "    [OK] Generated: $output_dir/invariants.txt"
    else
        echo "    [WARN] No invariants generated"
    fi
}

# メイン処理
main() {
    echo "=========================================="
    echo "Daikon Invariant Extraction"
    echo "=========================================="
    echo "DAIKONDIR: $DAIKONDIR"
    echo "BUILD_DIR: $BUILD_DIR"
    echo "OUTPUT_DIR: $INVARIANT_DIR"
    echo ""
    
    # invariantディレクトリをクリア
    rm -rf "$INVARIANT_DIR"
    mkdir -p "$INVARIANT_DIR"
    
    # 各プロジェクトを処理
    for project in $(get_projects); do
        echo ""
        echo "=== Project: $project ==="
        
        for case_num in $(get_cases "$project"); do
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
    mkdir -p "$INVARIANT_DIR"
    run_daikon_for_variant "$project" "$case_num" "$variant"
elif [[ $# -eq 0 ]]; then
    main
else
    echo "Usage: $0 [project case_num variant]"
    echo "  No arguments: process all projects"
    echo "  With arguments: process single variant"
    exit 1
fi
