# Invariant分析による修正結果の質的比較

## 概要

本レポートは、LLM修正の3つのカテゴリ（完全一致、軽微な差分あり、修正失敗）について、Daikonで抽出したinvariantの差分を**質的に比較**し、各カテゴリの特徴を明らかにする。また、**量的分析への発展可能性**を考察する。

---

## 1. カテゴリ別質的分析

### 1.1 完全一致（ORIGINAL == FIXED）: 7件（46.7%）

| ケース | 修正内容 | invariant変化の特徴 |
|---|---|---|
| ApacheGoraTest_56_1 | バイト配列処理ロジック | 条件分岐の簡素化、ヘルパーメソッド削減 |
| LnreaderaTest_1/2 | `super.onDestroy()`追加 | 真偽値の反転（false→true） |
| Tbuktu_ntruTest_473/474 | バッファ書き込み | 戻り値の定数変化（0→64, 0→65） |
| Tbuktu_ntruTest_476 | flush/close呼び出し | 真偽値の反転（false→true） |
| WordpressaTest_1 | Fragment状態チェック | 真偽値の反転、状態不変化 |

#### 共通特徴

```
// パターン1: 真偽値の単純な反転
- onDestroy:::EXIT | this.superOnDestroyCalled == orig(this.superOnDestroyCalled)
+ onDestroy:::EXIT | this.superOnDestroyCalled == true

// パターン2: 定数値の変化
- writeToBuffered:::EXIT | return == 0
+ writeToBuffered:::EXIT | return == 64

// パターン3: メソッド呼び出しの追加による状態変化
- checksIsAddedBeforeGetListView:::EXIT | return == false
+ checksIsAddedBeforeGetListView:::EXIT | return == true
```

**質的特性**:
- **単一の状態変化**: 1つの変数/戻り値の変化で表現可能
- **明確な値の変化**: `false→true`, `0→N`, `null→値` など
- **局所的な修正**: 影響範囲が限定的
- **決定論的な変化**: 毎回同じinvariantが得られる

---

### 1.2 軽微な差分あり: 5件（33.3%）

| ケース | 修正内容 | 差分の種類 | テスト結果 |
|---|---|---|:---:|
| CalligraphyTest_1 | フォントパス取得 | 戻り値表現の違い | PASS |
| ScreenNotificationsTest_1 | アイコン読み込み | null表現の違い | PASS |
| Thomas_s_b_visualeeTest_32 | コード解析 | メソッド名参照 | PASS |
| TucanmobileTest_1 | ダイアログ管理 | 検証タイミング | PASS |
| UshahidiaTest_1 | カーソル管理 | 行番号のずれ | PASS |

#### 差分パターンの分類

**パターンA: 表現の等価性**
```
// CalligraphyTest_1: 戻り値の表現方法が異なるが意味は同等
ORIGINAL: return == null
FIXED:    return has only one value

// ScreenNotificationsTest_1: nullの表現方法
ORIGINAL: return.apps[].icon contains no nulls
FIXED:    return.apps[].icon == [null]
```

**パターンB: 行番号のずれ**
```
// UshahidiaTest_1: 意味的には同一だが行番号が異なる
ORIGINAL: getReportState:::EXIT35 | ...
FIXED:    getReportState:::EXIT36 | ...
```

**パターンC: 検証タイミング・方法の違い**
```
// TucanmobileTest_1
ORIGINAL: onPostExecute:::EXIT | this.dialog.title has only one value
FIXED:    onPostExecute:::EXIT | this.dialog.title == orig(this.dialog.title)
```

**パターンD: 命名の違い（人工的差分）**
```
// Thomas_s_b_visualeeTest_32: テストドライバーのメソッド名
ORIGINAL: createOriginal:::EXIT | ...
FIXED:    createFixed:::EXIT | ...
```

**質的特性**:
- **意味的等価性**: 実行結果は同等だがinvariantの表現が異なる
- **非本質的差分**: null判定の方法論的違い、行番号のずれ
- **テストは成功**: 機能的な正しさは保証されている
- **Daikonの表現限界**: 等価な状態を異なる形式で表現

