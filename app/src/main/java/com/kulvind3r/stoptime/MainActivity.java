package com.kulvind3r.stoptime;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Chronometer appChronometer;
    private CountDownTimer appTimer;
    private Long currentTime;

    private ProgressBar progressBarCircle;
    private ImageView imageViewStartStopWatch;
    private ImageView imageViewStartStopTimer;

    private AppStatus appWatchStatus = AppStatus.WATCH_STOPPED;
    private AppStatus appTimerStatus = AppStatus.TIMER_STOPPED;

    private enum AppStatus {
        WATCH_STARTED,
        WATCH_STOPPED,
        TIMER_STARTED,
        TIMER_STOPPED
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        appChronometer = (Chronometer) findViewById(R.id.chronometer);
        progressBarCircle = (ProgressBar) findViewById(R.id.progressBarCircle);
        imageViewStartStopWatch = (ImageView) findViewById(R.id.startStopWatch);
        imageViewStartStopTimer = (ImageView) findViewById(R.id.startStopTimer);

        imageViewStartStopWatch.setOnClickListener(appWatchStartStopListener);
        imageViewStartStopTimer.setOnClickListener(appTimerStartStopListener);

        progressBarCircle.setMax(10800000); // Max Progress Bar is set to 3 hours

        appChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long timeInMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                updateChronometerDisplay(chronometer, timeInMillis);
            }
        });

        appChronometer.setText("00:00:00");
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void updateChronometerDisplay(Chronometer chronometer, long timeInMillis) {
        if (timeInMillis < 3600000) {
            chronometer.setTextColor(Color.parseColor("#ff3333"));
            progressBarCircle.setProgressDrawable(getResources().getDrawable(R.drawable.drawable_circle_red));
        }

        else if (timeInMillis > 3600000 && timeInMillis < 7200000) {
            chronometer.setTextColor(Color.parseColor("#ff751a"));
            progressBarCircle.setProgressDrawable(getResources().getDrawable(R.drawable.drawable_circle_amber));
        }
        else if (timeInMillis > 7200000) {
            chronometer.setTextColor(Color.parseColor("#47d147"));
            progressBarCircle.setProgressDrawable(getResources().getDrawable(R.drawable.drawable_circle_green));
        }

        String t = formatTime(timeInMillis);
        progressBarCircle.setProgress((int)timeInMillis);
        chronometer.setText(t);
    }

    private String formatTime(long timeInMillis) {
        int hour = (int) (timeInMillis / 3600000);
        int minute = (int) (timeInMillis - hour * 3600000) / 60000;
        int seconds = (int) (timeInMillis - hour * 3600000 - minute * 60000) / 1000;
        return (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute) + ":" + (seconds < 10 ? "0" + seconds : seconds);
    }

    private long textToTime () {
        String timeString = (String) appChronometer.getText();

        String hoursString = timeString.split(":")[0].trim();
        int hours = Integer.parseInt(hoursString) ;

        String minutesString = timeString.split(":")[1].trim();
        int minutes = Integer.parseInt(minutesString);

        String secondsString = timeString.split(":")[2].trim();
        int seconds = Integer.parseInt(secondsString);

        long milliseconds = (hours * 3600000) + (minutes * 60000) + (seconds * 1000);

        return milliseconds;
    }

    private void startTimer() {
        appTimer = new CountDownTimer(currentTime,1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                updateChronometerDisplay(appChronometer,millisUntilFinished);
            }

            @Override
            public void onFinish() {
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
                currentTime = textToTime();
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
                currentTime = textToTime();
                startTimer();
            }
            else if (appTimerStatus == AppStatus.TIMER_STARTED) {
                stopTimer();
            }
        }
    };
}
