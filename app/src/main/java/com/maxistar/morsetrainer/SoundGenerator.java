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

    private final double dashDuration = 0.5; // seconds
    private final double dipDuration = 0.25; // seconds
    private final double pauseDuration = 0.25; // seconds
    private final int sampleRate = 8000;
    private final double freqOfTone = 440; // hz
    private final int numberDebounce = 200;

    private final int pauseNumSamples = (int) (sampleRate * pauseDuration);

    private final byte[] generatedSndPause = new byte[2 * pauseNumSamples];

    private final int dashNumSamples = (int) (sampleRate * dashDuration);
    private final byte[] generatedSndDash = new byte[2 * dashNumSamples];

    private final int dipNumSamples = (int) (sampleRate * dipDuration);
    private final byte[] generatedSndDip = new byte[2 * dipNumSamples];

    private boolean generated = false; // shows if sounds generated

    public void generateSounds() {
        if (generated) {
            return;
        }

        double[] dip_sample = new double[dipNumSamples];
        int i;
        for (i = 0; i < dipNumSamples; i++) {
            dip_sample[i] = Math.sin(2 * Math.PI * i * freqOfTone / sampleRate);
        }
        debounce(dip_sample, i-1);

        convertTo16BitPcb(dip_sample, generatedSndDip);

        double[] dash_sample = new double[dashNumSamples];
        for (i = 0; i < dashNumSamples; i++) {
            dash_sample[i] = Math.sin(2 * Math.PI * i * freqOfTone / sampleRate);
        }
        debounce(dash_sample, i-1);


        convertTo16BitPcb(dash_sample, generatedSndDash);

        double[] space_sample = new double[pauseNumSamples];
        for (i = 0; i < pauseNumSamples; i++) {
            space_sample[i] = 0;
        }
        convertTo16BitPcb(space_sample, generatedSndPause);

        generated = true;
    }

    private void debounce(double[] sample, int numberSamples) {
        for (int i = 0; i < numberDebounce; i++) {
            sample[numberSamples - i] = sample[numberSamples - i] * ((1.0 * i) / numberDebounce);
        }
    }

    private void convertTo16BitPcb(double[] sample, byte[] generatedSnd) {
        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (double dVal : sample) {
            // scale to maximum amplitude
            short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
        }
    }

    public void initSounds(Context context) {
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

    /**
     *
     * @param pool Pool
     * @param context Content
     * @param morse_code Morse Code
     * @return int Code of the sound
     */
    public int getMorseSound(SoundPool pool, Context context, String morse_code) {
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
