package com.example.jobportalapp.Fragments;

import android.os.Bundle;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jobportalapp.R;

public class UserDashboardFragment extends Fragment {

    SwitchCompat mSwitch;

    // Parameters for fragment initialization
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public UserDashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method to create a new instance of this fragment using the provided parameters.
     */
    public static UserDashboardFragment newInstance(String param1, String param2) {
        UserDashboardFragment fragment = new UserDashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Extract parameters if needed
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_dashboard, container, false);

        // Initialize the SwitchCompat to navigate between fragments
        mSwitch = view.findViewById(R.id.UserDashBoardSwitch);
        mSwitch.setChecked(false);

        // Set the default fragment to be UserAllApplicationsFragment
        getChildFragmentManager().beginTransaction()
                .replace(R.id.UserDashBoardContainer, new UserAllApplicationsFragment())
                .commit();

        // Set up the onClickListener for the switch
        mSwitch.setOnClickListener(view1 -> {
            if (mSwitch.isChecked()) {
                // Navigate to UserPlacedApplicationsFragment
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.UserDashBoardContainer, new UserPlacedApplicationsFragment())
                        .addToBackStack(null)  // Add to back stack for easy navigation
                        .commit();
            } else {
                // Navigate back to UserAllApplicationsFragment
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.UserDashBoardContainer, new UserAllApplicationsFragment())
                        .addToBackStack(null)  // Add to back stack for easy navigation
                        .commit();
            }
        });

        return view;
    }
}
