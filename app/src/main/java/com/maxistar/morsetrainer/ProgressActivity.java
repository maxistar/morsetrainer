package com.maxistar.morsetrainer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.maxistar.morsetrainer.TrainingActivity.LetterStatistic;
import com.maxistar.morsetrainer.TrainingActivity.MorseCode;

public class ProgressActivity extends ListActivity {
	
	ProgressAdapter adapter;
	ArrayList<LetterInfo> values;
	Map<Character, LetterStatistic> history = null;
	protected Tracker mTracker = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_progress);
		
		this.history = getLearningInfo();
		initLetters();
		
		adapter = new ProgressAdapter(this, R.layout.progress_item);
		setListAdapter(adapter);
		// Obtain the shared Tracker instance.
		MorseApplication application = (MorseApplication) getApplication();
		mTracker = application.getDefaultTracker();

	}
	
	protected Map<Character, LetterStatistic> getLearningInfo() {
		Map<Character, LetterStatistic> map = (HashMap<Character, LetterStatistic>)
				this.readObjectFromFile(this, "history");
		if (map == null) {
			map = new HashMap<>();
		}
		return map;
	}
	
	public Object readObjectFromFile(Context context, String filename) {
		ObjectInputStream objectIn = null;
		Object object = null;
		try {
			FileInputStream fileIn = context.openFileInput(filename);
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
	
	protected void addMorseCodes(ArrayList<LetterInfo> letters, Map<Character, MorseCode> chars){
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

	@Override
	protected void onResume() {
		super.onResume();

		mTracker.setScreenName("ProgressActivity");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
	}


	protected void initLetters() {
		values = new ArrayList<>();
        
		addMorseCodes(values, Constants.latins);
		addMorseCodes(values, Constants.numbers);
		addMorseCodes(values, Constants.characters);
		addMorseCodes(values, Constants.cyrilics);
	}
	
	public class ProgressAdapter extends ArrayAdapter<LetterInfo> {

		public ProgressAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId, values);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.progress_item, null);
			}

			LetterInfo d = values.get(position);

			if (d != null) {
				TextView tv = (TextView) v.findViewById(R.id.letter);
				tv.setText("" + d.character);
				tv = (TextView) v.findViewById(R.id.code);
				tv.setText(d.morse_code);
				if (d.morse_singing_id != 0) {
					tv = (TextView) v.findViewById(R.id.lang_destination);
					tv.setText(d.morse_singing_id);
				}
			}
			return v;
		}
	}
}
