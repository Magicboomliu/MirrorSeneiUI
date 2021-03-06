package com.app.mirrorsensei;

import android.os.Bundle;

import com.app.mirrorsensei.UtilMax.utilmax;
import com.app.mirrorsensei.databinding.ActivityBarChartBinding;
import com.app.mirrorsensei.databinding.ActivityLineChartBinding;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ChartLine extends AppCompatActivity {
    private ActivityLineChartBinding binding;
    private LineChart lineChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLineChartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        utilmax.setCurrActivity(this);

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
