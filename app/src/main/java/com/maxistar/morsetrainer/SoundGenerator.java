package com.maxistar.morsetrainer;

import android.app.Activity;
import android.content.Context;
import android.media.SoundPool;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SoundGenerator {

    /** Called when the activity is first created. */
    private final double dash_duration = 0.5; // seconds
    private final double dip_duration = 0.25; // seconds
    private final double pause_duration = 0.25; // seconds
    private final int sampleRate = 8000;

    private final int pause_numSamples = (int) (sampleRate * pause_duration);
    //private final double pause_sample[] = new double[pause_numSamples];

    private final byte[] generatedSndPause = new byte[2 * pause_numSamples];

    private final int dash_numSamples = (int) (sampleRate * dash_duration);
    private final byte[] generatedSndDash = new byte[2 * dash_numSamples];

    private final int dip_numSamples = (int) (sampleRate * dip_duration);
    private final byte[] generatedSndDip = new byte[2 * dip_numSamples];

    boolean generated = false; // shows if sounds generated


    void generateSounds() {
        if (generated)
            return;

        double[] dip_sample = new double[dip_numSamples];
        // hz
        double freqOfTone = 440;
        for (int i = 0; i < dip_numSamples; ++i) {
            dip_sample[i] = Math.sin(2 * Math.PI * i
                    / (sampleRate / freqOfTone));
        }
        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (final double dVal : dip_sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSndDip[idx++] = (byte) (val & 0x00ff);
            generatedSndDip[idx++] = (byte) ((val & 0xff00) >>> 8);
        }

        double[] dash_sample = new double[dash_numSamples];
        for (int i = 0; i < dash_numSamples; ++i) {
            dash_sample[i] = Math.sin(
                    2 * Math.PI * i / (sampleRate / freqOfTone)
            );
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        idx = 0;
        for (final double dVal : dash_sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSndDash[idx++] = (byte) (val & 0x00ff);
            generatedSndDash[idx++] = (byte) ((val & 0xff00) >>> 8);
        }

        double[] space_sample = new double[pause_numSamples];
        for (int i = 0; i < pause_numSamples; ++i) {
            space_sample[i] = 0;
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        idx = 0;
        for (final double dVal : space_sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSndPause[idx++] = (byte) (val & 0x00ff);
            generatedSndPause[idx++] = (byte) ((val & 0xff00) >>> 8);
        }

    }

    void initSounds(Context context) {
        File file = context.getFileStreamPath("_dash.wav");
        if (!file.exists()) {
            createDashSound(context);
        }

        file = context.getFileStreamPath("_dip.wav");
        if (!file.exists()) {
            createDipSound(context);
        }
    }

    void createDipSound(Context context) {
        generateSounds();
        saveWav(context, generatedSndDip, "_dip.wav");
    }

    void createDashSound(Context context) {
        generateSounds();
        saveWav(context, generatedSndDash, "_dash.wav");
    }



    void saveWav(Context context, byte[] buffer, String filename) {
        DataOutputStream out;
        try {
            FileOutputStream fileOut = context
                    .openFileOutput(filename, Activity.MODE_PRIVATE);

            out = new DataOutputStream(fileOut);

            WaveHeader header = new WaveHeader(WaveHeader.FORMAT_PCM, (short)1, sampleRate, (short)16, buffer.length);

            header.write(out);

            out.write(buffer);

            fileOut.getFD().sync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String getMorseCodeFilename(String morse_code) {
        String res = "_" + morse_code; // not sure so just clone it
        res = res.replace('·', 'p');
        res = res.replace('-', 't');
        res = res + ".wav";
        return res;
    }

    int getMorseSound(SoundPool pool, Context context, String morse_code) {
        // make filename
        String filename = this.getMorseCodeFilename(morse_code);
        generateSounds();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        File file = context.getFileStreamPath(filename);
        if (!file.exists()) {
            // create it
            try {
                for (int i = 0; i < morse_code.length(); i++) {
                    if (i != 0) { // add pause
                        outputStream.write(this.generatedSndPause);
                    }
                    char c = morse_code.charAt(i);
                    if (c == '·') {
                        outputStream.write(this.generatedSndDip);
                    } else { // dash
                        outputStream.write(this.generatedSndDash);
                    }
                }
                saveWav(context, outputStream.toByteArray(), filename);
                // createSound();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return pool.load(file.getAbsolutePath(), 1);
    }

}
