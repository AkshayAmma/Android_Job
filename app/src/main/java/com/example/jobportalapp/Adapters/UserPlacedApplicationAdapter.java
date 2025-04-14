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

public class UserPlacedApplicationAdapter extends FirebaseRecyclerAdapter<Model, UserPlacedApplicationAdapter.Viewholder> {

    public UserPlacedApplicationAdapter(FirebaseRecyclerOptions<Model> options) {
        super(options);
    }

    @Override
    public void onBindViewHolder(@NonNull UserPlacedApplicationAdapter.Viewholder holder, int position, @NonNull Model model) {
        Context context = holder.itemView.getContext();  // More direct context retrieval

        // Check if the views are not null before setting text
        if (holder.txtTitle != null) {
            holder.txtTitle.setText("Company Name:"+model.getCompanyName());
        }

        if (holder.txtDesc != null) {
            holder.txtDesc.setText("Job Title:"+model.getJobTitle());
        }

        // Click Listener to open detailed information about the job
//        holder.itemView.setOnClickListener(view -> {
//            // Getting details for the selected job
//            String companyName = model.getCompanyName();
//            String jobTitle = model.getJobTitle();
//            String jobDescription = model.getAboutJob();
//            String jobSalary = model.getJobSalary();
//            String startDate = model.getJobStartDate();
//            String lastDate = model.getJobLastDate();
//            String totalOpenings = model.getTotalOpenings();
//            String requiredSkills = model.getSkillsRequired();
//            String additionalInfo = model.getAdditionalInfo();
//            String userId = model.getAdminId();

            // Creating an intent to show the details of the selected job
//            Intent intent = new Intent(context, JobDetailsActivity.class);
//
//            // Passing the job details to the next activity
//            intent.putExtra("companyName", companyName);
//            intent.putExtra("jobTitle", jobTitle);
//            intent.putExtra("jobDescription", jobDescription);
//            intent.putExtra("jobSalary", jobSalary);
//            intent.putExtra("startDate", startDate);
//            intent.putExtra("lastDate", lastDate);
//            intent.putExtra("totalOpenings", totalOpenings);
//            intent.putExtra("requiredSkills", requiredSkills);
//            intent.putExtra("additionalInfo", additionalInfo);
//            intent.putExtra("userId", userId);
//
//            // Starting the JobDetailsActivity
//            context.startActivity(intent);
//        });
    }

    @NonNull
    @Override
    public UserPlacedApplicationAdapter.Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflating the data objects into the XML file (single_data_file)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_data_file, parent, false);
        return new Viewholder(view);
    }

    // Viewholder to hold each object from the RecyclerView and show it in the RecyclerView
    public static class Viewholder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        TextView txtDesc;

        public Viewholder(View itemView) {
            super(itemView);

            // Assigning the address of the materials to the TextViews
            txtTitle = itemView.findViewById(R.id.Title);
            txtDesc = itemView.findViewById(R.id.Desc);
        }
    }
}
