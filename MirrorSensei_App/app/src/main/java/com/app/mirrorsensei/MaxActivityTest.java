package com.app.mirrorsensei;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.app.mirrorsensei.UtilMax.utilmax;

import java.io.File;

public class MaxActivityTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_max_test);

        //util init
        utilmax.setCurrActivity(this);

        //set button onClick
        findViewById(R.id.maxtestactivity_button1).setOnClickListener(v -> {
//            testFileIO();
//            createDeleteCache();
//            utilmax.prefTemplate();
        });
    }

    //note: write will overwrite existing content
    private void testFileIO(){
        File file = utilmax.makeFile("test");
        utilmax.log(file.getAbsolutePath());
        utilmax.createFile(file);
        utilmax.writeFileTemplate(file,"qwerqwerqwer");
        utilmax.writeFileTemplate(file,"asdfasdf");
        utilmax.writeFileTemplate(file,"zxcv");
        utilmax.writeFileTemplate(file,"qwer\nasdf\nzxcv");
        utilmax.log(utilmax.readFileTemplate(file));
    }

    private void createDeleteCache(){
        File cache = utilmax.createCacheFile("test");
        utilmax.log(cache.getAbsolutePath());
        utilmax.log(utilmax.deleteCacheFile(cache));
    }
}