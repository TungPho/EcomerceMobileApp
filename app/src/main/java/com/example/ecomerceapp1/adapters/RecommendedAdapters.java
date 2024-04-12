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
import com.example.ecomerceapp1.models.RecommendProducts;
import com.example.ecomerceapp1.models.ViewAllProductsModel;

import java.util.List;

public class RecommendedAdapters extends RecyclerView.Adapter<RecommendedAdapters.ViewHolder> {
    Context context;
    List<RecommendProducts> recommendProductsList;

    public RecommendedAdapters(Context context, List<RecommendProducts> recommendProductsList) {
        this.context = context;
        this.recommendProductsList = recommendProductsList;
    }

    @NonNull
    @Override
    public RecommendedAdapters.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecommendedAdapters.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recommended_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendedAdapters.ViewHolder holder, int position) {
        Glide.with(context).load(recommendProductsList.get(position).getImg_url()).into(holder.image);
        holder.name.setText(recommendProductsList.get(position).getName());
        holder.price.setText(recommendProductsList.get(position).getPrice());
        holder.decription.setText(recommendProductsList.get(position).getDecription());
        holder.rating.setText(recommendProductsList.get(position).getRating());
    }

    @Override
    public int getItemCount() {
        return recommendProductsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView decription, name, rating, price;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.recommended_image);
            decription = itemView.findViewById(R.id.recommended_description);
            name = itemView.findViewById(R.id.recommended_name);
            rating =  itemView.findViewById(R.id.recommended_rating);
            price = itemView.findViewById(R.id.recommended_price);

        }
    }
}
