# UshahidiaTest_1

## ORIGINAL
```
OpenGeoSmsDao:::OBJECT:::OBJECT
  size(this.mDb.lastCursor.intValues[]) == 1

OpenGeoSmsDao:::ENTER
  db.lastCursor == null
  db.queryResultCount == 1
  db.queryResultValue == 0

OpenGeoSmsDao:::EXIT
  db.lastCursor == orig(db.lastCursor)
  db.queryResultCount == orig(db.queryResultCount)
  db.queryResultValue == orig(db.queryResultValue)
  db.lastCursor == null
  db.queryResultCount == 1
  db.queryResultValue == 0

array:::ENTER
  i one of { 123, 456 }

array:::EXIT
  size(return[]) == 1

getReportState:::ENTER
  reportId one of { 123, 456 }

getReportState:::EXIT38
  return == -1
  orig(reportId) == 456

getReportState:::EXIT46
  return == 0
  orig(reportId) == 123

getReportState:::EXIT
  (this.mDb.lastCursor.count == 0)  <==>  (orig(reportId) == 456)
  (this.mDb.lastCursor.count == 0)  <==>  (return == -1)
  (this.mDb.lastCursor.count == 0)  <==>  (this.mDb.lastCursor.currentPosition == -1)
  (this.mDb.lastCursor.count == 0)  <==>  (this.mDb.queryResultCount == 0)
  (this.mDb.lastCursor.count == 1)  <==>  (orig(reportId) == 123)
  (this.mDb.lastCursor.count == 1)  <==>  (return == 0)
  (this.mDb.lastCursor.count == 1)  <==>  (this.mDb.lastCursor.currentPosition == 0)
  (this.mDb.lastCursor.count == 1)  <==>  (this.mDb.queryResultCount == 1)
  return one of { -1, 0 }
```

## MISUSE
```
OpenGeoSmsDao:::OBJECT:::OBJECT
  size(this.mDb.lastCursor.intValues[]) == 1

OpenGeoSmsDao:::ENTER
  db.lastCursor == null
  db.queryResultCount == 1
  db.queryResultValue == 0

OpenGeoSmsDao:::EXIT
  db.lastCursor == orig(db.lastCursor)
  db.queryResultCount == orig(db.queryResultCount)
  db.queryResultValue == orig(db.queryResultValue)
  db.lastCursor == null
  db.queryResultCount == 1
  db.queryResultValue == 0

array:::ENTER
  i one of { 123, 456 }

array:::EXIT
  size(return[]) == 1

getReportState:::ENTER
  reportId one of { 123, 456 }

getReportState:::EXIT35
  return == -1
  orig(reportId) == 456

getReportState:::EXIT38
  return == 0
  orig(reportId) == 123

getReportState:::EXIT
  (this.mDb.lastCursor.count == 0)  <==>  (orig(reportId) == 456)
  (this.mDb.lastCursor.count == 0)  <==>  (return == -1)
  (this.mDb.lastCursor.count == 0)  <==>  (this.mDb.lastCursor.currentPosition == -1)
  (this.mDb.lastCursor.count == 0)  <==>  (this.mDb.queryResultCount == 0)
  (this.mDb.lastCursor.count == 1)  <==>  (orig(reportId) == 123)
  (this.mDb.lastCursor.count == 1)  <==>  (return == 0)
  (this.mDb.lastCursor.count == 1)  <==>  (this.mDb.lastCursor.currentPosition == 0)
  (this.mDb.lastCursor.count == 1)  <==>  (this.mDb.queryResultCount == 1)
  return one of { -1, 0 }
```

## FIXED
```
OpenGeoSmsDao:::OBJECT:::OBJECT
  size(this.mDb.lastCursor.intValues[]) == 1

OpenGeoSmsDao:::ENTER
  db.lastCursor == null
  db.queryResultCount == 1
  db.queryResultValue == 0

OpenGeoSmsDao:::EXIT
  db.lastCursor == orig(db.lastCursor)
  db.queryResultCount == orig(db.queryResultCount)
  db.queryResultValue == orig(db.queryResultValue)
  db.lastCursor == null
  db.queryResultCount == 1
  db.queryResultValue == 0

array:::ENTER
  i one of { 123, 456 }

array:::EXIT
  size(return[]) == 1

getReportState:::ENTER
  reportId one of { 123, 456 }

getReportState:::EXIT36
  return == -1
  orig(reportId) == 456

getReportState:::EXIT39
  return == 0
  orig(reportId) == 123

getReportState:::EXIT
  (this.mDb.lastCursor.count == 0)  <==>  (orig(reportId) == 456)
  (this.mDb.lastCursor.count == 0)  <==>  (return == -1)
  (this.mDb.lastCursor.count == 0)  <==>  (this.mDb.lastCursor.currentPosition == -1)
  (this.mDb.lastCursor.count == 0)  <==>  (this.mDb.queryResultCount == 0)
  (this.mDb.lastCursor.count == 1)  <==>  (orig(reportId) == 123)
  (this.mDb.lastCursor.count == 1)  <==>  (return == 0)
  (this.mDb.lastCursor.count == 1)  <==>  (this.mDb.lastCursor.currentPosition == 0)
  (this.mDb.lastCursor.count == 1)  <==>  (this.mDb.queryResultCount == 1)
  return one of { -1, 0 }
```
