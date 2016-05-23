package com.eggheadgames.android_analytics_helper;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;

public class Analytics {

    /* CATEGORIES */
    public static final String CATEGORY_PUZZLE_EVENT = "Puzzle Event";
    public static final String CATEGORY_UI_EVENT = "UI Event";
    public static final String CATEGORY_ERASE_ALL = "Erase All";
    public static final String CATEGORY_IAP_EVENT = "IAP Event";
    public static final String CATEGORY_SOUND = "Sound";
    public static final String CATEGORY_INTERNAL_ERROR = "Internal error";

    /* SCREEN NAMES */
    public static final String PUZZLE_SCREEN = "puzzle";
    public static final String SCREEN_HELP = "Show Help";

    /* VALUES NAMES */
    public static final String PUZZLE_ID = "puzzle_id";
    public static final String HINT_COUNT = "hint_count";
    public static final String TOTAL_SECONDS = "total_seconds";

    /* ACTIONS */
    public static final String ACTION_PUZZLE_COMPLETED = "completed";
    public static final String ACTION_VOLUME = "volume";

    private Tracker mTracker;
    private Context mContext;

    public void init(Context context, int trackingIdResource, int globalTrackerXml, boolean dryRun) {
        mContext = context;
        final GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
        analytics.setDryRun(dryRun);
        final String trackingId = context.getString(trackingIdResource);
        if (trackingId.isEmpty()) {
            mTracker = analytics.newTracker(globalTrackerXml);
        } else {
            mTracker = analytics.newTracker(trackingId);
        }
    }

    public void logUiEvent(String action, String label) {
        logEvent(CATEGORY_UI_EVENT, action, label);
    }

    public void logEraseAll() {
        logUiEvent(CATEGORY_ERASE_ALL, "true");
    }

    public void logPurchasedVolume(String sku) {
        logEvent(CATEGORY_IAP_EVENT, ACTION_VOLUME, sku);
    }

    public void logCompletedPuzzle(int quoteId, int hintsTaken, int timeTaken) {
        HitBuilders.EventBuilder builder = new HitBuilders.EventBuilder()
                .setCategory(CATEGORY_PUZZLE_EVENT)
                .setAction(ACTION_PUZZLE_COMPLETED);

        builder.set(PUZZLE_ID, String.valueOf(quoteId));
        builder.set(HINT_COUNT, String.valueOf(hintsTaken));
        builder.set(TOTAL_SECONDS, String.valueOf(timeTaken));

        mTracker.send(builder.build());
    }

    public void logPuzzleEvent(String action, String puzzleId) {
        HitBuilders.EventBuilder builder = new HitBuilders.EventBuilder()
                .setCategory(CATEGORY_PUZZLE_EVENT)
                .setAction(action);

        builder.set(PUZZLE_ID, String.valueOf(puzzleId));
        mTracker.send(builder.build());
    }

    public void logSoundEnabled(boolean enable) {
        logUiEvent(CATEGORY_SOUND, enable ? "On" : "Off");
    }

    public void logShowHelp() {
        logScreen(SCREEN_HELP);
    }

    public void logInternalError(String location, String message) {
        logEvent(CATEGORY_INTERNAL_ERROR, location, message);
    }

    public void logException(Exception exception, boolean fatal) {
        mTracker.send(new HitBuilders.ExceptionBuilder().setDescription(
                new StandardExceptionParser(mContext, null).getDescription(Thread.currentThread().getName(), exception))
                .setFatal(fatal).build());
    }

    public void logScreen(String screenName) {
        mTracker.setScreenName(screenName);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void logEvent(String category, String action, String label) {
        HitBuilders.EventBuilder builder = new HitBuilders.EventBuilder()
                .setCategory(category).setAction(action).setLabel(label);
        mTracker.send(builder.build());
    }
}