---

### 1.3 修正失敗（テスト失敗）: 3件（20.0%）

| ケース | 問題点 | 失敗の兆候（invariant） |
|---|---|---|
| CalligraphyTest_2 | メソッド呼び出し欠落 | 必須メソッドのinvariantが出現しない |
| Thomas_s_b_visualeeTest_29 | メソッド参照誤り | 異なるメソッドのinvariantが混在 |
| Thomas_s_b_visualeeTest_30 | メソッド参照誤り | 同上 |

#### 失敗パターンの詳細分析

**パターンX: 必須メソッド呼び出しの欠落（CalligraphyTest_2）**
```
// ORIGINAL には存在するが FIXED には存在しない
ORIGINAL:
+ pullFontPathFromStyle:::ENTER | attributeId == 1
+ pullFontPathFromStyle:::ENTER | attrs has only one value
+ pullFontPathFromStyle:::EXIT | return == null

FIXED: （上記が出現せず、代わりに無関係なメソッドが出現）
+ getIndexCount:::EXIT | return == 1
+ getIndexCount:::EXIT | this.fallback == orig(this.fallback)
```

**パターンY: 誤ったメソッド実行（Thomas_s_b_visualeeTest_29/30）**
```
// MISUSE→ORIGINAL と MISUSE→FIXED の差分
ORIGINAL: createMisuse → createOriginal への変化
FIXED:    createMisuse → createFixed への変化

// 結果として ORIGINAL vs FIXED で不一致
ORIGINAL: createOriginal:::EXIT | return has only one value
FIXED:    createFixed:::EXIT | return has only one value
```

**質的特性**:
- **構造的差異**: メソッド呼び出しグラフが異なる
- **invariantの欠落**: 期待されるinvariantが生成されない
- **異なるメソッドの出現**: 本来呼ばれるべきでないメソッドのinvariant
- **条件分岐の複雑化**: エラーハンドリングのinvariantが増加

---

## 2. カテゴリ間の質的比較

### 2.1 差分の次元分析

| 差分の次元 | 完全一致 | 軽微な差分 | 修正失敗 |
|---|:---:|:---:|:---:|
| 値の変化 | ○ 単一・明確 | ○ 同等 | △ 異なる |
| メソッド呼び出し | → 同一 | → 同一 | ✗ 異なる |
| invariant数 | ≈ 同数 | ≈ 同数 | ≠ 差異大 |
| 行番号 | = 一致 | ≠ ずれ | — |
| 表現形式 | = 一致 | ≠ 異なる | — |

### 2.2 失敗予測の指標

修正失敗を予測できる質的指標：

1. **メソッド不在（Method Absence）**: ORIGINALに存在するメソッドのinvariantがFIXEDに存在しない
2. **新規メソッド出現（Method Emergence）**: ORIGINALに存在しないメソッドのinvariantがFIXEDに出現
3. **条件分岐の増加（Branching Increase）**: `EXIT;condition=`パターンの増加
4. **エラー関連invariant**: `this.error`に関するinvariantの出現パターン変化

---

## 3. 量的分析アプローチの可能性

### 3.1 提案する量的指標

#### A. Invariant差分メトリクス

| 指標名 | 定義 | 期待される傾向 |
|---|---|---|
| **Δinv_count** | FIXEDとORIGINALのinvariant行数の差 | 完全一致:0, 軽微:小, 失敗:大 |
| **Method_Jaccard** | メソッド集合のJaccard係数 | 完全一致:1.0, 軽微:≈1.0, 失敗:<0.8 |
| **Value_Match_Rate** | 同一変数の値が一致する割合 | 完全一致:1.0, 軽微:高, 失敗:低 |
| **Line_Shift_Ratio** | 行番号ずれのあるinvariantの割合 | 完全一致:0, 軽微:中, 失敗:— |

#### B. Invariantパターン分類

