package com.example.moviehood.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.moviehood.R;
import com.example.moviehood.ui.signin.SigninActivity;
import com.example.moviehood.utils.TimerHelper;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        TimerHelper.startTimer(3000, this::navigateToLoginActivity);
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(SplashScreenActivity.this, SigninActivity.class);
        startActivity(intent);
        finish();
    }
}
