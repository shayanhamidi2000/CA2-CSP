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
    private SensorManager sensorManager;
    private Sensor sensor;
    private int epoch = 0;
    private List<Entry> x_entries = new ArrayList<>();
    private List<Entry> y_entries = new ArrayList<>();
    private List<Entry> z_entries = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setup_chart();
        setup_sensors();
    }

    private void setup_sensors(){
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        LineChart chart = (LineChart) findViewById(R.id.chart);
        epoch ++;
        x_entries.add(new Entry(epoch, event.values[0]));
        y_entries.add(new Entry(epoch, event.values[1]));
        z_entries.add(new Entry(epoch, event.values[2]));
        Log.d("MainActivity","X accleration: " + String.valueOf(event.values[0]));
        Log.d("MainActivity","Y accleration: " + String.valueOf(event.values[1]));
        Log.d("MainActivity","Z accleration: " + String.valueOf(event.values[2]));
        chart.invalidate();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void say_kir(View view){
        TextView text_view = (TextView) findViewById(R.id.textView);
        text_view.setText("Kirram Dahanet!");
    }

    private void setup_chart(){
        x_entries.add(new Entry(0, 0));
        y_entries.add(new Entry(0 , 0));
        z_entries.add(new Entry(0 , 0));
        LineChart chart = (LineChart) findViewById(R.id.chart);
        LineDataSet dataSet_x = new LineDataSet(this.x_entries, "X");
        dataSet_x.setColor(Color.RED);
        dataSet_x.setValueTextColor(Color.RED);
        LineDataSet dataSet_y = new LineDataSet(this.y_entries, "Y");
        dataSet_y.setColor(Color.GREEN);
        dataSet_y.setValueTextColor(Color.GREEN);
        LineDataSet dataSet_z = new LineDataSet(this.z_entries, "Z");
        dataSet_z.setColor(Color.YELLOW);
        dataSet_z.setValueTextColor(Color.YELLOW);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet_x);
        dataSets.add(dataSet_y);
        dataSets.add(dataSet_z);
        LineData line = new LineData(dataSets);
        chart.setData(line);
    }
}