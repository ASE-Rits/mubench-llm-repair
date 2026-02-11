#!/bin/bash

INVARIANT_DIR="invariant"
OUTPUT_DIR="invariants_combined"

rm -rf "$OUTPUT_DIR"
mkdir -p "$OUTPUT_DIR"

# ターゲットクラスのセクションを抽出（パターン: project.case_num.variant.）
extract_target_sections() {
    local input_file=$1
    local project=$2
    local case_num=$3
    local variant=$4  # original, misuse, fixed
    
    # パターン例: calligraphy._2.fixed.
    pattern="${project}.${case_num}.${variant}."
    
    awk -v pat="$pattern" '
    BEGIN { print_section = 0; buffer = "" }
    /^={10,}$/ {
        if (print_section && buffer != "") print buffer
        buffer = $0 "\n"
        print_section = 0
        next
    }
    /:::/ {
        buffer = buffer $0 "\n"
        # クラス名部分を抽出（:::の前、(の前まで）
        split($0, parts, ":::")
        class_part = parts[1]
        # パラメータ部分を除去
        gsub(/\(.*/, "", class_part)
        # ターゲットクラスをチェック（Test_を除外、mocksクラス自体を除外）
        if (index(class_part, pat) > 0 && class_part !~ /Test_[0-9]/ && class_part !~ /\.mocks\./) {
            print_section = 1
        }
        next
    }
    {
        buffer = buffer $0 "\n"
    }
    END {
        if (print_section && buffer != "") print buffer
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
        
        # テスト名を取得
        test_class=$(find src/test/java -path "*${project}*" -name "*Test${case_num}.java" 2>/dev/null | head -1)
        test_name=$(basename "${test_class:-.}" .java)
        [[ "$test_name" == "." ]] && test_name="${project}Test${case_num}"
        
        output_file="$OUTPUT_DIR/${test_name}.txt"
        
        # 各バリアントの不変条件を抽出
        original_content=$(extract_target_sections "$original_file" "$project" "$case_num" "original")
        misuse_content=$(extract_target_sections "$misuse_file" "$project" "$case_num" "misuse")
        fixed_content=$(extract_target_sections "$fixed_file" "$project" "$case_num" "fixed")
        
        # 1つでも内容があれば出力
        if [[ -n "$original_content" || -n "$misuse_content" || -n "$fixed_content" ]]; then
            {
                echo "# ${test_name}"
                echo ""
                echo "## ORIGINAL"
                echo "$original_content"
                echo ""
                echo "## MISUSE"
                echo "$misuse_content"
                echo ""
                echo "## FIXED"
                echo "$fixed_content"
            } > "$output_file"
            echo "Created: $output_file"
        fi
    done
done

echo ""
echo "Done. Files: $(ls "$OUTPUT_DIR"/*.txt 2>/dev/null | wc -l)"
