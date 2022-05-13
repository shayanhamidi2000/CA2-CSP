package com.example.playground;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mAccSensor;
    private Sensor mLinSensor;
    private int chart_window_size = 50;
    private boolean isRunning = false;
    private LineChart chart;

    private double mCosTheta;
    private double mSinTheta;
    private double dt = .1;

    private List<Double> y_acc = new ArrayList<>();
    private List<Double> y_vel = new ArrayList<>();
    private List<Entry> entries = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.chart = (LineChart) findViewById(R.id.chart_acc);
        Button button = (Button) findViewById(R.id.button);
        button.setText("Start");
        setup_sensors();
    }

    private void setup_sensors(){
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLinSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mAccSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    private double calculate_theta(float acceleration_z){
        mCosTheta = acceleration_z / 9.9;
        mSinTheta = Math.sqrt(1.0 - Math.pow(mCosTheta, 2));
        return Math.acos(mCosTheta)*180/Math.PI;
    }

    private void addDataPointToAccelerationEntries(double dataPoint_y) {
        int number_of_data_points = y_acc.size();
        y_acc.add(dataPoint_y);
        if (number_of_data_points > chart_window_size)
            y_acc.remove(0);
    }

    private void addDataPointToVelocityEntries(double dataPoint_y){
        int number_of_data_points = y_vel.size();
        y_vel.add(dataPoint_y);
        if (number_of_data_points > chart_window_size)
            y_vel.remove(0);
    }

    private double delete_noise(double data, int accuracy){
        int rounded_data = (int)(data * Math.pow(10, 2));
        return rounded_data / Math.pow(10, 2);
    }

    private double get_average(List<Double> list){
        int iter = Math.min(10, list.size());
        double sum = 0;
        for (int i = list.size() - 1; i > list.size() - iter - 1; i--)
            sum += list.get(i);
        return sum / iter;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText("Kiram Dahanet: " + String.valueOf((int)calculate_theta(event.values[2])));
            Log.d("MainActivity","X acceleration: " + String.valueOf(event.values[2]));
        }else if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
            /*double acc_y = delete_noise(event.values[1], 1);
            addDataPointToAccelerationEntries(acc_y);
            addDataPointToVelocityEntries(get_average(y_acc)*dt);
            entries.add(new Entry((float)(get_average(y_vel)*dt*mCosTheta), (float)(get_average(y_vel)*dt*mSinTheta)));
            Log.d("MainActivity","Y acceleration: " + String.valueOf(acc_y));
            draw_chart();*/
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    public void say_kir(View view){
        Button button = (Button) findViewById(R.id.button);
        if(isRunning){
            mSensorManager.unregisterListener(this);
            button.setText("Start");
        }else{
            button.setText("Running...");
            mSensorManager.registerListener(this, mAccSensor, 100000);
            mSensorManager.registerListener(this, mLinSensor, 100000);
        }
        isRunning = !isRunning;
    }

    private void draw_chart(){
        LineDataSet dataSet = new LineDataSet(entries, "location");
        dataSet.setColor(Color.GREEN);
        dataSet.setValueTextColor(Color.GREEN);
        dataSet.setDrawCircles(false);
        LineData line = new LineData(dataSet);
        chart.setData(line);
        chart.invalidate();
    }
}