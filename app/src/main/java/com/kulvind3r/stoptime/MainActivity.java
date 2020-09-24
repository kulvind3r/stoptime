package com.kulvind3r.stoptime;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Chronometer appChronometer;
    Long elapsedRealTime;
    TextView timerValue;
    CountDownTimer appTimer;

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        appChronometer = (Chronometer) findViewById(R.id.chronometer);

        appChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                int hour   = (int)(time /3600000);
                int minute = (int)(time - hour*3600000)/60000;
                int seconds= (int)(time - hour*3600000- minute*60000)/1000 ;
                String t = (hour < 10 ? "0"+hour: hour)+":"+(minute < 10 ? "0"+minute: minute)+":"+ (seconds < 10 ? "0"+seconds: seconds);
                chronometer.setText(t);
            }
        });

        appChronometer.setText("00:00:00");
        elapsedRealTime = Long.valueOf(0);

        button = (Button) findViewById(R.id.watchStart);
        button.setOnClickListener(appWatchStartListener);

        button = (Button) findViewById(R.id.watchStop);
        button.setOnClickListener(appWatchStopListener);

//        button = (Button) findViewById(R.id.timerStart);
//        button.setOnClickListener(appTimerStartListener);
//
//        button = (Button) findViewById(R.id.timerStop);
//        button.setOnClickListener(appTimerStopListener);

    }

    View.OnClickListener appWatchStartListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            appChronometer.setBase(SystemClock.elapsedRealtime() + elapsedRealTime);
            appChronometer.start();

            button = (Button) findViewById(R.id.watchStart);
            button.setEnabled(false);

            button = (Button) findViewById(R.id.watchStop);
            button.setEnabled(true);
        }
    };

    View.OnClickListener appWatchStopListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            elapsedRealTime = appChronometer.getBase() - SystemClock.elapsedRealtime();
            appChronometer.stop();

            button = (Button) findViewById(R.id.watchStop);
            button.setEnabled(false);

            button = (Button) findViewById(R.id.watchStart);
            button.setEnabled(true);
        }
    };

//    View.OnClickListener appTimerStartListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            long currentTime = SystemClock.elapsedRealtime() - appChronometer.getBase();
//            timerValue = (TextView) findViewById(R.id.timerValue);
//            appTimer = new CountDownTimer(currentTime,1000) {
//                @Override
//                public void onTick(long millisUntilFinished) {
//                    int hour   = (int)(millisUntilFinished /3600000);
//                    int minute = (int)(millisUntilFinished - hour*3600000)/60000;
//                    int seconds= (int)(millisUntilFinished - hour*3600000- minute*60000)/1000 ;
//                    String t = (hour < 10 ? "0"+hour: hour)+":"+(minute < 10 ? "0"+minute: minute)+":"+ (seconds < 10 ? "0"+seconds: seconds);
//                    timerValue.setText(t);
//                }
//
//                @Override
//                public void onFinish() {
//                    timerValue.setText("00:00:00");
//                }
//            };
//        }
//    };
//
//    View.OnClickListener appTimerStopListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            appTimer.cancel();
//        }
//    };

}
