package com.example.ecomerceapp1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecomerceapp1.R;
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

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {
    Context context;
    List<MyOrderModel> myOrderModels;
    FirebaseFirestore db;
    FirebaseAuth auth;


    public MyOrderAdapter(Context context, List<MyOrderModel> myOrderModels) {
        this.context = context;
        this.myOrderModels = myOrderModels;
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public MyOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyOrderAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderAdapter.ViewHolder holder, int position) {
        holder.name.setText(myOrderModels.get(position).getProductName());
        holder.totalPrice.setText(String.valueOf(myOrderModels.get(position).getTotalPrice() + "$"));
        holder.address.append(myOrderModels.get(position).getAddress());
        holder.clientName.append(myOrderModels.get(position).getUsername());
        Glide.with(context).load(myOrderModels.get(position).getImg_url()).into(holder.productImg);

        holder.cancelDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("CurrentUser").document(auth.getCurrentUser().getUid()).collection("MyOrder").document(myOrderModels.get(position).getDocumentId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            myOrderModels.remove(myOrderModels.get(position));
                            notifyDataSetChanged();
                            Toast.makeText(context, "Order canceled", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }

    @Override
    public int getItemCount() {
        return myOrderModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, totalPrice, clientName, address;
        ImageView productImg;
        Button cancelDelivery;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.my_order_name);
            productImg = itemView.findViewById(R.id.my_order_image);
            totalPrice = itemView.findViewById(R.id.cart_product_total_price);
            clientName = itemView.findViewById(R.id.my_order_client_name);
            address = itemView.findViewById(R.id.my_order_address);
            cancelDelivery = itemView.findViewById(R.id.cancel_delivery_button);
        }
    }
}
