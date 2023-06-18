package com.pamgroup.restaurantlistapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.pamgroup.restaurantlistapp.model.Restaurant;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {

    private Context context;
    private List<Restaurant>  restaurantList;

    public RestaurantAdapter(Context context) {
        this.context = context;
    }

    public List<Restaurant> getRestaurantList() {
        return restaurantList;
    }

    public void setRestaurantList(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);

        holder.tvName.setText(restaurant.getName());
        holder.tvAddress.setText(restaurant.getAddress());

        Bundle restaurantBundle = new Bundle();
        restaurantBundle.putString("restaurantId", restaurant.getRestaurantId());
        restaurantBundle.putString("name", restaurant.getName());
        restaurantBundle.putString("address", restaurant.getRestaurantId());
        restaurantBundle.putString("businessHour", restaurant.getBusinessHour());
        restaurantBundle.putString("description", restaurant.getDescription());
        restaurantBundle.putString("imageURL", restaurant.getImageURL());

        holder.acivEdit.setOnClickListener(view -> {
            Intent editIntent = new Intent(this, EditRestaurant.class);
            editIntent.putExtra("restaurantBundle", restaurantBundle);
            context.startActivity(editIntent);
        });

        holder.ibDetail.setOnClickListener(view -> {
            Intent detailIntent = new Intent(this, DetailRestaurant.class);
            detailIntent.putExtra("restaurantBundle", restaurantBundle);
            context.startActivity(detailIntent);
        });

        holder.acivDelete.setOnClickListener(view -> {

        });
    }


    @Override
    public int getItemCount() {
        return restaurantList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName, tvAddress;
        private AppCompatImageView acivEdit, acivDelete;
        private ImageButton ibDetail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            tvAddress = itemView.findViewById(R.id.tv_address);
            acivEdit = itemView.findViewById(R.id.aciv_edit);
            acivDelete = itemView.findViewById(R.id.aciv_delete);
            ibDetail = itemView.findViewById(R.id.ib_detail);
        }
    }
}
