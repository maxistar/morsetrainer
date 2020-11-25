package com.maxistar.morsetrainer;

public class ServiceLocator {
    private static ServiceLocator instance = null;

    private ServiceLocator() {}

    public static ServiceLocator getInstance() {
        if (instance == null) {
            synchronized(ServiceLocator.class) {
                instance = new ServiceLocator();
            }
        }
        return instance;
    }

    SoundGenerator getSoundGenerator() {
        return new SoundGenerator();
    }

    HistoryPersistenseService getHistoryPersistenseSerice() {
        return new HistoryPersistenseService();
    }

    TrackerService getTrackerService() {
        return new TrackerService();
    }

    SoundPlayer getSoundPlayer() {
        return new SoundPlayer();
    }
}
