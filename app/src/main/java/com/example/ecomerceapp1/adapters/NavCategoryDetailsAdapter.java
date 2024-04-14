package com.example.ecomerceapp1.adapters;

import android.content.Context;
import android.media.Image;
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
import com.example.ecomerceapp1.activities.ProductDetailActivity;
import com.example.ecomerceapp1.models.NavCategoryDetailsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class NavCategoryDetailsAdapter extends RecyclerView.Adapter<NavCategoryDetailsAdapter.ViewHolder> {
    Context context;
    List<NavCategoryDetailsModel> navCategoryModelList;

    FirebaseAuth auth;
    FirebaseFirestore db;
    int quantity;

    public NavCategoryDetailsAdapter(Context context, List<NavCategoryDetailsModel> navCategoryModelList) {
        this.context = context;
        this.navCategoryModelList = navCategoryModelList;
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        quantity = 1;
    }

    @NonNull
    @Override
    public NavCategoryDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NavCategoryDetailsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_category_details_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NavCategoryDetailsAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(navCategoryModelList.get(position).getImg_url()).into(holder.imageView);
        holder.name.setText(navCategoryModelList.get(position).getName());
        holder.price.setText(navCategoryModelList.get(position).getPrice());
        holder.buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String saveCurrentDate, saveCurrentTime;
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
                saveCurrentDate = currentDate.format(calendar.getTime());

                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                saveCurrentTime = currentTime.format(calendar.getTime());

                int price = Integer.parseInt(navCategoryModelList.get(position).getPrice());

                final HashMap<String, Object> cartMap = new HashMap<>();
                cartMap.put("productName", navCategoryModelList.get(position).getName());// string
                cartMap.put("productPrice", holder.price.getText().toString()); //string
                cartMap.put("intPrice", price);// int
                cartMap.put("currentDate", saveCurrentDate);
                cartMap.put("currentTime", saveCurrentTime);
                cartMap.put("totalQuantity", holder.quantity.getText().toString());// string
                cartMap.put("totalPrice", price * quantity);//int
                cartMap.put("img_url", navCategoryModelList.get(position).getImg_url());//string

                db.collection("CurrentUser")
                        .document(auth.getCurrentUser().getUid())
                        .collection("AddToCart")
                        .document(navCategoryModelList.get(position).getName()).set(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context, "added to cart", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantity <= 10){
                    quantity += 1;
                    holder.quantity.setText(String.valueOf(quantity));
                }else{
                    Toast.makeText(context, "You can not buy more than 10 products", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantity > 1){
                    quantity -= 1;
                    holder.quantity.setText(String.valueOf(quantity));
                }else{
                    Toast.makeText(context, "The number of products must be greater than 1", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return navCategoryModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, price, quantity;
        Button buynow;
        ImageView addBtn, removeBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            buynow = itemView.findViewById(R.id.buy_now_button);
            addBtn = itemView.findViewById(R.id.add_item_plus_icon);
            removeBtn = itemView.findViewById(R.id.remove_icon);
            quantity = itemView.findViewById(R.id.nav_details_quantity);
        }
    }
}
