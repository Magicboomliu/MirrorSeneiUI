package com.app.mirrorsensei;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.app.mirrorsensei.UtilMax.utilmax;
import com.app.mirrorsensei.UtilMax.utilmaxMS;
import com.app.mirrorsensei.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        utilmax.setCurrActivity(this);

        binding.forgotPassword.setOnClickListener(view -> {
            utilmax.makeToast("no this thing right now");
        });
        binding.btnLogin.setOnClickListener(view -> {
            if (!isLoginValid(binding.inputUserName.getText().toString(),
                    binding.inputPassword.getText().toString())){
                utilmax.log("invalid data for registration");
                return;
            }
            login(binding.inputUserName.getText().toString(),
                    binding.inputPassword.getText().toString());
        });
        binding.textViewSignUp.setOnClickListener(view -> {
            startActivity(new Intent(utilmax.CURR_CONTEXT,RegisterActivity.class));
        });
    }

    private boolean isLoginValid(String name, String pwd){
        if (name==null){return false;}
        if (pwd==null){return false;}
        if (!utilmaxMS.loadAppFile(utilmaxMS.APP_FILE_LIST.userList,
                utilmaxMS.UserListFS.class).userList.containsKey(name)){return false;}
        if (!utilmaxMS.loadAppFile(utilmaxMS.APP_FILE_LIST.userList,
                utilmaxMS.UserListFS.class).userList.get(name).equals(pwd)){return false;}
        return true;
    }

    private void login(String name, String pwd){
        utilmaxMS.accountSignIn(name,pwd);
    }
}