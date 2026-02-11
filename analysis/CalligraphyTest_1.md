# CalligraphyTest_1

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
pullFontPathFromStyle:::ENTER
  attributeId == 1

pullFontPathFromStyle:::EXIT
  return has only one value

pullFontPathFromTheme:::ENTER
  styleId == 10
  attributeId == 11

pullFontPathFromTheme:::EXIT
  return has only one value
```
