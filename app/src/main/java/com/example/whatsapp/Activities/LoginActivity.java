package com.example.whatsapp.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.example.whatsapp.Api.UserApi;
import com.example.whatsapp.Entities.User;
import com.example.whatsapp.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private MutableLiveData<User> userListData;
    private UserApi userApi;
    private String userName;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userListData = new MutableLiveData<>();


        binding.RegisterButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegActivity.class);
            startActivity(intent);
        });

        binding.btnSetting.setOnClickListener(view ->{
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } );

        binding.Loggin.setOnClickListener(view -> {
            userName = binding.editTextUsername.getText().toString();
            password = binding.editTextPassword.getText().toString();

            User newUser = new User(userName, password);
            userListData.setValue(newUser);
            userApi = new UserApi(userListData,getApplicationContext());
            //userApi.addUser();
            userApi.getToken();

        });

    }
}