package com.app.mirrorsensei;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.app.mirrorsensei.UtilMax.utilmax;
import com.app.mirrorsensei.UtilMax.utilmaxMS;
import com.app.mirrorsensei.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        utilmax.setCurrActivity(this);

        binding.btnRegister.setOnClickListener(view -> {
            if (!isRegistrationValid(binding.inputUserName.getText().toString(),
                                    binding.inputPassword.getText().toString(),
                                    binding.inputConfirmPassword.getText().toString())){
                utilmax.log("invalid data for registration");
                return;
            }
            registerAccount(binding.inputUserName.getText().toString(),
                            binding.inputEmail.getText().toString(),
                            binding.inputPassword.getText().toString());
        });
        binding.alreadyHaveAccount.setOnClickListener(view -> {
            startActivity(new Intent(utilmax.CURR_CONTEXT,LoginActivity.class));
        });
    }

    private boolean isRegistrationValid(String name, String pwd, String confirmPwd){
        if (name==null){return false;}
        if (pwd==null){return false;}
        if (confirmPwd==null){return false;}
        if (!pwd.equals(confirmPwd)){return false;}
        if (utilmaxMS.loadAppFile(utilmaxMS.APP_FILE_LIST.userList, utilmaxMS.UserListFS.class)
                .userList.containsKey(name)){return false;}
        return true;
    }

    private void registerAccount(String name, String email, String pwd){
        utilmaxMS.accountSignUp(name,pwd,email,20,"(Fe)Male");
        utilmaxMS.accountSignIn(name, pwd);
    }
}