package com.example.ecomerceapp1.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecomerceapp1.R;
import com.example.ecomerceapp1.activities.NavigationProductDetailActivity;
import com.example.ecomerceapp1.models.NavCategoryModel;

import java.util.List;

public class NavCategoryAdapter extends RecyclerView.Adapter<NavCategoryAdapter.ViewHolder> {

    Context context;
    List<NavCategoryModel> navCategoryModelList;

    public NavCategoryAdapter(Context context, List<NavCategoryModel> navCategoryModelList) {
        this.context = context;
        this.navCategoryModelList = navCategoryModelList;
    }

    @NonNull
    @Override
    public NavCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NavCategoryAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_cat_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NavCategoryAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(navCategoryModelList.get(position).getImg_url()).into(holder.imageView);
        holder.name.setText(navCategoryModelList.get(position).getName());
        holder.discount.setText(navCategoryModelList.get(position).getDiscount());
        holder.description.setText(navCategoryModelList.get(position).getDescription());

        String type = navCategoryModelList.get(position).getType();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NavigationProductDetailActivity.class);
                intent.putExtra("type", type);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return navCategoryModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, description, discount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cat_nav_img);
            name = itemView.findViewById(R.id.nav_cat_name);
            description = itemView.findViewById(R.id.nav_cat_description);
            discount = itemView.findViewById(R.id.nav_cat_discount);
        }
    }
}
