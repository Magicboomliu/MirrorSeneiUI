package com.app.mirrorsensei;

import android.content.Intent;
import android.os.Bundle;

import com.app.mirrorsensei.UtilMax.utilmax;
import com.app.mirrorsensei.databinding.ActivityResultBinding;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {
    private ActivityResultBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        utilmax.setCurrActivity(this);

        binding.resultToBar.setOnClickListener(view -> {
            startActivity(new Intent(utilmax.CURR_CONTEXT,ChartBar.class));
        });
        binding.resultToLine.setOnClickListener(view -> {
            startActivity(new Intent(utilmax.CURR_CONTEXT,ChartLine.class));
        });
        binding.resultToPie.setOnClickListener(view -> {
            startActivity(new Intent(utilmax.CURR_CONTEXT,ChartPie.class));
        });
        binding.resultToRader.setOnClickListener(view -> {
            startActivity(new Intent(utilmax.CURR_CONTEXT,ChartRader.class));
        });
    }
}
