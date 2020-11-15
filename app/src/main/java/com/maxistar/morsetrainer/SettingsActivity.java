package com.maxistar.morsetrainer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.Locale;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

	protected Tracker mTracker = null;
	Preference mVersion;
	SettingsService settingsService;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		settingsService = SettingsService.getInstance(getApplicationContext());

		mVersion = this.findPreference("version_name");
		PackageInfo pInfo;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			mVersion.setSummary(pInfo.versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		// Obtain the shared Tracker instance.
		MorseApplication application = (MorseApplication) getApplication();
		mTracker = application.getDefaultTracker();

		Preference mPrivacy;
		mPrivacy = this.findPreference("privacy");

		mPrivacy.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("https://morze.maxistar.me/legal/privatepolicy/"));
				startActivity(intent);
				return false;
			}
		});
	}
	
    @Override
    protected void onResume() {
        super.onResume();
		mTracker.setScreenName("SettingsActivity");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());

		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

		// Unregister the listener whenever a key changes
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);    
    }

	protected void showToast(String toast_str) {
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, toast_str, duration);
		toast.show();
	}

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

		// check if all kinds of symbols are deselected and show message
		if (!sharedPreferences.getBoolean("learn_latinica", true)
				&& !sharedPreferences.getBoolean("learn_numbers", true)
				&& !sharedPreferences.getBoolean("learn_punctuation_signs", true)
				&& !sharedPreferences.getBoolean("learn_cyrilics", false)) {

			showToast(l(R.string.nothing_selected));
		}

		if (SettingsService.SETTING_LANGUAGE.equals(key)) {
			String lang = sharedPreferences.getString(SettingsService.SETTING_LANGUAGE, SettingsService.EN);
			setLocale(lang);
			SettingsService.setLanguageChangedFlag();
		}
		settingsService.reloadSettings(this.getApplicationContext());

    }

	public void setLocale(String lang) {
		Locale locale2 = new Locale(lang);
		Locale.setDefault(locale2);
		Configuration config2 = new Configuration();
		config2.locale = locale2;

		// updating locale
		//getApplicationContext().getResources().updateConfiguration(config2, null);
		getBaseContext().getResources().updateConfiguration(config2, null);
		showPreferences();
	}

	protected void showPreferences(){
		Intent intent = new Intent(this, SettingsActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	/**
	 * Returns translation
	 *
	 * @param id Text ID
	 * @return String
	 */
	String l(int id) {
		return getBaseContext().getResources().getString(id);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}
		return super.onKeyDown(keyCode, event);
	}
}