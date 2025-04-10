package com.example.jobportalapp.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.jobportalapp.Adapters.UserAllApplicationsAdapter;
import com.example.jobportalapp.Model.Model;
import com.example.jobportalapp.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class UserAllApplicationsFragment extends Fragment {

    RecyclerView recyclerView;
    UserAllApplicationsAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public UserAllApplicationsFragment() {
        // Required empty public constructor
    }

    public static UserAllApplicationsFragment newInstance(String param1, String param2) {
        UserAllApplicationsFragment fragment = new UserAllApplicationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_all_applications, container, false);

        // Initialize RecyclerView and SwipeRefreshLayout
        recyclerView = view.findViewById(R.id.UserAllApplicationsRecyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get current user ID from Firebase Authentication
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        // Set up FirebaseRecyclerOptions
        FirebaseRecyclerOptions<Model> options = new FirebaseRecyclerOptions.Builder<Model>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("jobApplications").child(userId), Model.class)
                .build();

        // Set up the adapter
        adapter = new UserAllApplicationsAdapter(options);
        recyclerView.setAdapter(adapter);

        // Set up swipe to refresh functionality
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh the data and stop the refresh animation
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        // Optional: Show a Toast if no job applications are available
        FirebaseDatabase.getInstance().getReference().child("jobApplications")
                .child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            Toast.makeText(getContext(), "No applications yet", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle any error here
                        Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Start listening for data when the fragment starts
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        // Stop listening for data when the fragment stops
        adapter.stopListening();
    }
}
