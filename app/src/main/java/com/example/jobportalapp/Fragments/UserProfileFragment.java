package com.example.jobportalapp.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jobportalapp.Activities.StartingActivity;
import com.example.jobportalapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileFragment extends Fragment {

    TextView userName;
    Button signOutBtn;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        // Initialize views
        CircleImageView imageView = view.findViewById(R.id.UserProfileImg);
        userName = view.findViewById(R.id.UserNameTxt);
        signOutBtn = view.findViewById(R.id.UserSignOutBtn);

        // Fetch the current user from Firebase Authentication
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Set the username
            String displayName = currentUser.getDisplayName();
            userName.setText(displayName != null ? displayName : "User");

            // Set profile picture if available
            if (currentUser.getPhotoUrl() != null) {
                Picasso.get()
                        .load(currentUser.getPhotoUrl())
                        .placeholder(R.drawable.img)  // Placeholder for the profile picture
                        .error(R.drawable.error_image)  // Error image if the URL is invalid
                        .into(imageView);
            } else {
                // Set a default image if no profile picture exists
                imageView.setImageResource(R.drawable.profile_icon);
            }
        } else {
            // Handle the case where the user is not logged in
            userName.setText("Guest");
            imageView.setImageResource(R.drawable.profile_icon);  // Set a default image if no user is logged in
        }

        // Implement sign-out functionality
        signOutBtn.setOnClickListener(view1 -> signOut());

        return view;
    }

    // Method to handle user sign out
    private void signOut() {
        // Sign out from Firebase Authentication
        FirebaseAuth.getInstance().signOut();

        // Show a confirmation message (toast)
        Toast.makeText(getContext(), "You have been signed out.", Toast.LENGTH_SHORT).show();

        // Redirect to StartingActivity after sign-out
        Intent intent = new Intent(getActivity(), StartingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
