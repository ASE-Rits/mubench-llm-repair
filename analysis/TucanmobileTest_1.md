# TucanmobileTest_1

## MISUSE → ORIGINAL（正しい修正）
```diff
---
- SimpleSecureBrowser:::OBJECT:::OBJECT | this.dialog.showing == true
- SimpleSecureBrowser:::OBJECT:::OBJECT | this.dialog.title has only one value
---
+ invokeOnPostExecute:::ENTER | result has only one value
+ invokeOnPostExecute:::ENTER | result.Cookies == null
+ invokeOnPostExecute:::ENTER | result.HTML_text has only one value
+ invokeOnPostExecute:::ENTER | result.lastcalledURL == null
+ invokeOnPostExecute:::ENTER | result.redirectURL has only one value
+ invokeOnPostExecute:::EXIT | result.Cookies == null
+ invokeOnPostExecute:::EXIT | result.Cookies == orig(result.Cookies)
+ invokeOnPostExecute:::EXIT | result.HTML_text == orig(result.HTML_text)
+ invokeOnPostExecute:::EXIT | result.HTML_text has only one value
+ invokeOnPostExecute:::EXIT | result.lastcalledURL == null
+ invokeOnPostExecute:::EXIT | result.lastcalledURL == orig(result.lastcalledURL)
+ invokeOnPostExecute:::EXIT | result.redirectURL == orig(result.redirectURL)
+ invokeOnPostExecute:::EXIT | result.redirectURL has only one value
+ invokeOnPostExecute:::EXIT | this.instance == orig(this.instance)
+ invokeOnPostExecute:::EXIT | this.instance.getClass().getName() == orig(this.instance.getClass().getName())
+ invokeOnPostExecute:::EXIT | this.targetClass == orig(this.targetClass)
---
+ onPostExecute:::ENTER | result has only one value
+ onPostExecute:::ENTER | result.Cookies == null
+ onPostExecute:::ENTER | result.HTML_text has only one value
+ onPostExecute:::ENTER | result.lastcalledURL == null
+ onPostExecute:::ENTER | result.redirectURL has only one value
+ onPostExecute:::ENTER | this.dialog has only one value
+ onPostExecute:::ENTER | this.dialog.showing == false
+ onPostExecute:::ENTER | this.dialog.title has only one value
+ onPostExecute:::EXIT | result.Cookies == null
+ onPostExecute:::EXIT | result.Cookies == orig(result.Cookies)
+ onPostExecute:::EXIT | result.HTML_text == orig(result.HTML_text)
+ onPostExecute:::EXIT | result.HTML_text has only one value
+ onPostExecute:::EXIT | result.lastcalledURL == null
+ onPostExecute:::EXIT | result.lastcalledURL == orig(result.lastcalledURL)
+ onPostExecute:::EXIT | result.redirectURL == orig(result.redirectURL)
+ onPostExecute:::EXIT | result.redirectURL has only one value
+ onPostExecute:::EXIT | this.dialog == orig(this.dialog)
+ onPostExecute:::EXIT | this.dialog has only one value
+ onPostExecute:::EXIT | this.dialog.showing == false
+ onPostExecute:::EXIT | this.dialog.showing == orig(this.dialog.showing)
+ onPostExecute:::EXIT | this.dialog.title has only one value
+ onPostExecute:::EXIT | this.outerCallingActivity == orig(this.outerCallingActivity)
+ onPostExecute:::EXIT | this.outerCallingListActivity == orig(this.outerCallingListActivity)
+ onPostExecute:::EXIT | this.outerCallingListActivity.resources == orig(this.outerCallingListActivity.resources)
---
+ onPreExecute:::EXIT | this.dialog.showing == true
+ onPreExecute:::EXIT | this.dialog.title has only one value
```

## MISUSE → FIXED（LLM修正による変化）
```diff
---
- SimpleSecureBrowser:::OBJECT:::OBJECT | this.dialog.showing == true
---
+ invokeOnPostExecute:::ENTER | result has only one value
+ invokeOnPostExecute:::ENTER | result.Cookies == null
+ invokeOnPostExecute:::ENTER | result.HTML_text has only one value
+ invokeOnPostExecute:::ENTER | result.lastcalledURL == null
+ invokeOnPostExecute:::ENTER | result.redirectURL has only one value
+ invokeOnPostExecute:::EXIT | result.Cookies == null
+ invokeOnPostExecute:::EXIT | result.Cookies == orig(result.Cookies)
+ invokeOnPostExecute:::EXIT | result.HTML_text == orig(result.HTML_text)
+ invokeOnPostExecute:::EXIT | result.HTML_text has only one value
+ invokeOnPostExecute:::EXIT | result.lastcalledURL == null
+ invokeOnPostExecute:::EXIT | result.lastcalledURL == orig(result.lastcalledURL)
+ invokeOnPostExecute:::EXIT | result.redirectURL == orig(result.redirectURL)
+ invokeOnPostExecute:::EXIT | result.redirectURL has only one value
+ invokeOnPostExecute:::EXIT | this.instance == orig(this.instance)
+ invokeOnPostExecute:::EXIT | this.instance.getClass().getName() == orig(this.instance.getClass().getName())
+ invokeOnPostExecute:::EXIT | this.targetClass == orig(this.targetClass)
---
+ onPostExecute:::ENTER | result has only one value
+ onPostExecute:::ENTER | result.Cookies == null
+ onPostExecute:::ENTER | result.HTML_text has only one value
+ onPostExecute:::ENTER | result.lastcalledURL == null
+ onPostExecute:::ENTER | result.redirectURL has only one value
+ onPostExecute:::ENTER | this.dialog has only one value
+ onPostExecute:::ENTER | this.dialog.showing == false
+ onPostExecute:::EXIT | result.Cookies == null
+ onPostExecute:::EXIT | result.Cookies == orig(result.Cookies)
+ onPostExecute:::EXIT | result.HTML_text == orig(result.HTML_text)
+ onPostExecute:::EXIT | result.HTML_text has only one value
+ onPostExecute:::EXIT | result.lastcalledURL == null
+ onPostExecute:::EXIT | result.lastcalledURL == orig(result.lastcalledURL)
+ onPostExecute:::EXIT | result.redirectURL == orig(result.redirectURL)
+ onPostExecute:::EXIT | result.redirectURL has only one value
+ onPostExecute:::EXIT | this.dialog == orig(this.dialog)
+ onPostExecute:::EXIT | this.dialog has only one value
+ onPostExecute:::EXIT | this.dialog.showing == false
+ onPostExecute:::EXIT | this.dialog.showing == orig(this.dialog.showing)
+ onPostExecute:::EXIT | this.dialog.title == orig(this.dialog.title)
+ onPostExecute:::EXIT | this.outerCallingActivity == orig(this.outerCallingActivity)
+ onPostExecute:::EXIT | this.outerCallingListActivity == orig(this.outerCallingListActivity)
+ onPostExecute:::EXIT | this.outerCallingListActivity.resources == orig(this.outerCallingListActivity.resources)
---
+ onPreExecute:::EXIT | this.dialog.showing == true
```

## ORIGINAL vs FIXED（残存する差分）
```diff
---
+ SimpleSecureBrowser:::OBJECT:::OBJECT | this.dialog.title has only one value
---
- onPostExecute:::ENTER | this.dialog.title has only one value
---
- onPostExecute:::EXIT | this.dialog.title has only one value
---
+ onPostExecute:::EXIT | this.dialog.title == orig(this.dialog.title)
---
- onPreExecute:::EXIT | this.dialog.title has only one value
```

