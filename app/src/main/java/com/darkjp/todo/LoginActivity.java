package com.darkjp.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    private ImageView imgHome;
    private EditText email, password;
    private ProgressBar progressBar;
    private Button login, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        progressBar = findViewById(R.id.login_progressBar);
        login = findViewById(R.id.login_btn_login);
        register = findViewById(R.id.login_btn_register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                Intent dashboard = new Intent(LoginActivity.this, DashboardActivity.class);
                startActivity(dashboard);
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}