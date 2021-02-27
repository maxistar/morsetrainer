package com.maxistar.morsetrainer.model;

public class MorseCode {
    public String code;
    public int sound_res;
    public int singing;

    public MorseCode(String code, int sound_res) {
        this.code = code;
        this.sound_res = sound_res;
        this.singing = 0;
    }

    public MorseCode(String code, int sound_res, int singing_res) {
        this.code = code;
        this.sound_res = sound_res;
        this.singing = singing_res;
    }
}
