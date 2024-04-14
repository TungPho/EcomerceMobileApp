package com.example.ecomerceapp1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecomerceapp1.R;
import com.example.ecomerceapp1.adapters.NavCategoryDetailsAdapter;
import com.example.ecomerceapp1.models.NavCategoryDetailsModel;
import com.example.ecomerceapp1.models.ViewAllProductsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NavigationProductDetailActivity extends AppCompatActivity {


    RecyclerView recyclerView;

    NavCategoryDetailsAdapter navCategoryDetailsAdapter;

    List<NavCategoryDetailsModel> navCategoryDetailsModelList;

    FirebaseFirestore db;

    String type = null;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_navigation_product_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = FirebaseFirestore.getInstance();
        navCategoryDetailsModelList = new ArrayList<>();
        navCategoryDetailsAdapter = new NavCategoryDetailsAdapter(NavigationProductDetailActivity.this, navCategoryDetailsModelList);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(NavigationProductDetailActivity.this,RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(navCategoryDetailsAdapter);

        //get type
        type = getIntent().getStringExtra("type");
        toolbar = findViewById(R.id.nav_details_toolbar);
        setSupportActionBar(toolbar);



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavigationProductDetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        //Read the View ALl Products and filter to get Type (Fruit)
        db.collection("NavCategoryDetails").whereEqualTo("type", type).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(NavigationProductDetailActivity.this, "OK"+ type, Toast.LENGTH_SHORT).show();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //if loaded data
                        NavCategoryDetailsModel navCategoryDetailsModel = document.toObject(NavCategoryDetailsModel.class);
                        Toast.makeText(NavigationProductDetailActivity.this, navCategoryDetailsModel.toString(), Toast.LENGTH_SHORT).show();
                        navCategoryDetailsModelList.add(navCategoryDetailsModel);
                        navCategoryDetailsAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(NavigationProductDetailActivity.this, "Error Getting Data", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


}
