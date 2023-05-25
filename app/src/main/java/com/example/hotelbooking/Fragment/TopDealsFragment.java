package com.example.hotelbooking.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelbooking.Adapter.RCVTopDeals2ManagerAdapter;
import com.example.hotelbooking.Adapter.RCVTopDealsManagerAdapter;
import com.example.hotelbooking.Adapter.RCVTopHotel2ManagerAdapter;
import com.example.hotelbooking.Adapter.RCVTopHotelAdapter;
import com.example.hotelbooking.Models.Hotel;
import com.example.hotelbooking.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TopDealsFragment extends Fragment {
    private View view;
    private ImageView img_back;
    private RCVTopDeals2ManagerAdapter rcvTopDeals2ManagerAdapter;
    private RecyclerView rcv_top_hotel;
    private ArrayList<Hotel> list;
    private EditText edt_Search;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_top_deals, container, false);
        anhxa();
        list = new ArrayList<>();
        set_up_rcv_top_hotel_2();
        call_back_house();
        tim_kiem();

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, new ExploreFragment()).commit();
            }
        });

        return view;
    }
    private void anhxa(){
        img_back = view.findViewById(R.id.img_back);
        rcv_top_hotel = view.findViewById(R.id.rcv_top_hotel);
        edt_Search = view.findViewById(R.id.edt_Search);


    }

    private void set_up_rcv_top_hotel_2(){
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(view.getContext().getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_top_hotel.setLayoutManager(linearLayoutManager);
        rcvTopDeals2ManagerAdapter = new RCVTopDeals2ManagerAdapter(list);
        rcv_top_hotel.setAdapter(rcvTopDeals2ManagerAdapter);
    }

    private void call_back_house(){
        DatabaseReference houseKingRef = FirebaseDatabase.getInstance().getReference().child("HouseBooking");
        Query query = houseKingRef.orderByChild("priceKM_hotel").startAt(0);
        query.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Hotel hotel = snapshot.getValue(Hotel.class);
                if (hotel != null){
                    list.add(hotel);
                    rcvTopDeals2ManagerAdapter.notifyDataSetChanged();
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
                rcvTopDeals2ManagerAdapter.notifyDataSetChanged();
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
                rcvTopDeals2ManagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void tim_kiem(){
        edt_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tu_khoa = edt_Search.getText().toString();
                if (tu_khoa == null || tu_khoa.isEmpty()){
                    set_up_rcv_top_hotel_2();
                } else {
                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("HouseBooking");
                    databaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<Hotel> list1 = new ArrayList<>();
                            for (DataSnapshot ds : snapshot.getChildren()){
                                Hotel hotel = ds.getValue(Hotel.class);
                                if (hotel.getAddress_hotel().equals(tu_khoa)){
                                    list1.add(hotel);
                                }
                            }
                            LinearLayoutManager linearLayoutManager= new LinearLayoutManager(view.getContext().getApplicationContext());
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            rcv_top_hotel.setLayoutManager(linearLayoutManager);
                            rcvTopDeals2ManagerAdapter = new RCVTopDeals2ManagerAdapter(list1);
                            rcv_top_hotel.setAdapter(rcvTopDeals2ManagerAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }


}
