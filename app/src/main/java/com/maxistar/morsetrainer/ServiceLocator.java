package com.maxistar.morsetrainer;

import com.maxistar.morsetrainer.service.WakeLockService;

public class ServiceLocator {
    private static ServiceLocator instance = null;

    private WakeLockService wakeLockService = null;

    private ServiceLocator() {}

    public static ServiceLocator getInstance() {
        if (instance == null) {
            synchronized(ServiceLocator.class) {
                instance = new ServiceLocator();
            }
        }
        return instance;
    }

    public SoundGenerator getSoundGenerator() {
        return new SoundGenerator();
    }

    public HistoryPersistenseService getHistoryPersistenseSerice() {
        return new HistoryPersistenseService();
    }

    public TrackerService getTrackerService() {
        return new TrackerService();
    }

    public SoundPlayer getSoundPlayer() {
        return new SoundPlayer();
    }

    public WakeLockService getWakeLockService() {
        if (wakeLockService == null) {
            wakeLockService = new WakeLockService();
        }
        return wakeLockService;
    }

}
