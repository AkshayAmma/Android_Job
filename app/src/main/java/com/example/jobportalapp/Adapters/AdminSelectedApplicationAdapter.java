package com.example.jobportalapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobportalapp.Model.Model;
import com.example.jobportalapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class AdminSelectedApplicationAdapter extends FirebaseRecyclerAdapter<Model, AdminSelectedApplicationAdapter.Viewholder> {

    public AdminSelectedApplicationAdapter(@NonNull FirebaseRecyclerOptions<Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AdminSelectedApplicationAdapter.Viewholder holder, int position, @NonNull Model model) {
        // Debugging log statements to ensure data is being fetched correctly
        Log.d("AdminSelectedApp", "UserName: " + model.getUserName());
        Log.d("AdminSelectedApp", "JobTitle: " + model.getJobTitle());

        // Loading the selected application user name into the recycler view
        holder.txtTitle.setText("User Name:"+model.getUserName());

        // Loading the selected application job title into the recycler view
        holder.txtaboutjob.setText("Job Title:"+model.getJobTitle());

        // Optional: You can load the description as well if needed
    }

    @NonNull
    @Override
    public AdminSelectedApplicationAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_data_file, parent, false);
        return new Viewholder(view);
    }

    /**
     * ViewHolder to hold each item and show it in the RecyclerView.
     * Each ViewHolder represents a job application data item.
     */
    public static class Viewholder extends RecyclerView.ViewHolder {

        TextView txtusername;

        TextView txtTitle; // TextView for user name
        TextView txtaboutjob;  // TextView for job title

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            // Assigning the views from the layout to the corresponding variables
            txtTitle = itemView.findViewById(R.id.Title);
            txtaboutjob = itemView.findViewById(R.id.Desc);

            // Optional: Set default visibility or other parameters here if needed
        }
    }
}
