package com.example.jobportalapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jobportalapp.R;

public class StartingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        // Initialize the "Get Started" button
        Button getStartedBtn = findViewById(R.id.GetStartedBtn);

        // Ensure the button is not null and set the click listener
        if (getStartedBtn != null) {
            getStartedBtn.setOnClickListener(view -> {
                // Create an intent to navigate to LoginActivity
                Intent intent = new Intent(StartingActivity.this, LoginActivity.class);

                // Add flags to ensure the user cannot navigate back to StartingActivity
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                // Start LoginActivity and finish the current activity to prevent navigation back
                startActivity(intent);
                // Close StartingActivity
            });
        }
    }
}
