package com.maxistar.morsetrainer.activities;

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

import com.maxistar.morsetrainer.Constants;
import com.maxistar.morsetrainer.HistoryPersistenseService;
import com.maxistar.morsetrainer.model.LetterInfo;
import com.maxistar.morsetrainer.LetterStatistic;
import com.maxistar.morsetrainer.MorseApplication;
import com.maxistar.morsetrainer.model.MorseCode;
import com.maxistar.morsetrainer.R;
import com.maxistar.morsetrainer.ServiceLocator;
import com.maxistar.morsetrainer.TrackerService;

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
				if (s != null) {
					l.count_tries = s.count_tries;
					l.learned = s.learned;
				}
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
				ViewGroup group = null;
				v = vi.inflate(R.layout.progress_item, group);
			}

			LetterInfo d = values.get(position);

			if (d != null) {
				TextView tv = v.findViewById(R.id.letter);
				tv.setText(String.valueOf(d.character));
				tv = v.findViewById(R.id.code);
				tv.setText(d.morse_code);
				if (d.morse_singing_id != 0) {
					tv = v.findViewById(R.id.morse_singing);
					tv.setText(d.morse_singing_id);
				}
				//TextView learned = (TextView) v.findViewById(R.id.learned);
				//if (d.learned) {
				//	learned.setText("learned");
				//} else {
				//	learned.setText("to learn");
				//}
				//TextView countTries = (TextView) v.findViewById(R.id.count_tries);
				//countTries.setText(String.valueOf(d.count_tries));
			}
			return v;
		}
	}
}
