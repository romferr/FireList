package com.darkjp.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private ImageView imgHome;
    private EditText email, password;
    private ProgressBar progressBar;
    private Button login, register;

    private FirebaseAuth mAuth;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        progressBar = findViewById(R.id.login_progressBar);
        login = findViewById(R.id.login_btn_login);
        register = findViewById(R.id.login_btn_register);

        //if user just logOut, email's already filled
        String userEmail = "";
        if (getIntent().hasExtra("email")) {
            userEmail = getIntent().getStringExtra("email");
            email.setText(userEmail);
        }

        // init Authentication
        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = email.getText().toString();
                String userPwd = password.getText().toString();

                if (!userEmail.contains("@")) {
                    Toast.makeText(LoginActivity.this, "Check your eMail.\nNot Valid", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (userEmail.equals("")) {
                    Toast.makeText(LoginActivity.this, "No email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (userPwd.length() < 8) {
                    Toast.makeText(LoginActivity.this, "Password is 8 signs minimum", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                signIn(userEmail, userPwd);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUp = new Intent(LoginActivity.this, SignUp.class);
                startActivity(signUp);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //check if user is signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent dashboard = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(dashboard);
        }
    }

    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            Intent dashboard = new Intent(LoginActivity.this, DashboardActivity.class);
                            startActivity(dashboard);
                            progressBar.setVisibility(View.GONE);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        progressBar.setVisibility(View.GONE);
    }
}