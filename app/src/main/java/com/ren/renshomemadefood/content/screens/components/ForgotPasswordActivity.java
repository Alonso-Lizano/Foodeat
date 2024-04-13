package com.ren.renshomemadefood.content.screens.components;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.ren.renshomemadefood.R;
import com.ren.renshomemadefood.content.screens.drawermenu.LoginScreen;

public class ForgotPasswordActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button reset;
    private EditText email;
    private ProgressBar progressBar;
    private Button back;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_screen);

        back = findViewById(R.id.btnForgotPasswordBack);
        reset = findViewById(R.id.btnReset);
        email = findViewById(R.id.edtForgotPasswordEmail);
        progressBar = findViewById(R.id.forgetPasswordProgressbar);

        mAuth = FirebaseAuth.getInstance();

        reset.setOnClickListener(v -> {
            userEmail = email.getText().toString().trim();
            if (!TextUtils.isEmpty(userEmail)) {
                resetPassword();
            } else {
                email.setError("Email field cant by empty");
            }
        });

        back.setOnClickListener(v -> onBackPressed());
    }

    private void resetPassword() {
        progressBar.setVisibility(View.VISIBLE);
        reset.setVisibility(View.INVISIBLE);
        mAuth.sendPasswordResetEmail(userEmail).addOnSuccessListener(unused -> {
            Toast.makeText(ForgotPasswordActivity.this, "Reset password link has been sent to" +
                    "your registered email", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ForgotPasswordActivity.this, LoginScreen.class);
            startActivity(intent);
            finish();
        }).addOnFailureListener(e -> {
            Toast.makeText(ForgotPasswordActivity.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            reset.setVisibility(View.VISIBLE);
        });
    }
}