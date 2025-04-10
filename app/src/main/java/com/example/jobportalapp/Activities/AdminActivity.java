package com.example.jobportalapp.Activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.jobportalapp.Fragments.AddJobFragment;
import com.example.jobportalapp.Fragments.AdminDashboardFragment;
import com.example.jobportalapp.Fragments.AdminProfileFragment;
import com.example.jobportalapp.R;

import java.util.HashMap;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize the FrameLayout and BottomNavigationView
        frameLayout = findViewById(R.id.AdminFragmentContainer);
        bottomNavigationView = findViewById(R.id.AdminBottomNavigationView);

        // Set the default fragment to AddJobFragment
        loadFragment(new AddJobFragment());

        // Set the listener for bottom navigation menu items
        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);
    }

    private boolean onNavigationItemSelected(MenuItem item) {
        // Load the appropriate fragment based on the menu item selected
        Fragment fragment = getFragmentForMenuItem(item);

        if (fragment != null) {
            loadFragment(fragment);
        }

        return true;
    }

    // This method maps the item ID to the appropriate fragment
    private Fragment getFragmentForMenuItem(MenuItem item) {
        Map<Integer, Fragment> fragmentMap = new HashMap<>();
        fragmentMap.put(R.id.homeMenu, new AddJobFragment());
        fragmentMap.put(R.id.Dashboard, new AdminDashboardFragment());
        fragmentMap.put(R.id.profileMenu, new AdminProfileFragment());

        return fragmentMap.getOrDefault(item.getItemId(), null);
    }


    // Helper method to load the selected fragment
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.AdminFragmentContainer, fragment)
                .commit();
    }
}
