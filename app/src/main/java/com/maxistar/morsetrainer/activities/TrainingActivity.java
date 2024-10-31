package com.maxistar.morsetrainer.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import com.maxistar.morsetrainer.Constants;
import com.maxistar.morsetrainer.HistoryPersistenseService;
import com.maxistar.morsetrainer.model.LetterInfo;
import com.maxistar.morsetrainer.LetterStatistic;
import com.maxistar.morsetrainer.MorseApplication;
import com.maxistar.morsetrainer.model.MorseCode;
import com.maxistar.morsetrainer.R;
import com.maxistar.morsetrainer.ServiceLocator;
import com.maxistar.morsetrainer.SettingsService;
import com.maxistar.morsetrainer.SoundGenerator;
import com.maxistar.morsetrainer.SoundPlayer;
import com.maxistar.morsetrainer.TrackerService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Stack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

public class TrainingActivity extends AppCompatActivity
{
    private static final int REQUEST_SETTINGS = 3;

    private final int dashDuration = 500; // s

    private static final String TRACKING_ACTIVITY_NAME = "TrainingActivity";

    PowerManager.WakeLock wl;

    Handler handler = new Handler();
    TextView letter;
    TextView morse_text;
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


    SettingsService settingsService;


    private final SoundGenerator soundGenerator = ServiceLocator.getInstance().getSoundGenerator();

    private final HistoryPersistenseService historyPersistenseService
            = ServiceLocator.getInstance().getHistoryPersistenseSerice();

    private final SoundPlayer soundPlayer = ServiceLocator.getInstance().getSoundPlayer();

    private final TrackerService trackerService = ServiceLocator.getInstance().getTrackerService();


    // start

    //private Button myButton;
    //private long pressStartTime;
    //private long pressDuration;
    //private Handler newHandler = new Handler();

    private long keyDownTime = 0;
    private boolean pressed = false;

    private Runnable longClickRunnable = new Runnable() {
        @Override
        public void run() {
            // Handle long press here
            // You can do something when the button is pressed for a long time
            // For example, show a context menu or perform some action.
        }
    };
    // stop


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        settingsService = SettingsService.getInstance(this.getApplicationContext());
        settingsService.applyLocale(this.getBaseContext());

        soundGenerator.initSounds(this.getApplicationContext());

        soundPlayer.initSounds(this.getApplicationContext());


