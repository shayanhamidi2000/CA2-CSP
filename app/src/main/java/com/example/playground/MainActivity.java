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
    private Sensor sensor;
    private int epoch = 0;
    private boolean isRunning = false;
    private LineChart chart;
    private double Theta;
    private double dt = .2;

    private List<Entry> y_entries = new ArrayList<>();
    private List<Entry> z_entries = new ArrayList<>();


    private List<Entry> v_entries_y = new ArrayList<>();
    private List<Entry> v_entries_z = new ArrayList<>();

    private double v_y = 0;
    private double v_z = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.chart = (LineChart) findViewById(R.id.chart_acc);
        setup_sensors();
    }

    private void setup_sensors(){
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    private double calculate_theta(float acceleration_z){
        Theta = Math.acos(acceleration_z / 9.89);
        return Theta*180/Math.PI;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        LineChart chart = (LineChart) findViewById(R.id.chart_acc);
        epoch ++;
        y_entries.add(new Entry(epoch, event.values[1]));
        z_entries.add(new Entry(epoch, event.values[2]));

        Log.d("MainActivity","X acceleration: " + String.valueOf(event.values[0]));
        Log.d("MainActivity","Y acceleration: " + String.valueOf(event.values[1]));
        Log.d("MainActivity","Z acceleration: " + String.valueOf(event.values[2]));
        draw_chart();



        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText("Kiram Dahanet: " + String.valueOf((int)calculate_theta(event.values[2])));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void say_kir(View view){
        Button button = (Button) findViewById(R.id.button);
        if(isRunning){
            mSensorManager.unregisterListener(this);
            v_y = 0;
            v_z = 0;
            draw_chart();
            button.setText("Start");
        }else{
            button.setText("Running...");
            mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        isRunning = !isRunning;
    }

    private void draw_chart(){
        LineDataSet dataSet_y = new LineDataSet(this.y_entries, "Y");
        dataSet_y.setColor(Color.GREEN);
        dataSet_y.setValueTextColor(Color.GREEN);
        LineDataSet dataSet_z = new LineDataSet(this.z_entries, "Z");
        dataSet_z.setColor(Color.YELLOW);
        dataSet_z.setValueTextColor(Color.YELLOW);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet_y);
        dataSets.add(dataSet_z);
        LineData line = new LineData(dataSets);
        chart.setData(line);
        chart.invalidate();
    }
}