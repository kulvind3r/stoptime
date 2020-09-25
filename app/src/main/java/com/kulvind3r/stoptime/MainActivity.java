package com.kulvind3r.stoptime;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class MainActivity extends AppCompatActivity {

    private Utils utility =  new Utils(this);

    private Chronometer appChronometer;
    private CountDownTimer appTimer;
    private ProgressBar progressBarCircle;
    private ImageView imageViewStartStopWatch;
    private ImageView imageViewStartStopTimer;
    private ImageView imageViewResetChronometer;

    private enum AppStatus {
        WATCH_STARTED,
        WATCH_STOPPED,
        TIMER_STARTED,
        TIMER_STOPPED
    }

    private AppStatus appWatchStatus = AppStatus.WATCH_STOPPED;
    private AppStatus appTimerStatus = AppStatus.TIMER_STOPPED;

    private Long currentTime;
    private static final String SAVE_STATE_FILE_NAME = "stopTimeState.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialiseViews();
        initialiseListeners();

        progressBarCircle.setMax(10800000); // Max Progress Bar is set to 3 hours

        appChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long timeInMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                updateChronometerDisplay(chronometer, timeInMillis);
            }
        });

        String time = utility.retrieveTimerState(SAVE_STATE_FILE_NAME);
        appChronometer.setText(time);
    }

    private void initialiseListeners() {
        imageViewStartStopWatch.setOnClickListener(appWatchStartStopListener);
        imageViewStartStopTimer.setOnClickListener(appTimerStartStopListener);
        imageViewResetChronometer.setOnClickListener(appChronometerResetListener);
    }

    private void initialiseViews() {
        appChronometer = findViewById(R.id.chronometer);
        progressBarCircle = findViewById(R.id.progressBarCircle);
        imageViewStartStopWatch = findViewById(R.id.startStopWatch);
        imageViewStartStopTimer = findViewById(R.id.startStopTimer);
        imageViewResetChronometer= findViewById(R.id.resetChronometer);
    }

    private void updateChronometerDisplay(Chronometer chronometer, long timeInMillis) {
        if (timeInMillis < 3600000) {
            setChronometerColors(chronometer, R.color.colorRed, R.drawable.drawable_circle_red);
        }
        else if (timeInMillis > 3600000 && timeInMillis < 7200000) {
            setChronometerColors(chronometer, R.color.colorAmber, R.drawable.drawable_circle_amber);
        }
        else if (timeInMillis > 7200000) {
            setChronometerColors(chronometer, R.color.colorGreen, R.drawable.drawable_circle_green);
        }

        String time = Utils.formatTime(timeInMillis);
        utility.saveTimerState(time, SAVE_STATE_FILE_NAME);

        if (timeInMillis < progressBarCircle.getMax())
            progressBarCircle.setProgress((int)timeInMillis);
        else
            progressBarCircle.setProgress(progressBarCircle.getMax());

        chronometer.setText(time);
    }

    private void startTimer() {
        appTimer = new CountDownTimer(currentTime,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateChronometerDisplay(appChronometer,millisUntilFinished);
            }

            @Override
            public void onFinish() {
                appChronometer.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorYellow, null));
                appChronometer.setText("00:00:00");
                stopTimer();
            }
        };

        appTimer.start();

        imageViewStartStopTimer.setImageResource(R.drawable.icon_stop);
        appTimerStatus = AppStatus.TIMER_STARTED;
    }

    private void stopTimer() {
        if (appTimer != null)
            appTimer.cancel();

        imageViewStartStopTimer.setImageResource(R.drawable.icon_start);
        appTimerStatus = AppStatus.TIMER_STOPPED;
    }

    private void startWatch() {
        appChronometer.setBase(SystemClock.elapsedRealtime() - currentTime);
        appChronometer.start();

        imageViewStartStopWatch.setImageResource(R.drawable.icon_stop);
        appWatchStatus = AppStatus.WATCH_STARTED;
    }

    private void stopWatch() {
        appChronometer.stop();

        imageViewStartStopWatch.setImageResource(R.drawable.icon_start);
        appWatchStatus = AppStatus.WATCH_STOPPED;
    }

    View.OnClickListener  appWatchStartStopListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (appWatchStatus == AppStatus.WATCH_STOPPED) {
                stopTimer();
                String timeString = (String) appChronometer.getText();
                currentTime = Utils.textToTime(timeString);
                startWatch();
            }
            else if (appWatchStatus == AppStatus.WATCH_STARTED) {
                stopWatch();
            }
        }
    };

    View.OnClickListener  appTimerStartStopListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (appTimerStatus == AppStatus.TIMER_STOPPED) {
                stopWatch();
                String timeString = (String) appChronometer.getText();
                currentTime = Utils.textToTime(timeString);
                startTimer();
            }
            else if (appTimerStatus == AppStatus.TIMER_STARTED) {
                stopTimer();
            }
        }
    };

    View.OnClickListener appChronometerResetListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogStyle);
            builder.setTitle("Reset Time");
            builder.setMessage("Are you sure, this cannot be undone?");
            builder.setIcon(R.drawable.icon_reset);

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    stopTimer();
                    stopWatch();

                    appChronometer.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorWhite, null));
                    appChronometer.setText("00:00:00");

                    progressBarCircle.setProgress(0);

                    utility.saveTimerState("00:00:00",SAVE_STATE_FILE_NAME);

                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = builder.create();

            alertDialog.show();
        }
    };

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setChronometerColors(Chronometer chronometer, int colorId, int drawableId) {
        chronometer.setTextColor(ResourcesCompat.getColor(getResources(), colorId, null));
        progressBarCircle.setProgressDrawable(getResources().getDrawable(drawableId));
    }
}
