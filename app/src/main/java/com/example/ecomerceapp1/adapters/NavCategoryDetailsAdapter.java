package com.example.ecomerceapp1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecomerceapp1.R;
import com.example.ecomerceapp1.models.NavCategoryDetailsModel;

import java.util.List;

public class NavCategoryDetailsAdapter extends RecyclerView.Adapter<NavCategoryDetailsAdapter.ViewHolder> {
    Context context;
    List<NavCategoryDetailsModel> navCategoryModelList;

    public NavCategoryDetailsAdapter(Context context, List<NavCategoryDetailsModel> navCategoryModelList) {
        this.context = context;
        this.navCategoryModelList = navCategoryModelList;
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
    }

    @Override
    public int getItemCount() {
        return navCategoryModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);

        }
    }
}
