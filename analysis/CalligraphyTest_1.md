# CalligraphyTest_1

## MISUSE → ORIGINAL（正しい修正）
```diff
---
+ Driver$InvocationResult:::OBJECT:::OBJECT | this.error == null
---
- isSuccess:::EXIT | return == false
---
+ getValue:::EXIT | return == null
+ getValue:::EXIT | this.error == orig(this.error)
+ getValue:::EXIT | this.value == orig(this.value)
+ isSuccess:::EXIT | return == true
---
+ pullFontPathFromStyle:::ENTER | attributeId == 1
+ pullFontPathFromStyle:::ENTER | attrs has only one value
+ pullFontPathFromStyle:::ENTER | context has only one value
+ pullFontPathFromStyle:::ENTER | context.resources has only one value
+ pullFontPathFromStyle:::EXIT | context.resources == orig(context.resources)
+ pullFontPathFromStyle:::EXIT | context.resources has only one value
+ pullFontPathFromStyle:::EXIT | return == null
+ pullFontPathFromTheme:::ENTER | attributeId == 11
+ pullFontPathFromTheme:::ENTER | context has only one value
+ pullFontPathFromTheme:::ENTER | context.resources has only one value
+ pullFontPathFromTheme:::ENTER | styleId == 10
+ pullFontPathFromTheme:::EXIT | context.resources == orig(context.resources)
+ pullFontPathFromTheme:::EXIT | context.resources has only one value
+ pullFontPathFromTheme:::EXIT | return == null
```

## MISUSE → FIXED（LLM修正による変化）
```diff
---
- Driver$InvocationResult:::OBJECT:::OBJECT | this.value == null
---
+ Driver$InvocationResult:::OBJECT:::OBJECT | this.error == null
---
+ getText:::ENTER | index == 0
+ getText:::EXIT | this.fallback == orig(this.fallback)
---
- isSuccess:::EXIT | return == false
---
+ getValue:::EXIT | this.error == orig(this.error)
+ getValue:::EXIT | this.value == orig(this.value)
+ getValue:::EXIT | this.value.getClass().getName() == orig(this.value.getClass().getName())
+ isSuccess:::EXIT | return == true
---
+ isSuccess:::EXIT | this.value.getClass().getName() == orig(this.value.getClass().getName())
---
+ pullFontPathFromStyle:::ENTER | attributeId == 1
+ pullFontPathFromStyle:::ENTER | attrs has only one value
+ pullFontPathFromStyle:::ENTER | context has only one value
+ pullFontPathFromStyle:::ENTER | context.resources has only one value
+ pullFontPathFromStyle:::EXIT | context.resources == orig(context.resources)
+ pullFontPathFromStyle:::EXIT | context.resources has only one value
+ pullFontPathFromStyle:::EXIT | return has only one value
+ pullFontPathFromTheme:::ENTER | attributeId == 11
+ pullFontPathFromTheme:::ENTER | context has only one value
+ pullFontPathFromTheme:::ENTER | context.resources has only one value
+ pullFontPathFromTheme:::ENTER | styleId == 10
+ pullFontPathFromTheme:::EXIT | context.resources == orig(context.resources)
+ pullFontPathFromTheme:::EXIT | context.resources has only one value
+ pullFontPathFromTheme:::EXIT | return has only one value
```

## ORIGINAL vs FIXED（残存する差分）
```diff
---
- Driver$InvocationResult:::OBJECT:::OBJECT | this.value == null
---
+ getText:::ENTER | index == 0
+ getText:::EXIT | this.fallback == orig(this.fallback)
---
- getValue:::EXIT | return == null
---
+ getValue:::EXIT | this.value.getClass().getName() == orig(this.value.getClass().getName())
---
+ isSuccess:::EXIT | this.value.getClass().getName() == orig(this.value.getClass().getName())
---
- pullFontPathFromStyle:::EXIT | return == null
---
+ pullFontPathFromStyle:::EXIT | return has only one value
---
- pullFontPathFromTheme:::EXIT | return == null
---
+ pullFontPathFromTheme:::EXIT | return has only one value
```

