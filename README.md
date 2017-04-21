[![Circle CI](https://circleci.com/gh/eggheadgames/android-analytics-helper.svg?style=svg)](https://circleci.com/gh/eggheadgames/android-analytics-helper)
[![Release](https://jitpack.io/v/eggheadgames/android-analytics-helper.svg)](https://jitpack.io/#eggheadgames/android-analytics-helper)
<a target="_blank" href="https://android-arsenal.com/api?level=15"><img src="https://img.shields.io/badge/API-15%2B-orange.svg"></a>
[![GitHub license](https://img.shields.io/badge/license-MIT-lightgrey.svg)](https://github.com/eggheadgames/android-analytics-helper/blob/master/LICENSE)

# android-analytics-helper
Use `init(Context context, String appId, String cognitoId)` to initialize newly created instance of `Analytics` class.

where

`appId` - Amazon Mobile Analytics App ID.

`cognitoId` - Amazon Cognito Identity Pool ID.

Amazon Analytics does not send reported events automatically, instead - developer is responsible for starting/pausing analytics session.
Current library has handy helper methods (`pause()` and `resume()`) to pause/resume analytics session. The most convenient place to use them are `onPause`/`onResume` methods of your Activity.


