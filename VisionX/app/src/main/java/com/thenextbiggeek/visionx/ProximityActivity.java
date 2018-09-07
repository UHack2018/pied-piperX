package com.thenextbiggeek.visionx;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeechService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Locale;

public class ProximityActivity extends AppCompatActivity implements SensorEventListener{



    private Context mContext;
    private SensorManager mSensorManager;
    private Sensor mSensorLight;
    private Sensor mSensorProximity;
    private int lightData;
    private int proxData;
    MediaPlayer mMediaPlayer1, mMediaPlayer2, mMediaPlayer3;
    TextToSpeech textToSpeech;
    TextView tv1, tv2;


    TextView sensorLight, sensorProx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv1 = (TextView) findViewById(R.id.sensor_light);
        tv2 = (TextView) findViewById(R.id.sensor_proximity);

        mContext = getApplicationContext();
        initTTS();
        initSensorVar();



    }

    private void initTTS() {
        textToSpeech = new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(textToSpeech != null){
                   textToSpeech.setLanguage(Locale.UK);
                }
            }
        });
    }

    private void initSensorVar() {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if((mSensorLight) != null && (mSensorProximity) != null){
            mSensorManager.registerListener(this, mSensorLight, SensorManager.SENSOR_DELAY_NORMAL);
            mSensorManager.registerListener(this, mSensorProximity, SensorManager.SENSOR_DELAY_FASTEST);
        }else{
            Log.e("TAG", "the sensors are not available");
        }
    }

    float currentValue;
    float sensorType;

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {
        currentValue = event.values[0];
        sensorType = event.sensor.getType();
        if(sensorType == Sensor.TYPE_LIGHT){
            //light value is taken
            Log.e("SENSORLIGHT", String.valueOf(currentValue));
            if(currentValue < 20){
                //low light alert
                speak("The light exposure is low!");
               // tv1.setText("Light exposure low");


            }else if(currentValue > 300){
                //heavy light alert
                speak("the light exposure is high!");
               // tv1.setText("Light exposure High");
            }


        }else if(sensorType == Sensor.TYPE_PROXIMITY){
            //proximity
            Log.e("SENSORPROX", String.valueOf(currentValue));
            if(currentValue == 0.0){
                playlongBeeps();
      //          tv2.setText("PROXIMITY CLOSE");
            }else if(currentValue == 8.0){
                playShortBeeps();
//                tv2.setText("PROXIMITY MEDIUM");
            }else{
                //nothing close for now.. just simple beeps
                playsimplebeeps();
                tv2.setText("PROXIMITY LOW");

            }
        }

    }

    int[] delayS = {0};

    private void speak(final String s) {
        Handler mHandle = new Handler();
        mHandle.postDelayed(new Runnable() {
            @Override
            public void run() {
                textToSpeech.speak(s, TextToSpeech.QUEUE_FLUSH, null);
                delayS[0] = 2000;
            }
        },delayS[0]);
    }

    @Override
    public void onStop(){
        super.onStop();
        if(mMediaPlayer1 != null){
            mMediaPlayer1.stop();
        }
        if(mMediaPlayer2 != null){
            mMediaPlayer2.stop();
        }
        if(mMediaPlayer3 != null){
            mMediaPlayer3.stop();
        }


    }

     int[] delay1 = new int[]{0};

    private void playsimplebeeps() {
        mMediaPlayer1 = MediaPlayer.create(this, R.raw.simple_beep);
        if(mMediaPlayer2 != null){
            mMediaPlayer2.stop();
        }
        if(mMediaPlayer3 != null){
            mMediaPlayer3.stop();
        }
        Handler mHandle = new Handler();
        mHandle.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMediaPlayer1.start();
                delay1[0] = 1000;
            }
        }, delay1[0]);

    }


     int[] delay2 = new int[]{0};

    private void playlongBeeps() {
        final int[] delay = {0};
        mMediaPlayer3 = MediaPlayer.create(this, R.raw.beep_levelthree);
        if(mMediaPlayer2 != null){
            mMediaPlayer2.stop();
        }
        if(mMediaPlayer1 != null){
            mMediaPlayer1.stop();
        }
        Handler mHandle = new Handler();
        mHandle.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMediaPlayer3.start();
                delay2[0] = 1000;
            }
        }, delay2[0]);
    }


     int[] delay3 = new int[]{0};

    private void playShortBeeps() {
        final int[] delay = {0};
        mMediaPlayer2 = MediaPlayer.create(this, R.raw.beep_leveltwo);
        if(mMediaPlayer1 != null){
            mMediaPlayer1.stop();
        }
        if(mMediaPlayer3 != null){
            mMediaPlayer3.stop();
        }
        Handler mHandle = new Handler();
        mHandle.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMediaPlayer2.start();
                delay3[0] = 1000;
            }
        },delay3[0]);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
