# Tbuktu_ntruTest_474

## MISUSE → ORIGINAL（正しい修正）
```diff
---
- writeToBuffered:::EXIT | return == 0
---
+ writeToBuffered:::EXIT | return == 65
```

## MISUSE → FIXED（LLM修正による変化）
```diff
---
- writeToBuffered:::EXIT | return == 0
---
+ writeToBuffered:::EXIT | return == 65
```

## ✅ ORIGINAL == FIXED（完全一致）

