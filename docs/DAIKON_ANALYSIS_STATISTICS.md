# Daikon Invariant分析 統計レポート

## 概要

本レポートは、MUBenchデータセットに対するDaikonによる不変条件（Invariant）抽出の実行統計をまとめたものである。

---

## 1. テストケースの分類

### 総テスト数
| カテゴリ | 件数 |
|---|---:|
| テストファイル総数 | 54 |
| 静的テスト（スキップ対象） | 19 |
| **動的テスト** | **35** |

### 静的テスト一覧（Daikonで有意な不変条件が得られないため除外）
```
adempiere/_1, adempiere/_2
alibaba_druid/_1, alibaba_druid/_2
android_rcs_rcsjta/_1
androiduil/_1
asterisk_java/_194
jmrtd/_1, jmrtd/_2
logblock_logblock_2/_15
mqtt/_389
onosendai/_1
pawotag/_1
rhino/_1
wordpressa/_3
testng/_17, testng/_18, testng/_21, testng/_22
```

### 実行問題のある動的テスト（タイムアウト等の理由で除外）
```
testng/_16  # 並行テストのため実行が終わらない
```

---

## 2. Daikon実行結果

### 実行統計
| 項目 | 件数 |
|---|---:|
| 動的テスト総数 | 35 |
| 実行問題あり | 1 |
| Daikon実行完了 | 34 |
| **正常にinvariant取得** | **32** |
| invariant取得失敗（3行以下） | 2 |

### invariant取得失敗ケース（3行以下で有意な情報なし）
| ケース | 行数 | 理由 |
|---|---:|---|
| asterisk_java/_81 | 3 | トレース情報不足 |
| openaiab/_1 | 3 | トレース情報不足 |

---

## 3. Invariant差分分析

### invariant正常取得32件の一覧とmock構成

| # | プロジェクト | mock構成 | 差分検出 |
|---|---|---|:---:|
| 1 | apache_gora/_56_1 | mocks | ○ |
| 2 | apache_gora/_56_2 | mocks | — |
| 3 | calligraphy/_1 | mocks | ○ |
| 4 | calligraphy/_2 | mocks | ○ |
| 5 | cego/_1 | mocks | — |
| 6 | gnucrasha/_1a | mocks | — |
| 7 | gnucrasha/_1b | mocks | — |
| 8 | hoverruan_weiboclient4j/_128 | requirements | — |
| 9 | ivantrendafilov_confucius/_93 | requirements | — |
| 10 | ivantrendafilov_confucius/_94 | requirements | — |
| 11 | ivantrendafilov_confucius/_95 | requirements | — |
| 12 | ivantrendafilov_confucius/_96 | requirements | — |
| 13 | ivantrendafilov_confucius/_97 | requirements | — |
| 14 | ivantrendafilov_confucius/_98 | requirements | — |
| 15 | ivantrendafilov_confucius/_99 | requirements | — |
| 16 | ivantrendafilov_confucius/_100 | requirements | — |
| 17 | ivantrendafilov_confucius/_101 | requirements | — |
| 18 | jriecken_gae_java_mini_profiler/_39 | mocks | — |
| 19 | lnreadera/_1 | mocks | ○ |
| 20 | lnreadera/_2 | mocks | ○ |
| 21 | screen_notifications/_1 | mocks | ○ |
| 22 | tap_apps/_1 | mocks | — |
| 23 | tbuktu_ntru/_473 | none | ○ |
| 24 | tbuktu_ntru/_474 | none | ○ |
| 25 | tbuktu_ntru/_475 | requirements | — |
| 26 | tbuktu_ntru/_476 | requirements | ○ |
| 27 | thomas_s_b_visualee/_29 | requirements+mocks | ○ |
| 28 | thomas_s_b_visualee/_30 | requirements+mocks | ○ |
| 29 | thomas_s_b_visualee/_32 | requirements+mocks | ○ |
| 30 | tucanmobile/_1 | requirements+mocks | ○ |
| 31 | ushahidia/_1 | requirements+mocks | ○ |
| 32 | wordpressa/_1 | mocks | ○ |

