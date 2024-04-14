package com.example.ecomerceapp1.activities;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.List;

public class ConfirmOrderActivity extends AppCompatActivity {


    Button placeOrderButton;
    EditText clientName, address, phoneNumber;
    ImageView backToCart;
    FirebaseAuth auth;
    FirebaseDatabase database;

    FirebaseFirestore db;
    List<Cart> cartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confirm_order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance("https://grocery-store-e0d7c-default-rtdb.asia-southeast1.firebasedatabase.app/");
        placeOrderButton = findViewById(R.id.place_order_button);
        backToCart = findViewById(R.id.back_to_cart);
        clientName = findViewById(R.id.confirm_client_name);
        address = findViewById(R.id.confirm_address);
        phoneNumber = findViewById(R.id.confirm_phone_number);
        auth = FirebaseAuth.getInstance();

        database.getReference().child("Users").child(auth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User currentUser = snapshot.getValue(User.class);
                        cartList = (List<Cart>) getIntent().getSerializableExtra("itemList");
                        clientName.setText(currentUser.getUsername());
                        address.setText(currentUser.getAddress());
                        phoneNumber.setText(currentUser.getPhoneNumber());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        //get from my cart
        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clientName.getText().equals("") || address.getText().equals("") || phoneNumber.getText().equals("")) {
                    Toast.makeText(ConfirmOrderActivity.this, "You must fill in all the informations", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (clientName.getText() == null || address.getText() == null || phoneNumber.getText() == null) {
                    Toast.makeText(ConfirmOrderActivity.this, "You must fill in all the informations", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Clear the current cart in DB

                db.collection("CurrentUser").document(auth.getCurrentUser().getUid()).collection("AddToCart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                Cart cart = doc.toObject(Cart.class);
                                db.collection("CurrentUser")
                                        .document(auth.getCurrentUser().getUid())
                                        .collection("AddToCart")
                                        .document(doc.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(ConfirmOrderActivity.this, "Cart Cleared", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });


                Intent intent = new Intent(ConfirmOrderActivity.this, PlaceOrderActivity.class);
                intent.putExtra("itemList", (Serializable) cartList);
                intent.putExtra("clientName", clientName.getText().toString());
                intent.putExtra("address", address.getText().toString());
                intent.putExtra("phoneNumber", phoneNumber.getText().toString());
                startActivity(intent);
            }
        });

    }
}