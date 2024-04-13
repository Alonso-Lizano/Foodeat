package com.ren.renshomemadefood.content.screens.drawermenu;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.ren.renshomemadefood.R;
import com.ren.renshomemadefood.content.screens.MainScreen;
import com.ren.renshomemadefood.content.screens.components.ForgotPasswordActivity;
import com.ren.renshomemadefood.content.screens.components.RegisterScreen;

public class LoginScreen extends AppCompatActivity {

    private TextView signUp;
    private TextView logEmail;
    private TextView logPass;
    private ProgressBar progressBar;
    private Button loginButton;
    private TextView forgetPassword;
    private FirebaseAuth mAuth;
    private ImageView showHidePass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        signUp = findViewById(R.id.signUp);
        logEmail = findViewById(R.id.logEmail);
        logPass = findViewById(R.id.logPass);
        progressBar = findViewById(R.id.progressBar);
        loginButton = findViewById(R.id.loginButton);
        forgetPassword = findViewById(R.id.forgetPassword);
        showHidePass = findViewById(R.id.show_hide_pass);

        mAuth = FirebaseAuth.getInstance();

        progressBar.setVisibility(View.INVISIBLE);

        setupOnBackPressed();

        onClickSignUp(signUp);

        onClickLogin(loginButton);

        forgetPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginScreen.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        showHidePass.setImageResource(R.drawable.hide_pass_icon);
        showHidePass.setOnClickListener(v -> {
            if (logPass.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                //si la contraseña es visible, entonces esto la oculta
                logPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                //esto cambia el icono
                showHidePass.setImageResource(R.drawable.hide_pass_icon);
            } else {
                logPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                showHidePass.setImageResource(R.drawable.show_pass_icon);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            updateUI();
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

    private void onClickSignUp(TextView textView) {
        textView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginScreen.this, RegisterScreen.class);
            startActivity(intent);
        });
    }

    private void onClickLogin(Button button) {
        button.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            button.setVisibility(View.INVISIBLE);

            final String email = logEmail.getText().toString();
            final String password = logPass.getText().toString();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT).show();
                logEmail.setError("Email is required.");
                logEmail.requestFocus();
                loginButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please re-enter your email.", Toast.LENGTH_SHORT).show();
                logEmail.setError("Valid email is required.");
                logEmail.requestFocus();
                loginButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show();
                logPass.setError("Password is required.");
                logPass.requestFocus();
                loginButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            } else {
                signIn(email, password);
            }
        });
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressBar.setVisibility(View.INVISIBLE);
                loginButton.setVisibility(View.VISIBLE);
                updateUI();
            } else {
                try {
                    throw task.getException();
                } catch (FirebaseAuthInvalidUserException e) {
                    logEmail.setError("User does not exists or is no longer valid. Please register again.");
                    logEmail.requestFocus();
                } catch (FirebaseAuthInvalidCredentialsException e) {
                    logEmail.setError("Invalid credentials, Kindly, check and re-enter.");
                    logEmail.requestFocus();
                } catch (Exception e) {
                    showMessage(e.getMessage());
                } finally {
                    loginButton.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }

            }
        });
    }

    private void updateUI() {
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
        finish();
    }

    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}