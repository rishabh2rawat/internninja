package com.rishabhrawat.internninja;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Starting home activity
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        // closeing splash activity
        finish();
    }
}
