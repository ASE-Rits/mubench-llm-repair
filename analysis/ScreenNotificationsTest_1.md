# ScreenNotificationsTest_1

## MISUSE → ORIGINAL（正しい修正）
```diff
---
+ AppsActivity:::EXIT | this.packageManager.installedApps[] == []
+ AppsActivity:::OBJECT:::OBJECT | size(this.packageManager.installedApps[]) one of { 0, 1 }
---
- AppsActivity:::OBJECT:::OBJECT | this.packageManager.installedApps[] == []
---
+ AppsActivity:::OBJECT:::OBJECT | this.packageManager.installedApps[] elements has only one value
+ AppsActivity:::OBJECT:::OBJECT | this.packageManager.installedApps[].getClass().getName() one of { [], [screen_notifications._1.mocks.android.content.pm.ApplicationInfo] }
---
+ loadInBackground:::ENTER | id == 0
+ loadInBackground:::ENTER | id == 0
+ loadInBackground:::ENTER | size(this.packageManager.installedApps[]) == 1
+ loadInBackground:::ENTER | this.packageManager.installedApps[] contains no nulls and has only one value, of length 1
+ loadInBackground:::EXIT | return has only one value
+ loadInBackground:::EXIT | return has only one value
+ loadInBackground:::EXIT | return.apps has only one value
+ loadInBackground:::EXIT | return.apps[] contains no nulls and has only one value, of length 1
+ loadInBackground:::EXIT | return.apps[] elements has only one value
+ loadInBackground:::EXIT | return.apps[].icon contains no nulls and has only one value, of length 1
+ loadInBackground:::EXIT | return.apps[].icon elements has only one value
+ loadInBackground:::EXIT | return.apps[].name contains no nulls and has only one value, of length 1
+ loadInBackground:::EXIT | return.apps[].name elements has only one value
+ loadInBackground:::EXIT | return.apps[].packageName contains no nulls and has only one value, of length 1
+ loadInBackground:::EXIT | return.apps[].packageName elements has only one value
+ loadInBackground:::EXIT | return.sections has only one value
+ loadInBackground:::EXIT | return.sections[] contains no nulls and has only one value, of length 1
+ loadInBackground:::EXIT | return.sections[] elements has only one value
+ loadInBackground:::EXIT | size(return.apps[]) == 1
+ loadInBackground:::EXIT | size(return.sections[]) == 1
+ loadInBackground:::EXIT | size(this.packageManager.installedApps[]) == 1
+ loadInBackground:::EXIT | this.activityInstance == orig(this.activityInstance)
+ loadInBackground:::EXIT | this.activityInstance.getClass().getName() == orig(this.activityInstance.getClass().getName())
+ loadInBackground:::EXIT | this.contentViewId == orig(this.contentViewId)
+ loadInBackground:::EXIT | this.loaderManager == orig(this.loaderManager)
+ loadInBackground:::EXIT | this.mAdapter == orig(this.mAdapter)
+ loadInBackground:::EXIT | this.mLoadingDialog == orig(this.mLoadingDialog)
+ loadInBackground:::EXIT | this.menuInflater == orig(this.menuInflater)
+ loadInBackground:::EXIT | this.packageManager == orig(this.packageManager)
+ loadInBackground:::EXIT | this.packageManager.installedApps == orig(this.packageManager.installedApps)
+ loadInBackground:::EXIT | this.packageManager.installedApps.getClass().getName() == orig(this.packageManager.installedApps.getClass().getName())
+ loadInBackground:::EXIT | this.packageManager.installedApps[] == orig(this.packageManager.installedApps[])
+ loadInBackground:::EXIT | this.packageManager.installedApps[] contains no nulls and has only one value, of length 1
+ loadInBackground:::EXIT | this.resources == orig(this.resources)
+ loadInBackground:::EXIT | this.targetClass == orig(this.targetClass)
+ loadInBackground:::EXIT | this.views == orig(this.views)
+ loadInBackground:::EXIT | this.views.getClass().getName() == orig(this.views.getClass().getName())
```

