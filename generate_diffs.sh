#!/bin/bash

# Diff生成スクリプト
# 各プロジェクトのoriginal/misuse/fixedの3つのバリアント間でdiffを生成

SRC_DIR="src/main/java"
DIFF_DIR="diffs"

# diffsディレクトリをクリーンアップして再作成
rm -rf "$DIFF_DIR"
mkdir -p "$DIFF_DIR"

echo "Generating diffs for all projects..."

# 全プロジェクトディレクトリを取得（CommonCasesを除く）
for project_dir in $(find "$SRC_DIR" -mindepth 1 -maxdepth 1 -type d | sort); do
    project=$(basename "$project_dir")
    
    # CommonCasesはスキップ
    if [[ "$project" == *"CommonCases"* ]]; then
        continue
    fi
    
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
        
        original_dir="$case_dir/original"
        misuse_dir="$case_dir/misuse"
        fixed_dir="$case_dir/fixed"
        
        # 3つのバリアントが全て存在するかチェック
        if [[ ! -d "$original_dir" ]] || [[ ! -d "$misuse_dir" ]] || [[ ! -d "$fixed_dir" ]]; then
            echo "  [SKIP] $project/$case_num (missing variants)"
            continue
        fi
        
        echo "  Processing: $project/$case_num"
        
        # ファイル名プレフィックス
        prefix="${camel_name}Test${case_num}.${camel_name}Test${case_num}"
        
        # 1. original vs misuse
        diff_file="$DIFF_DIR/${prefix}_original_vs_misuse.diff"
        diff -r -u "$original_dir" "$misuse_dir" > "$diff_file" 2>/dev/null || true
        echo "    Created: ${prefix}_original_vs_misuse.diff"
        
        # 2. misuse vs fixed
        diff_file="$DIFF_DIR/${prefix}_misuse_vs_fixed.diff"
        diff -r -u "$misuse_dir" "$fixed_dir" > "$diff_file" 2>/dev/null || true
        echo "    Created: ${prefix}_misuse_vs_fixed.diff"
        
        # 3. original vs fixed
        diff_file="$DIFF_DIR/${prefix}_original_vs_fixed.diff"
        diff -r -u "$original_dir" "$fixed_dir" > "$diff_file" 2>/dev/null || true
        echo "    Created: ${prefix}_original_vs_fixed.diff"
    done
done

echo ""
echo "=========================================="
echo "Diff generation complete!"
echo "Total files: $(find "$DIFF_DIR" -name "*.diff" | wc -l)"
echo "Output directory: $DIFF_DIR"
echo "=========================================="
