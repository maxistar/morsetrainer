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
}
