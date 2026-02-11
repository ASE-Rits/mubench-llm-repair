# TucanmobileTest_1

## ORIGINAL
```
onPostExecute:::ENTER
  result.lastcalledURL == null
  result.Cookies == null

onPostExecute:::EXIT
  result.HTML_text == orig(result.HTML_text)
  result.redirectURL == orig(result.redirectURL)
  result.lastcalledURL == orig(result.lastcalledURL)
  result.Cookies == orig(result.Cookies)
  result.lastcalledURL == null
  result.Cookies == null
```

## MISUSE
```

```

## FIXED
```
onPostExecute:::ENTER
  result.lastcalledURL == null
  result.Cookies == null

onPostExecute:::EXIT
  result.HTML_text == orig(result.HTML_text)
  result.redirectURL == orig(result.redirectURL)
  result.lastcalledURL == orig(result.lastcalledURL)
  result.Cookies == orig(result.Cookies)
  result.lastcalledURL == null
  result.Cookies == null
```
