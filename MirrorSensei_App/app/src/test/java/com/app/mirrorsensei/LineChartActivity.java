package com.app.mirrorsensei;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LineChartActivity extends AppCompatActivity {

    private static final  String TAG = "LineChartActivity";

    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);

        lineChart = (LineChart) findViewById(R.id.lineChart);

     //   lineChart.setOnChartGestureListener(LineChartActivity.this);
       // lineChart.setOnChartValueSelectedListener(LineChartActivity.this);

        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);
        ArrayList<Entry> visitors = new ArrayList<>();

        visitors.add(new Entry(0,10f));
        visitors.add(new Entry(1,25f));
        visitors.add(new Entry(2,40f));
        visitors.add(new Entry(3,50f));
        visitors.add(new Entry(4,30f));
        visitors.add(new Entry(5,60f));
        visitors.add(new Entry(6,75f));
        visitors.add(new Entry(7,80f));

        LineDataSet set1 = new LineDataSet(visitors,"Days");

        set1.setFillAlpha(110);

        set1.setLineWidth(4f);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data =new LineData(dataSets);

        lineChart.setData(data);
        lineChart.getDescription().setText("Progress on Each Day");





    }



}