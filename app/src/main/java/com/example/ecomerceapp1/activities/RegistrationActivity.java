package com.example.ecomerceapp1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import com.example.ecomerceapp1.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

     Button registerBtn;
     EditText name;
     EditText email;
     EditText password;
     TextView sign_in;

     ProgressBar progressBar;

     //Firebase DB actions
    FirebaseAuth auth;
    FirebaseDatabase database;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //initialize instances of Firebase DB
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://grocery-store-e0d7c-default-rtdb.asia-southeast1.firebasedatabase.app/");
        progressBar = findViewById(R.id.progress_reg);

        // set the progress bar, first is unvisible
        progressBar.setVisibility(View.GONE);


        registerBtn = findViewById(R.id.btn_register);
        name = findViewById(R.id.edt_name_reg);
        email = findViewById(R.id.edt_email_reg);
        password = findViewById(R.id.edt_password_reg);
        sign_in = findViewById(R.id.sign_in_txt_reg);
        //if user already have an account -> click on sign in -> go to Login Screen
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });
        //Click the SignUp or RegisterButton will create new User, check
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
                //Done creating
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }
    private void createUser(){
        String usernameInput = name.getText().toString().trim();
        String emailInput = email.getText().toString().trim();
        String passwordInput = password.getText().toString().trim();
        if(TextUtils.isEmpty(usernameInput)){
            Toast.makeText(RegistrationActivity.this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(emailInput)){
            Toast.makeText(RegistrationActivity.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(passwordInput)){
            Toast.makeText(RegistrationActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(passwordInput.length() < 6){
            Toast.makeText(RegistrationActivity.this, "Password length must be greater than 6", Toast.LENGTH_SHORT).show();
            return;
        }
        //Create User
        auth.createUserWithEmailAndPassword(emailInput, passwordInput).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User user = new User(usernameInput, emailInput, passwordInput, null);
                    //Get the userId to store in the DB
                    String userId = task.getResult().getUser().getUid();
                    Toast.makeText(RegistrationActivity.this, userId, Toast.LENGTH_SHORT).show();
                    //This is the realtime DB saving the users in
                    database.getReference().child("Users").child(userId).setValue(user);

                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegistrationActivity.this, "Registration Completed Success !", Toast.LENGTH_SHORT).show();
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegistrationActivity.this, "Registration Error !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}