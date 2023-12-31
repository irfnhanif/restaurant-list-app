package com.pamgroup.restaurantlistapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.pamgroup.restaurantlistapp.helper.RestaurantDatabase;
import com.pamgroup.restaurantlistapp.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {
    private Context context;
    private List<Restaurant> restaurantList = new ArrayList<>();

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

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.placeholder_restaurant)
                .error(R.drawable.placeholder_restaurant);

        Glide.with(context)
                .load(restaurant.getImageURL())
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.ivRestoran);

        Bundle restaurantBundle = new Bundle();
        restaurantBundle.putString("restaurantId", restaurant.getRestaurantId());
        restaurantBundle.putString("name", restaurant.getName());
        restaurantBundle.putString("address", restaurant.getAddress());
        restaurantBundle.putString("businessHour", restaurant.getBusinessHour());
        restaurantBundle.putString("description", restaurant.getDescription());
        restaurantBundle.putString("imageURL", restaurant.getImageURL());
        restaurantBundle.putString("longitude", restaurant.getLongitude());
        restaurantBundle.putString("latitude", restaurant.getLatitude());

        holder.acivEdit.setOnClickListener(view -> {
            Intent editIntent = new Intent(holder.itemView.getContext(), EditRestaurant.class);
            editIntent.putExtra("restaurantBundle", restaurantBundle);
            holder.itemView.getContext().startActivity(editIntent);
        });

        holder.ibDetail.setOnClickListener(view -> {
            Intent detailIntent = new Intent(holder.itemView.getContext(), DetailRestaurant.class);
            detailIntent.putExtra("restaurantBundle", restaurantBundle);
            holder.itemView.getContext().startActivity(detailIntent);
        });

        holder.acivDelete.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Hapus Menu?");
            builder.setMessage("Anda yakin ingin hapus " + restaurantList.get(holder.getAdapterPosition()).getName() + "?");
            builder.setPositiveButton("Ya", (dialog, which) -> {
                int itemPosition = holder.getAdapterPosition();
                if (itemPosition != RecyclerView.NO_POSITION) {
                    Thread thread = new Thread(() -> {
                        RestaurantDatabase database = new RestaurantDatabase();
                        RestaurantDatabase.DatabaseInterface di = isSuccess -> {
                            if (isSuccess) {
                                restaurantList.remove(itemPosition);
                                notifyItemRemoved(itemPosition);
                            }
                        };
                        database.deleteRestaurant(restaurantList.get(itemPosition).getRestaurantId(), di);
                    });
                    thread.start();
                }
            });
            builder.setNegativeButton("Batal", (dialog, which) -> dialog.dismiss());
            builder.show();
        });
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivRestoran;
        private TextView tvName;
        private TextView tvAddress;
        private AppCompatImageView acivEdit;
        private ImageButton ibDetail;
        private AppCompatImageView acivDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            tvAddress = itemView.findViewById(R.id.tv_address);
            acivEdit = itemView.findViewById(R.id.aciv_edit);
            acivDelete = itemView.findViewById(R.id.aciv_delete);
            ibDetail = itemView.findViewById(R.id.ib_detail);
            ivRestoran = itemView.findViewById(R.id.ivRestoran);
        }
    }
}
