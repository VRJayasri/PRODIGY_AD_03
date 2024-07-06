package com.example.stopwatch_03;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView textViewTimer;
    private Button buttonStart, buttonPause, buttonReset;
    private Handler handler = new Handler();
    private long startTime = 0L, timeInMillis = 0L, timeSwapBuff = 0L, updateTime = 0L;
    private Runnable updateTimerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewTimer = findViewById(R.id.textViewTimer);
        buttonStart = findViewById(R.id.buttonStart);
        buttonPause = findViewById(R.id.buttonPause);
        buttonReset = findViewById(R.id.buttonReset);

        updateTimerThread = new Runnable() {
            public void run() {
                timeInMillis = System.currentTimeMillis() - startTime;
                updateTime = timeSwapBuff + timeInMillis;
                int secs = (int) (updateTime / 1000);
                int mins = secs / 60;
                secs = secs % 60;
                int milliseconds = (int) (updateTime % 1000);
                textViewTimer.setText(String.format("%02d:%02d:%03d", mins, secs, milliseconds));
                handler.postDelayed(this, 0);
            }
        };

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = System.currentTimeMillis();
                handler.postDelayed(updateTimerThread, 0);
                buttonStart.setEnabled(false);
                buttonPause.setEnabled(true);
                buttonReset.setEnabled(true);
            }
        });

        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSwapBuff += timeInMillis;
                handler.removeCallbacks(updateTimerThread);
                buttonStart.setEnabled(true);
                buttonPause.setEnabled(false);
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = 0L;
                timeInMillis = 0L;
                timeSwapBuff = 0L;
                updateTime = 0L;
                textViewTimer.setText("00:00:000");
                handler.removeCallbacks(updateTimerThread);
                buttonStart.setEnabled(true);
                buttonPause.setEnabled(false);
                buttonReset.setEnabled(false);
            }
        });

        buttonPause.setEnabled(false);
        buttonReset.setEnabled(false);
    }
}
