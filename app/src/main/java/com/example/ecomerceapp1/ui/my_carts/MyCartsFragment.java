package com.example.ecomerceapp1.ui.my_carts;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ecomerceapp1.R;
import com.example.ecomerceapp1.activities.ConfirmOrderActivity;
import com.example.ecomerceapp1.activities.PlaceOrderActivity;
import com.example.ecomerceapp1.adapters.MyCartAdapter;
import com.example.ecomerceapp1.models.Cart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyCartsFragment extends Fragment {


    RecyclerView recyclerView;
    MyCartAdapter myCartAdapter;
    List<Cart> cartList;

    FirebaseAuth auth;
    FirebaseFirestore db;

    TextView total_price_of_all_products, refresh;

    int total_price = 0;

    Button buyNowButton;

    public MyCartsFragment() {
        // Required empty public constructor
    }
    View cartFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_carts, container, false);
        // Inflate the layout for this fragment
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        recyclerView = root.findViewById(R.id.my_carts_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        total_price_of_all_products = root.findViewById(R.id.sum_all_product_price);
        cartFragment = root.findViewById(R.id.cart_fragment);
        //our list and adapter
        cartList = new ArrayList<>();
        myCartAdapter = new MyCartAdapter(getActivity(), cartList);
        recyclerView.setAdapter(myCartAdapter);
        buyNowButton = root.findViewById(R.id.buy_now_button);
        //Read data from the DB:
        //with collection name: AddToCart
        //then the record with the Userid
        //then access the collection nested: CurrentUser
        db.collection("CurrentUser").document(auth.getCurrentUser().getUid()).collection("AddToCart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                        String documentId = documentSnapshot.getId();
                        Cart cart = documentSnapshot.toObject(Cart.class);
                        cart.setDocumentId(documentId);
                        cartList.add(cart);
                        total_price += cart.getTotalPrice();
                        myCartAdapter.notifyDataSetChanged();
                    }
                    total_price_of_all_products.setText(String.valueOf(total_price));

                }
            }
        });

        //place order
        buyNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ConfirmOrderActivity.class);
                intent.putExtra("itemList", (Serializable) cartList);
                startActivity(intent);
            }
        });

        //refresh
        cartFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total_price = 0;
                for (int i = 0; i < cartList.size(); i++) {
                    total_price += cartList.get(i).getTotalPrice();
                }
                total_price_of_all_products.setText(String.valueOf(total_price));
            }
        });




        return root;
    }
}