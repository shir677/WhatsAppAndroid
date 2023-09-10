package com.example.whatsapp;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsapp.UsefulClass.BaseUrl;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if the user is already registered
        Sql sql = new Sql(MainActivity.this);
        boolean exists = sql.haveDB();
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        BaseUrl base = BaseUrl.getInstance();

        // Check if the user is already registered
        Sql sql = new Sql(MainActivity.this);
        boolean exists = sql.haveDB();
        setContentView(R.layout.activity_main);
    }

}

