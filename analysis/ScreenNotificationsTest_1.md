# ScreenNotificationsTest_1

## ORIGINAL
```
AppsActivity:::OBJECT:::OBJECT
  size(this.packageManager.installedApps[]) one of { 0, 1 }

loadInBackground:::ENTER
  id == 0
  size(this.packageManager.installedApps[]) == 1

loadInBackground:::EXIT
  return has only one value
  return.sections has only one value
  return.sections[] contains no nulls and has only one value, of length 1
  return.sections[] elements has only one value
  return.sections[].getClass().getName() == [screen_notifications._1.original.Section]
  return.apps has only one value
  return.apps[] contains no nulls and has only one value, of length 1
  return.apps[] elements has only one value
  return.apps[].getClass().getName() == [screen_notifications._1.original.App]
  return.apps[].name contains no nulls and has only one value, of length 1
  return.apps[].name elements has only one value
  return.apps[].packageName contains no nulls and has only one value, of length 1
  return.apps[].packageName elements has only one value
  return.apps[].icon contains no nulls and has only one value, of length 1
  return.apps[].icon elements has only one value
  size(this.packageManager.installedApps[]) == 1
  size(return.sections[]) == 1
  size(return.apps[]) == 1
```

## MISUSE
```

```

## FIXED
```
AppsActivity:::OBJECT:::OBJECT
  size(this.packageManager.installedApps[]) one of { 0, 1 }

loadInBackground:::ENTER
  id == 0
  size(this.packageManager.installedApps[]) == 1

loadInBackground:::EXIT
  return has only one value
  return.sections has only one value
  return.sections[] contains no nulls and has only one value, of length 1
  return.sections[] elements has only one value
  return.sections[].getClass().getName() == [screen_notifications._1.fixed.Section]
  return.apps has only one value
  return.apps[] contains no nulls and has only one value, of length 1
  return.apps[] elements has only one value
  return.apps[].getClass().getName() == [screen_notifications._1.fixed.App]
  return.apps[].name contains no nulls and has only one value, of length 1
  return.apps[].name elements has only one value
  return.apps[].packageName contains no nulls and has only one value, of length 1
  return.apps[].packageName elements has only one value
  return.apps[].icon == [null]
  return.apps[].icon elements == null
  size(this.packageManager.installedApps[]) == 1
  size(return.sections[]) == 1
  size(return.apps[]) == 1
```
