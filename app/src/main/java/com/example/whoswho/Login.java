package com.example.whoswho;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends Activity {

    private LinearLayout loginLayout, signupLayout;
    private TextInputEditText etEmail, etPassword, etFullName, etSignupEmail, etSignupPassword;
    private MaterialButton loginButton, signupButton;
    private TextView signupLink, backToLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(Login.this, HomePage.class);
            startActivity(intent);
            finish();
            return;
        }

        // Initialize layouts
        loginLayout = findViewById(R.id.loginLayout);
        signupLayout = findViewById(R.id.signupLayout);

        // Login form
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        loginButton = findViewById(R.id.login_button);
        signupLink = findViewById(R.id.signupLink);

        // Signup form
        etFullName = findViewById(R.id.etFullName);
        etSignupEmail = findViewById(R.id.etSignupEmail);
        etSignupPassword = findViewById(R.id.etSignupPassword);
        signupButton = findViewById(R.id.signup_button);
        backToLogin = findViewById(R.id.backToLogin);

        // Switch to signup layout
        signupLink.setOnClickListener(v -> {
            loginLayout.setVisibility(View.GONE);
            signupLayout.setVisibility(View.VISIBLE);
        });

        // Switch back to login layout
        backToLogin.setOnClickListener(v -> {
            loginLayout.setVisibility(View.VISIBLE);
            signupLayout.setVisibility(View.GONE);
        });

        // Handle login
        loginButton.setOnClickListener(v -> handleLogin());

        // Handle signup
        signupButton.setOnClickListener(v -> handleSignup());
    }

    private void handleLogin() {
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email");
            etEmail.requestFocus();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, HomePage.class);
                        startActivity(intent);
                        finish();
                    } else {
                        String error = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                        Toast.makeText(this, "Login Failed: " + error, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void handleSignup() {
        String fullName = etFullName.getText() != null ? etFullName.getText().toString().trim() : "";
        String email = etSignupEmail.getText() != null ? etSignupEmail.getText().toString().trim() : "";
        String password = etSignupPassword.getText() != null ? etSignupPassword.getText().toString().trim() : "";

        if (fullName.isEmpty()) {
            etFullName.setError("Full name is required");
            etFullName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            etSignupEmail.setError("Email is required");
            etSignupEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etSignupEmail.setError("Enter a valid email");
            etSignupEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etSignupPassword.setError("Password is required");
            etSignupPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etSignupPassword.setError("Password must be at least 6 characters");
            etSignupPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(this, "User created successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, HomePage.class);
                        startActivity(intent);
                        finish();
                    } else {
                        String error = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                        Toast.makeText(this, "Signup Failed: " + error, Toast.LENGTH_LONG).show();
                    }
                });
    }
}
