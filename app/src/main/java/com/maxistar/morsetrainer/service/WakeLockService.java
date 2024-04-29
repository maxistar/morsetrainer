package com.maxistar.morsetrainer.service;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;
import android.util.Log;


public class WakeLockService {

    PowerManager.WakeLock fullWakeLock = null;

    final static String wakelock_tag = "morsetrainer:wakelog";

    public void acquireLock(Context context) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        fullWakeLock = powerManager.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), wakelock_tag);

        try {
            fullWakeLock.acquire(10 * 60 * 1000L /*10 minutes*/);

            KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
            keyguardLock.disableKeyguard();

        } catch (Exception e) {
            Log.e("ERROR", e.toString());
        }
    }

    public void releaseLock() {
        if (fullWakeLock == null) {
            return;
        }
        try {
            fullWakeLock.release();
        } catch (Exception e) {
            Log.e("ERROR", e.toString());
        }

    }

}
