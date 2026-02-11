# WordpressaTest_1

## MISUSE → ORIGINAL（正しい修正）
```diff
---
- NotificationsListFragment:::EXIT | this.listView.selectionFromTop == -1
---
- NotificationsListFragment:::OBJECT:::OBJECT | this.listView.selectionFromTop one of { -1, 0, 2 }
---
+ NotificationsListFragment:::OBJECT:::OBJECT | this.listView.selectionFromTop == -1
---
- NotificationsListFragment:::OBJECT:::OBJECT | this.mRestoredListPosition one of { -1, 0, 2 }
---
+ NotificationsListFragment:::OBJECT:::OBJECT | this.mRestoredListPosition one of { 0, 2 }
---
- checksIsAddedBeforeGetListView:::EXIT | return == false
---
+ checksIsAddedBeforeGetListView:::EXIT | return == true
---
- restoreListScrollPosition:::ENTER | this.listView.selectionFromTop == -1
- restoreListScrollPosition:::ENTER | this.mRestoredListPosition one of { 0, 2 }
---
- restoreListScrollPosition:::EXIT | this.listView.selectionFromTop one of { 0, 2 }
---
+ restoreListScrollPosition:::EXIT | this.listView.selectionFromTop == orig(this.listView.selectionFromTop)
---
- restoreListScrollPosition:::EXIT | this.mRestoredListPosition == -1
---
+ restoreListScrollPosition:::EXIT | this.mRestoredListPosition == orig(this.mRestoredListPosition)
---
- testRestoreScrollPosition:::EXIT | return == true
---
+ testRestoreScrollPosition:::EXIT | return == false
```

## MISUSE → FIXED（LLM修正による変化）
```diff
---
- NotificationsListFragment:::EXIT | this.listView.selectionFromTop == -1
---
- NotificationsListFragment:::OBJECT:::OBJECT | this.listView.selectionFromTop one of { -1, 0, 2 }
---
+ NotificationsListFragment:::OBJECT:::OBJECT | this.listView.selectionFromTop == -1
---
- NotificationsListFragment:::OBJECT:::OBJECT | this.mRestoredListPosition one of { -1, 0, 2 }
---
+ NotificationsListFragment:::OBJECT:::OBJECT | this.mRestoredListPosition one of { 0, 2 }
---
- checksIsAddedBeforeGetListView:::EXIT | return == false
---
+ checksIsAddedBeforeGetListView:::EXIT | return == true
---
- restoreListScrollPosition:::ENTER | this.listView.selectionFromTop == -1
- restoreListScrollPosition:::ENTER | this.mRestoredListPosition one of { 0, 2 }
---
- restoreListScrollPosition:::EXIT | this.listView.selectionFromTop one of { 0, 2 }
---
+ restoreListScrollPosition:::EXIT | this.listView.selectionFromTop == orig(this.listView.selectionFromTop)
---
- restoreListScrollPosition:::EXIT | this.mRestoredListPosition == -1
---
+ restoreListScrollPosition:::EXIT | this.mRestoredListPosition == orig(this.mRestoredListPosition)
---
- testRestoreScrollPosition:::EXIT | return == true
---
+ testRestoreScrollPosition:::EXIT | return == false
```

## ✅ ORIGINAL == FIXED（完全一致）

