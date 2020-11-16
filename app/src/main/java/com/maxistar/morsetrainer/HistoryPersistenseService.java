package com.maxistar.morsetrainer;

import android.app.Activity;
import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class HistoryPersistenseService {

    private static final String HISTORY_FILENAME = "history";

    public Map<Character, LetterStatistic> getLearningInfo(Context context) {
        HashMap<Character, LetterStatistic> map = (HashMap<Character, LetterStatistic>) this
                .readObjectFromFile(context);
        if (map == null) {
            map = new HashMap<>();
        }
        return map;
    }

    public void saveLearningInfo(Context context, Map<Character, LetterStatistic> data) {
        writeObjectToFile(context, data);
    }

    private Object readObjectFromFile(Context context) {
        ObjectInputStream objectIn = null;
        Object object = null;
        try {
            FileInputStream fileIn = context.openFileInput(HISTORY_FILENAME);
            objectIn = new ObjectInputStream(fileIn);
            object = objectIn.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (objectIn != null) {
                try {
                    objectIn.close();
                } catch (IOException e) {
                    // do nowt
                }
            }
        }
        return object;
    }

    private void writeObjectToFile(Context context, Object object) {
        ObjectOutputStream objectOut = null;
        try {
            FileOutputStream fileOut = context.openFileOutput(
                HISTORY_FILENAME,
                Activity.MODE_PRIVATE
            );
            objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(object);
            fileOut.getFD().sync();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (objectOut != null) {
                try {
                    objectOut.close();
                } catch (IOException e) {
                    // do nowt
                }
            }
        }
    }
}
