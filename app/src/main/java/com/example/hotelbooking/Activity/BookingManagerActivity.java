package com.example.hotelbooking.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.hotelbooking.Adapter.RCVBookingManagerAdapter;
import com.example.hotelbooking.Models.Booking;
import com.example.hotelbooking.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BookingManagerActivity extends AppCompatActivity {

    private RCVBookingManagerAdapter rcvBookingManagerAdapter;
    private RecyclerView rcv_booking_managerl;
    private ArrayList<Booking> list;
    private ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_manager);
        list = new ArrayList<>();
        anhxa();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String idUser = user.getUid();

        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_booking_managerl.setLayoutManager(linearLayoutManager);
        rcvBookingManagerAdapter = new RCVBookingManagerAdapter(list);
        rcv_booking_managerl.setAdapter(rcvBookingManagerAdapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Booking");
        reference.orderByChild("id_user").equalTo(idUser).addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Booking booking = snapshot.getValue(Booking.class);
                if (booking != null){
                    list.add(booking);
                    rcvBookingManagerAdapter.notifyDataSetChanged();
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Booking booking = snapshot.getValue(Booking.class);
                if (list == null || list.isEmpty()){
                    return;
                }

                for (int i = 0; i < list.size(); i++){
                    if (booking.getId_booking() == list.get(i).getId_booking()){
                        list.set(i, booking);
                        break;
                    }
                }
                rcvBookingManagerAdapter.notifyDataSetChanged();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Booking booking = snapshot.getValue(Booking.class);
                if (booking == null || list == null || list.isEmpty()){
                    return;
                }
                for (int i = 0; i < list.size(); i++){
                    if (booking.getId_booking() == list.get(i).getId_booking()){
                        list.remove(list.get(i));
                        break;
                    }
                }
                rcvBookingManagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BookingManagerActivity.this, HomeActivity.class));
                finishAffinity();
            }
        });





    }
    private void anhxa(){
        rcv_booking_managerl = findViewById(R.id.rcv_booking_managerl);
        img_back = findViewById(R.id.img_back);
    }
}