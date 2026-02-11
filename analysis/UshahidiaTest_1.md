# UshahidiaTest_1

## MISUSE → ORIGINAL（正しい修正）
```diff
---
- Driver:::OBJECT:::OBJECT | this.mockDb.lastCursor.closed == false
---
+ Driver:::OBJECT:::OBJECT | this.mockDb.lastCursor.closed == true
---
- OpenGeoSmsDao:::OBJECT:::OBJECT | this.mDb.lastCursor.closed == false
---
+ OpenGeoSmsDao:::OBJECT:::OBJECT | this.mDb.lastCursor.closed == true
---
- getLastCursor:::EXIT | return.closed == false
---
+ getLastCursor:::EXIT | return.closed == true
---
- getReportState:::EXIT35 | orig(reportId) == 456
- getReportState:::EXIT35 | orig(this) has only one value
- getReportState:::EXIT35 | return == -1
- getReportState:::EXIT35 | this.mDb has only one value
- getReportState:::EXIT35 | this.mDb.lastCursor has only one value
- getReportState:::EXIT35 | this.mDb.lastCursor.count == 0
- getReportState:::EXIT35 | this.mDb.lastCursor.currentPosition == -1
- getReportState:::EXIT35 | this.mDb.lastCursor.intValues has only one value
- getReportState:::EXIT35 | this.mDb.queryResultCount == 0
- getReportState:::EXIT38 | orig(reportId) == 123
---
+ getReportState:::EXIT38 | orig(reportId) == 456
---
- getReportState:::EXIT38 | return == 0
---
+ getReportState:::EXIT38 | return == -1
---
- getReportState:::EXIT38 | this.mDb.lastCursor.count == 1
- getReportState:::EXIT38 | this.mDb.lastCursor.currentPosition == 0
---
+ getReportState:::EXIT38 | this.mDb.lastCursor.count == 0
+ getReportState:::EXIT38 | this.mDb.lastCursor.currentPosition == -1
---
- getReportState:::EXIT38 | this.mDb.queryResultCount == 1
---
+ getReportState:::EXIT38 | this.mDb.queryResultCount == 0
+ getReportState:::EXIT46 | orig(reportId) == 123
+ getReportState:::EXIT46 | orig(this) has only one value
+ getReportState:::EXIT46 | return == 0
+ getReportState:::EXIT46 | this.mDb has only one value
+ getReportState:::EXIT46 | this.mDb.lastCursor has only one value
+ getReportState:::EXIT46 | this.mDb.lastCursor.count == 1
+ getReportState:::EXIT46 | this.mDb.lastCursor.currentPosition == 0
+ getReportState:::EXIT46 | this.mDb.lastCursor.intValues has only one value
+ getReportState:::EXIT46 | this.mDb.queryResultCount == 1
```

