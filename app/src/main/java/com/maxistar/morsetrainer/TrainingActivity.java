package com.maxistar.morsetrainer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Stack;

public class TrainingActivity extends Activity
{
	private static final int PROGRESS = 2;
	private static final int SETTINGS = 3;
	private static final int REQUEST_SETTINGS = 3;

	private static final String TRACKING_ACTIITY_NAME = "TrainingActivity";

	PowerManager.WakeLock wl;
	
	Handler handler = new Handler();
	TextView letter;
	TextView morze_text;
	TextView hint_text;
	TextView type_text;
	TextView singing_text;

	Map<Character, LetterStatistic> history = null;
	Stack<LetterInfo> letters = null;
	Stack<LetterInfo> letters_done = null;

	boolean is_error = false; // true if no errors done

	LetterInfo current = null;
	String user_code = "";
	int repeat = 0;
	int correct_sound = 0;
	int wrong_sound = 0;
	SoundPool pool;
	int dash_sound = 0;
	int dip_sound = 0;

	SettingsService settingsService;

	private SoundGenerator soundGenerator = ServiceLocator.getInstance().getSoundGenerator();

	private HistoryPersistenseService historyPersistenseService
			= ServiceLocator.getInstance().getHistoryPersistenseSerice();

	private TrackerService trackerService = ServiceLocator.getInstance().getTrackerService();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		settingsService = SettingsService.getInstance(this.getApplicationContext());
		settingsService.applyLocale(this.getBaseContext());

		pool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

		soundGenerator.initSounds(this.getApplicationContext());


		File file = this.getApplicationContext().getFileStreamPath("_dash.wav");
		dash_sound = pool.load(file.getAbsolutePath(), 1);

		file = this.getApplicationContext().getFileStreamPath("_dip.wav");
		dip_sound = pool.load(file.getAbsolutePath(), 1);

		correct_sound = pool.load(this.getApplicationContext(),R.raw.dialog_information,1);
		wrong_sound = pool.load(this.getApplicationContext(),R.raw.dialog_error,1);

		setContentView(R.layout.activity_trainig);

		this.history = historyPersistenseService.getLearningInfo(this.getApplicationContext());

		letter = this.findViewById(R.id.textView1);
		morze_text = this.findViewById(R.id.textView2);
		hint_text = this.findViewById(R.id.textView3);
		type_text = this.findViewById(R.id.textView4);
		singing_text = this.findViewById(R.id.singing);

		Button b1 = this.findViewById(R.id.button1);
		b1.setOnClickListener(v -> clickDash());

		Button b2 = this.findViewById(R.id.button2);
		b2.setOnClickListener(v -> clickDit());

		hint_text.setVisibility(View.GONE);

		initLetters();

		// use stack?
		letters_done = new Stack<>();
		current = letters.pop();
		showLetter(); // show it

