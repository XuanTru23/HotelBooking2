package com.example.hotelbooking.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelbooking.Activity.Progress_Dialog;
import com.example.hotelbooking.Models.Hotel;
import com.example.hotelbooking.R;
import com.example.hotelbooking.Adapter.RCVTopDealsManagerAdapter;
import com.example.hotelbooking.Adapter.RCVTopHotelAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExploreFragment extends Fragment {
    private View view;
    private RCVTopHotelAdapter rcvTopHotelAdapter;
    private RCVTopDealsManagerAdapter rcvTopDealsManagerAdapter;
    private RecyclerView rcv_top_hotel, rcv_top_deals;
    private ArrayList<Hotel> list, list_top_deal;
    private TextView tv_top_hotels, tv_top_deals;
    private Progress_Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_explore, container, false);
        anhxa();
        list = new ArrayList<>();
        list_top_deal = new ArrayList<>();
        set_up_rcv_top_hotel();
        set_up_rcv_top_deals();
        call_back_house();
        call_back_Top_deals();

        tv_top_hotels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                                .replace(R.id.frame_layout, new TopHotelsFragment()).commit();
//                startActivity(new Intent(view.getContext().getApplicationContext(), TopHotelsFragment.class));
            }
        });

        tv_top_deals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, new TopDealsFragment()).commit();
//                startActivity(new Intent(view.getContext().getApplicationContext(), TopHotelsFragment.class));
            }
        });

        return view;
    }
    private void anhxa(){
        rcv_top_hotel = view.findViewById(R.id.rcv_top_hotel);
        rcv_top_deals = view.findViewById(R.id.rcv_top_deals);
        tv_top_hotels = view.findViewById(R.id.tv_top_hotels);
        tv_top_deals = view.findViewById(R.id.tv_top_deals);
        dialog = new Progress_Dialog(view.getContext());
    }
    private void set_up_rcv_top_hotel(){
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(view.getContext().getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcv_top_hotel.setLayoutManager(linearLayoutManager);
        rcvTopHotelAdapter = new RCVTopHotelAdapter(list);
        rcv_top_hotel.setAdapter(rcvTopHotelAdapter);
    }
    private void set_up_rcv_top_deals(){
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(view.getContext().getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcv_top_deals.setLayoutManager(linearLayoutManager);
        rcvTopDealsManagerAdapter = new RCVTopDealsManagerAdapter(list_top_deal);
        rcv_top_deals.setAdapter(rcvTopDealsManagerAdapter);
    }
    private void call_back_house(){
        DatabaseReference houseKingRef = FirebaseDatabase.getInstance().getReference().child("HouseBooking");
        Query query = houseKingRef.orderByChild("priceKM_hotel").equalTo(null);

        query.addChildEventListener(new ChildEventListener() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Hotel hotel = snapshot.getValue(Hotel.class);
                if (hotel != null){
                    list.add(hotel);
                    rcvTopHotelAdapter.notifyDataSetChanged();
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
                rcvTopHotelAdapter.notifyDataSetChanged();
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
                rcvTopHotelAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void call_back_Top_deals(){
        DatabaseReference houseKingRef = FirebaseDatabase.getInstance().getReference().child("HouseBooking");
        Query query = houseKingRef.orderByChild("priceKM_hotel").startAt(0);

        query.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Hotel hotel = snapshot.getValue(Hotel.class);
                if (hotel != null){
                    list_top_deal.add(hotel);
                    rcvTopDealsManagerAdapter.notifyDataSetChanged();
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Hotel hotel = snapshot.getValue(Hotel.class);
                if (list_top_deal == null || list_top_deal.isEmpty()){
                    return;
                }

                for (int i = 0; i < list_top_deal.size(); i++){
                    if (hotel.getId_hotel() == list_top_deal.get(i).getId_hotel()){
                        list_top_deal.set(i, hotel);
                        break;
                    }
                }
                rcvTopDealsManagerAdapter.notifyDataSetChanged();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                Hotel hotel = snapshot.getValue(Hotel.class);
                if (hotel == null || list_top_deal == null || list_top_deal.isEmpty()){
                    return;
                }
                for (int i = 0; i < list_top_deal.size(); i++){
                    if (hotel.getId_hotel() == list_top_deal.get(i).getId_hotel()){
                        list_top_deal.remove(list_top_deal.get(i));
                        break;
                    }
                }
                rcvTopDealsManagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
