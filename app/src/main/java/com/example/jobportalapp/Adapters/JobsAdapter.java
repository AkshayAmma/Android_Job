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
    protected void onBindViewHolder(Viewholder holder, int position, Model model) {
        Context context = holder.itemView.getContext();  // Better practice for getting the context

        // For loading all job titles into the RecyclerView
        holder.txtTitle.setText(model.getJobTitle());

        // For loading all the job salaries into the RecyclerView
        holder.txtDesc.setText(model.getJobSalary());

        // Handling click event on job title to show more details
        holder.txtTitle.setOnClickListener(view -> {
            // Retrieving all the job details to pass in the Intent
            String companyName = model.getCompanyName();
            String jobTitle = model.getJobTitle();
            String jobDescription = model.getAboutJob();
            String jobSalary = model.getJobSalary();
            String startDate = model.getJobStartDate();
            String lastDate = model.getJobLastDate();
            String totalOpenings = model.getTotalOpenings();
            String requiredSkills = model.getSkillsRequired();
            String additionalInfo = model.getAdditionalInfo();
            String userId = model.getAdminId();

            // Create Intent to navigate to JobDetailsActivity
            Intent intent = new Intent(context, JobDetailsActivity.class);

            // Passing job details to JobDetailsActivity using Intent
            intent.putExtra("companyName", companyName);
            intent.putExtra("jobTitle", jobTitle);
            intent.putExtra("jobDescription", jobDescription);
            intent.putExtra("jobSalary", jobSalary);
            intent.putExtra("startDate", startDate);
            intent.putExtra("lastDate", lastDate);
            intent.putExtra("totalOpenings", totalOpenings);
            intent.putExtra("requiredSkills", requiredSkills);
            intent.putExtra("additionalInfo", additionalInfo);
            intent.putExtra("userId", userId);

            // Starting JobDetailsActivity
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflating the view for each RecyclerView item (job listing)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_data_file, parent, false);
        return new Viewholder(view);
    }

    // Viewholder to hold each individual job data in the RecyclerView
    public static class Viewholder extends RecyclerView.ViewHolder {

        TextView txtTitle;  // TextView for job title
        TextView txtDesc;   // TextView for job description (salary)

        public Viewholder(View itemView) {
            super(itemView);

            // Initializing the TextViews for job title and description
            txtTitle = itemView.findViewById(R.id.Title);
            txtDesc = itemView.findViewById(R.id.Desc);
        }
    }
}
