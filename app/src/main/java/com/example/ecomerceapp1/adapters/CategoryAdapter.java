package com.example.ecomerceapp1.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecomerceapp1.R;
import com.example.ecomerceapp1.activities.ViewsAllActivity;
import com.example.ecomerceapp1.models.HomeCategory;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    Context context;
    List<HomeCategory> homeCategoryList;

    public CategoryAdapter(Context context, List<HomeCategory> homeCategoryList) {
        this.context = context;
        this.homeCategoryList = homeCategoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_category_items, parent, false));
    }

    //bind with the viewholder below
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(homeCategoryList.get(position).getImg_url()).into(holder.categoryImg);
        holder.categoryName.setText(homeCategoryList.get(position).getName());
        String type = homeCategoryList.get(position).getType();
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
        return homeCategoryList.size();
    }

    //This is our view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryImg;
        TextView categoryName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImg = itemView.findViewById(R.id.category_image);
            categoryName = itemView.findViewById(R.id.category_name);
        }
    }
}
