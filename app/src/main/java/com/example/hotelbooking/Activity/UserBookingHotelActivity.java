package com.example.hotelbooking.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.hotelbooking.Adapter.RCVBookingManagerAdapter;
import com.example.hotelbooking.Adapter.RCVUserBookingHotelAdapter;
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

public class UserBookingHotelActivity extends AppCompatActivity {
    private RCVUserBookingHotelAdapter rcvUserBookingHotelAdapter;
    private RecyclerView rcv_user_booking_managerl;
    private ArrayList<Booking> list;
    private ImageView img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_booking_hotel);
        anhxa();
        list = new ArrayList<>();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String idUser = user.getUid();

        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_user_booking_managerl.setLayoutManager(linearLayoutManager);
        rcvUserBookingHotelAdapter = new RCVUserBookingHotelAdapter(list);
        rcv_user_booking_managerl.setAdapter(rcvUserBookingHotelAdapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Booking");
        reference.orderByChild("id_nguoi_dang").equalTo(idUser).addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Booking booking = snapshot.getValue(Booking.class);
                if (booking != null){
                    list.add(booking);
                    rcvUserBookingHotelAdapter.notifyDataSetChanged();
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
                rcvUserBookingHotelAdapter.notifyDataSetChanged();
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
                rcvUserBookingHotelAdapter.notifyDataSetChanged();
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
                onBackPressed();
                finish();
            }
        });

    }
    private void anhxa(){
        rcv_user_booking_managerl = findViewById(R.id.rcv_user_booking_managerl);
        img_back = findViewById(R.id.img_back);
    }
}