package com.example.ecomerceapp1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecomerceapp1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    TextView login;
    Button login_btn;

    ProgressBar progressBar;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.edt_email_login);
        password = findViewById(R.id.edt_password_login);
        login = findViewById(R.id.sign_up_txt_login);
        login_btn = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progress_login);

        //set progress bar
        progressBar.setVisibility(View.GONE);

        //Firebase
        auth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }
    public void loginUser(){
        String emailInput = email.getText().toString();
        String passwordInput = password.getText().toString();
        if(TextUtils.isEmpty(emailInput)){
            Toast.makeText(LoginActivity.this, "Email must not be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(passwordInput)){
            Toast.makeText(LoginActivity.this, "Email must not be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        //Login user with authentication
        auth.signInWithEmailAndPassword(emailInput, passwordInput).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(LoginActivity.this, "Login User Successful", Toast.LENGTH_SHORT).show();
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Login User Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

}