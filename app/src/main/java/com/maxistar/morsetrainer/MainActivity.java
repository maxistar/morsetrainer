package com.maxistar.morsetrainer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends Activity {

    private static final int TRAINING = 1;
    private static final int PROGRESS = 2;
    private static final int SETTINGS = 3;
    private static final int REQUEST_SETTINGS = 3;

    private SoundGenerator soundGenerator = ServiceLocator.getInstance().getSoundGenerator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        soundGenerator.generateSounds();

        setContentView(R.layout.activity_main);

        Button trainingButton = findViewById(R.id.button_training);
        trainingButton.setOnClickListener(view -> startActivity(new Intent(getBaseContext(), TrainingActivity.class)));

        Button progressButton = findViewById(R.id.button_progress);
        progressButton.setOnClickListener(view -> startActivity(new Intent(getBaseContext(), ProgressActivity.class)));

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, TRAINING, 0, l(R.string.training));
        menu.add(1, PROGRESS, 1, l(R.string.progress));
        menu.add(2, SETTINGS, 2, l(R.string.settings));
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case SETTINGS:
                Intent intent = new Intent(
                    this.getBaseContext(),
                    SettingsActivity.class
                );
                this.startActivityForResult(intent, REQUEST_SETTINGS);
                return true;
            case PROGRESS:
                this.startActivity(new Intent(this.getBaseContext(), ProgressActivity.class));
                return true;
            case TRAINING:
                this.startActivity(new Intent(this.getBaseContext(), TrainingActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}