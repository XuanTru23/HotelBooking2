package com.example.hotelbooking.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelbooking.Activity.DetailHotelActivity;
import com.example.hotelbooking.Models.Hotel;
import com.example.hotelbooking.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RCVHotelManagerAdapter extends RecyclerView.Adapter<RCVHotelManagerAdapter.HouseViewHolder>{

    ArrayList<Hotel> list;

    public RCVHotelManagerAdapter(ArrayList<Hotel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public HouseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv_hotel, parent,false);
        return new HouseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HouseViewHolder holder, int position) {
        Hotel hotel = list.get(position);
        Picasso.get().load(hotel.getImage_hotel()).placeholder(R.drawable.img_intro3).into(holder.img_item_rcv_house);
        holder.tv_name.setText(hotel.getName_hotel());
        holder.tv_price.setText(hotel.getPrice_hotel());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailHotelActivity.class);
                intent.putExtra("idHotel", hotel.getId_hotel());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HouseViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_item_rcv_house;
        private TextView tv_price, tv_name;
        public HouseViewHolder(@NonNull View itemView) {
            super(itemView);
            img_item_rcv_house = itemView.findViewById(R.id.img_item_rcv_house);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_name = itemView.findViewById(R.id.tv_name);

        }
    }
}
