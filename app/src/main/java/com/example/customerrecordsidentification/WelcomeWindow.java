package com.example.customerrecordsidentification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.os.Bundle;

public class WelcomeWindow extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_window);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(WelcomeWindow.this, Login.class);
            startActivity(intent);
            finish();
        }, 3000); // 3-second delay
    }
}