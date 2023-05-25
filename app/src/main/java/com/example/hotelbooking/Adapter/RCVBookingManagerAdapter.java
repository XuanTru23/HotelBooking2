package com.example.hotelbooking.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelbooking.Activity.DetailBookingHotelActivity;
import com.example.hotelbooking.Models.Booking;
import com.example.hotelbooking.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RCVBookingManagerAdapter extends RecyclerView.Adapter<RCVBookingManagerAdapter.HotelViewHolder>{

    private List<Booking> list;

    public RCVBookingManagerAdapter(List<Booking> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv_booking_manager, parent,false);
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {
        Booking booking = list.get(position);

        holder.tv_name.setText(booking.getName_hotel());
        Picasso.get().load(booking.getImage_hotel()).placeholder(R.drawable.img_intro3).into(holder.img);
        holder.tv_phone.setText(booking.getPhone());
        holder.tv_name_khach.setText(booking.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailBookingHotelActivity.class);
                intent.putExtra("idbooking", booking.getId_booking());
                view.getContext().startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HotelViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView tv_name, tv_price, tv_name_khach, tv_phone;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_name_khach = itemView.findViewById(R.id.tv_name_khach);
            tv_phone = itemView.findViewById(R.id.tv_phone);

        }
    }
}