        if (settingsService.isOneButtonMode()) { // one button
            setContentView(R.layout.activity_one_button_trainig);
            Button b1 = this.findViewById(R.id.button2);
            //b1.setOnTouchListener(v -> clickDotDash());

            b1.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            Log.w("TAG", "Action dows");
                            //pressStartTime = System.currentTimeMillis();
                            //newHandler.postDelayed(longClickRunnable, 1000); // Define your long press duration here (in milliseconds)
                            startClick();
                            v.setPressed(true);
                            return true;

                        case MotionEvent.ACTION_UP:
                            stopClick();
                            // v.performClick();
                            Log.w("UP", "Action up");
                            //event.
                            //newHandler.removeCallbacks(longClickRunnable);
                            //pressDuration = System.currentTimeMillis() - pressStartTime;
                            //if (pressDuration < 1000) { // Adjust this threshold as needed
                                // Handle short click here
                                // You can do something when the button is tapped.
                            //} else {
                                //
                            //}
                            v.setPressed(false);
                            return true;
                    }
                    return false;
                }
            });

        } else { // two buttons
            setContentView(R.layout.activity_trainig);
            Button b1 = this.findViewById(R.id.button1);
            b1.setOnClickListener(v -> clickDash());
            Button b2 = this.findViewById(R.id.button2);
            b2.setOnClickListener(v -> clickDit());
        }

        this.history = historyPersistenseService.getLearningInfo(this.getApplicationContext());

        letter = this.findViewById(R.id.textView1);
        morse_text = this.findViewById(R.id.textView2);
        hint_text = this.findViewById(R.id.textView3);
        type_text = this.findViewById(R.id.textView4);
        singing_text = this.findViewById(R.id.singing);


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
        this.morse_text.setText("");
        this.morse_text.setTextColor(this.getResources()
                .getColor(R.color.white));
        this.singing_text.setText("");

        soundPlayer.playSound(current.stream_id);
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
                if (s != null) {
                    l.count_tries = s.count_tries;
                    l.learned = s.learned;
                }
            }
            letters.add(l);
        }
    }

    protected void initLetters() {
        ArrayList<LetterInfo> letters = new ArrayList<>();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        boolean learnLatinica = sharedPreferences.getBoolean(SettingsService.LEARN_LATINICA, true);
        boolean learnNumbers = sharedPreferences.getBoolean(SettingsService.LEARN_NUMBERS, true);
        boolean learnPunctuationSigns = sharedPreferences.getBoolean(SettingsService.LEARN_PUNCTUATION_SIGNS, true);
        boolean learnCyrillic = sharedPreferences.getBoolean(SettingsService.LEARN_CYRILICS, false);
        boolean savetyCheck = !learnCyrillic && !learnLatinica && !learnNumbers && !learnPunctuationSigns;

        if (savetyCheck || learnLatinica) {
            addMorseCodes(letters, Constants.latins);
        }
        if (learnNumbers) {
            addMorseCodes(letters, Constants.numbers);
        }
        if (learnPunctuationSigns) {
            addMorseCodes(letters, Constants.characters);
        }
        if (learnCyrillic) {
            addMorseCodes(letters, Constants.cyrilics);
        }
        // sort list
        // first less shown
        // then shortest
        // then learned
        Collections.sort(letters, (o1, o2) -> {
            if (o1.count_tries != o2.count_tries) {
                return o1.count_tries - o2.count_tries;
            }
            if (o1.morse_code.length() != o2.morse_code.length()) {
                return o1.morse_code.length() - o2.morse_code.length();
            }
            if (o1.learned != o2.learned) {
                return o1.learned ? -1 : 1;
            }
            return 0;
        });

        int counter = 0;
        this.letters = new Stack<>();
        for (LetterInfo ss : letters) {
            int count_chars_to_learn = 5;
            if (counter >= count_chars_to_learn) {
                break;
            }

            counter++;

            if (soundPlayer.isSoundPresented()) {
                ss.stream_id =
                        soundPlayer.getPool().load(
                                this.getApplicationContext(),
                                ss.sound_res,
                                1
                        );

                ss.morse_sound_id = this.soundGenerator.getMorseSound(
                        soundPlayer.getPool(),
                        this.getApplicationContext(),
                        ss.morse_code
                );
            }

            this.letters.push(ss);
        }
        Collections.reverse(this.letters);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem searchItem = menu.findItem(R.id.options_menu_volume);
        if (searchItem != null) {
            searchItem.setChecked(settingsService.isUseVolumeButtons());
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.training_menu, menu);

        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_settings) {
            Intent intent = new Intent(
                    this.getBaseContext(),
                    SettingsActivity.class
            );
            this.startActivityForResult(intent, REQUEST_SETTINGS);
        } else if (itemId == R.id.menu_prograss) {
            this.startActivity(new Intent(this.getBaseContext(), ProgressActivity.class));
        } else if (itemId == R.id.options_menu_volume) {
            settingsService.toggleUseVolumeButtons(this.getApplicationContext());
        }
        return super.onOptionsItemSelected(item);
    }

    protected boolean useVolumeNavigation() {
        return settingsService.isUseVolumeButtons();
    }

    protected boolean isOneKeyMode() {
        return settingsService.isUseVolumeButtons();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (useVolumeNavigation()) {
            if (isOneKeyMode()) {
                if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
                    startClick();
                    return true;
                }
            } else {
                if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                    clickDash();
                    return true;
                }
                if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                    clickDit();
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void startClick() {
        if (pressed) {
            return;
        }
        startSound();
        pressed = true;
        keyDownTime = System.currentTimeMillis();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (useVolumeNavigation()) {
            if (isOneKeyMode()) {
                if (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                    stopClick();
                    return true;
                }
            } else {
                if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                    clickDash();
                    return true;
                }
                if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                    clickDit();
                    return true;
                }
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    private void stopClick() {
        pressed = false;
        stopSound();
        long keyUpTime = System.currentTimeMillis();
        long interval = keyUpTime - keyDownTime;
        if (interval > dashDuration) {
            //clickDash();
            clickButton('-');
        } else {
            //clickDit();
            clickButton('·');
        }
    }


    void saveHistory() {
        LetterInfo i;
        // pool.release();
        while (!letters_done.empty()) {
            i = letters_done.pop();
            soundPlayer.unload(i.stream_id);

            if (this.history.containsKey(i.character)) {
                LetterStatistic s = this.history.get(i.character);
                if (s != null) {
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
                }
            } else {
                LetterStatistic s = new LetterStatistic();
                this.history.put(i.character, s);
            }
        }
        this.historyPersistenseService.saveLearningInfo(this.getApplicationContext(), this.history);
    }

    protected void clickDit() {
        soundPlayer.playDitSound();
        clickButton('·');
    }

    protected void clickDash() {
        soundPlayer.playDashSound();
        clickButton('-');
    }

    protected void startSound() {
        soundPlayer.startDashSound();
    }

    protected void stopSound() {
        soundPlayer.stopDashSound();
    }


    protected boolean clickDotDash() {
        //soundPlayer.playDashSound();
        //clickButton('-');
        return false;
    }

    protected void clickButton(Character ch) {
        this.user_code = this.user_code + ch;
        this.morse_text.setText(this.user_code);
        if (!this.user_code.equals(this.current.morse_code)) {
            // this.morzeText //make red!
            if (!correctSoFar()) {
                soundPlayer.playWrongSound();
                this.morse_text.setText("");

                if (this.current.morse_singing_id != 0) {
                    this.singing_text.setText(this.current.morse_singing_id);
                }
                hint_text.setVisibility(View.VISIBLE);
                hint_text.setText(this.formatCode(this.current.morse_code));

                this.current.correct = false;
                this.is_error = true;
                this.user_code = ""; // try again
                handler.postDelayed(() -> soundPlayer.playSound(current.morse_sound_id), 500);
            }
        } else { // done ok
            hint_text.setVisibility(View.GONE);
            this.user_code = "";
            this.morse_text.setTextColor(
                this.getResources().getColor(
                    R.color.green
                )
            );
            soundPlayer.playCorrectSound();
            handler.postDelayed(this::showNextLetter, 1000);
            // shedule new letter
        }
    }

    String formatCode(String code) {
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
        if (this.user_code.length() > this.current.morse_code.length()) {
            return false;
        }
        String substring = this.current.morse_code.substring(
                0,
                this.user_code.length()
        );                                  // get substring of master string same
                                            // length as userinput
        return substring.equals(this.user_code);
    }

    @Override
    protected void onResume() {
        super.onResume();

        trackerService.track(TRACKING_ACTIVITY_NAME);
        try {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (pm != null) {
                wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "morse:MY_WAKE_TAG");
                wl.acquire(1000);
            }
        } catch (Exception e) {
            //can not acquire wake log
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (wl != null) {
                wl.release();
            }
        } catch (Exception e) {
            //can not release wake log
        }
    }
}
