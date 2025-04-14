package com.example.jobportalapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jobportalapp.MainActivity;
import com.example.jobportalapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RoleActivity extends AppCompatActivity {

    private static final String ROLE_JOB_SEEKER = "jobseeker";
    private static final String ROLE_ADMIN = "admin";

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role);

        // Initialize Firebase Authentication
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String userId = firebaseAuth.getCurrentUser() != null ? firebaseAuth.getCurrentUser().getUid() : null;

        // If the user is not logged in, redirect to login activity
        if (userId == null) {
            redirectToLogin();
            return;
        }

        // Initialize ProgressBar
        progressBar = findViewById(R.id.r_bar);
        progressBar.setVisibility(ProgressBar.INVISIBLE); // Use INVISIBLE instead of GONE for better reusability

        // Initialize buttons
        Button jobSeekerBtn = findViewById(R.id.JobSeekerBtn);
        Button adminBtn = findViewById(R.id.AdminBtn);

        // Set role as Job Seeker
        jobSeekerBtn.setOnClickListener(view -> setUserRole(userId, ROLE_JOB_SEEKER, MainActivity.class));

        // Set role as Admin
        adminBtn.setOnClickListener(view -> setUserRole(userId, ROLE_ADMIN, AdminActivity.class));
    }

    /**
     * Helper method to set the user role and navigate to the respective activity.
     * @param userId - The user ID of the logged-in user.
     * @param role - The role to set for the user (jobseeker or admin).
     * @param nextActivity - The next activity to navigate to after setting the role.
     */
    private void setUserRole(String userId, String role, Class<?> nextActivity) {
        progressBar.setVisibility(ProgressBar.VISIBLE);  // Show the progress bar while processing

        FirebaseDatabase.getInstance().getReference().child("users")
                .child(userId)
                .child("role")
                .setValue(role)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(ProgressBar.INVISIBLE);  // Hide the progress bar when done
                    if (task.isSuccessful()) {
                        // Navigate to the corresponding activity based on the role
                        Intent intent = new Intent(getApplicationContext(), nextActivity);
                        startActivity(intent);
                    } else {
                        // Handle the error if something goes wrong
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error occurred.";
                        Toast.makeText(RoleActivity.this, "Failed to update role: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Redirect to the Login activity if the user is not logged in.
     */
    private void redirectToLogin() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
}
