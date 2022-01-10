package com.app.mirrorsensei;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.app.mirrorsensei.UtilMax.utilmax;
import com.app.mirrorsensei.UtilMax.utilmaxMS;
import com.google.gson.Gson;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

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
//            Arrays.stream(utilmax.listFile()).forEach(s -> utilmax.log(s));
//            testAC(); Maxyyyyyy
            testJSON();
//            testClass();
        });
    }

    private void testClass(){
        String test = "asdf";
        Object thing = (Object)test;
        utilmax.log(((Object)test).getClass().toString());
        utilmax.log(thing.getClass().toString());
        utilmax.log(utilmaxMS.APP_FILE_LIST.userList.getRelatedStructureClass().toString());
        utilmax.log(!(test instanceof String));
        utilmax.log(utilmaxMS.APP_FILE_LIST.userList.load(utilmaxMS.UserListFS.class).getClass().toString());
    }

    private void testJSON(){
        try {
            utilmaxMS.UserListFS userListFS = new utilmaxMS.UserListFS();
            utilmax.log(userListFS==null);
            userListFS.userList = new HashMap<>();
            userListFS.userList.put("name123","pwd123");
            userListFS.userList.put("name456","pwd456");
            userListFS.userList.put("name789","pwd789");
            utilmax.saveFileJSONClass(utilmaxMS.APP_FILE_LIST.userList.getAppFile(), userListFS);
//            utilmax.writeFileTemplate(utilmaxMS.APP_FILE_LIST.userList.getAppFile()
//                    , new Gson().toJson(userListFS));

            utilmaxMS.UserListFS readedUserList = utilmax.loadFileJSONClass(utilmaxMS.APP_FILE_LIST.userList.getAppFile(), utilmaxMS.UserListFS.class);
//            String msg = utilmax.readFileTemplate(utilmaxMS.APP_FILE_LIST.userList.getAppFile());
//            utilmaxMS.UserListFS readedUserList = new Gson().fromJson(msg, utilmaxMS.UserListFS.class);
            utilmax.log(readedUserList.userList.size());
            utilmax.log(readedUserList.userList.get("name123"));
            utilmax.log(readedUserList.userList.get("name456"));
            utilmax.log(readedUserList.userList.get("name789"));
            utilmax.log(readedUserList.userList.get("name"));
        } catch (Exception e){
            utilmax.log(e);
        }
    }

    private void testAC(){
        utilmax.writeFileTemplate(utilmaxMS.APP_FILE_LIST.userList.getAppFile(), "name1\npwd1\nname2");
        utilmaxMS.APP_FILE_LIST.userList.load(utilmaxMS.UserListFS.class).userList.forEach((s, s2) -> utilmax.log(s+" "+s2));
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
        utilmax.log(utilmax.CACHE_DIR.getAbsolutePath());
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