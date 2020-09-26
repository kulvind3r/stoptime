package com.kulvind3r.stoptime;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Utils {
    private final MainActivity mainActivity;

    public Utils(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void saveTimerState(String time, String fileName) {
        FileOutputStream stopTimeStateFileOutputStream = null;

        try {
            stopTimeStateFileOutputStream = mainActivity.openFileOutput(fileName, MainActivity.MODE_PRIVATE);
            stopTimeStateFileOutputStream.write(time.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stopTimeStateFileOutputStream != null) {
                try {
                    stopTimeStateFileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String retrieveTimerState(String fileName) {
        FileInputStream stopTimeStateFileInputStream = null;
        String time = "";

        try {
            stopTimeStateFileInputStream = mainActivity.openFileInput(fileName);
            InputStreamReader stopTimeStateInputReader = new InputStreamReader(stopTimeStateFileInputStream);
            BufferedReader stopTimeStateBufferedReader = new BufferedReader(stopTimeStateInputReader);

            time = stopTimeStateBufferedReader.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stopTimeStateFileInputStream != null) {
                try {
                    stopTimeStateFileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (time.isEmpty())
            time = "00:00:00";

        return time;
    }

    public static String formatTime(long timeInMillis) {
        int hour = (int) (timeInMillis / 3600000);
        int minute = (int) (timeInMillis - hour * 3600000) / 60000;
        int seconds = (int) (timeInMillis - hour * 3600000 - minute * 60000) / 1000;
        return (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute) + ":" + (seconds < 10 ? "0" + seconds : seconds);
    }

    public static long textToTime(String timeString) {

        String[] timeArray = timeString.split(":");

        String hoursString = timeArray[0].trim();
        int hours = Integer.parseInt(hoursString);

        String minutesString = timeArray[1].trim();
        int minutes = Integer.parseInt(minutesString);

        String secondsString = timeArray[2].trim();
        int seconds = Integer.parseInt(secondsString);

        return (hours * 3600000) + (minutes * 60000) + seconds * 1000;
    }
}