package com.example.madmp2;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.madmp2.db.BloodRequest;
import com.example.madmp2.db.DatabaseHelper;

public class RequestActivity extends AppCompatActivity {
    private static final String TAG = "RequestActivity";
    private DatabaseHelper dbHelper;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_request);
            Log.d(TAG, "Layout set successfully");
        } catch (Exception e) {
            Log.e(TAG, "Failed to set layout: " + e.getMessage(), e);
            Toast.makeText(this, "Error loading request screen", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        dbHelper = new DatabaseHelper(this);
        userId = getIntent().getIntExtra("USER_ID", -1);
        Log.d(TAG, "Started with userId: " + userId);
        if (userId == -1) {
            Toast.makeText(this, "Invalid user ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        EditText editPatientName = findViewById(R.id.edit_patient_name);
        Spinner spinnerBloodGroup = findViewById(R.id.request_spinner_blood_group);
        EditText editUnitsNeeded = findViewById(R.id.edit_units_needed);
        EditText editHospitalName = findViewById(R.id.edit_hospital_name);
        RadioGroup radioUrgent = findViewById(R.id.radio_urgent);
        Button btnSubmit = findViewById(R.id.btn_submit_request);

        try {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.blood_groups, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerBloodGroup.setAdapter(adapter);
            Log.d(TAG, "Spinner populated with blood groups");
        } catch (Exception e) {
            Log.e(TAG, "Failed to populate spinner: " + e.getMessage(), e);
            Toast.makeText(this, "Error loading blood groups", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnSubmit.setOnClickListener(v -> {
            try {
                String patientName = editPatientName.getText().toString().trim();
                String bloodGroup = spinnerBloodGroup.getSelectedItem().toString();
                String unitsNeededStr = editUnitsNeeded.getText().toString().trim();
                String hospitalName = editHospitalName.getText().toString().trim();
                int checkedRadioButtonId = radioUrgent.getCheckedRadioButtonId();

                if (patientName.isEmpty() || unitsNeededStr.isEmpty() || hospitalName.isEmpty()) {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "Validation failed: Empty fields");
                    return;
                }
                if (checkedRadioButtonId == -1) {
                    Toast.makeText(this, "Please select urgency", Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "Validation failed: Urgency not selected");
                    return;
                }

                int unitsNeeded = Integer.parseInt(unitsNeededStr);
                if (unitsNeeded <= 0) {
                    Toast.makeText(this, "Units needed must be greater than 0", Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "Validation failed: Invalid units " + unitsNeededStr);
                    return;
                }

                BloodRequest request = new BloodRequest();
                request.setUserId(userId);
                request.setPatientName(patientName);
                request.setBloodGroup(bloodGroup);
                request.setUnitsNeeded(unitsNeeded);
                request.setHospitalName(hospitalName);
                request.setUrgent(checkedRadioButtonId == R.id.radio_urgent_yes);

                long requestId = dbHelper.submitBloodRequest(request);
                Log.d(TAG, "Submit result: " + requestId);
                if (requestId > 0) {
                    String generatedRequestId = "BL-" + requestId;
                    Toast.makeText(this, "Request submitted successfully! Track it with ID: " + generatedRequestId, Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(this, "Request submission failed. Please try again.", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Failed to submit request to database");
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid number of units", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "NumberFormatException: " + e.getMessage());
            } catch (Exception e) {
                Toast.makeText(this, "Submission error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Unexpected error: " + e.getMessage(), e);
            }
        });
    }
}