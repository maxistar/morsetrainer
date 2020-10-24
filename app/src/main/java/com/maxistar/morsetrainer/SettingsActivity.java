package com.maxistar.morsetrainer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

	protected Tracker mTracker = null;
	Preference mVersion;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		mVersion = this.findPreference("version_name");
		PackageInfo pInfo;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			mVersion.setSummary(pInfo.versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
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
		CharSequence text = toast_str;
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
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

    }

	/**
	 * Returns translation
	 *
	 * @param id
	 * @return
	 */
	String l(int id) {
		return getBaseContext().getResources().getString(id);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			Intent intent = new Intent(this, TrainingActivity.class);
			startActivity(intent);
		}
		return super.onKeyDown(keyCode, event);
	}
}