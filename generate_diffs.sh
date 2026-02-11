#!/bin/bash

# Diff生成スクリプト
# 各プロジェクトのoriginal/misuse/fixedの3つのバリアント間でDaikon不変条件のdiffを生成
#
# Usage: ./generate_diffs.sh

INVARIANT_DIR="invariant"
DIFF_DIR="diffs"

# diffsディレクトリをクリーンアップして再作成
rm -rf "$DIFF_DIR"
mkdir -p "$DIFF_DIR"

echo "Generating invariant diffs for all projects..."
echo "Input: $INVARIANT_DIR -> Output: $DIFF_DIR"
echo ""

# 全プロジェクトディレクトリを取得（invariantディレクトリから）
for project_dir in $(find "$INVARIANT_DIR" -mindepth 1 -maxdepth 1 -type d | sort); do
    project=$(basename "$project_dir")
    
    # プロジェクト名をキャメルケースに変換（例: android_rcs_rcsjta -> AndroidRcsRcsjta）
    IFS='_' read -ra PARTS <<< "$project"
    camel_name=""
    for part in "${PARTS[@]}"; do
        # 最初の文字を大文字に、残りはそのまま
        camel_name+="${part^}"
    done
    
    # 各ケースディレクトリを処理
    for case_dir in $(find "$project_dir" -mindepth 1 -maxdepth 1 -type d | sort); do
        case_num=$(basename "$case_dir")
        
        original_inv="$case_dir/original/invariants.txt"
        misuse_inv="$case_dir/misuse/invariants.txt"
        fixed_inv="$case_dir/fixed/invariants.txt"
        
        # 3つのバリアントのinvariants.txtが全て存在するかチェック
        if [[ ! -f "$original_inv" ]] || [[ ! -f "$misuse_inv" ]] || [[ ! -f "$fixed_inv" ]]; then
            echo "  [SKIP] $project/$case_num (missing invariants.txt)"
            continue
        fi
        
        echo "  Processing: $project/$case_num"
        
        # ファイル名プレフィックス
        prefix="${camel_name}Test${case_num}.${camel_name}Test${case_num}"
        
        # 1. original vs misuse
        diff_file="$DIFF_DIR/${prefix}_original_vs_misuse.diff"
        diff -u "$original_inv" "$misuse_inv" > "$diff_file" 2>/dev/null || true
        echo "    Created: ${prefix}_original_vs_misuse.diff"
        
        # 2. misuse vs fixed
        diff_file="$DIFF_DIR/${prefix}_misuse_vs_fixed.diff"
        diff -u "$misuse_inv" "$fixed_inv" > "$diff_file" 2>/dev/null || true
        echo "    Created: ${prefix}_misuse_vs_fixed.diff"
        
        # 3. original vs fixed
        diff_file="$DIFF_DIR/${prefix}_original_vs_fixed.diff"
        diff -u "$original_inv" "$fixed_inv" > "$diff_file" 2>/dev/null || true
        echo "    Created: ${prefix}_original_vs_fixed.diff"
    done
done

echo ""
echo "=========================================="
echo "Invariant diff generation complete!"
echo "Total files: $(find "$DIFF_DIR" -name "*.diff" | wc -l)"
echo "Output directory: $DIFF_DIR"
echo "=========================================="
