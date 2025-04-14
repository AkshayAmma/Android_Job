package com.example.jobportalapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobportalapp.Activities.JobDetailsActivity;  // Assuming you want to open a job details activity
import com.example.jobportalapp.Model.Model;
import com.example.jobportalapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class UserAllApplicationsAdapter extends FirebaseRecyclerAdapter<Model, UserAllApplicationsAdapter.Viewholder> {

    public UserAllApplicationsAdapter(@NonNull FirebaseRecyclerOptions<Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserAllApplicationsAdapter.Viewholder holder, int position, @NonNull Model model) {
        Context context = holder.itemView.getContext();  // More reliable context retrieval

        // For loading applications of a user into RecyclerView
        holder.txtTitle.setText("Company Name:"+model.getCompanyName());
        holder.txtDesc.setText("Job Title:"+model.getJobTitle());

        // OnClickListener to open job details
//        holder.txtTitle.setOnClickListener(view -> {
//            // Ensure data exists before passing it
//            Intent intent = new Intent(context, JobDetailsActivity.class);
//            intent.putExtra("companyName", model.getCompanyName());
//            intent.putExtra("jobTitle", model.getJobTitle());
//            intent.putExtra("jobDescription", model.getAboutJob());
//            intent.putExtra("jobSalary", model.getJobSalary());
//            intent.putExtra("startDate", model.getJobStartDate());
//            intent.putExtra("lastDate", model.getJobLastDate());
//            intent.putExtra("totalOpenings", model.getTotalOpenings());
//            intent.putExtra("requiredSkills", model.getSkillsRequired());
//            intent.putExtra("additionalInfo", model.getAdditionalInfo());
//            intent.putExtra("userId", model.getAdminId());
//
//            context.startActivity(intent); // Starting JobDetailsActivity
//        });
    }

    @NonNull
    @Override
    public UserAllApplicationsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflating the data objects into the XML file single_data_item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_data_file, parent, false);
        return new Viewholder(view);
    }

    // Viewholder to hold each object from RecyclerView and display it
    public static class Viewholder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        TextView txtDesc;

        public Viewholder(View itemView) {
            super(itemView);

            // Assigning the address of the materials
            txtTitle = itemView.findViewById(R.id.Title);
            txtDesc = itemView.findViewById(R.id.Desc);
        }
    }
}
