# ApacheGoraTest_56_1

## MISUSE → ORIGINAL（正しい修正）
```diff
---
- applyVariantEncoding:::ENTER | value one of { 1, 31243722414882816L, 72057594037927935L }
---
- applyVariantEncoding:::EXIT | return one of { 0, 31243722414882816L, 72057594037927680L }
---
- areAllBytes:::ENTER | data has only one value
- areAllBytes:::ENTER | data[] == [0, 0, 0, 0, 0, 0, 0, 0]
- areAllBytes:::ENTER | data[] elements == 0
---
- areAllBytes:::EXIT | data[] == [0, 0, 0, 0, 0, 0, 0, 0]
---
- areAllBytes:::EXIT | data[] elements == 0
---
- computeFollowingKey:::ENTER | data[] elements one of { 0, 111 }
- computeFollowingKey:::ENTER | data[] one of { [0, 0, 0, 0, 0, 0, 0, 0], [0, 111] }
---
- computeFollowingKey:::EXIT | (data[] == [0, 0, 0, 0, 0, 0, 0, 0])  <==>  (orig(size(data[])) == 8)
- computeFollowingKey:::EXIT | (data[] == [0, 0, 0, 0, 0, 0, 0, 0])  <==>  (return == 1)
- computeFollowingKey:::EXIT | (data[] == [0, 0, 0, 0, 0, 0, 0, 0])  ==>  (data[] elements == 0)
- computeFollowingKey:::EXIT | (data[] == [0, 111])  <==>  (orig(size(data[])) == 2)
- computeFollowingKey:::EXIT | (data[] == [0, 111])  <==>  (return == 31243722414882816L)
- computeFollowingKey:::EXIT | (data[] == [0, 111])  ==>  (data[] elements one of { 0, 111 })
---
+ computeFollowingKey:::EXIT | (orig(size(data[])) == 2)  ==>  (data[] == [0, 111])
+ computeFollowingKey:::EXIT | (orig(size(data[])) == 2)  ==>  (data[] elements one of { 0, 111 })
+ computeFollowingKey:::EXIT | (orig(size(data[])) == 2)  ==>  (orig(data) has only one value)
+ computeFollowingKey:::EXIT | (orig(size(data[])) == 2)  ==>  (return == 31243722414882816L)
---
- computeFollowingKey:::EXIT | data[] elements one of { 0, 111 }
- computeFollowingKey:::EXIT | data[] one of { [0, 0, 0, 0, 0, 0, 0, 0], [0, 111] }
- computeFollowingKey:::EXIT | return one of { 1, 31243722414882816L }
---
- computeFollowingKey:::EXIT114 | data[] == [0, 0, 0, 0, 0, 0, 0, 0]
- computeFollowingKey:::EXIT114 | data[] elements == 0
- computeFollowingKey:::EXIT114 | orig(data) has only one value
---
- computeFollowingKey:::EXIT114 | return == 1
---
+ computeFollowingKey:::EXIT116 | data[] elements one of { 0, 111 }
---
- computeLastPossibleKey:::ENTER | data has only one value
- computeLastPossibleKey:::ENTER | data[] == [1]
- computeLastPossibleKey:::ENTER | data[] elements == 1
- computeLastPossibleKey:::ENTER | size(data[]) == 1
---
+ computeLastPossibleKey:::ENTER | data[] one of { [-1, 111], [0, 111], [1] }
+ computeLastPossibleKey:::ENTER | size(data[]) one of { 1, 2 }
---
- computeLastPossibleKey:::EXIT | data[] == [1]
---
- computeLastPossibleKey:::EXIT | data[] elements == 1
- computeLastPossibleKey:::EXIT | return == 72057594037927935L
---
+ computeLastPossibleKey:::EXIT | data[] one of { [-1, 111], [0, 111], [1] }
+ computeLastPossibleKey:::EXIT | return one of { -40813871623045121L, 31243722414882815L, 72057594037927935L }
---
- safeAdd:::ENTER | base == 0
---
- safeAdd:::EXIT | return == 1
- toPaddedLong:::ENTER | data[] elements one of { 0, 1, 111 }
- toPaddedLong:::ENTER | data[] one of { [0, 0, 0, 0, 0, 0, 0, 0], [0, 111], [1] }
---
- toPaddedLong:::EXIT | data[] elements one of { 0, 1, 111 }
- toPaddedLong:::EXIT | data[] one of { [0, 0, 0, 0, 0, 0, 0, 0], [0, 111], [1] }
- toPaddedLong:::EXIT | return one of { 0, 31243722414882816L, 72057594037927936L }
```

