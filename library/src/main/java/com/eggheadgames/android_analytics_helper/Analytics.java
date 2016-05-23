package com.eggheadgames.android_analytics_helper;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;

import java.util.Map;

public class Analytics {

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
        logEvent("UI Event", action, label);
    }

    public void logEraseAll() {
        logUiEvent("Erase All", "true");
    }

    public void logPurchasedVolume(String sku) {
        logEvent("IAP Event", "volume", sku);
    }

    public void logCompletedPuzzle(Map<String, String> map) {
        logPuzzleEvent("completed", map);
    }

    public void logPuzzleEvent(String action, Map<String, String> map) {
        logEvent("Puzzle Event", action, map);
    }

    public void logSoundEnabled(boolean enable) {
        logUiEvent("Sound", enable ? "On" : "Off");
    }

    public void logShowHelp() {
        logScreen("Show Help");
    }

    public void logInternalError(String location, String message) {
        logEvent("Internal error", location, message);
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

    private void logEvent(final String category, final String action, final String label) {
        HitBuilders.EventBuilder builder = new HitBuilders.EventBuilder()
                .setCategory(category).setAction(action).setLabel(label);
        mTracker.send(builder.build());

    }

    private void logEvent(final String category, final String action, final Map<String, String> map) {
        HitBuilders.EventBuilder builder = new HitBuilders.EventBuilder()
                .setCategory(category).setAction(action);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            builder.set(entry.getKey(), entry.getValue());
        }
        mTracker.send(builder.build());
    }
}
