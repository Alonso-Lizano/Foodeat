package com.ren.renshomemadefood.content.screens.components;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ren.renshomemadefood.R;
import com.ren.renshomemadefood.content.screens.drawermenu.ProfileScreen;
import com.squareup.picasso.Picasso;

public class ProfileDetailsScreen extends AppCompatActivity {
    private static final int REGISTER_CODE = 1;
    private ImageView editImgUser;
    private LottieAnimationView progressBar;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private StorageReference reference;
    private Uri uri;
    private Button saveChangesBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_details_screen);

        editImgUser = findViewById(R.id.editImgUser);
        saveChangesBtn = findViewById(R.id.saveChanges);
        progressBar = findViewById(R.id.progressBarAnimated);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        progressBar.setVisibility(View.INVISIBLE);
        saveChangesBtn.setVisibility(View.VISIBLE);

        reference = FirebaseStorage.getInstance().getReference("User Images");

        uri = currentUser.getPhotoUrl();

        Picasso.get().load(uri).into(editImgUser);

        onClickImgUser(editImgUser);

        saveChangesBtn.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            saveChangesBtn.setVisibility(View.INVISIBLE);
            saveChanges();
        });
    }

    private void saveChanges() {
        if (uri != null) {
            StorageReference storageReference = reference.child(mAuth.getCurrentUser().getUid() +
                    "." + getFileExtension(uri));

            storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    currentUser = mAuth.getCurrentUser();

                    UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                            .setPhotoUri(uri).build();

                    currentUser.updateProfile(request).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            finish();
                        } else {
                            Toast.makeText(ProfileDetailsScreen.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                        }
                    });
                });

                showMessage("Upload Successful");
            });
        }
    }

    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    //Obtiene la extencion de la imagen
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap typeMap = MimeTypeMap.getSingleton();
        return typeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void onClickImgUser(ImageView userImg) {
        userImg.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 25) {
                checkAndRequestPermission();
            } else {
                openGallery();
            }
        });
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
                                editImgUser.setImageURI(uri);
                            }
                        }
                    }
                }
            });
}