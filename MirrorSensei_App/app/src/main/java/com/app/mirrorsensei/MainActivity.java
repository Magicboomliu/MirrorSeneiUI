package com.app.mirrorsensei;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.app.mirrorsensei.UtilMax.utilmax;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //util init msg
        utilmax.log("STARTSTART GOGOGO");
        utilmax.setAppContext(getApplicationContext());
        utilmax.setCurrActivity(this);
        utilmax.INIT();

        //set button to max test activity
        findViewById(R.id.button_to_maxtestactivity).setOnClickListener(view -> {
            Intent intent = new Intent(this,MaxActivityTest.class);
            startActivity(intent);
        });
    }
}