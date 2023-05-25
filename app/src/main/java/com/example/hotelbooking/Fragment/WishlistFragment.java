package com.example.hotelbooking.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelbooking.Adapter.RCVTopHotel2ManagerAdapter;
import com.example.hotelbooking.Adapter.RCVWishlistManagerAdapter;
import com.example.hotelbooking.Models.Hotel;
import com.example.hotelbooking.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WishlistFragment extends Fragment {
    private View view;
    private RCVWishlistManagerAdapter rcvWishlistManagerAdapter;
    private RecyclerView rcv_top_hotel;
    private ArrayList<Hotel> list;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        anhxa();
        list = new ArrayList<>();

        set_up_rcv_Wishlist();
        call_back_house();
        return view;
    }
    private void anhxa(){
        rcv_top_hotel = view.findViewById(R.id.rcv_top_hotel);
    }
    private void set_up_rcv_Wishlist(){
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(view.getContext().getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_top_hotel.setLayoutManager(linearLayoutManager);
        rcvWishlistManagerAdapter = new RCVWishlistManagerAdapter(list);
        rcv_top_hotel.setAdapter(rcvWishlistManagerAdapter);
    }
    private void call_back_house(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String idUser = user.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("HouseBooking");
        Query savedHousesQuery = reference.orderByChild("isSaved/" + idUser).equalTo(true);
        savedHousesQuery.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Hotel hotel = snapshot.getValue(Hotel.class);
                if (hotel != null){
                    list.add(hotel);
                    rcvWishlistManagerAdapter.notifyDataSetChanged();
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
                rcvWishlistManagerAdapter.notifyDataSetChanged();
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
                rcvWishlistManagerAdapter.notifyDataSetChanged();
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
