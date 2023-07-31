package com.example.moviehood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.moviehood.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread timer = new Thread() {
            @Override
            public void run() {
//                super.run();
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    Intent intent =new Intent(SplashScreenActivity.this, LoginActivity.class);

                    startActivity(intent);

                    finish();
                }
            }
        };

        timer.start();
    }
}