## MISUSE → FIXED（LLM修正による変化）
```diff
---
- Driver:::OBJECT:::OBJECT | this.mockDb.lastCursor.closed == false
---
+ Driver:::OBJECT:::OBJECT | this.mockDb.lastCursor.closed == true
---
- OpenGeoSmsDao:::OBJECT:::OBJECT | this.mDb.lastCursor.closed == false
---
+ OpenGeoSmsDao:::OBJECT:::OBJECT | this.mDb.lastCursor.closed == true
---
- getLastCursor:::EXIT | return.closed == false
---
+ getLastCursor:::EXIT | return.closed == true
---
- getReportState:::EXIT35 | orig(reportId) == 456
- getReportState:::EXIT35 | orig(this) has only one value
- getReportState:::EXIT35 | return == -1
- getReportState:::EXIT35 | this.mDb has only one value
- getReportState:::EXIT35 | this.mDb.lastCursor has only one value
- getReportState:::EXIT35 | this.mDb.lastCursor.count == 0
- getReportState:::EXIT35 | this.mDb.lastCursor.currentPosition == -1
- getReportState:::EXIT35 | this.mDb.lastCursor.intValues has only one value
- getReportState:::EXIT35 | this.mDb.queryResultCount == 0
- getReportState:::EXIT38 | orig(reportId) == 123
- getReportState:::EXIT38 | orig(this) has only one value
- getReportState:::EXIT38 | return == 0
- getReportState:::EXIT38 | this.mDb has only one value
- getReportState:::EXIT38 | this.mDb.lastCursor has only one value
- getReportState:::EXIT38 | this.mDb.lastCursor.count == 1
- getReportState:::EXIT38 | this.mDb.lastCursor.currentPosition == 0
- getReportState:::EXIT38 | this.mDb.lastCursor.intValues has only one value
- getReportState:::EXIT38 | this.mDb.queryResultCount == 1
---
+ getReportState:::EXIT36 | orig(reportId) == 456
+ getReportState:::EXIT36 | orig(this) has only one value
+ getReportState:::EXIT36 | return == -1
+ getReportState:::EXIT36 | this.mDb has only one value
+ getReportState:::EXIT36 | this.mDb.lastCursor has only one value
+ getReportState:::EXIT36 | this.mDb.lastCursor.count == 0
+ getReportState:::EXIT36 | this.mDb.lastCursor.currentPosition == -1
+ getReportState:::EXIT36 | this.mDb.lastCursor.intValues has only one value
+ getReportState:::EXIT36 | this.mDb.queryResultCount == 0
+ getReportState:::EXIT39 | orig(reportId) == 123
+ getReportState:::EXIT39 | orig(this) has only one value
+ getReportState:::EXIT39 | return == 0
+ getReportState:::EXIT39 | this.mDb has only one value
+ getReportState:::EXIT39 | this.mDb.lastCursor has only one value
+ getReportState:::EXIT39 | this.mDb.lastCursor.count == 1
+ getReportState:::EXIT39 | this.mDb.lastCursor.currentPosition == 0
+ getReportState:::EXIT39 | this.mDb.lastCursor.intValues has only one value
+ getReportState:::EXIT39 | this.mDb.queryResultCount == 1
```

## ORIGINAL vs FIXED（残存する差分）
```diff
---
- getReportState:::EXIT38 | orig(reportId) == 456
- getReportState:::EXIT38 | orig(this) has only one value
- getReportState:::EXIT38 | return == -1
- getReportState:::EXIT38 | this.mDb has only one value
- getReportState:::EXIT38 | this.mDb.lastCursor has only one value
- getReportState:::EXIT38 | this.mDb.lastCursor.count == 0
- getReportState:::EXIT38 | this.mDb.lastCursor.currentPosition == -1
- getReportState:::EXIT38 | this.mDb.lastCursor.intValues has only one value
- getReportState:::EXIT38 | this.mDb.queryResultCount == 0
- getReportState:::EXIT46 | orig(reportId) == 123
- getReportState:::EXIT46 | orig(this) has only one value
- getReportState:::EXIT46 | return == 0
- getReportState:::EXIT46 | this.mDb has only one value
- getReportState:::EXIT46 | this.mDb.lastCursor has only one value
- getReportState:::EXIT46 | this.mDb.lastCursor.count == 1
- getReportState:::EXIT46 | this.mDb.lastCursor.currentPosition == 0
- getReportState:::EXIT46 | this.mDb.lastCursor.intValues has only one value
- getReportState:::EXIT46 | this.mDb.queryResultCount == 1
---
+ getReportState:::EXIT36 | orig(reportId) == 456
+ getReportState:::EXIT36 | orig(this) has only one value
+ getReportState:::EXIT36 | return == -1
+ getReportState:::EXIT36 | this.mDb has only one value
+ getReportState:::EXIT36 | this.mDb.lastCursor has only one value
+ getReportState:::EXIT36 | this.mDb.lastCursor.count == 0
+ getReportState:::EXIT36 | this.mDb.lastCursor.currentPosition == -1
+ getReportState:::EXIT36 | this.mDb.lastCursor.intValues has only one value
+ getReportState:::EXIT36 | this.mDb.queryResultCount == 0
+ getReportState:::EXIT39 | orig(reportId) == 123
+ getReportState:::EXIT39 | orig(this) has only one value
+ getReportState:::EXIT39 | return == 0
+ getReportState:::EXIT39 | this.mDb has only one value
+ getReportState:::EXIT39 | this.mDb.lastCursor has only one value
+ getReportState:::EXIT39 | this.mDb.lastCursor.count == 1
+ getReportState:::EXIT39 | this.mDb.lastCursor.currentPosition == 0
+ getReportState:::EXIT39 | this.mDb.lastCursor.intValues has only one value
+ getReportState:::EXIT39 | this.mDb.queryResultCount == 1
```

