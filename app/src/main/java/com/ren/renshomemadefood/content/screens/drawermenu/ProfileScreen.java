package com.ren.renshomemadefood.content.screens.drawermenu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ren.renshomemadefood.R;
import com.ren.renshomemadefood.content.screens.components.ProfileDetailsScreen;
import com.squareup.picasso.Picasso;

public class ProfileScreen extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private TextView userName;
    private ImageView userImg;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_screen);

        //init
        userName = findViewById(R.id.userName);
        userImg = findViewById(R.id.userImg);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        userImg.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileDetailsScreen.class);
            startActivity(intent);
        });

        setupOnBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateProfile();
    }

    private void updateProfile() {
        userName.setText(currentUser.getDisplayName());
        if (currentUser != null) {
            uri = currentUser.getPhotoUrl();
            if (uri != null) {
                Picasso.get().load(uri).into(userImg);
            } else {
                Picasso.get().load(R.drawable.user_img).into(userImg);
            }
        } else {
            Picasso.get().load(R.drawable.user_img).into(userImg);
        }
    }

    private void setupOnBackPressed() {
        final OnBackPressedDispatcher dispatcher = getOnBackPressedDispatcher();
        dispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }
}