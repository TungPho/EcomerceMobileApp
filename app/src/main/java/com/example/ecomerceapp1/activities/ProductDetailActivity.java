package com.example.ecomerceapp1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.ecomerceapp1.R;
import com.example.ecomerceapp1.models.ViewAllProductsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailActivity extends AppCompatActivity {


    ImageView detailsImage;
    TextView price, rating, description, quantity;

    Button addToCart;

    ImageView plusIcon;
    ImageView removeIcon;

    ViewAllProductsModel viewAllProductsModel = null;

    int total_quantity = 1;
    int total_price = 0;

    //Save the cart to the order
    FirebaseFirestore db;
    FirebaseAuth auth;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        detailsImage = findViewById(R.id.details_image);
        plusIcon = findViewById(R.id.add_item_plus_icon);
        removeIcon = findViewById(R.id.remove_icon);
        price = findViewById(R.id.details_price);
        rating = findViewById(R.id.details_rating);
        description = findViewById(R.id.details_description);
        quantity = findViewById(R.id.quantity);
        //button add to cart
        addToCart = findViewById(R.id.add_to_cart_button);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        final Object object = getIntent().getSerializableExtra("product");
        if (object instanceof ViewAllProductsModel) {
            viewAllProductsModel = (ViewAllProductsModel) object;
        }

        if (viewAllProductsModel != null) {
            /// set the details of product by taking the product from view all products
            Glide.with(getApplicationContext()).load(viewAllProductsModel.getImg_url()).into(detailsImage);
            price.setText("Price: $" + String.valueOf(viewAllProductsModel.getPrice() + "/kg"));
            description.setText(viewAllProductsModel.getDescription());
            rating.setText(viewAllProductsModel.getRating());
        }

        plusIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (total_quantity < 10) {
                    total_quantity += 1;
                    total_price = viewAllProductsModel.getPrice() * total_quantity;
                    quantity.setText(String.valueOf(total_quantity));
                } else {
                    Toast.makeText(getApplicationContext(), "You only get to add no more than 10 products at a time!", Toast.LENGTH_SHORT);
                }
            }
        });
        removeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (total_quantity <= 1) {
                    Toast.makeText(getApplicationContext(), "You remove all the items!", Toast.LENGTH_SHORT).show();
                } else {
                    total_quantity -= 1;
                    total_price = viewAllProductsModel.getPrice() * total_quantity;
                    quantity.setText(String.valueOf(total_quantity));
                }
            }
        });


        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addedToCart();
            }
        });

    }

    public void addedToCart() {
        String saveCurrentDate, saveCurrentTime;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("productName", viewAllProductsModel.getName());// string
        cartMap.put("productPrice", price.getText().toString()); //string
        cartMap.put("intPrice", viewAllProductsModel.getPrice());// int
        cartMap.put("currentDate", saveCurrentDate);
        cartMap.put("currentTime", saveCurrentTime);
        cartMap.put("totalQuantity", quantity.getText().toString());// string
        cartMap.put("totalPrice", total_price);//int
        cartMap.put("img_url", viewAllProductsModel.getImg_url());//string

        db.collection("CurrentUser")
                .document(auth.getCurrentUser().getUid())
                .collection("AddToCart")
                .document(viewAllProductsModel.getName()).set(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ProductDetailActivity.this, "added to cart", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}