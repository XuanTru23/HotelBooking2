package com.example.hotelbooking.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelbooking.Activity.DetailActivity;
import com.example.hotelbooking.Models.Hotel;
import com.example.hotelbooking.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RCVTopDeals2ManagerAdapter extends RecyclerView.Adapter<RCVTopDeals2ManagerAdapter.HotelViewHolder>{

    ArrayList<Hotel> list;
    boolean isSaved;
    public RCVTopDeals2ManagerAdapter(ArrayList<Hotel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv_top_deals_2, parent,false);
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {
        Hotel hotel = list.get(position);
        Picasso.get().load(hotel.getImage_hotel()).placeholder(R.drawable.img_intro3).into(holder.img_rcv);
        holder.tv_name.setText(hotel.getName_hotel());
        holder.tv_gia.setText(hotel.getPrice_hotel());

        holder.img_btn_save.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String idUser = user.getUid();
                String idHotel = hotel.getId_hotel();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("HouseBooking");

                boolean isSaved = hotel.isSaved();
                // Thay đổi trạng thái "lưu" của house
                hotel.setSaved(!isSaved);
                // Cập nhật dữ liệu trong Firebase Realtime Database
                if (hotel.isSaved()) {
                    reference.child(idHotel).child("isSaved").child(idUser).setValue(true);
                    Toast.makeText(view.getContext(), "Bạn vừa lưu.", Toast.LENGTH_SHORT).show();
                } else {
                    reference.child(idHotel).child("isSaved").child(idUser).setValue(false);
                    Toast.makeText(view.getContext(), "Bạn vừa hủy lưu.", Toast.LENGTH_SHORT).show();
                }
                // Cập nhật giao diện RecyclerView
                notifyItemChanged(position);
                notifyDataSetChanged();
                // Cập nhật trạng thái màu sắc của nút like

            }
        });


        holder.btn_book_now.setOnClickListener(new View.OnClickListener() {
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
        private ImageView img_rcv;
        private TextView tv_gia, tv_name;
        private AppCompatImageView img_btn_save;
        private AppCompatButton btn_book_now;
        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            img_rcv = itemView.findViewById(R.id.img_rcv);
            tv_gia = itemView.findViewById(R.id.tv_gia);
            tv_name = itemView.findViewById(R.id.tv_name);
            img_btn_save = itemView.findViewById(R.id.img_btn_save);
            btn_book_now = itemView.findViewById(R.id.btn_book_now);

        }
    }
}
