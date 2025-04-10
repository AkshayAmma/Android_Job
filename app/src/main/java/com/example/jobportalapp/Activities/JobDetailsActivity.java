package com.example.jobportalapp.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jobportalapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class JobDetailsActivity extends AppCompatActivity {

    // Declare UI components
    private TextView companyNameTxt;
    private TextView jobTitleTxt;
    private TextView jobDescriptionTxt;
    private TextView jobSalaryTxt;
    private TextView startDateTxt;
    private TextView lastDateTxt;
    private TextView totalOpeningsTxt;
    private TextView requiredSkillsTxt;
    private TextView additionalInfoTxt;
    private TextView selectedFileNameTxt;
    private Button applyJobBtn;
    private Button uploadResumeBtn;

    // Firebase user details
    private String userId, userName, adminId;
    private String companyName, jobTitle, jobDescription, jobSalary, startDate, lastDate, totalOpenings, requiredSkills, additionalInfo;
    private Uri resumeUri;

    // Registering the result launcher for file picker
    private final ActivityResultLauncher<String> getContentLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null) {
                        resumeUri = result;
                        selectedFileNameTxt.setText("Selected: " + result.getLastPathSegment());
                        Log.d("ResumeSelection", "Selected URI: " + result.toString());
                    } else {
                        selectedFileNameTxt.setText("No file selected");
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        // Getting data from previous intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            companyName = bundle.getString("companyName", "");
            jobTitle = bundle.getString("jobTitle", "");
            jobDescription = bundle.getString("jobDescription", "");
            jobSalary = bundle.getString("jobSalary", "");
            startDate = bundle.getString("startDate", "");
            lastDate = bundle.getString("lastDate", "");
            totalOpenings = bundle.getString("totalOpenings", "");
            requiredSkills = bundle.getString("requiredSkills", "");
            additionalInfo = bundle.getString("additionalInfo", "");
            adminId = bundle.getString("userId", "");

            // Get the current signed-in user details from Firebase Authentication
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                userId = currentUser.getUid(); // Get user ID

                // Fetch user name or fallback to email if not available
                userName = currentUser.getDisplayName();
                if (userName == null || userName.isEmpty()) {
                    userName = currentUser.getEmail(); // Fallback to email if name is not set
                }

                // Log user details for debugging
                Log.d("JobDetailsActivity", "User Name: " + userName);
            }
        }

        // Assign UI components
        companyNameTxt = findViewById(R.id.CompanyNameTxt);
        jobTitleTxt = findViewById(R.id.JobTitleTxt);
        jobDescriptionTxt = findViewById(R.id.JobDescriptionTxt);
        jobSalaryTxt = findViewById(R.id.SalaryTxt);
        startDateTxt = findViewById(R.id.JobStartDateTxt);
        lastDateTxt = findViewById(R.id.LastDateToApplyTxt);
        totalOpeningsTxt = findViewById(R.id.TotolNoOfOpeningsTxt);
        requiredSkillsTxt = findViewById(R.id.RequiredSkillsTxt);
        additionalInfoTxt = findViewById(R.id.AdditionalDataTxt);
        selectedFileNameTxt = findViewById(R.id.SelectedFileNameTxt);
        uploadResumeBtn = findViewById(R.id.SelectResumeBtn);
        applyJobBtn = findViewById(R.id.ApplyJobBtn);

        // Set the job details in the respective TextViews
        companyNameTxt.setText(companyName);
        jobTitleTxt.setText(jobTitle);
        jobDescriptionTxt.setText(jobDescription);
        jobSalaryTxt.setText(jobSalary);
        startDateTxt.setText(startDate);
        lastDateTxt.setText(lastDate);
        totalOpeningsTxt.setText(totalOpenings);
        requiredSkillsTxt.setText(requiredSkills);
        additionalInfoTxt.setText(additionalInfo);

        // OnClickListener for Upload Resume button
        uploadResumeBtn.setOnClickListener(view -> {
            // Open file picker for PDF
            getContentLauncher.launch("application/pdf");
        });

        // OnClickListener for Apply Job button
        applyJobBtn.setOnClickListener(view -> {
            if (resumeUri == null) {
                Toast.makeText(JobDetailsActivity.this, "Please upload a resume", Toast.LENGTH_SHORT).show();
            } else {
                applyForJob(resumeUri.toString());
            }
        });
    }

    private void applyForJob(String resumeLink) {
        HashMap<String, Object> applicationData = new HashMap<>();

        // Generate a unique key for this application
        String key = FirebaseDatabase.getInstance().getReference().child("jobApplications").push().getKey();
        if (key == null) return;

        // Fill the data
        applicationData.put("userId", userId);
        applicationData.put("userName", userName);
        applicationData.put("jobTitle", jobTitle);
        applicationData.put("companyName", companyName);
        applicationData.put("resumeLink", resumeLink);
        applicationData.put("adminId", adminId);

        // Upload to Firebase
        FirebaseDatabase.getInstance().getReference().child("jobApplications")
                .child(adminId).child(key).setValue(applicationData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseDatabase.getInstance().getReference().child("jobApplications")
                                .child(userId).child(key).setValue(applicationData)
                                .addOnCompleteListener(task2 -> {
                                    if (task2.isSuccessful()) {
                                        Toast.makeText(JobDetailsActivity.this, "Successfully Applied For Job", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(JobDetailsActivity.this, "Error saving user application", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(e -> Toast.makeText(JobDetailsActivity.this, "Failed to apply. Please try again.", Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(JobDetailsActivity.this, "Failed to apply. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(JobDetailsActivity.this, "Failed to apply. Please try again.", Toast.LENGTH_SHORT).show());
    }
}
