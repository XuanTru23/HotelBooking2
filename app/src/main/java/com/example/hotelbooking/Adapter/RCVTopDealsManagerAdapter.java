package com.example.hotelbooking.Adapter;

import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
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

public class RCVTopDealsManagerAdapter extends RecyclerView.Adapter<RCVTopDealsManagerAdapter.HotelViewHolder>{

    ArrayList<Hotel> list;

    public RCVTopDealsManagerAdapter(ArrayList<Hotel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv_top_deals, parent,false);
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {
        Hotel hotel = list.get(position);
        Picasso.get().load(hotel.getImage_hotel()).placeholder(R.drawable.img_intro3).into(holder.img_item_rcv);
        holder.tv_name.setText(hotel.getName_hotel());
//        holder.tv_price.setText(hotel.getPrice_hotel());
        holder.tv_priceKM.setText(hotel.getPriceKM_hotel());

        String text = hotel.getPrice_hotel();
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new StrikethroughSpan(), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tv_price.setText(spannableString);

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
        private TextView tv_price, tv_name, tv_priceKM;
        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            img_item_rcv = itemView.findViewById(R.id.img_item_rcv);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_priceKM = itemView.findViewById(R.id.tv_priceKM);
            tv_price.setTextDirection(View.TEXT_DIRECTION_RTL);


        }
    }
}
