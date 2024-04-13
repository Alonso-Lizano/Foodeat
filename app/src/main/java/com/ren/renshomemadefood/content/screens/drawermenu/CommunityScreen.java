package com.ren.renshomemadefood.content.screens.drawermenu;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ren.renshomemadefood.R;
import com.ren.renshomemadefood.content.adapters.PostAdapter;
import com.ren.renshomemadefood.content.models.Post;
import com.ren.renshomemadefood.content.screens.MainScreen;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CommunityScreen extends AppCompatActivity {
    private static final int REGISTER_CODE = 2;
    private static final String POST_KEY = "Posts";
    private Dialog popAddPost;
    private ImageView userImage;
    private ImageView imgPost;
    private ImageView addPost;
    private TextView popupTitle;
    private TextView popupDescription;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Uri uri;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private List<Post> postList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_screen);

        recyclerView = findViewById(R.id.postRv);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference(POST_KEY);

        initPopup();
        setupPopupImgClick();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> popAddPost.show());

        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,
                false));
        recyclerView.setHasFixedSize(true);

        setupOnBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    postList.add(post);
                }
                postAdapter = new PostAdapter(CommunityScreen.this, postList);
                recyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setupPopupImgClick() {
        imgPost.setOnClickListener(v -> checkAndRequestPermission());
    }

    private void checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "Please accept for required permission",
                        Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REGISTER_CODE);
            }
        } else {
            openGallery();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            uri = data.getData();
                            if (uri != null) {
                                imgPost.setImageURI(uri);
                            }
                        }
                    }
                }
            });

    private void initPopup() {
        popAddPost = new Dialog(this);
        popAddPost.setContentView(R.layout.add_post);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;

        //init view
        userImage = popAddPost.findViewById(R.id.userImage);
        imgPost = popAddPost.findViewById(R.id.imgPost);
        popupTitle = popAddPost.findViewById(R.id.popupTitle);
        popupDescription = popAddPost.findViewById(R.id.popupDescription);
        addPost = popAddPost.findViewById(R.id.addPost);
        progressBar = popAddPost.findViewById(R.id.progressBar);

        Picasso.get().load(currentUser.getPhotoUrl()).into(userImage);

        addPost.setOnClickListener(v -> {
            addPost.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            if (!popupTitle.getText().toString().isEmpty() && !popupDescription.getText().toString().isEmpty()
                    && uri != null) {
                StorageReference reference = FirebaseStorage.getInstance().getReference().child("blog_img");
                final StorageReference imageFilePath = reference.child(uri.getLastPathSegment());
                imageFilePath.putFile(uri).addOnSuccessListener(taskSnapshot -> imageFilePath.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            String imgDownload = uri.toString();
                            if (currentUser.getPhotoUrl() != null) {
                                Post post = new Post(popupTitle.getText().toString(),
                                        popupDescription.getText().toString(),
                                        imgDownload, currentUser.getUid(),
                                        currentUser.getPhotoUrl().toString());

                                addNewPost(post);
                            } else {
                                Post post = new Post(popupTitle.getText().toString(),
                                        popupDescription.getText().toString(),
                                        imgDownload, currentUser.getUid(),
                                        null);

                                addNewPost(post);
                            }
                        }).addOnFailureListener(e -> {
                            showMessage(e.getMessage());
                            progressBar.setVisibility(View.INVISIBLE);
                            addPost.setVisibility(View.VISIBLE);
                        }));
            } else {
                showMessage("Please verify all fields");
                addPost.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void addNewPost(Post post) {
        DatabaseReference reference = database.getReference(POST_KEY).push();

        String postKey = reference.getKey();
        post.setId(postKey);

        reference.setValue(post).addOnSuccessListener(unused -> {
            showMessage("Success");
            progressBar.setVisibility(View.INVISIBLE);
            addPost.setVisibility(View.VISIBLE);
            popAddPost.dismiss();
        });
    }

    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
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