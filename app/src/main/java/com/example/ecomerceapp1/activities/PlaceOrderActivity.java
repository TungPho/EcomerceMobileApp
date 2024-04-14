package com.example.ecomerceapp1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.ecomerceapp1.R;
import com.example.ecomerceapp1.models.Cart;
import com.example.ecomerceapp1.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class PlaceOrderActivity extends AppCompatActivity {


    FirebaseAuth auth;
    FirebaseFirestore db;
    Button back;
    FirebaseDatabase database;
    User currentUser = null;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_place_order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        back = findViewById(R.id.back_to_home_button);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance("https://grocery-store-e0d7c-default-rtdb.asia-southeast1.firebasedatabase.app/");

        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUser = snapshot.getValue(User.class);

                Toast.makeText(PlaceOrderActivity.this, currentUser.toString(), Toast.LENGTH_SHORT).show();
                List<Cart> cartList = (List<Cart>) getIntent().getSerializableExtra("itemList");
                if (cartList != null) {
                    for (Cart item : cartList) {
                        final HashMap<String, Object> cartMap = new HashMap<>();
                        cartMap.put("productName", item.getProductName());
                        cartMap.put("productPrice", item.getProductPrice());
                        cartMap.put("currentDate", item.getCurrentDate());
                        cartMap.put("currentTime", item.getCurrentTime());
                        cartMap.put("totalQuantity", item.getTotalQuantity());
                        cartMap.put("totalPrice", item.getTotalPrice());
                        cartMap.put("address", currentUser.getAddress());
                        cartMap.put("username", currentUser.getUsername());
                        cartMap.put("phoneNumber", currentUser.getPhoneNumber());
                        cartMap.put("img_url", item.getImg_url());

                        // So if the cart has 3 products, then MyOrder will have 3 products in the 1 order
                        db.collection("CurrentUser").document(auth.getCurrentUser().getUid()).collection("MyOrder").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                Toast.makeText(PlaceOrderActivity.this, "Your Order has been placed!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //read the address and username
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
