package com.maxistar.morsetrainer;

import java.io.Serializable;

public class LetterStatistic implements Serializable {
    private static final long serialVersionUID = 1L;
    // Character character = null;
    int count_corrects = 0;
    boolean learned = false;
    int count_tries = 0;
}
