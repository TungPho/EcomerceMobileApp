package com.example.ecomerceapp1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecomerceapp1.R;
import com.google.firebase.auth.FirebaseAuth;

public  class HomeActivty extends AppCompatActivity {
    Button login_btn, sign_up_btn;
    ProgressBar progressBar;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        login_btn = findViewById(R.id.btn_login_welcome);
        sign_up_btn = findViewById(R.id.btn_register_welcome);
        progressBar = findViewById(R.id.progress_welcome);
        progressBar.setVisibility(View.GONE);
        auth = FirebaseAuth.getInstance();
       // if already login a user
//        if(auth.getCurrentUser() != null){
//            progressBar.setVisibility(View.VISIBLE);
//            startActivity(new Intent(HomeActivty.this, MainActivity.class));
//            Toast.makeText(HomeActivty.this, "Please Wait", Toast.LENGTH_SHORT).show();
//            finish();
//        }


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                startActivity(new Intent(HomeActivty.this, LoginActivity.class));
            }
        });
        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                startActivity(new Intent(HomeActivty.this, RegistrationActivity.class));
            }
        });
    }

}