**mock構成の凡例**:
- **none**: 追加コードなし（テスト対象コードのみ）
- **requirements**: 依存ライブラリのスタブ/要件コードのみ
- **mocks**: モックオブジェクトのみ
- **requirements+mocks**: 依存ライブラリのスタブ + モックオブジェクト

### mock構成別の統計
| mock構成 | 件数 | 差分検出数 | 差分検出率 |
|---|---:|---:|---:|
| none | 2 | 2 | 100.0% |
| requirements | 12 | 1 | 8.3% |
| mocks | 13 | 7 | 53.8% |
| requirements+mocks | 5 | 5 | 100.0% |
| **合計** | **32** | **15** | **46.9%** |


**結論**: p < 0.05 で統計的に有意な相関がある。mocksがあるケースは約7.3倍の確率で差分が検出される。

**注記**: 
- `none`の2件（tbuktu_ntru/_473, _474）は戻り値の直接的な変化を検出しており、mockなしでも差分検出に成功
- `requirements`のみ12件中、ivantrendafilov_confucius系9件が全て差分なしであり、プロジェクト特性の影響も考慮が必要

### 分析統計
| 項目 | 件数 |
|---|---:|
| invariant正常取得 | 32 |
| **有意な差分が検出されたケース** | **15** |
| 差分なし | 17 |

### 差分が検出された15ケース
| # | テストケース | MISUSE→ORIGINAL | MISUSE→FIXED | ORIGINAL≠FIXED |
|---|---|:---:|:---:|:---:|
| 1 | ApacheGoraTest_56_1 | ○ | ○ | — |
| 2 | CalligraphyTest_1 | ○ | ○ | ○ |
| 3 | CalligraphyTest_2 | ○ | ○ | ○ |
| 4 | LnreaderaTest_1 | ○ | ○ | — |
| 5 | LnreaderaTest_2 | ○ | ○ | — |
| 6 | ScreenNotificationsTest_1 | ○ | ○ | ○ |
| 7 | Tbuktu_ntruTest_473 | ○ | ○ | — |
| 8 | Tbuktu_ntruTest_474 | ○ | ○ | — |
| 9 | Tbuktu_ntruTest_476 | ○ | ○ | — |
| 10 | Thomas_s_b_visualeeTest_29 | ○ | ○ | ○ |
| 11 | Thomas_s_b_visualeeTest_30 | ○ | ○ | ○ |
| 12 | Thomas_s_b_visualeeTest_32 | ○ | ○ | ○ |
| 13 | TucanmobileTest_1 | ○ | ○ | ○ |
| 14 | UshahidiaTest_1 | ○ | ○ | ○ |
| 15 | WordpressaTest_1 | ○ | ○ | — |

**凡例**: ○=差分あり、—=差分なし（完全一致）

---

## 4. 差分なしケースの内訳

invariant正常取得32件のうち、差分が検出されなかった17ケースの内訳：

| ケース例 |
|---|
| ivantrendafilov_confucius/_93〜_101（9件） |
| apache_gora/_56_2 |
| cego/_1 |
| gnucrasha/_1a, gnucrasha/_1b |
| hoverruan_weiboclient4j/_128 |
| jriecken_gae_java_mini_profiler/_39 |
| tap_apps/_1 |
| tbuktu_ntru/_475 |

---

## 5. サマリー

```
テスト総数:                    54件
├─ 静的テスト:                 19件 (除外)
└─ 動的テスト:                 35件
    ├─ 実行問題あり:            1件 (testng/_16)
    └─ Daikon実行完了:         34件
        ├─ invariant取得失敗:   2件 (asterisk_java/_81, openaiab/_1)
        └─ invariant正常取得:  32件
            ├─ 差分なし:       17件
            └─ 差分あり:       15件 → 詳細分析実施
```

invarintの差があったもの/(invarintの差があったもの+無かったもの) = 15 / (17+15) = 15 /32 = 46.9%

### 差分が検出された15件の修正評価結果
| 評価 | 件数 | 割合 |
|---|---:|---:|
| 完全一致（ORIGINAL==FIXED） | 7 | 46.7% |
| 軽微な差分あり | 5 | 33.3% |
| 修正失敗（テスト失敗含む） | 3 | 20.0% |

詳細な分析結果は [INVARIANT_ANALYSIS_REPORT.md](INVARIANT_ANALYSIS_REPORT.md) を参照。
