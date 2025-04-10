package com.example.jobportalapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobportalapp.Adapters.UserPlacedApplicationAdapter;
import com.example.jobportalapp.Model.Model;
import com.example.jobportalapp.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class UserPlacedApplicationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserPlacedApplicationAdapter adapter;
    private TextView noApplicationsMessage;

    public UserPlacedApplicationsFragment() {
        // Required empty public constructor
    }

    public static UserPlacedApplicationsFragment newInstance(String param1, String param2) {
        UserPlacedApplicationsFragment fragment = new UserPlacedApplicationsFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Handle parameters if needed
            String mParam1 = getArguments().getString("param1");
            String mParam2 = getArguments().getString("param2");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_placed_applications, container, false);

        // Assign RecyclerView and TextView for empty message
        recyclerView = view.findViewById(R.id.UserPlacedApplicationsRecyclerView);
        noApplicationsMessage = view.findViewById(R.id.noApplicationsMessage);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Getting the current user ID from Firebase Authentication
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        // FirebaseRecyclerOptions to get data from Firebase database using Model class and reference
        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("selectedApplications").child(userId), Model.class)
                        .build();

        // Setting the adapter to the RecyclerView
        adapter = new UserPlacedApplicationAdapter(options);
        recyclerView.setAdapter(adapter);

        // Add a listener to check if the list is empty and show the message
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                if (adapter.getItemCount() == 0) {
                    noApplicationsMessage.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    noApplicationsMessage.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Starts listening for data from Firebase when this fragment starts
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        // Stops listening for data when this fragment stops
        adapter.stopListening();
    }
}
