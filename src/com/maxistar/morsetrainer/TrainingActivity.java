package com.maxistar.morsetrainer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TrainingActivity extends Activity {
	private static final int PROGRESS = 2;
	private static final int SETTINGS = 3;
	private static final int REQUEST_PROGRESS = 2;
	private static final int REQUEST_SETTINGS = 3;


	
	
	/** Called when the activity is first created. */
	private final double dash_duration = 0.5; // seconds
	private final double dip_duration = 0.25; // seconds
	private final double pause_duration = 0.25; // seconds
	private final int sampleRate = 8000;

	private final int pause_numSamples = (int) (sampleRate * pause_duration);
	//private final double pause_sample[] = new double[pause_numSamples];

	private final double freqOfTone = 440; // hz

	private final int count_chars_to_learn = 5;
	private final int repeat_to_remember = 3;
	private final int corrects_to_be_learned = 3;

	private final byte generatedSndPause[] = new byte[2 * pause_numSamples];

	private final int dash_numSamples = (int) (sampleRate * dash_duration);
	private final byte generatedSndDash[] = new byte[2 * dash_numSamples];

	private final int dip_numSamples = (int) (sampleRate * dip_duration);
	private final byte generatedSndDip[] = new byte[2 * dip_numSamples];

	boolean generated = false; // shows if sounds generated

	PowerManager.WakeLock wl;
	
	Handler handler = new Handler();
	AudioTrack audioTrack = null;
	TextView letter;
	TextView morze_text;
	TextView hint_text;
	TextView type_text;

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
	SoundPool pool2;
	int dash_sound = 0;
	int dip_sound = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		pool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);


		File file = this.getApplicationContext().getFileStreamPath("_dash.wav");
		if (!file.exists()) {
			createDashSound();
		}
		dash_sound = pool.load(file.getAbsolutePath(), 1);

		file = this.getApplicationContext().getFileStreamPath("_dip.wav");
		if (!file.exists()) {
			createDipSound();
		}
		dip_sound = pool.load(file.getAbsolutePath(), 1);

		correct_sound = pool.load(this.getApplicationContext(),R.raw.dialog_information,1);
		wrong_sound = pool.load(this.getApplicationContext(),R.raw.dialog_error,1);

		setContentView(R.layout.main);

		this.history = getLearningInfo();

		letter = (TextView) this.findViewById(R.id.textView1);
		morze_text = (TextView) this.findViewById(R.id.textView2);
		hint_text = (TextView) this.findViewById(R.id.textView3);
		type_text = (TextView) this.findViewById(R.id.textView4);


		Button b1 = (Button) this.findViewById(R.id.button1);
		b1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clickDash();
			}
		});

		Button b2 = (Button) this.findViewById(R.id.button2);
		b2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clickDit();
			}
		});

		hint_text.setVisibility(View.GONE);

		initLetters();

		// use stack?
		letters_done = new Stack<LetterInfo>();
		current = letters.pop();
		showLetter(); // show it
		
	}

	void showLetter() {

		if (Constants.latins.containsKey(current.character)) {
			this.type_text.setText("latins");
		} else if (Constants.numbers.containsKey(current.character)) {
			this.type_text.setText("number");
		} else if (Constants.characters.containsKey(current.character)) {
			this.type_text.setText("character");
		} else if (Constants.cyrilics.containsKey(current.character)) {
			this.type_text.setText("cyrilic");
		} else {
			this.type_text.setText("unknown");
		}

		letter.setText("" + current.character);
		this.morze_text.setText("");
		this.morze_text.setTextColor(this.getResources()
				.getColor(R.color.white));

		pool.play(current.stream_id, 1, 1, 1, 0, 1);
	}

	protected void showToast(String toast_str) {
		Context context = getApplicationContext();
		CharSequence text = toast_str;
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	
	protected void addMorseCodes(ArrayList<LetterInfo> letters, Map<Character, MorseCode> chars){
		LetterInfo l = null;
		LetterStatistic s = null;

		for (Map.Entry<Character, MorseCode> entry : chars.entrySet()) {
			// System.out.println(entry.getKey() + "/" + entry.getValue());
			l = new LetterInfo();
			l.character = entry.getKey();
			l.morse_code = entry.getValue().code;
			l.sound_res = entry.getValue().sound_res;

			if (history.containsKey(entry.getKey())) {
				s = history.get(entry.getKey());
				l.count_tries = s.count_tries;
				l.learned = s.learned;
			}
			letters.add(l);
		}
	}

	protected void initLetters() {
		ArrayList<LetterInfo> letters = new ArrayList<LetterInfo>();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

		if (sharedPreferences.getBoolean("learn_latinica", true)){
			addMorseCodes(letters,Constants.latins);
		}
		if (sharedPreferences.getBoolean("learn_numbers", true)){
			addMorseCodes(letters,Constants.numbers);
		}
		if (sharedPreferences.getBoolean("learn_punctuation_signs", true)){
			addMorseCodes(letters,Constants.characters);
		}
		if (sharedPreferences.getBoolean("learn_cyrilics", false)){
			addMorseCodes(letters,Constants.cyrilics);
		}
		// sort list
		// first less shown
		// then shortest
		// then learned
		Collections.sort(letters, new Comparator<LetterInfo>() {
			@Override
			public int compare(LetterInfo o1, LetterInfo o2) {
				if (o1.count_tries != o2.count_tries)
					return o1.count_tries - o2.count_tries;
				if (o1.morse_code.length() != o2.morse_code.length())
					return o1.morse_code.length() - o2.morse_code.length();
				if (o1.learned != o2.learned)
					return o1.learned ? -1 : 1;
				return 0;
			}
		});

		int counter = 0;
		this.letters = new Stack<LetterInfo>();
		for (LetterInfo ss : letters) {
			if (counter >= this.count_chars_to_learn)
				break;
			counter++;
			ss.stream_id = pool.load(this.getApplicationContext(),
					ss.sound_res, 1);

			ss.morse_sound_id = this.getMorseSound(ss.morse_code);

			this.letters.push(ss);
		}
		Collections.reverse(this.letters);
 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0, SETTINGS, 0, "Settings");
		
		menu.add(1, PROGRESS, 1, "Progress");
//				.setIcon(R.drawable.settings);

		return super.onCreateOptionsMenu(menu);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case SETTINGS:
			Intent intent = new Intent(this.getBaseContext(),
					SettingsActivity.class);
			this.startActivityForResult(intent, REQUEST_SETTINGS);
			return true;
		case PROGRESS:
			this.startActivity(new Intent(this.getBaseContext(), ProgressActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	

	String getMorseCodeFilename(String morse_code) {
		String res = "_" + morse_code; // not sure so just clone it
		res = res.replace('·', 'p');
		res = res.replace('-', 't');
		res = res + ".wav";
		return res;
	}

	int getMorseSound(String morse_code) {
		// make filename
		String filename = this.getMorseCodeFilename(morse_code);
		generateSounds();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		File file = this.getApplicationContext().getFileStreamPath(filename);
		if (!file.exists()) {
			// create it
			try {
				int i;
				char c;
				for (i = 0; i < morse_code.length(); i++) {
					if (i != 0) { // add pause
						outputStream.write(this.generatedSndPause);
					}
					c = morse_code.charAt(i);
					if (c == '·') {
						outputStream.write(this.generatedSndDip);
					} else { // dash
						outputStream.write(this.generatedSndDash);
					}
				}
				saveWav(outputStream.toByteArray(), filename);
				// createSound();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return pool.load(file.getAbsolutePath(), 1);
	}

	void saveHistory() {
		LetterInfo i = null;
		// pool.release();
		while (!letters_done.empty()) {
			i = letters_done.pop();
			pool.unload(i.stream_id);
			// i.mp.release();
			// i.mp = null;//release Media Player

			if (this.history.containsKey(i.character)) {
				LetterStatistic s = this.history.get(i.character);
				s.count_tries++;
				if (!i.correct) {
					s.count_corrects = 0;
				} else {
					s.count_corrects++;
					if (s.count_corrects >= this.corrects_to_be_learned) {
						s.learned = true;
					}
				}
			} else {
				LetterStatistic s = new LetterStatistic();
				this.history.put(i.character, s);
			}
		}
		this.writeObjectToFile(this, this.history, "history");
	}

	protected Map<Character, LetterStatistic> getLearningInfo() {
		Map<Character, LetterStatistic> map = (HashMap<Character, LetterStatistic>) this
				.readObjectFromFile(this, "history");
		if (map == null) {
			map = new HashMap<Character, LetterStatistic>();
		}
		return map;
	}

	public void writeObjectToFile(Context context, Object object,
			String filename) {
		ObjectOutputStream objectOut = null;
		try {
			FileOutputStream fileOut = context.openFileOutput(filename,
					Activity.MODE_PRIVATE);
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

	public Object readObjectFromFile(Context context, String filename) {
		ObjectInputStream objectIn = null;
		Object object = null;
		try {
			FileInputStream fileIn = context.openFileInput(filename);
			objectIn = new ObjectInputStream(fileIn);
			object = objectIn.readObject();
			// fileIn.getFD().sync();
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

	protected void clickDit() {
		pool.play(this.dip_sound, (float)0.5, (float)0.5, 1, 0, 1);
		clickButton('·');
	}

	protected void clickDash() {
		pool.play(this.dash_sound, (float)0.5, (float)0.5, 1, 0, 1);
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
				hint_text.setVisibility(View.VISIBLE);
				hint_text.setText(this.formatCode(this.current.morse_code));
				
				this.current.correct = false;
				this.is_error = true;
				this.user_code = ""; // try again
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						pool.play(current.morse_sound_id, 1, 1, 1, 0, 1);
					}
				}, 500);
			}
		} else { // done ok
			hint_text.setVisibility(View.GONE);
			this.user_code = "";
			this.morze_text.setTextColor(this.getResources().getColor(
					R.color.green));
			pool.play(this.correct_sound,1,1,1,0,1);
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					showNextLetter();
				}
			}, 1000);

			// shedule new letter
		}

	}
	
	String formatCode(String code){
		//return code.replace('', '');
		return code;
	}

	void showNextLetter() {

		if (this.repeat < this.repeat_to_remember && this.is_error) {
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
		
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
		wl.acquire();
		
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
	

	void generateSounds() {
		if (generated)
			return;

		double dip_sample[] = new double[dip_numSamples];
		for (int i = 0; i < dip_numSamples; ++i) {
			dip_sample[i] = Math.sin(2 * Math.PI * i
					/ (sampleRate / freqOfTone));
		}
		// convert to 16 bit pcm sound array
		// assumes the sample buffer is normalised.
		int idx = 0;
		for (final double dVal : dip_sample) {
			// scale to maximum amplitude
			final short val = (short) ((dVal * 32767));
			// in 16 bit wav PCM, first byte is the low order byte
			generatedSndDip[idx++] = (byte) (val & 0x00ff);
			generatedSndDip[idx++] = (byte) ((val & 0xff00) >>> 8);
		}

		double dash_sample[] = new double[dash_numSamples];
		for (int i = 0; i < dash_numSamples; ++i) {
			dash_sample[i] = Math.sin(2 * Math.PI * i
					/ (sampleRate / freqOfTone));
		}

		// convert to 16 bit pcm sound array
		// assumes the sample buffer is normalised.
		idx = 0;
		for (final double dVal : dash_sample) {
			// scale to maximum amplitude
			final short val = (short) ((dVal * 32767));
			// in 16 bit wav PCM, first byte is the low order byte
			generatedSndDash[idx++] = (byte) (val & 0x00ff);
			generatedSndDash[idx++] = (byte) ((val & 0xff00) >>> 8);
		}

		double space_sample[] = new double[pause_numSamples];
		for (int i = 0; i < pause_numSamples; ++i) {
			space_sample[i] = 0;
		}

		// convert to 16 bit pcm sound array
		// assumes the sample buffer is normalised.
		idx = 0;
		for (final double dVal : space_sample) {
			// scale to maximum amplitude
			final short val = (short) ((dVal * 32767));
			// in 16 bit wav PCM, first byte is the low order byte
			generatedSndPause[idx++] = (byte) (val & 0x00ff);
			generatedSndPause[idx++] = (byte) ((val & 0xff00) >>> 8);
		}

	}

	void createDipSound() {
		generateSounds();
		saveWav(generatedSndDip, "_dip.wav");
	}

	void createDashSound() {
		generateSounds(); //todo refactor this!
		saveWav(generatedSndDash, "_dash.wav");
	}

	public static void writeShortLE(DataOutputStream out, short value) throws IOException {
		  out.writeByte(value & 0xFF);
		  out.writeByte((value >> 8) & 0xFF);
	}
	
	void saveWav(byte buffer[], String filename) {
		DataOutputStream out;
		try {
			FileOutputStream fileOut = this.getApplicationContext()
					.openFileOutput(filename, Activity.MODE_PRIVATE);
			
			out = new DataOutputStream(fileOut);

			WaveHeader header = new WaveHeader(WaveHeader.FORMAT_PCM, (short)1, sampleRate, (short)16, buffer.length);

			header.write(out);
			
			out.write(buffer);
			
			fileOut.getFD().sync();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
	}

	static class LetterStatistic implements Serializable {
		private static final long serialVersionUID = 1L;
		// Character character = null;
		int count_corrects = 0;
		boolean learned = false;
		int count_tries = 0;
	}

	static class MorseCode {
		String code;
		int sound_res;

		MorseCode(String code, int sound_res) {
			this.code = code;
			this.sound_res = sound_res;
		}
	}
}
