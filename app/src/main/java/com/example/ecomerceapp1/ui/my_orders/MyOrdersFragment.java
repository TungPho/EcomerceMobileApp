package com.example.ecomerceapp1.ui.my_orders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ecomerceapp1.R;
import com.example.ecomerceapp1.activities.MainActivity;
import com.example.ecomerceapp1.activities.ViewsAllActivity;
import com.example.ecomerceapp1.adapters.MyOrderAdapter;
import com.example.ecomerceapp1.models.Cart;
import com.example.ecomerceapp1.models.MyOrderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class MyOrdersFragment extends Fragment {



    RecyclerView recyclerView;
    MyOrderAdapter myOrderAdapter;
    List<MyOrderModel> myOrderModelList;

    FirebaseFirestore db;
    FirebaseAuth auth;

    ProgressBar progressBar;

    public MyOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_my_orders, container, false);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        myOrderModelList = new ArrayList<>();
        myOrderAdapter = new MyOrderAdapter(getContext(), myOrderModelList);

        //Recycler view
        recyclerView = root.findViewById(R.id.orders_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(myOrderAdapter);


        progressBar = root.findViewById(R.id.progress_bar);

//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);


//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ViewsAllActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });
        db.collection("CurrentUser").document(auth.getCurrentUser().getUid()).collection("MyOrder").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //success
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                        MyOrderModel myOrderModel = documentSnapshot.toObject(MyOrderModel.class);
                        myOrderModelList.add(myOrderModel);
                        myOrderAdapter.notifyDataSetChanged();
                    
                    }
                }
            }
        });



        return  root;
    }
}