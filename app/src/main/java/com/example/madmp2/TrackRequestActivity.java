package com.example.madmp2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.madmp2.db.BloodRequest;
import com.example.madmp2.db.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TrackRequestActivity extends AppCompatActivity {
    private static final String TAG = "TrackRequestActivity";
    private DatabaseHelper dbHelper;
    private int userId;
    private LinearLayout recentRequestsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_track);
            Log.d(TAG, "Layout set successfully");
        } catch (Exception e) {
            Log.e(TAG, "Failed to set layout: " + e.getMessage(), e);
            Toast.makeText(this, "Error loading screen: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

        try {
            ImageView backButton = findViewById(R.id.back_button);
            EditText editRequestId = findViewById(R.id.edit_text_request_id);
            Button trackButton = findViewById(R.id.button_track);
            recentRequestsContainer = findViewById(R.id.recent_requests_container);

            backButton.setOnClickListener(v -> finish());

            trackButton.setOnClickListener(v -> {
                String requestId = editRequestId.getText().toString().trim();
                if (requestId.isEmpty()) {
                    Toast.makeText(this, "Please enter a Request ID (e.g., BL-1)", Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "Empty request ID");
                    return;
                }
                BloodRequest request = dbHelper.getBloodRequestByRequestId(requestId);
                if (request != null && request.getUserId() == userId) {
                    recentRequestsContainer.removeAllViews();
                    addRequestCard(request);
                    Toast.makeText(this, "Request found: " + requestId, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Request found: " + requestId);
                } else {
                    Toast.makeText(this, "Request not found or not yours", Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "Request not found: " + requestId);
                }
            });

            loadRecentRequests();
        } catch (Exception e) {
            Log.e(TAG, "Error initializing UI: " + e.getMessage(), e);
            Toast.makeText(this, "Error initializing screen: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadRecentRequests() {
        recentRequestsContainer.removeAllViews();
        try {
            List<BloodRequest> requests = dbHelper.getBloodRequestsByUserId(userId);
            Log.d(TAG, "Loaded " + requests.size() + " requests");
            if (requests.isEmpty()) {
                TextView noRequestsText = new TextView(this);
                noRequestsText.setText("No recent requests found. Submit a request to track it.");
                noRequestsText.setTextColor(getResources().getColor(android.R.color.black));
                noRequestsText.setTextSize(16);
                noRequestsText.setPadding(16, 16, 16, 16);
                recentRequestsContainer.addView(noRequestsText);
                Log.d(TAG, "No requests to display");
            } else {
                for (BloodRequest request : requests) {
                    addRequestCard(request);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading requests: " + e.getMessage(), e);
            Toast.makeText(this, "Error loading requests: " + e.getMessage(), Toast.LENGTH_LONG).show();
            TextView errorText = new TextView(this);
            errorText.setText("Failed to load requests. Please ensure the app is updated.");
            errorText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            errorText.setTextSize(16);
            errorText.setPadding(16, 16, 16, 16);
            recentRequestsContainer.addView(errorText);
        }
    }

    private void addRequestCard(BloodRequest request) {
        try {
            View cardView = LayoutInflater.from(this).inflate(R.layout.request_card_layout, recentRequestsContainer, false);

            TextView txtRequestId = cardView.findViewById(R.id.txt_request_id);
            TextView txtBloodType = cardView.findViewById(R.id.txt_blood_type);
            TextView txtStatus = cardView.findViewById(R.id.txt_status);
            TextView txtHospital = cardView.findViewById(R.id.txt_hospital);
            TextView txtRequestDate = cardView.findViewById(R.id.txt_request_date);
            Button btnViewDetails = cardView.findViewById(R.id.button_view_details);

            txtRequestId.setText("Request ID: " + request.getRequestId());
            txtBloodType.setText("Blood Type: " + request.getBloodGroup());
            txtStatus.setText(request.getStatus());
            txtHospital.setText(request.getHospitalName());
            txtRequestDate.setText("Requested on: " + formatDate(request.getRequestDate()));

            try {
                switch (request.getStatus().toLowerCase()) {
                    case "active":
                        txtStatus.setBackgroundResource(R.drawable.status_active_background);
                        break;
                    case "completed":
                        txtStatus.setBackgroundResource(R.drawable.status_completed_background);
                        break;
                    case "expired":
                    case "pending":
                        txtStatus.setBackgroundResource(R.drawable.status_expired_background);
                        break;
                    default:
                        txtStatus.setBackgroundResource(android.R.color.darker_gray);
                        Log.w(TAG, "Unknown status: " + request.getStatus());
                }
            } catch (Exception e) {
                Log.e(TAG, "Drawable error: " + e.getMessage(), e);
                txtStatus.setBackgroundResource(android.R.color.darker_gray);
            }

            btnViewDetails.setOnClickListener(v -> {
                Toast.makeText(this, "Details for " + request.getRequestId() + " (Not implemented yet)", Toast.LENGTH_SHORT).show();
            });

            recentRequestsContainer.addView(cardView);
            Log.d(TAG, "Added card for request: " + request.getRequestId());
        } catch (Exception e) {
            Log.e(TAG, "Error adding request card: " + e.getMessage(), e);
            Toast.makeText(this, "Error displaying request: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return sdf.format(new Date(timestamp * 1000));
    }
}