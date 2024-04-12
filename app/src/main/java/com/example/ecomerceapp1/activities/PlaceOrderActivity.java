package com.example.ecomerceapp1.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecomerceapp1.R;
import com.example.ecomerceapp1.models.Cart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class PlaceOrderActivity extends AppCompatActivity {


    FirebaseAuth auth;
    FirebaseFirestore db;


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

        auth  = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        List<Cart> cartList = (List<Cart>) getIntent().getSerializableExtra("itemList");
        if(cartList != null){
            for (Cart item : cartList) {
                final HashMap<String, Object> cartMap = new HashMap<>();
                cartMap.put("productName", item.getProductName());
                cartMap.put("productPrice", item.getProductPrice());
                cartMap.put("currentDate", item.getCurrentDate());
                cartMap.put("currentTime", item.getCurrentTime());
                cartMap.put("totalQuantity", item.getTotalQuantity());
                cartMap.put("totalPrice", item.getTotalPrice());
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
}