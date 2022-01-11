package com.app.mirrorsensei;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;

import com.app.mirrorsensei.UtilMax.utilmax;
import com.app.mirrorsensei.UtilMax.utilmaxMS;
import com.app.mirrorsensei.databinding.ActivityMaxTestBinding;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MaxActivityTest extends AppCompatActivity {
    private ActivityMaxTestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_max_test);
        binding = ActivityMaxTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //util init
        utilmax.setCurrActivity(this);

        //Camera test
        testCameraOnStart();

        //set button onClick
        findViewById(R.id.maxtestactivity_button1).setOnClickListener(v -> {
//            testFileIO();
//            createDeleteCache();
//            utilmax.prefTemplate();
//            testCreateFile();
//            testSharing();
//            Arrays.stream(utilmax.listFile()).forEach(s -> utilmax.log(s));
//            testAC();
//            testJSON();
//            testClass();
            testCamera();
        });
        findViewById(R.id.maxtestactivity_start).setOnClickListener(v -> {
            if (cameraProvider != null){
                testCameraBindPreview(cameraProvider);
            }
        });
        findViewById(R.id.maxtestactivity_stop).setOnClickListener(v -> {
            cameraProvider.unbindAll();
        });
        findViewById(R.id.maxtestactivity_enable).setOnClickListener(v -> {
            utilmax.log("start analyzing");
            isImageTaken = false;
            imageAnalysis.setAnalyzer(executorService, imageProxy -> {
//                if (!isImageTaken) {
                    utilmax.log("image taken");
                    utilmax.log("height" + imageProxy.getHeight());
                    utilmax.log("width" + imageProxy.getWidth());
                    utilmax.log("rotation angle" + imageProxy.getImageInfo().getRotationDegrees());
                    isImageTaken = true;
//                }
                imageProxy.close();
            });
        });
        findViewById(R.id.maxtestactivity_disable).setOnClickListener(v -> {
            imageAnalysis.clearAnalyzer();
            utilmax.log("clear analyzer");
        });
    }

//    https://developers.google.com/ml-kit/vision/text-recognition/android#java
    private boolean isImageTaken = true;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ProcessCameraProvider cameraProvider;
    private ExecutorService executorService;
    private ImageAnalysis imageAnalysis;
    private void testCameraOnStart(){
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},1);
        }
        imageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(720,1280))
                .setOutputImageRotationEnabled(true)
                .setTargetRotation(Surface.ROTATION_0)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();
//        executorService = Executors.newFixedThreadPool(2);
        executorService = Executors.newSingleThreadExecutor();
//        ActivityMaxTestBinding binding = ActivityMaxTestBinding.inflate(LayoutInflater.from(this));
//        ActivityMaxTestBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_max_test);
//        binding.
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                utilmax.log("got cameraProvider");
                testCameraBindPreview(cameraProvider);
            } catch (InterruptedException | ExecutionException e) {}
        }, ContextCompat.getMainExecutor(this));
    }
    private void testCamera(){
        utilmax.log("log ok");
        utilmax.makeVibrate(1000);
        utilmax.makeFlash_toggle();
    }
    private void testCameraBindPreview(ProcessCameraProvider cameraProvider){
        if (cameraProvider == null){utilmax.log("no camera acquired");return;}
        utilmax.log("start camera");
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
        preview.setSurfaceProvider(binding.maxtestactivityPreviewView.getSurfaceProvider());
        cameraProvider.bindToLifecycle(this,cameraSelector,preview,imageAnalysis);
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