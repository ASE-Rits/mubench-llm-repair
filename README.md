# mubench-llm-repair

MuBench ベンチマークデータセットを用いた Java API 誤用パターンの検証・解析基盤です。

**作成元**: ASE Lab at Ritsumeikan University

## 概要

このリポジトリは、MuBench（API誤用検出ベンチマーク）由来のコードを対象に、バグ検出・修正の研究を支援します。各ケースは original（元コード）/ misuse（バグあり）/ fixed（修正済み）の3バリアントで構成され、JUnit 5 テストで検証可能です。

## 要件

- Java 17 以上（Gradle Toolchain で自動取得）
- macOS / Linux / Windows（WSL）

## クイックスタート

```bash
# リポジトリ取得
git clone https://github.com/ASE-Rits/mubench-llm-repair.git
cd mubench-llm-repair

# ビルドとテスト実行
./gradlew build

# ビルドに失敗した場合（依存JARを手動取得）
./gradlew dependencies --configuration compileClasspath
# または Maven Central から直接取得:
# https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar

# テストのみ実行
./gradlew test

# テスト結果を確認（build/reports/tests/test/index.html）
```

## ディレクトリ構成

```
.
├── datasets/                 # MuBench メタデータ（出典から変更なし）
│   └── <project>/
│       └── <case>/
│           ├── misuse.yml    # バグ情報
│           ├── original.java # 元コード
│           ├── misuse.java   # バグあり
│           └── fix.java      # 正解修正
├── src/
│   ├── main/java/            # テスト対象コード（※1）
│   │   └── <project>/
│   │       └── _<case>/
│   │           ├── original/          # 実際に修正が行われた修正後のコード
│   │           ├── misuse/            # 誤用（バグを含むコード）
│   │           ├── fixed/             # LLM によって修正されたコード
│   │           ├── requirements/ # （※2）
│   │           ├── mocks/        # （※3）
│   │           └── Driver.java   # バリアント切替用（※4）
│   └── test/java/            # JUnit 5 テストスイート
├── libs/                     # 依存ライブラリ JAR（※5）
├── docs/                     # ドキュメント
│   ├── ARCHITECTURE.md       # プロジェクト構造
│   ├── DATASETS.md           # データセット一覧
│   ├── DEPENDENCIES.md       # 依存ライブラリ一覧
│   ├── TESTING.md            # テスト
│   └── BINARY_EXPORT.md      # バイナリエクスポート
├── build.gradle.kts          # Gradle 設定
└── gradlew / gradlew.bat     # Gradle Wrapper
```

### 注記

- **※1 `src/main/java/`**: datasets/ のコードをビルド可能にするため、パッケージ宣言・インポート文・public クラス修飾子のみ修正しています。ロジックは変更していません。
- **※2 `requirements/`**: 元プロジェクトから依存クラスを取得し、同様にパッケージ宣言・インポート文・public クラス修飾子のみ修正しています。すべてのケースに存在するわけではありません。
- **※3 `mocks/`**: ビルドを通すためのダミー実装や簡易実装です。すべてのケースに存在するわけではありません。
- **※4 `Driver.java`**: リフレクションを使用して original/misuse/fixed バリアントを切り替えます。テストクラスから呼び出されます。
- **※5 `libs/`**: 依存ライブラリの JAR ファイルを格納しています。一般的に JAR ファイルをリポジトリに含めることはアンチパターンとされていますが、本プロジェクトはベンチマーク**データセット**としての永続性・再現性を重視し、外部リポジトリ（Maven Central 等）の将来的な変更や廃止に依存しない形で配布するため、あえて JAR ファイルを直接管理しています。詳細は [docs/DEPENDENCIES.md](docs/DEPENDENCIES.md) を参照してください。

## ドキュメント

| ドキュメント | 説明 |
|-------------|------|
| [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) | プロジェクト構造 |
| [docs/DATASETS.md](docs/DATASETS.md) | データセット一覧 |
| [docs/DEPENDENCIES.md](docs/DEPENDENCIES.md) | 依存ライブラリ一覧 |
| [docs/TESTING.md](docs/TESTING.md) | テスト |
| [docs/TEST_CLASSIFICATION.md](docs/TEST_CLASSIFICATION.md) | テストコード分類一覧 |
| [docs/LLM_TASK.md](docs/LLM_TASK.md) | LLMタスク |
| [docs/CONCERNS.md](docs/CONCERNS.md) | 懸念事項 |
| [docs/BINARY_EXPORT.md](docs/BINARY_EXPORT.md) | バイナリエクスポート |

## バイナリエクスポート

クラスファイルをエクスポートできます。

```bash
# 全エクスポート（推奨）
./gradlew exportBinaries
```

詳細は [docs/BINARY_EXPORT.md](docs/BINARY_EXPORT.md) を参照してください。

## Daikon 不変条件抽出

[Daikon](https://plse.cs.washington.edu/daikon/) を使用して動的テストから不変条件を抽出できます。

### 前提条件

- Daikon 5.8.22 以上
- JDK 17（JDK 21 は Daikon との互換性問題あり）
- 環境変数 `DAIKONDIR` を設定（デフォルト: `/home/moriwaki-y/tools/daikon-5.8.22`）

### 実行方法

```bash
# ビルド（必須）
./gradlew build

# 全動的テストの不変条件を抽出（静的テストは自動スキップ）
./run_daikon.sh

# 特定のプロジェクト/ケース/バリアントのみ実行
./run_daikon.sh apache_gora _56_1 original
./run_daikon.sh testng _16 misuse

# 結果は invariant/ ディレクトリに出力
```

### Diff 生成

```bash
# 全プロジェクトの original/misuse/fixed 間の diff を生成
./generate_diffs.sh

# 結果は diffs/ ディレクトリに出力
# 例: diffs/ApacheGoraTest_56_1.ApacheGoraTest_56_1_original_vs_misuse.diff
```

### 注意事項

- 静的テスト（ソースコード解析のみ）は Daikon で有意な不変条件が得られないため、自動的にスキップされます
- 静的/動的テストの分類は [docs/TEST_CLASSIFICATION.md](docs/TEST_CLASSIFICATION.md) を参照
- toString() 関連の不変条件はノイズとして自動フィルタリングされます

## ライセンス

各データセットは元プロジェクトのライセンスに従います。
