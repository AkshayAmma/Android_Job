package com.example.jobportalapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jobportalapp.MainActivity;
import com.example.jobportalapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);
        TextView signUpLink = findViewById(R.id.to_reg);
        progressBar = findViewById(R.id.progressBar);

        // Check if user is already signed in
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            checkUserRoleAndNavigate(currentUser.getUid());
        }

        // Login button click
        loginButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                showToast("Please enter both email and password.");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showToast("Please enter a valid email address.");
            } else if (password.length() < 6) {
                showToast("Password must be at least 6 characters.");
            } else {
                loginUser(email, password);
            }
        });

        // Navigate to RegistrationActivity
        signUpLink.setOnClickListener(v -> {
            Intent signUpIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(signUpIntent);
        });
    }

    private void loginUser(String email, String password) {
        progressBar.setVisibility(ProgressBar.VISIBLE);

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(ProgressBar.INVISIBLE);

                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            updateLoginInfo(user.getUid(), email);
                            checkUserRoleAndNavigate(user.getUid());
                        }
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Authentication failed.";
                        showToast(errorMessage);
                    }
                });
    }

    private void updateLoginInfo(String userId, String email) {
        Map<String, Object> userLoginData = new HashMap<>();
        userLoginData.put("email", email);
        userLoginData.put("lastLogin", System.currentTimeMillis());

        databaseReference.child(userId).updateChildren(userLoginData)
                .addOnSuccessListener(aVoid -> {
                    // Optional: log success
                })
                .addOnFailureListener(e -> {
                    // Optional: log failure
                });
    }

    private void checkUserRoleAndNavigate(String userId) {
        progressBar.setVisibility(ProgressBar.VISIBLE);

        databaseReference.child(userId).child("role")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progressBar.setVisibility(ProgressBar.INVISIBLE);

                        if (snapshot.exists()) {
                            String role = snapshot.getValue(String.class);
                            if ("admin".equals(role)) {
                                navigateToAdminDashboard();
                            } else if ("jobseeker".equals(role)) {
                                navigateToMainActivity();
                            } else {
                                showToast("Unknown role assigned.");
                            }
                        } else {
                            // No role selected yet → navigate to SelectRoleActivity
                            Intent intent = new Intent(LoginActivity.this, RoleActivity.class);
                            intent.putExtra("userId", userId);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressBar.setVisibility(ProgressBar.INVISIBLE);
                        showToast("Failed to get role: " + error.getMessage());
                    }
                });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void navigateToAdminDashboard() {
        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void showToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
