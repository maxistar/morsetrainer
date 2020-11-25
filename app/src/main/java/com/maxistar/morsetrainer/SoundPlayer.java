package com.maxistar.morsetrainer;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.io.File;

public class SoundPlayer {
    SoundPool pool;

    int dash_sound = 0;
    int dip_sound = 0;
    int correct_sound = 0;
    int wrong_sound = 0;

    void initSounds(Context context) {
        pool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

        File file = context.getFileStreamPath("_dash.wav");
        dash_sound = pool.load(file.getAbsolutePath(), 1);

        file = context.getFileStreamPath("_dip.wav");
        dip_sound = pool.load(file.getAbsolutePath(), 1);

        correct_sound = pool.load(context, R.raw.dialog_information, 1);
        wrong_sound = pool.load(context, R.raw.dialog_error, 1);
    }

    void playDitSound() {
        pool.play(this.dip_sound, (float) 0.5, (float) 0.5, 1, 0, 1);
    }

    void playDashSound() {
        pool.play(this.dash_sound, (float) 0.5, (float) 0.5, 1, 0, 1);
    }

    void playSound(int streamId) {
        pool.play(streamId, 1, 1, 1, 0, 1);
    }

    void playWrongSound() {
        pool.play(this.wrong_sound, 1, 1, 1, 0, 1);
    }

    void playCorrectSound() {
        pool.play(this.correct_sound, 1, 1, 1, 0, 1);
    }

    void unload(int streamId) {
        pool.unload(streamId);
    }

    SoundPool getPool() {
        return pool;
    }
}
