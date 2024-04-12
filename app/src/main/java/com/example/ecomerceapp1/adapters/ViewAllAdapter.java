package com.example.ecomerceapp1.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecomerceapp1.R;
import com.example.ecomerceapp1.activities.ProductDetailActivity;
import com.example.ecomerceapp1.models.ViewAllProductsModel;

import java.util.List;

public class ViewAllAdapter extends RecyclerView.Adapter<ViewAllAdapter.ViewHolder> {
    Context context;
    List<ViewAllProductsModel> viewAllProductsModelList;

    public ViewAllAdapter(Context context, List<ViewAllProductsModel> viewAllProductsModelList) {
        this.context = context;
        this.viewAllProductsModelList = viewAllProductsModelList;
    }

    @NonNull
    @Override
    public ViewAllAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewAllAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_all_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAllAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(viewAllProductsModelList.get(position).getImg_url()).into(holder.imageView);
        holder.name.setText(viewAllProductsModelList.get(position).getName());
        holder.description.setText(viewAllProductsModelList.get(position).getDescription());
        holder.price.setText(String.valueOf(viewAllProductsModelList.get(position).getPrice()+"/kg"));
        holder.rating.setText(viewAllProductsModelList.get(position).getRating());

        if(viewAllProductsModelList.get(position).getType().equals("egg")){
            holder.price.setText(String.valueOf(viewAllProductsModelList.get(position).getPrice() + "/dozen"));
        }
        if(viewAllProductsModelList.get(position).getType().equals("milk")){
            holder.price.setText(String.valueOf(viewAllProductsModelList.get(position).getPrice() + "/boxes"));
        }

        ViewAllProductsModel viewAllProductsModel = viewAllProductsModelList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(context, ProductDetailActivity.class);
                intent.putExtra("product", viewAllProductsModel);
                Toast.makeText(context, viewAllProductsModel.toString(), Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return viewAllProductsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView description, rating, price, name;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.view_all_name_item);
            imageView = itemView.findViewById(R.id.view_all_img);
            description = itemView.findViewById(R.id.view_all_description_item);
            rating = itemView.findViewById(R.id.view_all_rating_item);
            price = itemView.findViewById(R.id.view_all_price_item);
        }
    }
}
