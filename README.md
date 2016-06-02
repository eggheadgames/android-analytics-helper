[![Circle CI](https://circleci.com/gh/eggheadgames/android-analytics-helper.svg?style=svg)](https://circleci.com/gh/eggheadgames/android-analytics-helper)
[![Release](https://jitpack.io/v/eggheadgames/android-analytics-helper.svg)](https://jitpack.io/#eggheadgames/android-analytics-helper)
<a target="_blank" href="https://android-arsenal.com/api?level=15"><img src="https://img.shields.io/badge/API-15%2B-orange.svg"></a>
[![GitHub license](https://img.shields.io/badge/license-MIT-lightgrey.svg)](https://github.com/eggheadgames/android-analytics-helper/blob/master/LICENSE)

# android-analytics-helper
Use `init(Context context, int trackingIdResource, int globalTrackerXml, boolean dryRun)` after instance creation of `Analytics`.

For removing warning from Lint about unused string `ga_trackingId` we use it from string values and from `globalTrackedXml`

```
  String trackingId = context.getString(trackingIdResource);
  if(trackingId.isEmpty()) {
    this.mTracker = analytics.newTracker(globalTrackerXml);
  } else {
    this.mTracker = analytics.newTracker(trackingId);
  }
```
