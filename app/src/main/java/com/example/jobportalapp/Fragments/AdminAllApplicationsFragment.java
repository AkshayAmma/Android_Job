package com.example.jobportalapp.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jobportalapp.Adapters.AdminAllApplicationsAdapter;
import com.example.jobportalapp.Model.Model;
import com.example.jobportalapp.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class AdminAllApplicationsFragment extends Fragment {

    RecyclerView recyclerView;
    AdminAllApplicationsAdapter adapter;

    public AdminAllApplicationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_all_applications, container, false);

        // Assigning the RecyclerView to display all applications
        recyclerView = view.findViewById(R.id.AdminAllApplicationsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Getting admin ID from Firebase Authentication
        String adminId = FirebaseAuth.getInstance().getCurrentUser() != null ?
                FirebaseAuth.getInstance().getCurrentUser().getUid() : null;

        if (adminId == null) {
            // If no user is logged in, handle it (e.g., show a login prompt or message)
            // Example: You could navigate the user to the login screen if no admin is logged in.
            // Show a message or prompt to log in
            return view; // Optionally show an error or prompt the user to log in.
        }

        // Firebase Recycler Options to get the data from Firebase Database using the Model class and reference
        FirebaseRecyclerOptions<Model> options = new FirebaseRecyclerOptions.Builder<Model>()
                .setQuery(FirebaseDatabase.getInstance().getReference()
                        .child("jobApplications").child(adminId), Model.class)
                .build();

        // Setting the adapter to RecyclerView
        adapter = new AdminAllApplicationsAdapter(options);
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
