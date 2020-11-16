package com.maxistar.morsetrainer;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import java.util.Locale;

public class SettingsService {
    public static final String SETTING_LANGUAGE = "language";

    public static final String LEARN_LATINICA = "learn_latinica";

    public static final String LEARN_NUMBERS = "learn_numbers";

    public static final String LEARN_PUNCTUATION_SIGNS = "learn_punctuation_signs";

    public static final String LEARN_CYRILICS = "learn_cyrilics";

    public static final String EMPTY = "";

    public static final String EN = "en";

    private String language;

    private static boolean languageWasChanged = false;


    private SettingsService(Context context) {
        loadSettings(context);
    }

    private void loadSettings(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        language = sharedPref.getString(SETTING_LANGUAGE, EMPTY);
    }

    public void reloadSettings(Context context) {
        loadSettings(context);
    }

    static public SettingsService getInstance(Context context) {
        return new SettingsService(context);
    }

    private void setSettingValue(String name, String value, Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(name, value);
        editor.apply();
    }

    private void setSettingValue(String name, int value, Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(name, value);
        editor.apply();
    }


    public String getLanguage() {
        return language;
    }

    static public void setLanguageChangedFlag() {
        languageWasChanged = true;
    }

    static public boolean isLanguageWasChanged() {
        boolean value = languageWasChanged;
        languageWasChanged = false;
        return value;
    }

    public void applyLocale(Context context){
        String lang = getLanguage();
        if (EMPTY.equals(lang)) {
            return; //use system default
        }
        Locale locale2 = new Locale(lang);
        Locale.setDefault(locale2);
        Configuration config2 = new Configuration();
        config2.locale = locale2;
        context.getResources().updateConfiguration(config2, null);
    }
}
