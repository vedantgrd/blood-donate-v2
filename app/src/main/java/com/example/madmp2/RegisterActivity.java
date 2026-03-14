package com.example.madmp2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.madmp2.db.DatabaseHelper;
import com.example.madmp2.db.Donation;
import com.example.madmp2.db.User;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private DatabaseHelper dbHelper;
    private int loggedInUserId; // The user who is logged in

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this);
        loggedInUserId = getIntent().getIntExtra("USER_ID", -1); // Get the logged-in user's ID
        Log.d(TAG, "Logged-in userId: " + loggedInUserId);

        TextInputEditText editFullName = findViewById(R.id.edit_full_name);
        TextInputEditText editPhone = findViewById(R.id.edit_phone);
        TextInputEditText editEmail = findViewById(R.id.edit_email);
        TextInputEditText editAddress = findViewById(R.id.edit_address);
        Spinner spinnerBloodGroup = findViewById(R.id.spinner_blood_group);
        Button btnRegister = findViewById(R.id.btn_register);

        // Populate Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.blood_groups, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBloodGroup.setAdapter(adapter);

        btnRegister.setOnClickListener(v -> {
            String fullName = editFullName.getText().toString().trim();
            String phone = editPhone.getText().toString().trim();
            String emailPhone = editEmail.getText().toString().trim();
            String address = editAddress.getText().toString().trim();
            String bloodGroup = spinnerBloodGroup.getSelectedItem().toString();

            // Validate inputs
            if (fullName.isEmpty() || phone.isEmpty() || emailPhone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = new User();
            user.setFullName(fullName);
            user.setPhone(phone);
            user.setEmailPhone(emailPhone);
            user.setAddress(address);
            user.setBloodGroup(bloodGroup);

            long newUserId = dbHelper.registerDonor(user);
            Log.d(TAG, "New donor registration result: " + newUserId);
            if (newUserId > 0) {
                // Log a donation for the logged-in user with real-time timestamp
                Donation donation = new Donation();
                donation.setUserId(loggedInUserId); // Use logged-in user's ID
                donation.setDonationDate(System.currentTimeMillis() / 1000); // Real-time in seconds
                long donationResult = dbHelper.addDonation(donation);
                Log.d(TAG, "Donation recording result for userId " + loggedInUserId + ": " + donationResult);

                if (donationResult > 0) {
                    Toast.makeText(this, "Donor registered and donation recorded", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Donor registered, but donation recording failed", Toast.LENGTH_SHORT).show();
                }
                finish(); // Return to MainActivity
            } else {
                Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}