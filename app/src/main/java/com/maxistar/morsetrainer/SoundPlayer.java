package com.maxistar.morsetrainer;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import java.io.File;

public class SoundPlayer {

    private static final float LOW_VOLUME = 0.5f;

    private static final float FULL_VOLUME = 0.1f;

    SoundPool pool;

    int dash_sound = 0;
    int dip_sound = 0;
    int correct_sound = 0;
    int wrong_sound = 0;

    boolean presented = false;

    void initSounds(Context context) {
        presented = isAudioPresented(context);

        if (!presented) {
            return;
        }

        try {
            pool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

            File file = context.getFileStreamPath("_dash.wav");
            dash_sound = pool.load(file.getAbsolutePath(), 1);

            file = context.getFileStreamPath("_dip.wav");
            dip_sound = pool.load(file.getAbsolutePath(), 1);

            correct_sound = pool.load(context, R.raw.dialog_information, 1);
            wrong_sound = pool.load(context, R.raw.dialog_error, 1);
        } catch (Exception e) {
            presented = false;
        }
    }

    private boolean isAudioPresented(Context context) {
        return speakerIsSupported(context);
    }

    public final boolean speakerIsSupported(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PackageManager packageManager = context.getPackageManager();
            // The results from AudioManager.getDevices can't be trusted unless the device
            // advertises FEATURE_AUDIO_OUTPUT.
            if (!packageManager.hasSystemFeature(PackageManager.FEATURE_AUDIO_OUTPUT)) {
                return false;
            }
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            AudioDeviceInfo[] devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
            for (AudioDeviceInfo device : devices) {
                if (device.getType() == AudioDeviceInfo.TYPE_BUILTIN_SPEAKER) {
                    return true;
                }
            }
        } else {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            return audioManager.isBluetoothA2dpOn() ||
                    audioManager.isSpeakerphoneOn() ||
                    audioManager.isWiredHeadsetOn();
        }
        return false;
    }

    boolean isSoundPresented() {
        return presented;
    }

    void playDitSound() {
        playSoundById(this.dip_sound, LOW_VOLUME);
    }

    void playDashSound() {
        playSoundById(this.dash_sound, LOW_VOLUME);
    }

    void playSound(int streamId) {
        playSoundById(streamId, FULL_VOLUME);
    }

    void playWrongSound() {
        playSoundById(this.wrong_sound, FULL_VOLUME);
    }

    void playCorrectSound() {
        playSoundById(this.correct_sound, FULL_VOLUME);
    }

    void playSoundById(int soundId, float volume) {
        if (!presented) {
            return;
        }

        try {
            pool.play(soundId, volume, volume, 1, 0, 1);
        } catch (Exception e) {
            //
        }
    }

    void unload(int streamId) {
        if (!presented) {
            return;
        }

        pool.unload(streamId);
    }

    SoundPool getPool() {
        return pool;
    }
}
