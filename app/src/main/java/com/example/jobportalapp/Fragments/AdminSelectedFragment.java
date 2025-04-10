package com.example.jobportalapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobportalapp.Adapters.AdminSelectedApplicationAdapter;
import com.example.jobportalapp.Model.Model;
import com.example.jobportalapp.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class AdminSelectedFragment extends Fragment {

    RecyclerView recyclerView;
    AdminSelectedApplicationAdapter adapter;

    public AdminSelectedFragment() {
        // Required empty public constructor
    }

    public static AdminSelectedFragment newInstance(String param1, String param2) {
        AdminSelectedFragment fragment = new AdminSelectedFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_selected, container, false);

        // Assigning the RecyclerView to display selected applications
        recyclerView = view.findViewById(R.id.AdminSelectedApplicationsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Getting the current logged-in user ID from Firebase Authentication
        String adminId = FirebaseAuth.getInstance().getCurrentUser() != null ?
                FirebaseAuth.getInstance().getCurrentUser().getUid() : null;

        // Handle case where no user is logged in
        if (adminId == null) {
            Toast.makeText(getContext(), "No user logged in. Please log in first.", Toast.LENGTH_SHORT).show();
            return view;
        }

        // Firebase Recycler Options to get data from Firebase database using Model class and reference
        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("selectedApplications").child(adminId), Model.class)
                        .build();

        // Setting adapter to RecyclerView
        adapter = new AdminSelectedApplicationAdapter(options);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Starts listening for data from Firebase when this fragment starts
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // Stops listening for data from Firebase when this fragment stops
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}
