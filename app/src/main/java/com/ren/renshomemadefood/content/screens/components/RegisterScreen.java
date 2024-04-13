package com.ren.renshomemadefood.content.screens.components;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.ren.renshomemadefood.R;
import com.ren.renshomemadefood.content.screens.MainScreen;
import com.ren.renshomemadefood.content.screens.drawermenu.LoginScreen;

public class RegisterScreen extends AppCompatActivity {

    private ImageView imgUserPhoto;
    private Uri uri;
    private EditText regName;
    private EditText regEmail;
    private EditText regPass;
    private EditText conPass;
    private ProgressBar progressBar;
    private Button regBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);

        imgUserPhoto = findViewById(R.id.imgUserPhoto);
        regName = findViewById(R.id.regName);
        regEmail = findViewById(R.id.regEmail);
        regPass = findViewById(R.id.regPass);
        conPass = findViewById(R.id.conPass);
        progressBar = findViewById(R.id.progressBar);
        regBtn = findViewById(R.id.regBtn);

        progressBar.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        onClickRegisterBtn(regBtn);
        //onClickImgUser(imgUserPhoto);

        setupOnBackPressed();
    }

    /*private void onClickImgUser(ImageView imageView) {
        imageView.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 25) {
                checkAndRequestPermission();
            } else {
                openGallery();
            }
        });
    }*/

    /*private void checkAndRequestPermission() {
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
    }*/

    /*private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }*/

    /*private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            uri = data.getData();
                            if (uri != null) {
                                imgUserPhoto.setImageURI(uri);
                            }
                        }
                    }
                }
            });*/

    private void onClickRegisterBtn(Button button) {
        button.setOnClickListener(v -> {
            regBtn.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            final String email = regEmail.getText().toString();
            final String password = regPass.getText().toString();
            final String conPassword = conPass.getText().toString();
            final String name = regName.getText().toString();

            if (email.isEmpty() || password.isEmpty() || !conPassword.equals(password) || name.isEmpty()) {
                showMessage("Please verify all fields");
                regBtn.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            } else {
                createUserAcc(email, name, password);
            }
        });
    }

    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void createUserAcc(String email, String name, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        if (mAuth.getCurrentUser() != null) {
                            sendEmailVerification(mAuth.getCurrentUser());
                        }
                        updateUserInfo(name, mAuth.getCurrentUser());
                        showMessage("Account created");

                        /*if (uri != null) {
                            updateUserInfo(name, uri, mAuth.getCurrentUser());
                        } else {
                            updateUserInfoWithoutPhoto(name, mAuth.getCurrentUser());
                        }*/
                    } else {
                        try {
                            //showMessage("Account creation failed" + task.getException().getMessage());
                            throw task.getException();
                        } catch (FirebaseAuthUserCollisionException e) {
                            regEmail.setError("Your email is invalid or already in use");
                            regEmail.requestFocus();
                            regBtn.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                        } catch (Exception e) {
                            Log.e("registerActivity", e.getMessage());
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            regBtn.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    private void sendEmailVerification(FirebaseUser user) {
        user.sendEmailVerification().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                showMessage("Verification email sent. Please check your inbox.");
                mAuth.signOut();
            } else {
                showMessage("Failed to send verification email. Please try again later.");
            }
        });
    }


    /*private void updateUserInfo(final String name, Uri imgUserPhoto, FirebaseUser firebaseUser) {
        StorageReference reference = FirebaseStorage.getInstance().getReference().child("user_photos");
        StorageReference imgFilePath = reference.child(imgUserPhoto.getLastPathSegment());
        imgFilePath.putFile(imgUserPhoto).addOnSuccessListener(taskSnapshot -> imgFilePath.getDownloadUrl().addOnSuccessListener(uri -> {
            UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .setPhotoUri(uri)
                    .build();

            firebaseUser.updateProfile(request)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            updateUI();
                        }
                    });
        }));
    }*/

    //updateUserInfoWithoutPhoto
    private void updateUserInfo(final String name, FirebaseUser firebaseUser) {
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        firebaseUser.updateProfile(request).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                updateUI();
            }
        });
    }

    private void updateUI() {
        Intent intent = new Intent(this, MainScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
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