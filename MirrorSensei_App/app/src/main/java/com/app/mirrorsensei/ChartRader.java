package com.app.mirrorsensei;

import android.graphics.Color;
import android.os.Bundle;

import com.app.mirrorsensei.UtilMax.utilmax;
import com.app.mirrorsensei.databinding.ActivityBarChartBinding;
import com.app.mirrorsensei.databinding.ActivityRadarChartBinding;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ChartRader extends AppCompatActivity {
    private ActivityRadarChartBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRadarChartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        utilmax.setCurrActivity(this);

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