```python
# 提案: invariant差分の自動分類
class InvariantDiffType(Enum):
    VALUE_CHANGE = 1      # 値の変化（0→64, false→true）
    NULL_HANDLING = 2     # null表現の違い
    LINE_NUMBER = 3       # 行番号のずれ
    METHOD_MISSING = 4    # メソッド欠落（失敗指標）
    METHOD_EXTRA = 5      # 余分なメソッド（失敗指標）
    EXPRESSION_EQUIV = 6  # 表現の等価な差分
```

### 3.2 機械学習による修正品質予測

**特徴量候補**:
```
1. invariant行数の比率: |FIXED| / |ORIGINAL|
2. メソッドJaccard係数
3. EXIT点の数の差
4. 条件分岐invariant (EXIT;condition=) の数
5. エラー関連invariant (this.error) の有無
6. null関連invariant (== null, has only one value) の比率変化
7. 真偽値反転パターンの数
8. 定数値変化パターンの数
```

**予測モデル**:
- **目的変数**: 修正カテゴリ（完全一致/軽微/失敗）または テスト結果（PASS/FAIL）
- **アルゴリズム**: 決定木、ランダムフォレスト、勾配ブースティング
- **期待される効果**: テスト実行前に修正品質を予測可能

### 3.3 Invariant類似度の定量化

**編集距離ベースアプローチ**:
```
InvariantSimilarity(ORIGINAL, FIXED) = 1 - (
    LevenshteinDistance(ORIGINAL_lines, FIXED_lines) /
    max(|ORIGINAL_lines|, |FIXED_lines|)
)
```

**構造的類似度**:
```
StructuralSimilarity = α × MethodOverlap + β × ValueMatch + γ × (1 - LineShift)
```
- α, β, γ: 重み係数（データから学習）

### 3.4 統計的検定の拡張

現在の分析で実施した相関分析を拡張：

| 検定対象 | 独立変数 | 従属変数 | 手法 |
|---|---|---|---|
| mock効果 | mock有無 | 差分検出 | Fisher正確確率 ✓（実施済） |
| 修正複雑度 | invariant変化量 | 修正成功 | ロジスティック回帰 |
| プロジェクト効果 | プロジェクト | 成功率 | χ²検定 |
| 修正パターン | 修正タイプ | 成功率 | 多重比較 |

---

## 4. 量的分析の実装ロードマップ

### Phase 1: データ整備（短期）
- [ ] invariant差分をパースして構造化データに変換
- [ ] メソッド名、変数名、値の抽出
- [ ] 差分タイプの自動分類器の実装

### Phase 2: メトリクス計算（中期）
- [ ] Jaccard係数、編集距離等の計算
- [ ] 各ケースのメトリクスを算出
- [ ] 相関分析・回帰分析の実施

### Phase 3: 予測モデル（長期）
- [ ] 特徴量エンジニアリング
- [ ] 学習モデルの構築・評価
- [ ] テスト実行前の品質予測システム

---

## 5. 結論

### 質的分析の知見

1. **完全一致ケース**: 単一の状態変化（真偽値反転、定数変化）で特徴づけられ、invariantが正確に一致
2. **軽微な差分ケース**: 意味的には等価だがDaikonの表現形式が異なる。行番号ずれ、null表現の違いが主因
3. **修正失敗ケース**: メソッド呼び出しグラフに構造的差異。必須メソッドの欠落や誤ったメソッドの出現が特徴

### 量的分析への展望

- **実現可能性**: invariant差分は構造化可能であり、メトリクス化の余地が大きい
- **予測精度**: メソッドの出現パターンが失敗予測の強い指標となりうる
- **実用性**: テスト実行前の早期フィードバックに活用可能

### 限界と注意点

- サンプルサイズが15件と限定的（統計的検出力の制約）
- プロジェクト固有の特性（Thomas_s_b_visualee系が3件失敗）が結果に影響
- Daikonの設定（深度、トレース範囲）による表現のばらつき
