package com.app.mirrorsensei;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.app.mirrorsensei.UtilMax.utilmax;

public class MaxActivityTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_max_test);

        //util init
        utilmax.setCurrActivity(this);
    }
}