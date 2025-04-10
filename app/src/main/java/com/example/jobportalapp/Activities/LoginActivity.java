package com.example.jobportalapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.example.jobportalapp.MainActivity;
import com.example.jobportalapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    // Declare UI components
    private EditText emailEditText, passwordEditText;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize UI components
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);
        TextView signUpLink = findViewById(R.id.to_reg);  // Sign Up link

        // Initialize ProgressBar
        progressBar = findViewById(R.id.progressBar);

        // Initialize Firebase Realtime Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Check if the user is already logged in
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            navigateToMainActivity();
        }

        // Login button click listener
        loginButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Validate input fields
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter both email and password.", Toast.LENGTH_SHORT).show();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(LoginActivity.this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
            } else if (password.length() < 6) {
                Toast.makeText(LoginActivity.this, "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(email, password);
            }
        });

        // Sign Up link click listener
        signUpLink.setOnClickListener(v -> {
            // Navigate to the Sign Up activity
            Intent signUpIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(signUpIntent);
        });
    }

    private void loginUser(String email, String password) {
        // Show ProgressBar
        progressBar.setVisibility(ProgressBar.VISIBLE);

        // Firebase Authentication: Sign in with email and password
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Get the signed-in user
                        FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();

                        // Check if user is not null and proceed
                        if (user != null) {
                            // Log the UID of the signed-in user
                            String uid = user.getUid();
                            Log.d("Auth UID", "User ID: " + uid);  // Log the UID

                            // Create a map to save user login details in Firebase Realtime Database
                            Map<String, Object> userLoginData = new HashMap<>();
                            userLoginData.put("email", email);
                            userLoginData.put("lastLogin", System.currentTimeMillis());

                            // Save user login information in Firebase Realtime Database
                            String userId = user.getUid();
                            if (userId != null) {
                                databaseReference.child(userId).updateChildren(userLoginData)
                                        .addOnSuccessListener(aVoid -> {
                                            // After successful data update, go to MainActivity
                                            progressBar.setVisibility(ProgressBar.INVISIBLE);  // Hide ProgressBar
                                            navigateToMainActivity();
                                        })
                                        .addOnFailureListener(e -> {
                                            progressBar.setVisibility(ProgressBar.INVISIBLE);  // Hide ProgressBar
                                            Log.e("LoginActivity", "Error saving login info", e);  // Log the error
                                            Toast.makeText(LoginActivity.this, "Error saving login info: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            } else {
                                progressBar.setVisibility(ProgressBar.INVISIBLE);
                                Toast.makeText(LoginActivity.this, "User ID is null", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        // If authentication failed, show a message
                        progressBar.setVisibility(ProgressBar.INVISIBLE);  // Hide ProgressBar
                        Toast.makeText(LoginActivity.this, "Authentication failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void navigateToMainActivity() {
        // Navigate to MainActivity
        Intent mainIntent = new Intent(LoginActivity.this, RoleActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish(); // Finish the login activity so the user cannot navigate back to it
    }
}
