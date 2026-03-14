package com.example.madmp2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.madmp2.db.DatabaseHelper;
import com.example.madmp2.db.User;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);

        TextInputEditText editEmailPhone = findViewById(R.id.edit_text_email_phone);
        TextInputEditText editPassword = findViewById(R.id.edit_text_password);
        Button buttonLogin = findViewById(R.id.button_login);

        buttonLogin.setOnClickListener(v -> {
            String emailPhone = editEmailPhone.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            User user = dbHelper.validateLogin(emailPhone, password);
            if (user != null) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("USER_ID", user.getId());
                intent.putExtra("EMAIL_PHONE", emailPhone); // Pass email/phone for consistency
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }
}