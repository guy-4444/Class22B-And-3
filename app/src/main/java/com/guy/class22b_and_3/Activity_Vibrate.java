package com.guy.class22b_and_3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class Activity_Vibrate extends AppCompatActivity {

    public static final String EXTRA_LIGHT = "EXTRA_LIGHT";

    private MaterialTextView vibrate_LBL_light;
    private MaterialButton vibrate_BTN_once;
    private MaterialButton vibrate_BTN_multiple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibrate);

        findViews();
        initViews();



        String lux = getIntent().getStringExtra(EXTRA_LIGHT);
        vibrate_LBL_light.setText(lux);

    }

    private void vibrateMultipleTimes() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        long[] pattern = {100, 500, 100, 500, 100, 500, 100, 500, 100, 500, 100, 500};
        int[] strength = {1, 50, 1, 255, 1, 50, 1, 255, 1, 50, 1, 255};

        if (Build.VERSION.SDK_INT >= 26) {
            //VibrationEffect vibrationEffect = VibrationEffect.createWaveform(pattern, -1);
            VibrationEffect vibrationEffect = VibrationEffect.createWaveform(pattern, strength, -1);
            v.vibrate(vibrationEffect);
        } else {
            v.vibrate(pattern, -1);
        }
    }

    private void vibrateOnce() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

    private void initViews() {
        vibrate_BTN_once.setOnClickListener(view -> vibrateOnce());
        vibrate_BTN_once.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyFeedback.vibrate(Activity_Vibrate.this);
            }
        });
        vibrate_BTN_multiple.setOnClickListener(view -> vibrateMultipleTimes());
    }

    private void findViews() {
        vibrate_LBL_light = findViewById(R.id.vibrate_LBL_light);
        vibrate_BTN_once = findViewById(R.id.vibrate_BTN_once);
        vibrate_BTN_multiple = findViewById(R.id.vibrate_BTN_multiple);
    }
}