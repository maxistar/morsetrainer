package com.maxistar.morsetrainer;

public class MorseCode {
    String code;
    int sound_res;
    int singing;

    MorseCode(String code, int sound_res) {
        this.code = code;
        this.sound_res = sound_res;
        this.singing = 0;
    }

    MorseCode(String code, int sound_res, int singing_res) {
        this.code = code;
        this.sound_res = sound_res;
        this.singing = singing_res;
    }
}
