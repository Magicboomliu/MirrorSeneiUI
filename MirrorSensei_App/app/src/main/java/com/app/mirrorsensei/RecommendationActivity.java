package com.app.mirrorsensei;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.app.mirrorsensei.UtilMax.utilmax;
import com.app.mirrorsensei.databinding.ActivitySuggestionBinding;

public class RecommendationActivity extends AppCompatActivity {
    private ActivitySuggestionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySuggestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        utilmax.setCurrActivity(this);

        binding.textfortap.setOnClickListener(view -> {
            startActivity(new Intent(utilmax.CURR_CONTEXT,MaxActivityTest.class));
        });
    }
}