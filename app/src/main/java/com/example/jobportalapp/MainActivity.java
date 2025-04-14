package com.example.jobportalapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.jobportalapp.Activities.AdminActivity;
import com.example.jobportalapp.Activities.RoleActivity;
import com.example.jobportalapp.Activities.StartingActivity;
import com.example.jobportalapp.Fragments.AdminAllApplicationsFragment;
import com.example.jobportalapp.Fragments.DisplayJobFragment;
import com.example.jobportalapp.Fragments.UserDashboardFragment;
import com.example.jobportalapp.Fragments.UserProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        frameLayout = findViewById(R.id.UserFragmentContainer);
        bottomNavigationView = findViewById(R.id.UserBottomNavigationView);

        // Default fragment setup
        getSupportFragmentManager().beginTransaction().replace(R.id.UserFragmentContainer, new DisplayJobFragment()).commit();

        // Bottom navigation click listener
        bottomNavigationView.setOnItemSelectedListener(bottomNavigationMethod);
    }

    private final BottomNavigationView.OnItemSelectedListener bottomNavigationMethod = item -> {
        Fragment fragment = null;

        // Show appropriate Fragment based on selected item
        if (item.getItemId() == R.id.homeMenu) {
            fragment = new DisplayJobFragment();
        } else if (item.getItemId() == R.id.Dashboard) {
            fragment = new UserDashboardFragment();
        } else if (item.getItemId() == R.id.profileMenu) {
            fragment = new UserProfileFragment();
        }

        // Replace fragment in FrameLayout
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.UserFragmentContainer, fragment).commit();
        }

        return true;
    };

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        if (mUser == null) {
            // If not logged in, redirect to StartingActivity (login screen)
            Intent intent = new Intent(MainActivity.this, StartingActivity.class);
            startActivity(intent);
        } else {
            // If logged in, check user role
            String userId = mUser.getUid();
            Log.d("MainActivityRoleCheck", "User ID: " + userId);

            // Reference to the "role" in Firebase Realtime Database
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("role");

            // Listen for the role data from Firebase
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String role = dataSnapshot.getValue(String.class);

                        if (role != null) {
                            // Navigate based on the role
                            if (role.equals("admin")) {
                                // Redirect to AdminActivity
                                Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                                startActivity(intent);
                            } else if (role.equals("user")) {
                                // Stay in current activity, handle user-specific UI
                                handleUserRole();
                            }
                        } else {
                            Log.e("MainActivityRoleCheck", "Role is null for user " + userId);
                        }
                    } else {
                        Log.e("MainActivityRoleCheck", "Role data not found for user " + userId);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("MainActivityRoleCheck", "Error fetching user role: " + databaseError.getMessage());
                }
            });
        }
    }

    private void handleUserRole() {
        // Setup specific UI or navigation for normal user roles
        // Here, you could set up fragments like user dashboard or job listings based on your app design.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.UserFragmentContainer, new DisplayJobFragment());  // Default fragment for users
        transaction.commit();
    }
}
