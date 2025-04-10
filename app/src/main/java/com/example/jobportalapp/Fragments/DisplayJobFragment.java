package com.example.jobportalapp.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jobportalapp.Adapters.JobsAdapter;
import com.example.jobportalapp.Model.Model;
import com.example.jobportalapp.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class DisplayJobFragment extends Fragment {

    private JobsAdapter jobsAdapter;

    public DisplayJobFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_job, container, false);

        // Assign RecyclerView to display all jobs
        RecyclerView recyclerView = view.findViewById(R.id.JobsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set up FirebaseRecyclerOptions
        FirebaseRecyclerOptions<Model> options = new FirebaseRecyclerOptions.Builder<Model>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("jobs"), Model.class)
                .build();

        // Set up the adapter
        jobsAdapter = new JobsAdapter(options);
        recyclerView.setAdapter(jobsAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Starts listening for data from Firebase when this fragment starts
        jobsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        // Stops listening for data from Firebase when this fragment stops
        jobsAdapter.stopListening();
    }
}
