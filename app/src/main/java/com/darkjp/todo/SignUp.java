package com.darkjp.todo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    private static final String TAG = "SignUp";

    private ImageView imgProfile;
    private EditText pseudo, email, password;
    private Button register;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference dataRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Log.i(TAG, "onCreate: reached");

        imgProfile = findViewById(R.id.signUp_imgProfile);
        pseudo = findViewById(R.id.signUp_pseudo);
        email = findViewById(R.id.signUp__email);
        password = findViewById(R.id.signUp_password);
        progressBar = findViewById(R.id.signUp_progressBar);
        register = findViewById(R.id.signUp_btn_register);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SignUp.this, "Not available for now", Toast.LENGTH_SHORT).show();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userPseudo = pseudo.getText().toString();
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();

                if (userPseudo.equals("")){
                    Toast.makeText(SignUp.this, "Please enter a pseudo", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (userEmail.equals("") || !userEmail.contains("@") || !userEmail.contains(".")){
                    Toast.makeText(SignUp.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (userPassword.length() < 8 ){
                    Toast.makeText(SignUp.this, "Password needs at least 8 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                createAccountForAuthentication(userEmail, userPassword, userPseudo);
            }
        });
    }

    private void createAccountForAuthentication(final String userEmail, String password, final String userPseudo) {
        mAuth.createUserWithEmailAndPassword(userEmail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();

                    // add user and customs infos to database
                    // probably not the best choice but "hey... who cares for now ?"
                    if (user != null)
                        createCustomUserInDatabase(user, userPseudo);
                    // then get back to normal flow

                    Intent login = new Intent(SignUp.this, LoginActivity.class);
                    startActivity(login);

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(SignUp.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createCustomUserInDatabase(FirebaseUser firebaseUser, String userPseudo) {
        User user = new User(firebaseUser.getUid(), userPseudo, firebaseUser.getEmail());

        database = FirebaseDatabase.getInstance();
        dataRef = database.getReference("user/" + user.getId());

        dataRef.setValue(user);
        Log.d(TAG, "createCustomUserInDatabase: new user Created : " + firebaseUser.getUid());

    }
}