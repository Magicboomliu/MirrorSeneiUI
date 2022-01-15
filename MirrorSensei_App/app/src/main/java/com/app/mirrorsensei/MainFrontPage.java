package com.app.mirrorsensei;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

        //set button onClick
        binding.currUser.setOnClickListener(view -> {
            if (utilmaxMS.currUser==null){
                startActivity(new Intent(utilmax.CURR_CONTEXT,LoginActivity.class));
            } else {
                utilmaxMS.accountSignOut();
            }
        });
        binding.socialshareMainscreen.setOnClickListener(view -> {
            if (utilmaxMS.currUser==null){
                utilmax.makeToast("Please sign in first");
                return;
            }
            Intent intent = utilmax.getSharingIntent();
            intent.putExtra(Intent.EXTRA_TEXT, "I am currently practicing "+ utilmaxMS.loadUserFile().subjectCategory+". Join me at Mirror Sensei!");
            intent.setType("text/plain");
            utilmax.startSharingSharesheet(intent,"Share current learning topic to:");
        });
        binding.tutorialMainscreen.setOnClickListener(view -> {
            utilmax.log("for starting new course");
        });
        //set button onClick for buttons that won't be used
        binding.ARFeedbackMainscreen.setOnClickListener(view -> utilmax.makeToast("Intended for AR feedback. Not implemented right now."));
        binding.motionrecordingMainscreen.setOnClickListener(view -> utilmax.makeToast("Intended for Motion Recording related functions. Not implemented right now."));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (utilmaxMS.currUser!=null){
            binding.currUser.setText(utilmaxMS.currUser);
        }
    }
}