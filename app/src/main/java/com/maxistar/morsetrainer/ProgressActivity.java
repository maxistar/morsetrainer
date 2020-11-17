package com.maxistar.morsetrainer;

import java.util.ArrayList;
import java.util.Map;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ProgressActivity extends ListActivity {
	
	ProgressAdapter adapter;
	ArrayList<LetterInfo> values;
	Map<Character, LetterStatistic> history = null;

	private HistoryPersistenseService historyPersistenseService
			= ServiceLocator.getInstance().getHistoryPersistenseSerice();

	private TrackerService trackerService = ServiceLocator.getInstance().getTrackerService();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_progress);
		
		this.history = historyPersistenseService.getLearningInfo(this.getApplicationContext());
		initLetters();
		
		adapter = new ProgressAdapter(this, R.layout.progress_item);
		setListAdapter(adapter);

		trackerService.initTracker((MorseApplication) getApplication());
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
		trackerService.track("ProgressActivity");
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
				TextView learned = (TextView) v.findViewById(R.id.learned);
				if (d.learned) {
					learned.setText("learned");
				} else {
					learned.setText("to learn");
				}
				TextView countTries = (TextView) v.findViewById(R.id.count_tries);
				countTries.setText(String.valueOf(d.count_tries));
			}
			return v;
		}
	}
}