## MISUSE → FIXED（LLM修正による変化）
```diff
---
+ AppsActivity:::EXIT | this.packageManager.installedApps[] == []
+ AppsActivity:::OBJECT:::OBJECT | size(this.packageManager.installedApps[]) one of { 0, 1 }
---
- AppsActivity:::OBJECT:::OBJECT | this.packageManager.installedApps[] == []
---
+ AppsActivity:::OBJECT:::OBJECT | this.packageManager.installedApps[] elements has only one value
+ AppsActivity:::OBJECT:::OBJECT | this.packageManager.installedApps[].getClass().getName() one of { [], [screen_notifications._1.mocks.android.content.pm.ApplicationInfo] }
---
+ loadInBackground:::ENTER | id == 0
+ loadInBackground:::ENTER | id == 0
+ loadInBackground:::ENTER | size(this.packageManager.installedApps[]) == 1
+ loadInBackground:::ENTER | this.packageManager.installedApps[] contains no nulls and has only one value, of length 1
+ loadInBackground:::EXIT | return has only one value
+ loadInBackground:::EXIT | return has only one value
+ loadInBackground:::EXIT | return.apps has only one value
+ loadInBackground:::EXIT | return.apps[] contains no nulls and has only one value, of length 1
+ loadInBackground:::EXIT | return.apps[] elements has only one value
+ loadInBackground:::EXIT | return.apps[].icon == [null]
+ loadInBackground:::EXIT | return.apps[].icon elements == null
+ loadInBackground:::EXIT | return.apps[].name contains no nulls and has only one value, of length 1
+ loadInBackground:::EXIT | return.apps[].name elements has only one value
+ loadInBackground:::EXIT | return.apps[].packageName contains no nulls and has only one value, of length 1
+ loadInBackground:::EXIT | return.apps[].packageName elements has only one value
+ loadInBackground:::EXIT | return.sections has only one value
+ loadInBackground:::EXIT | return.sections[] contains no nulls and has only one value, of length 1
+ loadInBackground:::EXIT | return.sections[] elements has only one value
+ loadInBackground:::EXIT | size(return.apps[]) == 1
+ loadInBackground:::EXIT | size(return.sections[]) == 1
+ loadInBackground:::EXIT | size(this.packageManager.installedApps[]) == 1
+ loadInBackground:::EXIT | this.activityInstance == orig(this.activityInstance)
+ loadInBackground:::EXIT | this.activityInstance.getClass().getName() == orig(this.activityInstance.getClass().getName())
+ loadInBackground:::EXIT | this.contentViewId == orig(this.contentViewId)
+ loadInBackground:::EXIT | this.loaderManager == orig(this.loaderManager)
+ loadInBackground:::EXIT | this.mAdapter == orig(this.mAdapter)
+ loadInBackground:::EXIT | this.mLoadingDialog == orig(this.mLoadingDialog)
+ loadInBackground:::EXIT | this.menuInflater == orig(this.menuInflater)
+ loadInBackground:::EXIT | this.packageManager == orig(this.packageManager)
+ loadInBackground:::EXIT | this.packageManager.installedApps == orig(this.packageManager.installedApps)
+ loadInBackground:::EXIT | this.packageManager.installedApps.getClass().getName() == orig(this.packageManager.installedApps.getClass().getName())
+ loadInBackground:::EXIT | this.packageManager.installedApps[] == orig(this.packageManager.installedApps[])
+ loadInBackground:::EXIT | this.packageManager.installedApps[] contains no nulls and has only one value, of length 1
+ loadInBackground:::EXIT | this.resources == orig(this.resources)
+ loadInBackground:::EXIT | this.targetClass == orig(this.targetClass)
+ loadInBackground:::EXIT | this.views == orig(this.views)
+ loadInBackground:::EXIT | this.views.getClass().getName() == orig(this.views.getClass().getName())
```

## ORIGINAL vs FIXED（残存する差分）
```diff
---
- loadInBackground:::EXIT | return.apps[].icon contains no nulls and has only one value, of length 1
- loadInBackground:::EXIT | return.apps[].icon elements has only one value
---
+ loadInBackground:::EXIT | return.apps[].icon == [null]
+ loadInBackground:::EXIT | return.apps[].icon elements == null
```