## MISUSE → FIXED（LLM修正による変化）
```diff
---
- applyVariantEncoding:::ENTER | value one of { 1, 31243722414882816L, 72057594037927935L }
---
- applyVariantEncoding:::EXIT | return one of { 0, 31243722414882816L, 72057594037927680L }
---
- areAllBytes:::ENTER | data has only one value
- areAllBytes:::ENTER | data[] == [0, 0, 0, 0, 0, 0, 0, 0]
- areAllBytes:::ENTER | data[] elements == 0
---
- areAllBytes:::EXIT | data[] == [0, 0, 0, 0, 0, 0, 0, 0]
---
- areAllBytes:::EXIT | data[] elements == 0
---
- computeFollowingKey:::ENTER | data[] elements one of { 0, 111 }
- computeFollowingKey:::ENTER | data[] one of { [0, 0, 0, 0, 0, 0, 0, 0], [0, 111] }
---
- computeFollowingKey:::EXIT | (data[] == [0, 0, 0, 0, 0, 0, 0, 0])  <==>  (orig(size(data[])) == 8)
- computeFollowingKey:::EXIT | (data[] == [0, 0, 0, 0, 0, 0, 0, 0])  <==>  (return == 1)
- computeFollowingKey:::EXIT | (data[] == [0, 0, 0, 0, 0, 0, 0, 0])  ==>  (data[] elements == 0)
- computeFollowingKey:::EXIT | (data[] == [0, 111])  <==>  (orig(size(data[])) == 2)
- computeFollowingKey:::EXIT | (data[] == [0, 111])  <==>  (return == 31243722414882816L)
- computeFollowingKey:::EXIT | (data[] == [0, 111])  ==>  (data[] elements one of { 0, 111 })
---
+ computeFollowingKey:::EXIT | (orig(size(data[])) == 2)  ==>  (data[] == [0, 111])
+ computeFollowingKey:::EXIT | (orig(size(data[])) == 2)  ==>  (data[] elements one of { 0, 111 })
+ computeFollowingKey:::EXIT | (orig(size(data[])) == 2)  ==>  (orig(data) has only one value)
+ computeFollowingKey:::EXIT | (orig(size(data[])) == 2)  ==>  (return == 31243722414882816L)
---
- computeFollowingKey:::EXIT | data[] elements one of { 0, 111 }
- computeFollowingKey:::EXIT | data[] one of { [0, 0, 0, 0, 0, 0, 0, 0], [0, 111] }
- computeFollowingKey:::EXIT | return one of { 1, 31243722414882816L }
---
- computeFollowingKey:::EXIT114 | data[] == [0, 0, 0, 0, 0, 0, 0, 0]
- computeFollowingKey:::EXIT114 | data[] elements == 0
- computeFollowingKey:::EXIT114 | orig(data) has only one value
---
- computeFollowingKey:::EXIT114 | return == 1
---
+ computeFollowingKey:::EXIT116 | data[] elements one of { 0, 111 }
---
- computeLastPossibleKey:::ENTER | data has only one value
- computeLastPossibleKey:::ENTER | data[] == [1]
- computeLastPossibleKey:::ENTER | data[] elements == 1
- computeLastPossibleKey:::ENTER | size(data[]) == 1
---
+ computeLastPossibleKey:::ENTER | data[] one of { [-1, 111], [0, 111], [1] }
+ computeLastPossibleKey:::ENTER | size(data[]) one of { 1, 2 }
---
- computeLastPossibleKey:::EXIT | data[] == [1]
---
- computeLastPossibleKey:::EXIT | data[] elements == 1
- computeLastPossibleKey:::EXIT | return == 72057594037927935L
---
+ computeLastPossibleKey:::EXIT | data[] one of { [-1, 111], [0, 111], [1] }
+ computeLastPossibleKey:::EXIT | return one of { -40813871623045121L, 31243722414882815L, 72057594037927935L }
---
- safeAdd:::ENTER | base == 0
---
- safeAdd:::EXIT | return == 1
- toPaddedLong:::ENTER | data[] elements one of { 0, 1, 111 }
- toPaddedLong:::ENTER | data[] one of { [0, 0, 0, 0, 0, 0, 0, 0], [0, 111], [1] }
---
- toPaddedLong:::EXIT | data[] elements one of { 0, 1, 111 }
- toPaddedLong:::EXIT | data[] one of { [0, 0, 0, 0, 0, 0, 0, 0], [0, 111], [1] }
- toPaddedLong:::EXIT | return one of { 0, 31243722414882816L, 72057594037927936L }
```

## ✅ ORIGINAL == FIXED（完全一致）

