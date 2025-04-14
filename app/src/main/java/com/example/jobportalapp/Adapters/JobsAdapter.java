package com.example.jobportalapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobportalapp.Activities.JobDetailsActivity;
import com.example.jobportalapp.Model.Model;
import com.example.jobportalapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class JobsAdapter extends FirebaseRecyclerAdapter<Model, JobsAdapter.Viewholder> {

    public JobsAdapter(FirebaseRecyclerOptions<Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Viewholder holder, int position, @NonNull Model model) {
        Context context = holder.itemView.getContext();

        // Show readable text
        holder.txtTitle.setText("Job Title: " + model.getJobTitle());
        holder.txtDesc.setText("Salary: " + model.getJobSalary());

        // Click event for navigating to job details
        holder.txtTitle.setOnClickListener(view -> {
            Intent intent = new Intent(context, JobDetailsActivity.class);

            // Only pass raw data, format in UI later
            intent.putExtra("companyName", model.getCompanyName());
            intent.putExtra("jobTitle", model.getJobTitle());
            intent.putExtra("jobDescription", model.getAboutJob());
            intent.putExtra("jobSalary", model.getJobSalary());
            intent.putExtra("startDate", model.getJobStartDate());
            intent.putExtra("lastDate", model.getJobLastDate());
            intent.putExtra("totalOpenings", model.getTotalOpenings());
            intent.putExtra("requiredSkills", model.getSkillsRequired());
            intent.putExtra("additionalInfo", model.getAdditionalInfo());
            intent.putExtra("userId", model.getAdminId()); // ⚠️ This is admin ID

            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_data_file, parent, false);
        return new Viewholder(view);
    }

    public static class Viewholder extends RecyclerView.ViewHolder {

        TextView txtTitle, txtDesc;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.Title);
            txtDesc = itemView.findViewById(R.id.Desc);
        }
    }
}
