package com.maxistar.morsetrainer;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class TrackerService {
    protected Tracker mTracker = null;

    TrackerService() {
    }

    public void initTracker(MorseApplication application) {
        mTracker = application.getDefaultTracker();
    }

    public void track(String name) {
        mTracker.setScreenName(name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

}
