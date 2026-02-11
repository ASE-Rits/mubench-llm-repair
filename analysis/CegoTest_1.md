# CegoTest_1

## ORIGINAL
```
fetchLastStartedIntent:::EXIT
  return has only one value
  return.action has only one value
  return.data has only one value
  return.data.path has only one value
  return.type has only one value

setCurrentDrawable:::EXIT
  drawable.bitmap == orig(drawable.bitmap)
  drawable.bitmap.placeholder == orig(drawable.bitmap.placeholder)

viewImageInStandardApp:::EXIT
  image.bitmap == orig(image.bitmap)
  image.bitmap.placeholder == orig(image.bitmap.placeholder)
```

## MISUSE
```
fetchLastStartedIntent:::EXIT
  return has only one value
  return.action has only one value
  return.data has only one value
  return.data.path has only one value
  return.type has only one value

setCurrentDrawable:::EXIT
  drawable.bitmap == orig(drawable.bitmap)
  drawable.bitmap.placeholder == orig(drawable.bitmap.placeholder)

viewImageInStandardApp:::EXIT
  image.bitmap == orig(image.bitmap)
  image.bitmap.placeholder == orig(image.bitmap.placeholder)
```

## FIXED
```
fetchLastStartedIntent:::EXIT
  return has only one value
  return.action has only one value
  return.data has only one value
  return.data.path has only one value
  return.type has only one value

setCurrentDrawable:::EXIT
  drawable.bitmap == orig(drawable.bitmap)
  drawable.bitmap.placeholder == orig(drawable.bitmap.placeholder)

viewImageInStandardApp:::EXIT
  image.bitmap == orig(image.bitmap)
  image.bitmap.placeholder == orig(image.bitmap.placeholder)
```
