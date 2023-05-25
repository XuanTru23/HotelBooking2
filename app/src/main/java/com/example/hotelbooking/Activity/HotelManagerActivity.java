package com.example.hotelbooking.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.hotelbooking.Models.Hotel;
import com.example.hotelbooking.R;
import com.example.hotelbooking.Adapter.RCVHotelManagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HotelManagerActivity extends AppCompatActivity {

    private RecyclerView recyclerViewHouseManager;
    private RCVHotelManagerAdapter RCVHotelManagerAdapter;
    private ArrayList<Hotel> list;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private ImageView img_back;
    private FloatingActionButton FABtn_addHouse;

    private ConstraintLayout manage_booking;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotels_manager);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        anhxa();
        click_event();
        list = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(HotelManagerActivity.this, 2);
        recyclerViewHouseManager.setLayoutManager(gridLayoutManager);
        RCVHotelManagerAdapter = new RCVHotelManagerAdapter(list);
        recyclerViewHouseManager.setAdapter(RCVHotelManagerAdapter);
        String idUser = user.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("HouseBooking");
        reference.orderByChild("id_user").equalTo(idUser).addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Hotel hotel = snapshot.getValue(Hotel.class);
                if (hotel != null){
                    list.add(hotel);
                    RCVHotelManagerAdapter.notifyDataSetChanged();
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Hotel hotel = snapshot.getValue(Hotel.class);
                if (list == null || list.isEmpty()){
                    return;
                }

                for (int i = 0; i < list.size(); i++){
                    if (hotel.getId_hotel() == list.get(i).getId_hotel()){
                        list.set(i, hotel);
                        break;
                    }
                }
                RCVHotelManagerAdapter.notifyDataSetChanged();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Hotel hotel = snapshot.getValue(Hotel.class);
                if (hotel == null || list == null || list.isEmpty()){
                    return;
                }
                for (int i = 0; i < list.size(); i++){
                    if (hotel.getId_hotel() == list.get(i).getId_hotel()){
                        list.remove(list.get(i));
                        break;
                    }
                }
                RCVHotelManagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void anhxa(){
        recyclerViewHouseManager = findViewById(R.id.recyclerViewHouseManager);
        img_back = findViewById(R.id.img_back);
        FABtn_addHouse = findViewById(R.id.FABtn_addHouse);
        manage_booking = findViewById(R.id.manage_booking);
    }
    private void click_event(){
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HotelManagerActivity.this, HomeActivity.class));
                finishAffinity();
            }
        });
        FABtn_addHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HotelManagerActivity.this, AddHotelActivity.class));
                finish();

            }
        });

        manage_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HotelManagerActivity.this, UserBookingHotelActivity.class));
            }
        });

    }
}