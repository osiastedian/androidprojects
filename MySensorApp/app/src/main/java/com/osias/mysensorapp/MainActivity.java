package com.osias.mysensorapp;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private TextView valueTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(mAccelerometer == null)
        {
            Log.e("Sensor", "No Accelerometer");
        }
        valueTextView = findViewById(R.id.valueTextView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mSensorManager != null)
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(mSensorManager != null)
            mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.i("Accelerometer value", String.format("%f %f %f", sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]));
        valueTextView.setText(String.format("%f %f %f", sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]));
        float red = Math.abs(sensorEvent.values[0] / 9.81f);
        float green = Math.abs(sensorEvent.values[1] / 9.81f);;
        float blue = Math.abs(sensorEvent.values[2] / 9.81f);;
        float alpha = 1.0f;
        getWindow().getDecorView().setBackgroundColor(Color.argb(alpha, red, green, blue));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
