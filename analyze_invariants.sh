#!/bin/bash

INVARIANT_DIR="invariant"
OUTPUT_DIR="analysis"

rm -rf "$OUTPUT_DIR"
mkdir -p "$OUTPUT_DIR"

# Fixedバージョンで失敗したテストケースのリスト
# ./gradlew testの結果を確認し、Fixedで失敗したケースをここに追加する
# 例: FAILED_FIXED_TESTS=("WordpressaTest_3" "CalligraphyTest_1")
FAILED_FIXED_TESTS=(
    "AdempiereTest_1"
    "AlibabaDruidTest_2"
    "CalligraphyTest_2"
    "Ivantrendafilov_confuciusTest_100"
    "Ivantrendafilov_confuciusTest_101"
    "Ivantrendafilov_confuciusTest_94"
    "Ivantrendafilov_confuciusTest_97"
    "JmrtdTest_2"
    "Thomas_s_b_visualeeTest_29"
    "Thomas_s_b_visualeeTest_30"
    "WordpressaTest_3"
)

# 失敗したテストケースをhash mapとして保存（高速検索用）
declare -A FAILED_TEST_MAP
for test in "${FAILED_FIXED_TESTS[@]}"; do
    FAILED_TEST_MAP["$test"]=1
done

extract_meaningful() {
    local input_file=$1
    local project=$2
    local case_num=$3
    local variant=$4
    
    pattern="${project}.${case_num}.${variant}."
    
    # Driverクラス用のパターン (variant部分なし)
    driver_pattern="${project}.${case_num}.Driver"
    
    awk -v pat="$pattern" -v driver_pat="$driver_pattern" '
    BEGIN { in_section = 0; header = "" }
    /^={10,}$/ {
        in_section = 0
        header = ""
        next
    }
    /:::/ {
        split($0, parts, ":::")
        class_part = parts[1]
        gsub(/\(.*/, "", class_part)
        # 対象クラス OR Driverクラス、ただしTestクラスとmocksは除外
        if ((index(class_part, pat) > 0 || index(class_part, driver_pat) > 0) && class_part !~ /Test_[0-9]/ && class_part !~ /\.mocks\./) {
            in_section = 1
            match($0, /[^.]+\.[^(]+/)
            method = substr($0, RSTART, RLENGTH)
            gsub(/.*\./, "", method)
            section_type = parts[2]
            header = method ":::" section_type
        }
        next
    }
    {
        if (in_section && header != "") {
            line = $0
            if (line == "") next
            
            # === 最小限のフィルタ（自明なノイズのみ除去） ===
            # Enum $VALUES配列関連（variant名を含むのでノイズ）
            if (line ~ /\$VALUES/) next
            # 型名リスト（variant名を含むのでノイズ）
            if (line ~ /\.getClass\(\)\.getName\(\) == \[/) next
            # variant固有の完全修飾クラス名（比較時に差分として出てしまう）
            if (line ~ /\.(original|misuse|fixed)\./) next
            
            # セクションヘッダと内容を1行にまとめて出力（差分比較のため）
            gsub(/^[ \t]+/, "", line)  # 先頭空白を除去
            print header " | " line
        }
    }
    ' "$input_file"
}

# 差分を計算する関数
compute_diff() {
    local content1="$1"
    local content2="$2"
    local label1="$3"
    local label2="$4"
    
    diff_result=$(diff <(echo "$content1" | sort) <(echo "$content2" | sort) 2>/dev/null)
    
    if [[ -n "$diff_result" ]]; then
        echo "### ${label1} → ${label2}"
        echo '```diff'
        echo "$diff_result" | sed 's/^< /- /; s/^> /+ /'
        echo '```'
        echo ""
    fi
}

for project_dir in "$INVARIANT_DIR"/*/; do
    project=$(basename "$project_dir")
    
    for case_dir in "$project_dir"/*/; do
        case_num=$(basename "$case_dir")
        
        original_file="$case_dir/original/invariants.txt"
        misuse_file="$case_dir/misuse/invariants.txt"
        fixed_file="$case_dir/fixed/invariants.txt"
        
        [[ ! -f "$original_file" || ! -f "$misuse_file" || ! -f "$fixed_file" ]] && continue
        
        test_class=$(find src/test/java -path "*${project}*" -name "*Test${case_num}.java" 2>/dev/null | head -1)
        test_name=$(basename "${test_class:-.}" .java)
        [[ "$test_name" == "." ]] && test_name="${project}Test${case_num}"
        
        original_content=$(extract_meaningful "$original_file" "$project" "$case_num" "original")
        misuse_content=$(extract_meaningful "$misuse_file" "$project" "$case_num" "misuse")
        fixed_content=$(extract_meaningful "$fixed_file" "$project" "$case_num" "fixed")
        
        # 差分があるか確認
        diff_om=$(diff <(echo "$original_content" | sort) <(echo "$misuse_content" | sort) 2>/dev/null)
        diff_mo=$(diff <(echo "$misuse_content" | sort) <(echo "$original_content" | sort) 2>/dev/null)
        diff_mf=$(diff <(echo "$misuse_content" | sort) <(echo "$fixed_content" | sort) 2>/dev/null)
        diff_of=$(diff <(echo "$original_content" | sort) <(echo "$fixed_content" | sort) 2>/dev/null)
        
        if [[ -n "$diff_om" || -n "$diff_mf" || -n "$diff_of" ]]; then
            output_file="$OUTPUT_DIR/${test_name}.md"
            {
                # Fixedで失敗しているかチェックして警告を追加
                if [[ -n "${FAILED_TEST_MAP[$test_name]}" ]]; then
                    echo "⚠️ **WARNING: Fixed version failed tests** - 修正が失敗しています"
                    echo ""
                fi
                echo "# ${test_name}"
                echo ""
                
                # MISUSE vs ORIGINAL（正しい修正）
                if [[ -n "$diff_mo" ]]; then
                    echo "## MISUSE → ORIGINAL（正しい修正）"
                    echo '```diff'
                    echo "$diff_mo" | sed 's/^< /- /; s/^> /+ /; s/^[0-9].*$/---/'
                    echo '```'
                    echo ""
                fi
                
                # MISUSE vs FIXED（LLM修正による変化）
                if [[ -n "$diff_mf" ]]; then
                    echo "## MISUSE → FIXED（LLM修正による変化）"
                    echo '```diff'
                    echo "$diff_mf" | sed 's/^< /- /; s/^> /+ /; s/^[0-9].*$/---/'
                    echo '```'
                    echo ""
                fi
                
                # ORIGINAL vs FIXED（修正の妥当性）
                if [[ -n "$diff_of" ]]; then
                    echo "## ORIGINAL vs FIXED（残存する差分）"
                    echo '```diff'
                    echo "$diff_of" | sed 's/^< /- /; s/^> /+ /; s/^[0-9].*$/---/'
                    echo '```'
                    echo ""
                else
                    echo "## ✅ ORIGINAL == FIXED（完全一致）"
                    echo ""
                fi
            } > "$output_file"
            echo "Created: $output_file"
        fi
    done
done

echo ""
echo "Done. Files: $(ls "$OUTPUT_DIR"/*.md 2>/dev/null | wc -l)"
