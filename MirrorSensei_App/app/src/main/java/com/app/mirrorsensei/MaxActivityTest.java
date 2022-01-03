package com.app.mirrorsensei;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
//            testCreateFile();
//            testSharing();
        });
    }

    private void testSharing(){
//        Intent intent = new Intent();
        Intent intent = utilmax.getSharingIntent();
//        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "extra text desu");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE,"extra title");
//        Intent share = Intent.createChooser(intent,"titletitle");
//        startActivity(share);
        utilmax.startSharingSharesheet(intent,"titletitle");
//        utilmax.startSharingSpreadsheet(intent,null);
    }

    //note: createFile will not overwrite existing file
    private void testCreateFile(){
        utilmax.log(utilmax.FILE_DIR);
        File file = utilmax.makeFile("test");
        file.delete();
        utilmax.log(utilmax.createFile(file));
        utilmax.writeFileTemplate(file,"qwerasdfzxcv");
        utilmax.log(utilmax.createFile(file));
        utilmax.log(utilmax.readFileTemplate(file));
    }

    private void createDeleteCache(){
        File cache = utilmax.createCacheFile("test");
        utilmax.log(cache.getAbsolutePath());
        utilmax.log(utilmax.deleteCacheFile(cache));
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
}