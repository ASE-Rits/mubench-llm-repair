# Tbuktu_ntruTest_473

## MISUSE → ORIGINAL（正しい修正）
```diff
---
- writeToBuffered:::EXIT | return == 0
---
+ writeToBuffered:::EXIT | return == 64
```

## MISUSE → FIXED（LLM修正による変化）
```diff
---
- writeToBuffered:::EXIT | return == 0
---
+ writeToBuffered:::EXIT | return == 64
```

## ✅ ORIGINAL == FIXED（完全一致）

