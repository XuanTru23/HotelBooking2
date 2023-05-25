package com.example.hotelbooking.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.hotelbooking.Activity.LoginActivity;
import com.example.hotelbooking.Activity.MyAccountActivity;
import com.example.hotelbooking.Activity.Progress_Dialog;
import com.example.hotelbooking.Activity.HotelManagerActivity;
import com.example.hotelbooking.Activity.BookingManagerActivity;
import com.example.hotelbooking.R;
import com.example.hotelbooking.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    private View view;
    private ConstraintLayout log_out, my_account,pay, past_book,Help_Support, Privacy_Policy;
    private ImageView img_profile;
    private TextView tv_name_profile;
    private Progress_Dialog dialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        anhxa();
        click_event();
        set_profile();
        return view;
    }
    private void anhxa(){
        log_out = view.findViewById(R.id.log_out);
        my_account = view.findViewById(R.id.my_account);
        pay = view.findViewById(R.id.pay);
        past_book = view.findViewById(R.id.past_book);
        Help_Support = view.findViewById(R.id.Help_Support);
        Privacy_Policy = view.findViewById(R.id.Privacy_Policy);
        img_profile = view.findViewById(R.id.img_profile);
        tv_name_profile = view.findViewById(R.id.tv_name_profile);
        dialog = new Progress_Dialog(view.getContext());
    }
    private void click_event(){
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finishAffinity();
            }
        });
        my_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MyAccountActivity.class));
            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), HotelManagerActivity.class));
            }
        });
        past_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), BookingManagerActivity.class));
            }
        });
    }
    private void set_profile(){
        dialog.ShowDilag("Đang lấy dữ liệu.");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User users = snapshot.getValue(User.class);
                String imageUrl = snapshot.child("imageUrl").getValue(String.class);
                // Sử dụng thư viện Glide để tải và hiển thị ảnh từ URL
                Glide.with(view.getContext().getApplicationContext())
                        .load(imageUrl)
                        .circleCrop()
                        .into(img_profile);

                if (users != null){
                    dialog.HideDialog();
                    tv_name_profile.setText(users.getName());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
