package com.app.mirrorsensei;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class BarChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        BarChart barChart = findViewById(R.id.barChart);

        ArrayList<BarEntry> visitors = new ArrayList<>();
        // Bar chart: x-axis: Days of tutorial episode, y: total time of that tutorial (in min)
        visitors.add(new BarEntry(1, 30));
        visitors.add(new BarEntry(2,45));
        visitors.add(new BarEntry(3,20));
        visitors.add(new BarEntry(4,30));
        visitors.add(new BarEntry(5,15));
        visitors.add(new BarEntry(6,25));
        visitors.add(new BarEntry(7,35));

        BarDataSet barDataSet = new BarDataSet(visitors, "Days");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Bar Chart Example");
        barChart.animateY(2000);

    }
}