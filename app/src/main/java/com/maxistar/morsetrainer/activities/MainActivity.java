package com.maxistar.morsetrainer.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.maxistar.morsetrainer.R;
import com.maxistar.morsetrainer.ServiceLocator;
import com.maxistar.morsetrainer.SoundGenerator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_SETTINGS = 3;

    private final SoundGenerator soundGenerator = ServiceLocator.getInstance().getSoundGenerator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        soundGenerator.generateSounds();

        setContentView(R.layout.activity_main);

        Button trainingButton = findViewById(R.id.button_training);
        trainingButton.setOnClickListener(view -> startActivity(new Intent(getBaseContext(), TrainingActivity.class)));

        Button progressButton = findViewById(R.id.button_progress);
        progressButton.setOnClickListener(view -> startActivity(new Intent(getBaseContext(), ProgressActivity.class)));

        Button telegraphButton = findViewById(R.id.button_telegraph);
        telegraphButton.setOnClickListener(view -> startActivity(new Intent(getBaseContext(), TelegraphActivity.class)));

        Button progressSettings = findViewById(R.id.button_settings);
        if (progressSettings != null) {
            progressSettings.setOnClickListener(view -> showSettings());
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }

        return true;
    }

    private void showSettings() {
        Intent intent = new Intent(
                this.getBaseContext(),
                SettingsActivity.class
        );
        this.startActivityForResult(intent, REQUEST_SETTINGS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_settings) {
            showSettings();
        } else if (itemId == R.id.menu_training) {
            this.startActivity(new Intent(this.getBaseContext(), TrainingActivity.class));
        } else if (itemId == R.id.menu_prograss) {
            this.startActivity(new Intent(this.getBaseContext(), ProgressActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}