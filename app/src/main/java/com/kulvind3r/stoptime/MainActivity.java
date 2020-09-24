package com.kulvind3r.stoptime;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Chronometer stopTimeChronometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button button;

        stopTimeChronometer = (Chronometer) findViewById(R.id.chronometer);

        button = (Button) findViewById(R.id.watchStart);
        button.setOnClickListener(stopTimeStartListener);


        button = (Button) findViewById(R.id.watchStop);
        button.setOnClickListener(stopTimeStopListener);


    }

    View.OnClickListener stopTimeStartListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            stopTimeChronometer.start();
        }
    };

    View.OnClickListener stopTimeStopListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            stopTimeChronometer.stop();
        }
    };

}
