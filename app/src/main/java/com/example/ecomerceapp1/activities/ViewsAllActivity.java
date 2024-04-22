package com.example.ecomerceapp1.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecomerceapp1.R;
import com.example.ecomerceapp1.adapters.ViewAllAdapter;
import com.example.ecomerceapp1.models.PopularModel;
import com.example.ecomerceapp1.models.ViewAllProductsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewsAllActivity extends AppCompatActivity {

    List<ViewAllProductsModel> viewAllProductsModelList;

    ViewAllAdapter viewAllAdapter;
    RecyclerView recyclerView;
    FirebaseFirestore db;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_views_all);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewsAllActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        //The FireStore DB
        db = FirebaseFirestore.getInstance();
        String type = getIntent().getStringExtra("type");
        viewAllProductsModelList = new ArrayList<ViewAllProductsModel>();
        viewAllAdapter = new ViewAllAdapter(ViewsAllActivity.this, viewAllProductsModelList);

        //Our Layout
        recyclerView = findViewById(R.id.view_all_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(ViewsAllActivity.this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(viewAllAdapter);


        //Read the View ALl Products and filter to get Type (Fruit)
        if (type != null) {
            db.collection("All Products").whereEqualTo("type", type).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ViewsAllActivity.this, "OK", Toast.LENGTH_SHORT).show();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            //if loaded data
                            ViewAllProductsModel viewAllProduct = document.toObject(ViewAllProductsModel.class);
                            viewAllProductsModelList.add(viewAllProduct);
                            viewAllAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(ViewsAllActivity.this, "Error Getting Data", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if (Objects.equals(type, "all")) {
            db.collection("All Products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ViewsAllActivity.this, "OK", Toast.LENGTH_SHORT).show();
                        for (DocumentSnapshot document : task.getResult().getDocuments()) {
                            //if loaded data
                            ViewAllProductsModel viewAllProduct = document.toObject(ViewAllProductsModel.class);
                            viewAllProduct.setDocumentId(document.getId());
                            viewAllProductsModelList.add(viewAllProduct);
                            viewAllAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(ViewsAllActivity.this, "Error Getting Data", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}