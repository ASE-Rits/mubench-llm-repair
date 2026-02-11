⚠️ **WARNING: Fixed version failed tests** - 修正が失敗しています

# CalligraphyTest_2

## ORIGINAL
```
pullFontPathFromStyle:::ENTER
  attributeId == 1

pullFontPathFromStyle:::EXIT
  return == null

pullFontPathFromTheme:::ENTER
  styleId == 10
  attributeId == 11

pullFontPathFromTheme:::EXIT
  return == null
```

## MISUSE
```

```

## FIXED
```
pullFontPathFromTheme:::ENTER
  styleId == 10
  attributeId == 11

pullFontPathFromTheme:::EXIT
  return == null
```
