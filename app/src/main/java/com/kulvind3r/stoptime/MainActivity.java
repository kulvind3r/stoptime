package com.kulvind3r.stoptime;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Chronometer appChronometer;
    CountDownTimer appTimer;
    Long currentTime;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        appChronometer = (Chronometer) findViewById(R.id.chronometer);

        appChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long timeInMillis = SystemClock.elapsedRealtime() - chronometer.getBase();

                String t = formatTime(timeInMillis);

                chronometer.setText(t);
            }
        });

        appChronometer.setText("00:00:00");

        button = (Button) findViewById(R.id.watchStart);
        button.setOnClickListener(appWatchStartListener);

        button = (Button) findViewById(R.id.watchStop);
        button.setOnClickListener(appWatchStopListener);

        button = (Button) findViewById(R.id.timerStart);
        button.setOnClickListener(appTimerStartListener);

        button = (Button) findViewById(R.id.timerStop);
        button.setOnClickListener(appTimerStopListener);

    }

    private String formatTime(long timeInMillis) {
        int hour = (int) (timeInMillis / 3600000);
        int minute = (int) (timeInMillis - hour * 3600000) / 60000;
        int seconds = (int) (timeInMillis - hour * 3600000 - minute * 60000) / 1000;
        return (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute) + ":" + (seconds < 10 ? "0" + seconds : seconds);
    }

    private void stopTimer() {

        if (appTimer != null)
            appTimer.cancel();

        button = (Button) findViewById(R.id.timerStart);
        button.setEnabled(true);

        button = (Button) findViewById(R.id.timerStop);
        button.setEnabled(false);
    }

    private void stopWatch() {
        appChronometer.stop();

        button = (Button) findViewById(R.id.watchStop);
        button.setEnabled(false);

        button = (Button) findViewById(R.id.watchStart);
        button.setEnabled(true);
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

    View.OnClickListener appWatchStartListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            stopTimer();

            button = (Button) findViewById(R.id.watchStart);
            button.setEnabled(false);

            button = (Button) findViewById(R.id.watchStop);
            button.setEnabled(true);

            currentTime = textToTime();

            appChronometer.setBase(SystemClock.elapsedRealtime() - currentTime);
            appChronometer.start();


        }
    };

    View.OnClickListener appWatchStopListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            stopWatch();
        }
    };


    View.OnClickListener appTimerStartListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            stopWatch();

            button = (Button) findViewById(R.id.timerStart);
            button.setEnabled(false);

            button = (Button) findViewById(R.id.timerStop);
            button.setEnabled(true);

            long currentTime = textToTime();

            appTimer = new CountDownTimer(currentTime,1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                    String t = formatTime(millisUntilFinished);

                    appChronometer.setText(t);
                }

                @Override
                public void onFinish() {
                    appChronometer.setText("00:00:00");
                    stopTimer();
                }
            };

            appTimer.start();
        }
    };

    View.OnClickListener appTimerStopListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            stopTimer();
        }
    };

}
