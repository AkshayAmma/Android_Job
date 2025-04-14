package com.example.jobportalapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.jobportalapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mailid, pwd, pwd1;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mailid = findViewById(R.id.et_signup_email);
        pwd = findViewById(R.id.et_signup_pwd);
        pwd1 = findViewById(R.id.et_signup_pwd1);
        progressBar = findViewById(R.id.p_bar);  // Add a ProgressBar in your layout

        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.btn_signup).setOnClickListener(this);
    }

    private void registerUser() {
        String email = mailid.getText().toString().trim();
        String password = pwd.getText().toString().trim();
        String password1 = pwd1.getText().toString().trim();

        // Validation checks
        if (!isValidInput(email, password, password1)) {
            return;
        }

        // Show the ProgressBar while registration is happening
        progressBar.setVisibility(View.VISIBLE);

        // Disable signup button to prevent multiple clicks
        Button signUpButton = findViewById(R.id.btn_signup);
        signUpButton.setEnabled(false);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            progressBar.setVisibility(View.INVISIBLE);  // Hide ProgressBar
            signUpButton.setEnabled(true);  // Re-enable the button

            if (task.isSuccessful()) {
                // Registration was successful
                Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();  // Sign out after successful registration
                clearInputFields();  // Clear the input fields

                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                // Handle errors
                handleRegistrationError(task.getException());
            }
        });
    }

    private boolean isValidInput(String email, String password, String password1) {
        if (email.isEmpty()) {
            mailid.setError("Email is required");
            mailid.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mailid.setError("Please enter a valid email");
            mailid.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            pwd.setError("Password is required");
            pwd.requestFocus();
            return false;
        }

        if (password1.isEmpty()) {
            pwd1.setError("Please confirm your password");
            pwd1.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            pwd.setError("Password should be at least 6 characters");
            pwd.requestFocus();
            return false;
        }

        if (!password1.equals(password)) {
            pwd1.setError("Passwords don't match");
            pwd1.requestFocus();
            return false;
        }

        return true;
    }

    private void handleRegistrationError(Exception exception) {
        if (exception instanceof FirebaseAuthUserCollisionException) {
            Toast.makeText(RegistrationActivity.this, "Email is already registered", Toast.LENGTH_SHORT).show();
        } else if (exception != null) {
            Toast.makeText(RegistrationActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(RegistrationActivity.this, "Registration failed due to an unknown error", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearInputFields() {
        mailid.setText("");
        pwd.setText("");
        pwd1.setText("");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_signup) {
            registerUser();
        }
    }
}
