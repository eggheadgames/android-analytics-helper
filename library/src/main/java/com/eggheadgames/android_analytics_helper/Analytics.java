package com.eggheadgames.android_analytics_helper;

import android.content.Context;
import android.util.Log;

import com.amazonaws.mobileconnectors.amazonmobileanalytics.AnalyticsEvent;
import com.amazonaws.mobileconnectors.amazonmobileanalytics.InitializationException;
import com.amazonaws.mobileconnectors.amazonmobileanalytics.MobileAnalyticsManager;

import java.util.HashMap;
import java.util.Map;

public class Analytics {

    /* EVENTS*/
    public static final String PUZZLE_COMPLETED = "Puzzle Completed";
    public static final String ERASE_ALL_EVENT = "Erase All";
    public static final String IAP_EVENT = "IAP Event";
    public static final String SOUND_EVENT = "Sound";
    public static final String INTERNAL_ERROR = "Internal error";
    public static final String EXCEPTION = "Exception";
    public static final String SCREEN_EVENT = "Screen Seen";

    /* SCREEN NAMES */
    public static final String PUZZLE_SCREEN = "puzzle";
    public static final String SCREEN_HELP = "Show Help";

    /* VALUES NAMES */
    public static final String PUZZLE_ID = "puzzle_id";
    public static final String HINT_COUNT = "hint_count";
    public static final String TOTAL_SECONDS = "total_seconds";
    public static final String VOLUME_STATE = "volume_state";
    public static final String LOCATION = "location";
    public static final String MESSAGE = "message";
    public static final String FATAL = "fatal";
    public static final String THREAD_NAME = "thread_name";
    public static final String DESCRIPTION = "description";
    public static final String VOLUME = "volume";
    public static final String SCREEN_NAME = "screen_name";

    private MobileAnalyticsManager analytics;

    public void init(Context context, String appId, String cognitoId) {
        try {
            analytics = MobileAnalyticsManager.getOrCreateInstance(
                    context.getApplicationContext(),
                    appId, //Amazon Mobile Analytics App ID
                    cognitoId //Amazon Cognito Identity Pool ID
            );
        } catch (InitializationException ex) {
            Log.e(this.getClass().getName(), "Failed to initialize Amazon Mobile Analytics", ex);
        }
    }

    public void pause() {
        if(analytics != null) {
            analytics.getSessionClient().pauseSession();
            analytics.getEventClient().submitEvents();
        }
    }

    public void resume() {
        if(analytics != null) {
            analytics.getSessionClient().resumeSession();
        }
    }


    public void logEraseAll() {
        logEvent(ERASE_ALL_EVENT);
    }

    public void logPurchasedVolume(String sku) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put(VOLUME, sku);
        logEvent(IAP_EVENT, attributes);
    }

    public void logCompletedPuzzle(int quoteId, int hintsTaken, int timeTaken) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put(PUZZLE_ID, String.valueOf(quoteId));
        attributes.put(HINT_COUNT, String.valueOf(hintsTaken));
        attributes.put(TOTAL_SECONDS, String.valueOf(timeTaken));
        logEvent(PUZZLE_COMPLETED, attributes);
    }

    public void logPuzzleEvent(String eventName, String puzzleId) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put(PUZZLE_ID, String.valueOf(puzzleId));
        logEvent(eventName, attributes);
    }

    public void logSoundEnabled(boolean enable) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put(VOLUME_STATE, enable ? "On" : "Off");
        logEvent(SOUND_EVENT, attributes);
    }

    public void logShowHelp() {
        logScreen(SCREEN_HELP);
    }

    public void logInternalError(String location, String message) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put(LOCATION, location);
        attributes.put(MESSAGE, message);
        logEvent(INTERNAL_ERROR, attributes);
    }

    public void logException(Exception exception, boolean fatal) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put(FATAL, String.valueOf(fatal));
        attributes.put(THREAD_NAME, Thread.currentThread().getName());
        attributes.put(DESCRIPTION, exception.getMessage());
        logEvent(EXCEPTION, attributes);
    }

    public void logScreen(String screenName) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put(SCREEN_NAME, screenName);
        logEvent(SCREEN_EVENT, attributes);
    }

    public void logEvent(String eventName, Map<String, String> attributes) {
        AnalyticsEvent event = analytics.getEventClient().createEvent(eventName);
        if (attributes != null) {
            for(Map.Entry<String, String> item : attributes.entrySet()) {
                event.addAttribute(item.getKey(), item.getValue());
            }
        }
        analytics.getEventClient().recordEvent(event);
    }

    public void logEvent(String eventName) {
        logEvent(eventName, null);
    }
}
