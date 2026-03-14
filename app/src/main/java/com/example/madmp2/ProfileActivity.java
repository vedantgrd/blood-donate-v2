package com.example.madmp2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.madmp2.db.DatabaseHelper;
import com.example.madmp2.db.User;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private DatabaseHelper dbHelper;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dbHelper = new DatabaseHelper(this);
        userId = getIntent().getIntExtra("USER_ID", -1);
        Log.d(TAG, "Profile opened with userId: " + userId);

        // Load static profile details once
        TextView txtName = findViewById(R.id.profile_txt_name);
        TextView txtEmail = findViewById(R.id.profile_txt_email);
        TextView txtPhone = findViewById(R.id.profile_txt_phone);
        TextView txtBloodGroup = findViewById(R.id.profile_txt_blood_group);
        TextView txtAddress = findViewById(R.id.profile_txt_address);

        User user = dbHelper.getUserById(userId);
        if (user != null) {
            txtName.setText(user.getFullName());
            txtEmail.setText(user.getEmailPhone());
            txtPhone.setText(user.getPhone());
            txtBloodGroup.setText(user.getBloodGroup());
            txtAddress.setText(user.getAddress());
        } else {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initial donation stats load
        updateDonationStats();

        Button btnLogout = findViewById(R.id.profile_btn_logout);
        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Profile resumed with userId: " + userId);
        updateDonationStats(); // Refresh only donation stats
    }

    private void updateDonationStats() {
        TextView txtTotalDonations = findViewById(R.id.profile_txt_total_donations);
        TextView txtLastDonation = findViewById(R.id.profile_txt_last_donation);

        int totalDonations = dbHelper.getTotalDonations(userId);
        long daysSinceLast = dbHelper.getDaysSinceLastDonation(userId);
        Log.d(TAG, "Total donations: " + totalDonations + ", Days since last: " + daysSinceLast);

        txtTotalDonations.setText(String.valueOf(totalDonations));
        txtLastDonation.setText(daysSinceLast >= 0 ? daysSinceLast + " days" : "N/A");
    }
}