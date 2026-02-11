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
    
    awk -v pat="$pattern" '
    BEGIN { in_section = 0; header = ""; content = "" }
    /^={10,}$/ {
        if (in_section && content != "") {
            print header
            print content
        }
        in_section = 0
        header = ""
        content = ""
        next
    }
    /:::/ {
        split($0, parts, ":::")
        class_part = parts[1]
        gsub(/\(.*/, "", class_part)
        if (index(class_part, pat) > 0 && class_part !~ /Test_[0-9]/ && class_part !~ /\.mocks\./) {
            in_section = 1
            match($0, /[^.]+\.[^(]+/)
            method = substr($0, RSTART, RLENGTH)
            gsub(/.*\./, "", method)
            type = parts[2]
            header = method ":::" type
        }
        next
    }
    {
        if (in_section) {
            line = $0
            if (line == "") next
            # ノイズをフィルタリング
            if (line ~ /has only one value/ && line !~ /^return/) next
            if (line ~ /orig\(this\)/) next
            if (line ~ /\.resources ==/) next
            if (line ~ /\.resources has/) next
            if (line ~ /^this ==/) next
            if (line ~ /^this\./) next
            # 静的フィールド == orig() パターンを除外（定数の不変性）
            if (line ~ /\.[A-Z_]+ == orig\(/) next
            if (line ~ /\.getClass\(\)\.getName\(\) == orig\(/) next
            # 意味のある行のみ追加
            content = content "  " line "\n"
        }
    }
    END {
        if (in_section && content != "") {
            print header
            print content
        }
    }
    ' "$input_file"
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
        
        if [[ -n "$original_content" || -n "$misuse_content" || -n "$fixed_content" ]]; then
            output_file="$OUTPUT_DIR/${test_name}.md"
            {
                # Fixedで失敗しているかチェックして警告を追加
                if [[ -n "${FAILED_TEST_MAP[$test_name]}" ]]; then
                    echo "⚠️ **WARNING: Fixed version failed tests** - 修正が失敗しています"
                    echo ""
                fi
                echo "# ${test_name}"
                echo ""
                echo "## ORIGINAL"
                echo '```'
                echo "$original_content"
                echo '```'
                echo ""
                echo "## MISUSE"  
                echo '```'
                echo "$misuse_content"
                echo '```'
                echo ""
                echo "## FIXED"
                echo '```'
                echo "$fixed_content"
                echo '```'
            } > "$output_file"
            echo "Created: $output_file"
        fi
    done
done

echo ""
echo "Done. Files: $(ls "$OUTPUT_DIR"/*.md 2>/dev/null | wc -l)"
