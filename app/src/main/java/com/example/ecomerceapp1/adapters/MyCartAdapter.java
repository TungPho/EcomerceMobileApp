package com.example.ecomerceapp1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecomerceapp1.R;
import com.example.ecomerceapp1.models.Cart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.ViewHolder> {

    //When ever you want to add data or delete data, use the Firebasefirestore
    Context context;
    List<Cart> cartList;

    FirebaseFirestore db;
    FirebaseAuth auth;

    public MyCartAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public MyCartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyCartAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cart_ver2, parent, false));
    }

    //price string, total price: int
    @Override
    public void onBindViewHolder(@NonNull MyCartAdapter.ViewHolder holder, int position) {
        holder.name.setText(cartList.get(position).getProductName());
        holder.quantity.setText(cartList.get(position).getTotalQuantity());
        holder.price.setText(cartList.get(position).getProductPrice());
        holder.totalPrice.setText(String.valueOf(cartList.get(position).getTotalPrice()));
        Glide.with(context).load(cartList.get(position).getImg_url()).into(holder.productImg);

        int prodPrice = cartList.get(position).getIntPrice();

        //When click on addIcon
        holder.addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantityTotal = Integer.parseInt(holder.quantity.getText().toString()) + 1;
                cartList.get(position).setTotalPrice(prodPrice * quantityTotal);
                cartList.get(position).setTotalQuantity(String.valueOf(quantityTotal));
                holder.quantity.setText(String.valueOf(quantityTotal));
                holder.totalPrice.setText(String.valueOf(cartList.get(position).getTotalPrice()) + "/kg");

                //string
                db.collection("CurrentUser")
                        .document(auth.getCurrentUser().getUid())
                        .collection("AddToCart")
                        .document(holder.name.getText().toString()).update("totalQuantity", holder.quantity.getText().toString());
                //int
                db.collection("CurrentUser")
                        .document(auth.getCurrentUser().getUid())
                        .collection("AddToCart")
                        .document(holder.name.getText().toString()).update("totalPrice", cartList.get(position).getTotalPrice()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "updated", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });


        holder.removeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int quantityTotal = Integer.parseInt(holder.quantity.getText().toString()) - 1;
                if(quantityTotal <= 1){
                    Toast.makeText(context, "can not be <= 1", Toast.LENGTH_SHORT).show();
                    return;
                }
                cartList.get(position).setTotalPrice(prodPrice * quantityTotal);
                cartList.get(position).setTotalQuantity(String.valueOf(quantityTotal));
                holder.quantity.setText(String.valueOf(quantityTotal));
                holder.totalPrice.setText(String.valueOf(cartList.get(position).getTotalPrice()) + "/kg");

                //string
                db.collection("CurrentUser")
                        .document(auth.getCurrentUser().getUid())
                        .collection("AddToCart")
                        .document(holder.name.getText().toString()).update("totalQuantity", holder.quantity.getText().toString());
                //int
                db.collection("CurrentUser")
                        .document(auth.getCurrentUser().getUid())
                        .collection("AddToCart")
                        .document(holder.name.getText().toString()).update("totalPrice", cartList.get(position).getTotalPrice()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "updated", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        //When click on Delete icon -> Delete that item in cart
        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Remove in the Database
                db.collection("CurrentUser").document(auth.getCurrentUser().getUid()).collection("AddToCart").document(cartList.get(position).getDocumentId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            cartList.remove(cartList.get(position));
                            notifyDataSetChanged();
                            Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show();
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
        return cartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, quantity, totalPrice;

        ImageView deleteIcon, addIcon, removeIcon;
        ImageView productImg;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cart_product_name);
            price = itemView.findViewById(R.id.cart_product_price);
            quantity = itemView.findViewById(R.id.cart_product_quantity);
            productImg = itemView.findViewById(R.id.cart_image);
            deleteIcon = itemView.findViewById(R.id.delete_item);
            addIcon = itemView.findViewById(R.id.add_icon);
            removeIcon = itemView.findViewById(R.id.remove_icon);
            totalPrice = itemView.findViewById(R.id.total_price);
        }
    }
}
