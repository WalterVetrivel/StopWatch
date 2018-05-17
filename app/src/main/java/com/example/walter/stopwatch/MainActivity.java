package com.example.walter.stopwatch;

import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private final int STOPWATCH_TAB = 1;
    private final int TIMER_TAB = 2;

    private boolean isStopWatchRunning = false;
    private boolean isTimerRunning = false;
    private int minutes = 0;
    private int seconds = 0;
    private CountDownTimer countDownTimer;

    private int currentTab = STOPWATCH_TAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        final Button controlButton = (Button) findViewById(R.id.controlButton);
        final Button resetButton = (Button) findViewById(R.id.resetButton);
        final TextView timeView = (TextView) findViewById(R.id.timeView);
        final SeekBar timerBar = (SeekBar) findViewById(R.id.timerBar);

        final Handler handler = new Handler();

        timerBar.setVisibility(View.INVISIBLE);
        timerBar.setProgress(0);

        controlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isStopWatchRunning) {
                    isStopWatchRunning = true;
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            handler.postDelayed(this, 1000);
                            if(seconds + 1 < 60) {
                                seconds++;
                            } else {
                                seconds = 0;
                                minutes++;
                            }
                            timeView.setText(minutes + ":" + String.format("%02d", seconds));
                            controlButton.setText("Stop");
                        }
                    };
                    handler.post(runnable);
                } else {
                    isStopWatchRunning = false;
                    handler.removeCallbacksAndMessages(null);
                    controlButton.setText("Start");
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStopWatchRunning = false;
                seconds = 0;
                minutes = 0;
                controlButton.setText("Start");
                handler.removeCallbacksAndMessages(null);
                timeView.setText("0:00");
                timerBar.setProgress(0);
                if(countDownTimer != null) {
                    countDownTimer.cancel();
                }
                if(currentTab == TIMER_TAB) {
                    timerBar.setEnabled(true);
                    timerBar.setProgress(0);
                }
            }
        });

        timerBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                minutes = progress / 60;
                seconds = progress % 60;
                timeView.setText(minutes + ":" + String.format("%02d", seconds));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText().toString().toUpperCase().equals("STOPWATCH")) {
                    currentTab = STOPWATCH_TAB;
                    timerBar.setVisibility(View.INVISIBLE);
                    seconds = 0;
                    minutes = 0;
                    timeView.setText("0:00");
                    controlButton.setText("Start");
                    isStopWatchRunning = false;
                    handler.removeCallbacksAndMessages(null);
                    if(countDownTimer != null) {
                        countDownTimer.cancel();
                    }
                    timerBar.setProgress(0);
                    controlButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!isStopWatchRunning) {
                                isStopWatchRunning = true;
                                Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        handler.postDelayed(this, 1000);
                                        if(seconds + 1 < 60) {
                                            seconds++;
                                        } else {
                                            seconds = 0;
                                            minutes++;
                                        }
                                        timeView.setText(minutes + ":" + String.format("%02d", seconds));
                                        controlButton.setText("Stop");
                                    }
                                };
                                handler.post(runnable);
                            } else {
                                isStopWatchRunning = false;
                                handler.removeCallbacksAndMessages(null);
                                controlButton.setText("Start");
                            }
                        }
                    });
                } else {
                    currentTab = TIMER_TAB;
                    timerBar.setVisibility(View.VISIBLE);
                    timerBar.setProgress(0);
                    timerBar.setEnabled(true);
                    timeView.setText("0:00");
                    seconds = 0;
                    minutes = 0;
                    controlButton.setText("Start");
                    isStopWatchRunning = false;
                    handler.removeCallbacksAndMessages(null);
                    controlButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!isTimerRunning) {
                                controlButton.setText("Stop");
                                timerBar.setEnabled(false);
                                isTimerRunning = true;
                                countDownTimer = new CountDownTimer(timerBar.getProgress() * 1000, 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        timerBar.setProgress(timerBar.getProgress() - 1);
                                        int minutes = (int) (millisUntilFinished / 1000) / 60;
                                        int seconds = (int) (millisUntilFinished / 1000) % 60;
                                        String timer = String.valueOf(minutes) + ":" + String.format("%02d", seconds);
                                        timeView.setText(timer);
                                    }

                                    @Override
                                    public void onFinish() {
                                        isTimerRunning = false;
                                        controlButton.setText("Start");
                                        timerBar.setEnabled(true);
                                    }
                                };
                                countDownTimer.start();
                            } else {
                                controlButton.setText("Start");
                                isTimerRunning = false;
                                timerBar.setEnabled(true);
                                countDownTimer.cancel();
                            }
                        }
                    });
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
