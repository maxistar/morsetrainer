package com.maxistar.morze2text;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Morze2TextActivity extends Activity {

	public static final Set<Character> latins = new TreeSet<Character>();
	static {
		latins.add('A');
		latins.add('B');
		latins.add('C');
		latins.add('D');
		latins.add('E');
		latins.add('F');
		latins.add('G');
		latins.add('H');
		latins.add('J');
		latins.add('I');
		latins.add('K');
		latins.add('L');
		latins.add('M');
		latins.add('N');
		latins.add('O');
		latins.add('P');
		latins.add('Q');
		latins.add('R');
		latins.add('S');
		latins.add('T');
		latins.add('U');
		latins.add('V');
		latins.add('W');
		latins.add('X');
		latins.add('Y');
		latins.add('Z');
	}
	public static final Set<Character> numbers = new TreeSet<Character>();
	static {
		numbers.add('1');
		numbers.add('2');
		numbers.add('3');
		numbers.add('4');
		numbers.add('5');
		numbers.add('6');
		numbers.add('7');
		numbers.add('8');
		numbers.add('9');
		numbers.add('0');
	}
	public static final Set<Character> characters = new TreeSet<Character>();
	static {
		characters.add('.');
		characters.add(':');
		characters.add(',');
		characters.add(';');
		characters.add('?');
		characters.add('=');
		characters.add('\'');
		characters.add('+');
		characters.add('!');
		characters.add('-');
		characters.add('/');
		characters.add('_');
		characters.add('(');
		characters.add('"');
		characters.add(')');
		characters.add('$');
		characters.add('&');
		characters.add('@');
	}

	public static final Set<Character> cyrilics = new TreeSet<Character>();
	static {
		cyrilics.add('А');
		cyrilics.add('Б');
		cyrilics.add('В');
		cyrilics.add('Г');
		cyrilics.add('Д');
		cyrilics.add('Е');
		cyrilics.add('Ё');
		cyrilics.add('Ж');
		cyrilics.add('З');
		cyrilics.add('И');
		cyrilics.add('Й');
		cyrilics.add('К');
		cyrilics.add('Л');
		cyrilics.add('М');
		cyrilics.add('Н');
		cyrilics.add('О');
		cyrilics.add('П');
		cyrilics.add('Р');
		cyrilics.add('С');
		cyrilics.add('Т');
		cyrilics.add('У');
		cyrilics.add('Ф');
		cyrilics.add('Х');
		cyrilics.add('Ц');
		cyrilics.add('Ч');
		cyrilics.add('Ш');
		cyrilics.add('Щ');
		cyrilics.add('Ъ');
		cyrilics.add('Ы');
		cyrilics.add('Ь');
		cyrilics.add('Э');
		cyrilics.add('Ю');
		cyrilics.add('Я');
	}
	public static final Map<Character, MorseCode> morze = new HashMap<Character, MorseCode>();
	static {

		morze.put('A', new MorseCode("·-", R.raw.a));
		morze.put('B', new MorseCode("-···", R.raw.b));
		morze.put('C', new MorseCode("-·-·", R.raw.c));
		morze.put('D', new MorseCode("-··", R.raw.d));
		morze.put('E', new MorseCode("·", R.raw.e));
		morze.put('F', new MorseCode("··-·", R.raw.f));
		morze.put('G', new MorseCode("--·", R.raw.g));
		morze.put('H', new MorseCode("····", R.raw.h));
		morze.put('J', new MorseCode("·---", R.raw.j));
		morze.put('I', new MorseCode("··", R.raw.i));
		morze.put('K', new MorseCode("-·-", R.raw.k));
		morze.put('L', new MorseCode("·-··", R.raw.l));
		morze.put('M', new MorseCode("--", R.raw.m));
		morze.put('N', new MorseCode("-·", R.raw.n));
		morze.put('O', new MorseCode("---", R.raw.o));
		morze.put('P', new MorseCode("·--·", R.raw.p));
		morze.put('Q', new MorseCode("--·-", R.raw.q));
		morze.put('R', new MorseCode("·-·", R.raw.r));
		morze.put('S', new MorseCode("···", R.raw.s));
		morze.put('T', new MorseCode("-", R.raw.t));
		morze.put('U', new MorseCode("··-", R.raw.u));
		morze.put('V', new MorseCode("···-", R.raw.v));
		morze.put('W', new MorseCode("·--", R.raw.w));
		morze.put('X', new MorseCode("-··-", R.raw.x));
		morze.put('Y', new MorseCode("-·--", R.raw.y));
		morze.put('Z', new MorseCode("--··", R.raw.z));

		morze.put('1', new MorseCode("·----", R.raw.r1));
		morze.put('2', new MorseCode("··---", R.raw.r2));
		morze.put('3', new MorseCode("···--", R.raw.r3));
		morze.put('4', new MorseCode("····-", R.raw.r4));
		morze.put('5', new MorseCode("·····", R.raw.r5));
		morze.put('6', new MorseCode("-····", R.raw.r6));
		morze.put('7', new MorseCode("--···", R.raw.r7));
		morze.put('8', new MorseCode("---··", R.raw.r8));
		morze.put('9', new MorseCode("----·", R.raw.r9));
		morze.put('0', new MorseCode("-----", R.raw.r0));

		morze.put('.', new MorseCode("·-·-·-", R.raw.point));
		morze.put(':', new MorseCode("---···", R.raw.column));
		morze.put(',', new MorseCode("--··--", R.raw.coma));
		morze.put(';', new MorseCode("-·-·-·", R.raw.semicolumn));
		morze.put('?', new MorseCode("··--··", R.raw.question));
		morze.put('=', new MorseCode("-···-", R.raw.equals));
		morze.put('\'', new MorseCode("·----·", R.raw.rus_apostrof));
		morze.put('+', new MorseCode("·-·-·", R.raw.plus));
		morze.put('!', new MorseCode("-·-·--", R.raw.exclamation));
		morze.put('-', new MorseCode("-····-", R.raw.minus));
		morze.put('/', new MorseCode("-··-·", R.raw.slash));
		morze.put('_', new MorseCode("··--·-", R.raw.understroke));
		morze.put('(', new MorseCode("-·--·", R.raw.opening_par));
		morze.put('"', new MorseCode("·-··-·", R.raw.quote));
		morze.put(')', new MorseCode("-·--·-", R.raw.closing_par));
		morze.put('$', new MorseCode("···-··-", R.raw.dollar));
		morze.put('&', new MorseCode("·-···", R.raw.and));
		morze.put('@', new MorseCode("·--·-·", R.raw.at));

		morze.put('А', new MorseCode("·-", R.raw.rus_a));
		morze.put('Б', new MorseCode("-···", R.raw.rus_b));
		morze.put('В', new MorseCode("·--", R.raw.rus_v));
		morze.put('Г', new MorseCode("--·", R.raw.rus_g));
		morze.put('Д', new MorseCode("-··", R.raw.rus_d));
		morze.put('Е', new MorseCode("·", R.raw.rus_e));
		morze.put('Ё', new MorseCode("·", R.raw.rus_ee));
		morze.put('Ж', new MorseCode("···-", R.raw.rus_j));
		morze.put('З', new MorseCode("--··", R.raw.rus_z));
		morze.put('И', new MorseCode("··", R.raw.rus_i));
		morze.put('Й', new MorseCode("·---", R.raw.rus_ii));
		morze.put('К', new MorseCode("-·-", R.raw.rus_k));
		morze.put('Л', new MorseCode("·-··", R.raw.rus_l));
		morze.put('М', new MorseCode("--", R.raw.rus_m));
		morze.put('Н', new MorseCode("-·", R.raw.rus_n));
		morze.put('О', new MorseCode("---", R.raw.rus_o));
		morze.put('П', new MorseCode("·--·", R.raw.rus_p));
		morze.put('Р', new MorseCode("·-·", R.raw.rus_p));
		morze.put('С', new MorseCode("···", R.raw.rus_s));
		morze.put('Т', new MorseCode("-", R.raw.rus_t));
		morze.put('У', new MorseCode("··-", R.raw.rus_u));
		morze.put('Ф', new MorseCode("··-·", R.raw.rus_f));
		morze.put('Х', new MorseCode("····", R.raw.rus_h));
		morze.put('Ц', new MorseCode("-·-·", R.raw.rus_c));
		morze.put('Ч', new MorseCode("---·", R.raw.rus_ch));
		morze.put('Ш', new MorseCode("----", R.raw.rus_sh));
		morze.put('Щ', new MorseCode("--·-", R.raw.rus_sch));
		morze.put('Ъ', new MorseCode("--·--", R.raw.rus_tz));
		morze.put('Ы', new MorseCode("-·--", R.raw.rus_y));
		morze.put('Ь', new MorseCode("-··-", R.raw.rus_mz));
		morze.put('Э', new MorseCode("··-··", R.raw.rus_ee));
		morze.put('Ю', new MorseCode("··--", R.raw.rus_yu));
		morze.put('Я', new MorseCode("·-·-", R.raw.rus_ya));

	}

	/** Called when the activity is first created. */
	private final double dash_duration = 0.5; // seconds
	private final double dip_duration = 0.25; // seconds
	private final double pause_duration = 0.25; // seconds
	private final int sampleRate = 8000;

	private final int pause_numSamples = (int) (sampleRate * pause_duration);
	private final double pause_sample[] = new double[pause_numSamples];

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

		pool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);

		// pool2 = new SoundPool(2,AudioManager.STREAM_MUSIC,0);

		File file = this.getApplicationContext().getFileStreamPath("dash.wav");
		if (!file.exists()) {
			createDashSound();
		}
		dash_sound = pool.load(file.getAbsolutePath(), 1);

		file = this.getApplicationContext().getFileStreamPath("dip.wav");
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

		if (latins.contains(current.character)) {
			this.type_text.setText("latins");
		} else if (numbers.contains(current.character)) {
			this.type_text.setText("number");
		} else if (characters.contains(current.character)) {
			this.type_text.setText("character");
		} else if (cyrilics.contains(current.character)) {
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

	protected void initLetters() {
		ArrayList<LetterInfo> letters = new ArrayList<LetterInfo>();
		LetterInfo l = null;
		LetterStatistic s = null;
		for (Map.Entry<Character, MorseCode> entry : morze.entrySet()) {
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
			// ss.mp = MediaPlayer.create(this.getApplicationContext(),
			// ss.sound_res);
			// if (ss.mp==null){
			// this.showToast("created null player!");
			// }
			ss.stream_id = pool.load(this.getApplicationContext(),
					ss.sound_res, 1);

			ss.morse_sound_id = this.getMorseSound(ss.morse_code);

			this.letters.push(ss);
		}
		Collections.reverse(this.letters);

	}

	String getMorseCodeFilename(String morse_code) {
		String res = "" + morse_code; // not sure so just clone it
		res.replace('.', 'p');
		res.replace('-', 't');
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
					if (c == '.') {
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

	public static void writeObjectToFile(Context context, Object object,
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

	public static Object readObjectFromFile(Context context, String filename) {
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
				hint_text.setText(this.current.morse_code);
				
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
		/*
		 * // Use a new tread as this can take a while final Thread thread = new
		 * Thread(new Runnable() { public void run() { genTone();
		 * handler.post(new Runnable() {
		 * 
		 * public void run() { playSound(); } }); } }); thread.start();
		 */
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
		saveWav(generatedSndDip, "dip.wav");
	}

	void createDashSound() {
		generateSounds();
		saveWav(generatedSndDash, "dash.wav");
	}

	void saveWav(byte buffer[], String filename) {
		DataOutputStream out;
		try {
			FileOutputStream fileOut = this.getApplicationContext()
					.openFileOutput(filename, Activity.MODE_PRIVATE);
			out = new DataOutputStream(fileOut);
			// objectOut.writeObject(object);

			short nChannels = 1;
			short bSamples = (short) (buffer.length / 2);
			int payloadSize = buffer.length; // 16 bite is 2 byte

			out.writeBytes("RIFF");
			out.writeInt(Integer.reverseBytes(36 + payloadSize)); // Final file
																	// size not
																	// known
																	// yet,
																	// write 0
			out.writeBytes("WAVE");
			out.writeBytes("fmt ");
			out.writeInt(Integer.reverseBytes(16)); // Sub-chunk size, 16 for
													// PCM
			out.writeShort(Short.reverseBytes((short) 1)); // AudioFormat, 1 for
															// PCM
			out.writeShort(Short.reverseBytes(nChannels));// Number of channels,
															// 1 for mono, 2 for
															// stereo
			out.writeInt(Integer.reverseBytes(sampleRate)); // Sample rate
			out.writeInt(Integer.reverseBytes(sampleRate * bSamples * nChannels
					/ 8)); // Byte rate,
							// SampleRate*NumberOfChannels*BitsPerSample/8
			out.writeShort(Short
					.reverseBytes((short) (nChannels * bSamples / 8))); // Block
																		// align,
																		// NumberOfChannels*BitsPerSample/8
			out.writeShort(Short.reverseBytes(bSamples)); // Bits per sample
			out.writeBytes("data");
			out.writeInt(payloadSize); //
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

	static class LetterInfo {
		Character character = null;
		String morse_code = null;
		boolean correct = true;
		boolean learned = false;
		int count_tries = 1;
		// MediaPlayer mp = null;
		int sound_res = 0;
		int stream_id = 0;
		int morse_sound_id = 0;
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
