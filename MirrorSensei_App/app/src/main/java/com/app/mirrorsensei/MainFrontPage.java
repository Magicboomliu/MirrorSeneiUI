package com.app.mirrorsensei;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.app.mirrorsensei.UtilMax.utilmax;
import com.app.mirrorsensei.UtilMax.utilmaxMS;
import com.app.mirrorsensei.databinding.ActivityFrontpageBinding;
import com.app.mirrorsensei.databinding.ActivityMaxTestBinding;

public class MainFrontPage extends AppCompatActivity {
    private ActivityFrontpageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFrontpageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //util init
        utilmax.log("STARTSTART GOGOGO");
        utilmax.setAppContext(getApplicationContext());
        utilmax.setCurrActivity(this);
        utilmax.INIT();
        utilmaxMS.INIT_MS();

        //set button onclick
        binding.ARFeedbackMainscreen.setOnClickListener(view -> {
            utilmax.log("it works");
        });
    }
}