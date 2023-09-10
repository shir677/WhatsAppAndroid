package com.example.whatsapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.whatsapp.Activities.ContactsActivity;
import com.example.whatsapp.UsefulClass.BaseUrl;
import com.example.whatsapp.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {
    private ActivitySettingsBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_settings);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
    }

    public void onChangeIpClick(View view){
        String url = binding.server.getText().toString();
        BaseUrl b = BaseUrl.getInstance();
        if (url!=null){
            b.setBaseUrl(url);
            Intent i = getIntent();
            if (i.hasExtra("username")) {
                Intent intent = new Intent(this, ContactsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Add the flag
                String username = (String) i.getSerializableExtra("username");
                intent.putExtra("username", username);
                startActivity(intent);
            }
            Toast.makeText(this,"IP Changed!",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void onChangeThemeClick(View view) {
        // Toggle between light and dark theme
        int currentNightMode = getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_NO) {
            // Switch to dark theme
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            saveThemeSelection(true);
        } else {
            // Switch to light theme
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            saveThemeSelection(false);
        }
        recreate();
    }

    private void saveThemeSelection(boolean isDarkTheme) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isDarkTheme", isDarkTheme);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Apply saved theme preference on activity resume
        boolean isDarkTheme = sharedPreferences.getBoolean("isDarkTheme", false);
        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
