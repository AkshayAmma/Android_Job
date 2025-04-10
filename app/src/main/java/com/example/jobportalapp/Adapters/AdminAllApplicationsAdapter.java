package com.example.jobportalapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobportalapp.Model.Model;
import com.example.jobportalapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AdminAllApplicationsAdapter extends FirebaseRecyclerAdapter<Model, AdminAllApplicationsAdapter.Viewholder> {

    public AdminAllApplicationsAdapter(@NonNull FirebaseRecyclerOptions<Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AdminAllApplicationsAdapter.Viewholder holder, int position, @NonNull Model model) {
        Context context = holder.itemView.getContext();

        Log.d("AdminAllApplicationsAdapter", "Binding data for model at position " + position + ": " + model.toString());

        String userName = model.getUserName();
        if (userName != null && !userName.isEmpty()) {
            holder.txtTitle.setText(userName);
        } else {
            holder.txtTitle.setText(context.getString(R.string.unknown_user));
            Log.d("custom_user", "User name is empty or null. Displaying 'Unknown User'.");
        }

        if (model.getJobTitle() != null) {
            holder.txtDesc.setText(model.getJobTitle());
        } else {
            holder.txtDesc.setText(context.getString(R.string.unknown_job_title));
        }

        // Display and handle resume PDF view
        String resumeLink = model.getResumeLink();
        if (resumeLink != null && !resumeLink.isEmpty()) {
            holder.viewResumeBtn.setVisibility(View.VISIBLE);
            holder.viewResumeBtn.setOnClickListener(view -> {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(resumeLink), "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    context.startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(context, "Unable to open resume. Please try again.", Toast.LENGTH_SHORT).show();
                    Log.e("ResumeView", "Error opening resume URI: " + resumeLink, e);
                }
            });
        } else {
            holder.viewResumeBtn.setVisibility(View.GONE);
        }

        // Accept Job Application
        final String finalUserName = userName;

        holder.acceptJobApplicationBtn.setOnClickListener(view -> {
            String adminId = model.getAdminId();
            String userId = model.getUserId();
            String jobTitle = model.getJobTitle();
            String companyName = model.getCompanyName();

            if (adminId != null && userId != null && jobTitle != null && companyName != null && finalUserName != null) {
                acceptJobApplication(adminId, userId, jobTitle, companyName, finalUserName, context);
            } else {
                showToast(context, context.getString(R.string.error_missing_data));
            }
        });
    }

    private void acceptJobApplication(String adminId, String userId, String jobTitle, String companyName, String userName, Context context) {
        HashMap<String, Object> applicationDetails = new HashMap<>();

        String key = FirebaseDatabase.getInstance().getReference().child("selectedApplications").push().getKey();

        if (key == null) {
            showToast(context, context.getString(R.string.error_failed_to_generate_key));
            return;
        }

        applicationDetails.put("jobTitle", jobTitle);
        applicationDetails.put("adminId", adminId);
        applicationDetails.put("companyName", companyName);
        applicationDetails.put("userId", userId);
        applicationDetails.put("userName", userName);

        FirebaseDatabase.getInstance().getReference().child("selectedApplications").child(adminId).child(key)
                .updateChildren(applicationDetails)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseDatabase.getInstance().getReference().child("selectedApplications").child(userId).child(key)
                                .updateChildren(applicationDetails)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        showToast(context, context.getString(R.string.application_accepted));
                                    } else {
                                        Log.e("AdminAllApplicationsAdapter", "Error updating user data", task1.getException());
                                        showToast(context, context.getString(R.string.error_update_user_data));
                                    }
                                });
                    } else {
                        Log.e("AdminAllApplicationsAdapter", "Error updating admin data", task.getException());
                        showToast(context, context.getString(R.string.error_update_admin_data));
                    }
                });
    }

    private void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public AdminAllApplicationsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_job_application_accept_file, parent, false);
        return new Viewholder(view);
    }

    public static class Viewholder extends RecyclerView.ViewHolder {

        TextView txtusername;
        TextView txtTitle;
        TextView txtDesc;
        Button viewResumeBtn;
        Button acceptJobApplicationBtn;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            txtusername = itemView.findViewById(R.id.username);
            txtTitle = itemView.findViewById(R.id.Title);
            txtDesc = itemView.findViewById(R.id.Desc);
            viewResumeBtn = itemView.findViewById(R.id.ViewResumeBtn);
            acceptJobApplicationBtn = itemView.findViewById(R.id.AcceptJobApplicationBtn);
        }
    }
}