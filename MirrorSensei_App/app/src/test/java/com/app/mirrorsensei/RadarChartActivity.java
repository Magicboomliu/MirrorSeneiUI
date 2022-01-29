package com.app.mirrorsensei;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

public class RadarChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar_chart);

        RadarChart radarChart = findViewById(R.id.radarChart);

        ArrayList<RadarEntry> mirrorsensei = new ArrayList<>();
        mirrorsensei.add(new RadarEntry(40));
        mirrorsensei.add(new RadarEntry(50));
        mirrorsensei.add(new RadarEntry(58));
        mirrorsensei.add(new RadarEntry(60));
        mirrorsensei.add(new RadarEntry(75));
        mirrorsensei.add(new RadarEntry(80));
        mirrorsensei.add(new RadarEntry(95));
        mirrorsensei.add(new RadarEntry(80));

        RadarDataSet radarDataSetmirrorsensei = new RadarDataSet(mirrorsensei, " % Progress in each tutorial episode");
        radarDataSetmirrorsensei.setColor(Color.RED);
        radarDataSetmirrorsensei.setValueTextSize(16f);
        RadarData radarData = new RadarData();
        radarData.addDataSet(radarDataSetmirrorsensei);
        String[] labels = {"Day1", "Day2", "Day3","Day4","Day5","Day6","Day7","Day8"};

        XAxis xAxis = radarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        radarChart.getDescription().setText("Tutorial Progress Report");
        radarChart.setData(radarData);



    }
}