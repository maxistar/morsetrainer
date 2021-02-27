package com.maxistar.morsetrainer;

import java.io.Serializable;

public class LetterStatistic implements Serializable {
    public static final long serialVersionUID = 1L;
    public int count_corrects = 0;
    public boolean learned = false;
    public int count_tries = 0;
}