		trackerService.initTracker((MorseApplication) getApplication());
	}

	void showLetter() {

		if (Constants.latins.containsKey(current.character)) {
			this.type_text.setText(R.string.latins);
		} else if (Constants.numbers.containsKey(current.character)) {
			this.type_text.setText(R.string.number);
		} else if (Constants.characters.containsKey(current.character)) {
			this.type_text.setText(R.string.character);
		} else if (Constants.cyrilics.containsKey(current.character)) {
			this.type_text.setText(R.string.cyrilic);
		} else {
			this.type_text.setText(R.string.unknown);
		}

		letter.setText(String.valueOf(current.character));
		this.morze_text.setText("");
		this.morze_text.setTextColor(this.getResources()
				.getColor(R.color.white));
		this.singing_text.setText("");

		pool.play(current.stream_id, 1, 1, 1, 0, 1);
	}
	
	protected void addMorseCodes(ArrayList<LetterInfo> letters, Map<Character, MorseCode> chars) {
		LetterInfo l;
		LetterStatistic s;

		for (Map.Entry<Character, MorseCode> entry : chars.entrySet()) {
			l = new LetterInfo();
			l.character = entry.getKey();
			l.morse_code = entry.getValue().code;
			l.sound_res = entry.getValue().sound_res;
			l.morse_singing_id = entry.getValue().singing;

			if (history.containsKey(entry.getKey())) {
				s = history.get(entry.getKey());
				l.count_tries = s.count_tries;
				l.learned = s.learned;
			}
			letters.add(l);
		}
	}

	/**
	 * Returns translation
	 *
	 * @param id ID
	 * @return String
	 */
	String l(int id) {
		return getBaseContext().getResources().getString(id);
	}

	protected void initLetters() {
		ArrayList<LetterInfo> letters = new ArrayList<>();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

		boolean learnLatinica = sharedPreferences.getBoolean(SettingsService.LEARN_LATINICA, true);
		boolean learnNumbers = sharedPreferences.getBoolean(SettingsService.LEARN_NUMBERS, true);
		boolean learnPunctuationSigns = sharedPreferences.getBoolean(SettingsService.LEARN_PUNCTUATION_SIGNS, true);
		boolean learnCirilic = sharedPreferences.getBoolean(SettingsService.LEARN_CYRILICS, false);
		boolean savetyCheck = false;
		if (!learnCirilic && !learnLatinica && !learnNumbers && !learnPunctuationSigns) {
			savetyCheck = true;
		}

		if (savetyCheck || learnLatinica) {
			addMorseCodes(letters, Constants.latins);
		}
		if (learnNumbers) {
			addMorseCodes(letters, Constants.numbers);
		}
		if (learnPunctuationSigns) {
			addMorseCodes(letters, Constants.characters);
		}
		if (learnCirilic) {
			addMorseCodes(letters, Constants.cyrilics);
		}
		// sort list
		// first less shown
		// then shortest
		// then learned
		Collections.sort(letters, (o1, o2) -> {
			if (o1.count_tries != o2.count_tries)
				return o1.count_tries - o2.count_tries;
			if (o1.morse_code.length() != o2.morse_code.length())
				return o1.morse_code.length() - o2.morse_code.length();
			if (o1.learned != o2.learned)
				return o1.learned ? -1 : 1;
			return 0;
		});

		int counter = 0;
		this.letters = new Stack<>();
		for (LetterInfo ss : letters) {
			int count_chars_to_learn = 5;
			if (counter >= count_chars_to_learn)
				break;
			counter++;
			ss.stream_id = pool.load(
					this.getApplicationContext(),
					ss.sound_res,
					1
			);

			ss.morse_sound_id = this.soundGenerator.getMorseSound(
					pool,
					this.getApplicationContext(),
					ss.morse_code
			);

			this.letters.push(ss);
		}
		Collections.reverse(this.letters);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.training_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_settings:
				Intent intent = new Intent(
						this.getBaseContext(),
						SettingsActivity.class
				);
				this.startActivityForResult(intent, REQUEST_SETTINGS);
				return true;
			case R.id.menu_prograss:
				this.startActivity(new Intent(this.getBaseContext(), ProgressActivity.class));
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	void saveHistory() {
		LetterInfo i;
		// pool.release();
		while (!letters_done.empty()) {
			i = letters_done.pop();
			pool.unload(i.stream_id);

			if (this.history.containsKey(i.character)) {
				LetterStatistic s = this.history.get(i.character);
				s.count_tries++;
				if (!i.correct) {
					s.count_corrects = 0;
				} else {
					s.count_corrects++;
					int corrects_to_be_learned = 3;
					if (s.count_corrects >= corrects_to_be_learned) {
						s.learned = true;
					}
				}
			} else {
				LetterStatistic s = new LetterStatistic();
				this.history.put(i.character, s);
			}
		}
		this.historyPersistenseService.saveLearningInfo(this.getApplicationContext(), this.history);
	}

	protected void clickDit() {
		pool.play(this.dip_sound, (float) 0.5, (float) 0.5, 1, 0, 1);
		clickButton('·');
	}

	protected void clickDash() {
		pool.play(this.dash_sound, (float) 0.5, (float) 0.5, 1, 0, 1);
		clickButton('-');
	}

	protected void clickButton(Character ch) {
		this.user_code = this.user_code + ch;
		this.morze_text.setText(this.user_code);
		if (!this.user_code.equals(this.current.morse_code)) {
			// this.morzeText //make red!
			if (!correctSoFar()) {
				pool.play(this.wrong_sound,1,1,1,0,1);
				this.morze_text.setText("");

				if (this.current.morse_singing_id != 0) {
					this.singing_text.setText(this.current.morse_singing_id);
				}
				hint_text.setVisibility(View.VISIBLE);
				hint_text.setText(this.formatCode(this.current.morse_code));
				
				this.current.correct = false;
				this.is_error = true;
				this.user_code = ""; // try again
				handler.postDelayed(() -> pool.play(current.morse_sound_id, 1, 1, 1, 0, 1), 500);
			}
		} else { // done ok
			hint_text.setVisibility(View.GONE);
			this.user_code = "";
			this.morze_text.setTextColor(
				this.getResources().getColor(
					R.color.green
				)
			);
			pool.play(this.correct_sound, 1, 1, 1, 0, 1);
			handler.postDelayed(this::showNextLetter, 1000);
			// shedule new letter
		}
	}
	
	String formatCode(String code){
		//return code.replace('', '');
		return code;
	}

	void showNextLetter() {
		int repeat_to_remember = 3;
		if (this.repeat < repeat_to_remember && this.is_error) {
			this.repeat++;
		} else {
			this.is_error = false;
			if (letters.empty()) {
				// save current statistic
				saveHistory();
				initLetters();
			}
			this.letters_done.push(this.current);
			this.current = letters.pop(); // get new
			repeat = 0;
		}
		this.showLetter();
	}

	protected boolean correctSoFar() {
		if (this.user_code.length() > this.current.morse_code.length())
			return false;
		String substring = this.current.morse_code.substring(0,
				this.user_code.length());// get substring of master string same
											// length as userinput
		return substring.equals(this.user_code);
	}

	@Override
	protected void onResume() {
		super.onResume();

		trackerService.track(TRACKING_ACTIITY_NAME);

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		if (pm != null) {
			wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "morse:MY_WAKE_TAG");
			wl.acquire(1000);
		}
		
		/*
		 * // Use a new tread as this can take a while final Thread thread = new
		 * Thread(new Runnable() { public void run() { genTone();
		 * handler.post(new Runnable() {
		 * 
		 * public void run() { playSound(); } }); } }); thread.start();
		 */
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		wl.release();
	}
}
