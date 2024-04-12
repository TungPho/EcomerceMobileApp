package com.example.ecomerceapp1.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecomerceapp1.R;
import com.example.ecomerceapp1.activities.ViewsAllActivity;
import com.example.ecomerceapp1.models.PopularModel;

import java.util.List;

//custom adapter to show the products to the recycler view
public class PopularAdapters extends RecyclerView.Adapter<PopularAdapters.ViewHolder> {

    private Context context;
    private List<PopularModel> popularProductsList;

    public PopularAdapters(Context context, List<PopularModel> popularProductsList) {
        this.context = context;
        this.popularProductsList = popularProductsList;
    }

    @NonNull
    @Override
    public PopularAdapters.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PopularAdapters.ViewHolder holder, int position) {
        //load the image, the holder will have the element we want
        Glide.with(context).load(popularProductsList.get(position).getImg_url()).into(holder.popImg);
        holder.name.setText(popularProductsList.get(position).getName());
        holder.discount.setText(popularProductsList.get(position).getDiscount());
        holder.rating.setText(popularProductsList.get(position).getRating());
        holder.description.setText(popularProductsList.get(position).getDescription());


        String type = popularProductsList.get(position).getType();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewsAllActivity.class);
                intent.putExtra("type", type);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return popularProductsList.size();
    }

    //Create an innerclass ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView popImg;
        TextView name, description, rating, discount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            popImg = itemView.findViewById(R.id.pop_img);
            name = itemView.findViewById(R.id.pop_name);
            description = itemView.findViewById(R.id.pop_des);
            rating = itemView.findViewById(R.id.pop_rating);
            discount = itemView.findViewById(R.id.pop_discount);
        }
    }
}
