package com.example.jobportalapp.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.jobportalapp.R;

public class AdminDashboardFragment extends Fragment {

    private boolean isSwitchChecked = false;  // To store switch state

    public AdminDashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);

        // Switch to navigate between fragments
        SwitchCompat mSwitch = view.findViewById(R.id.AdminDashBoardSwitch);

        // Check if we have a saved state (e.g., after a screen rotation)
        if (savedInstanceState != null) {
            isSwitchChecked = savedInstanceState.getBoolean("switch_state", false);
        }

        // Set the switch to the saved state
        mSwitch.setChecked(isSwitchChecked);

        // Initial fragment transaction based on the switch state
        if (!isSwitchChecked) {
            showAllApplicationsFragment();
        } else {
            showSelectedApplicationsFragment();
        }

        // Set up switch listener
        mSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isSwitchChecked = isChecked;

            // Navigate to the appropriate fragment based on switch state
            if (isChecked) {
                showSelectedApplicationsFragment();
            } else {
                showAllApplicationsFragment();
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the switch state to handle screen rotations
        outState.putBoolean("switch_state", isSwitchChecked);
    }

    // Helper method to show the AdminAllApplicationsFragment
    private void showAllApplicationsFragment() {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.AdminDashBoardContainer, new AdminAllApplicationsFragment())
                .commit();
    }

    // Helper method to show the AdminSelectedFragment
    private void showSelectedApplicationsFragment() {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.AdminDashBoardContainer, new AdminSelectedFragment())
                .commit();
    }
}
