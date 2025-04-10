package com.example.jobportalapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.jobportalapp.Activities.AdminActivity;
import com.example.jobportalapp.Activities.RoleActivity;
import com.example.jobportalapp.Activities.StartingActivity;
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

    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the views
        frameLayout = findViewById(R.id.UserFragmentContainer);
        bottomNavigationView = findViewById(R.id.UserBottomNavigationView);

        // Setting the default fragment as DisplayJobFragment
        getSupportFragmentManager().beginTransaction().replace(R.id.UserFragmentContainer, new DisplayJobFragment()).commit();

        // Bottom navigation click listener
        bottomNavigationView.setOnItemSelectedListener(bottomNavigationMethod);
    }

    private final BottomNavigationView.OnItemSelectedListener bottomNavigationMethod =
            item -> {
                // Assigning Fragment as Null
                Fragment fragment = null;

                // Show the appropriate Fragment based on the selected item
                if (item.getItemId() == R.id.homeMenu) {
                    fragment = new DisplayJobFragment();
                } else if (item.getItemId() == R.id.Dashboard) {
                    fragment = new UserDashboardFragment();
                } else if (item.getItemId() == R.id.profileMenu) {
                    fragment = new UserProfileFragment();
                }

                // Replacing the Fragment in the FrameLayout
                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.UserFragmentContainer, fragment).commit();
                }

                return true;
            };

    @Override
    protected void onStart() {
        super.onStart();
        // Check if the user is already logged in
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser == null) {
            // If the user is not logged in, redirect to StartingActivity
            Intent intent = new Intent(MainActivity.this, StartingActivity.class);
            startActivity(intent);
            finish();  // Optional: Finish MainActivity so the user can't go back to it
        } else {
            String userId = mUser.getUid();
            Log.d("MainActivityRoleCheck", "User ID: " + userId);

            // Reference to the "role" in Firebase Realtime Database for the current user
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("role");

            // Listen for the role data from Firebase
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String role = dataSnapshot.getValue(String.class);

                        if (role != null) {
                            // Navigate to the appropriate activity based on role
                            if (role.equals("admin")) {
                                // Start Admin Activity
                                Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                                startActivity(intent);
                                finish();  // Optional: Finish MainActivity to prevent going back
                            } else if (role.equals("user")) {
                                // Start User Activity
                                Intent intent = new Intent(MainActivity.this, RoleActivity.class);
                                startActivity(intent);
                                finish();  // Optional: Finish MainActivity to prevent going back
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
}
