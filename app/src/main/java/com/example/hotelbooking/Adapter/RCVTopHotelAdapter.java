package com.example.hotelbooking.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelbooking.Activity.DetailActivity;
import com.example.hotelbooking.Models.Hotel;
import com.example.hotelbooking.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RCVTopHotelAdapter extends RecyclerView.Adapter<RCVTopHotelAdapter.HotelViewHolder>{

    ArrayList<Hotel> list;

    public RCVTopHotelAdapter(ArrayList<Hotel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv_top_hotel, parent,false);
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {
        Hotel hotel = list.get(position);
        Picasso.get().load(hotel.getImage_hotel()).placeholder(R.drawable.img_intro3).into(holder.img_item_rcv);
        holder.tv_name.setText(hotel.getName_hotel());
        holder.tv_price.setText(hotel.getPrice_hotel());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailActivity.class);
                intent.putExtra("idHotel", hotel.getId_hotel());
                intent.putExtra("imageHotel", hotel.getImage_hotel());
                intent.putExtra("nameHotel", hotel.getName_hotel());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HotelViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_item_rcv;
        private TextView tv_price, tv_name;
        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            img_item_rcv = itemView.findViewById(R.id.img_item_rcv);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_name = itemView.findViewById(R.id.tv_name);

        }
    }
}
