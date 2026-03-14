package com.example.madmp2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private int userId;
    private String emailPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userId = getIntent().getIntExtra("USER_ID", -1);
        emailPhone = getIntent().getStringExtra("EMAIL_PHONE");
        Log.d(TAG, "Started with userId: " + userId);

        CardView btnRegisterDonor = findViewById(R.id.btn_register_donor);
        CardView btnRequestBlood = findViewById(R.id.btn_request_blood);
        CardView btnTrackRequest = findViewById(R.id.btn_track_request);
        CardView btnViewProfile = findViewById(R.id.btn_view_profile);

        btnRegisterDonor.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        btnRequestBlood.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RequestActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
            Log.d(TAG, "Launching RequestActivity");
        });

        btnTrackRequest.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TrackRequestActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
            Log.d(TAG, "Launching TrackRequestActivity");
        });

        btnViewProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            intent.putExtra("USER_ID", userId);
            intent.putExtra("EMAIL_PHONE", emailPhone);
            startActivity(intent);
        });
    }
}