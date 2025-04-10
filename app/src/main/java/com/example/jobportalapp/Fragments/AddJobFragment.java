package com.example.jobportalapp.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jobportalapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddJobFragment extends Fragment {

    // Declare all the EditTexts
    EditText companyNameEditTxt, jobTitleEditTxt, jobSalaryEditTxt, jobStartDateEditTxt, jobLastDateEditTxt;
    EditText totalOpeningsEditTxt, aboutJobEditTxt, skillsRequiredEditTxt, additionalInfoEditTxt;
    Button addJobBtn;

    // Add a progress bar or loader view if required
    // ProgressBar progressBar;

    public AddJobFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_job, container, false);

        // Assigning all the addresses of the Android Materials to get Job Details
        companyNameEditTxt = view.findViewById(R.id.CompanyNameEditTxt);
        jobTitleEditTxt = view.findViewById(R.id.JobTitleEditTxt);
        jobSalaryEditTxt = view.findViewById(R.id.JobSalaryEditTxt);
        jobStartDateEditTxt = view.findViewById(R.id.JobStartDateEditTxt);
        jobLastDateEditTxt = view.findViewById(R.id.JobLastDateEditTxt);
        totalOpeningsEditTxt = view.findViewById(R.id.TotalOpeningsEditTxt);
        aboutJobEditTxt = view.findViewById(R.id.AboutJobEditTxt);
        skillsRequiredEditTxt = view.findViewById(R.id.SkillsRequiredEditTxt);
        additionalInfoEditTxt = view.findViewById(R.id.AddationalInfoEditTxt);  // Fixed typo here

        // AddJob onClick Implementation to add Job Details to Firebase
        addJobBtn = view.findViewById(R.id.AddJobBtn);
        addJobBtn.setOnClickListener(view1 -> {

            String companyName = companyNameEditTxt.getText().toString().trim();
            String jobTitle = jobTitleEditTxt.getText().toString().trim();
            String jobSalary = jobSalaryEditTxt.getText().toString().trim();
            String jobStartDate = jobStartDateEditTxt.getText().toString().trim();
            String jobLastDate = jobLastDateEditTxt.getText().toString().trim();
            String totalOpenings = totalOpeningsEditTxt.getText().toString().trim();
            String aboutJob = aboutJobEditTxt.getText().toString().trim();
            String skillsRequired = skillsRequiredEditTxt.getText().toString().trim();
            String additionalInfo = additionalInfoEditTxt.getText().toString().trim();

            // Check if any fields are empty
            if (companyName.isEmpty() || jobTitle.isEmpty() || jobSalary.isEmpty() || jobStartDate.isEmpty() ||
                    jobLastDate.isEmpty() || totalOpenings.isEmpty() || aboutJob.isEmpty() || skillsRequired.isEmpty()) {
                Toast.makeText(getContext(), "Please, Enter All Required Details", Toast.LENGTH_SHORT).show();
            } else {
                // Optionally show a progress indicator
                // progressBar.setVisibility(View.VISIBLE);

                // Proceed to add job details to Firebase
                addJobDetailsToDatabase(companyName, jobTitle, jobSalary, jobStartDate, jobLastDate, totalOpenings, aboutJob, skillsRequired, additionalInfo);
            }
        });

        return view;
    }

    private void addJobDetailsToDatabase(String companyName, String jobTitle, String jobSalary, String jobStartDate, String jobLastDate,
                                         String totalOpenings, String aboutJob, String skillsRequired, String additionalInfo) {

        // Create a hashmap to store job details
        HashMap<String, Object> jobDetails = new HashMap<>();

        // Get the current user ID from Firebase Authentication
        String adminId = FirebaseAuth.getInstance().getCurrentUser() != null ? FirebaseAuth.getInstance().getCurrentUser().getUid() : null;

        if (adminId == null) {
            Toast.makeText(getContext(), "No user logged in. Please log in first.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate a unique key for the job entry
        String key = FirebaseDatabase.getInstance().getReference().child("jobs").push().getKey();

        // Add job details to hashmap
        jobDetails.put("companyName", companyName);
        jobDetails.put("jobTitle", jobTitle);
        jobDetails.put("jobSalary", jobSalary);
        jobDetails.put("jobStartDate", jobStartDate);
        jobDetails.put("jobLastDate", jobLastDate);
        jobDetails.put("totalOpenings", totalOpenings);
        jobDetails.put("aboutJob", aboutJob);
        jobDetails.put("skillsRequired", skillsRequired);
        jobDetails.put("additionalInfo", additionalInfo);
        jobDetails.put("adminId", adminId);

        // Add job details to Firebase
        assert key != null;
        FirebaseDatabase.getInstance().getReference().child("jobs")
                .child(key)
                .updateChildren(jobDetails)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Clear the input fields and show success message
                        Toast.makeText(getContext(), "Job Details Added Successfully", Toast.LENGTH_SHORT).show();

                        // Optionally hide the progress indicator
                        // progressBar.setVisibility(View.GONE);

                        companyNameEditTxt.setText("");
                        jobTitleEditTxt.setText("");
                        jobSalaryEditTxt.setText("");
                        jobStartDateEditTxt.setText("");
                        jobLastDateEditTxt.setText("");
                        totalOpeningsEditTxt.setText("");
                        aboutJobEditTxt.setText("");
                        skillsRequiredEditTxt.setText("");
                        additionalInfoEditTxt.setText("");
                    } else {
                        // Handle failure case
                        Toast.makeText(getContext(), "Failed to add job details", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
