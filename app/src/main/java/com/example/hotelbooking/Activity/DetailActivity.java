package com.example.hotelbooking.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hotelbooking.Models.Hotel;
import com.example.hotelbooking.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DetailActivity extends AppCompatActivity {
    private ImageView img, img_back;
    private TextView tv_name, tv_diachi, tv_description, tv_email, tv_phone, tv_price, tv_priceKM;
    private AppCompatButton btn_book_now;
    private AppCompatImageView img_save;
    private String Url_anh, idHotel, id_nguoi_dang, imageHotel, nameHotel;
    private ConstraintLayout cl6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        idHotel = getIntent().getStringExtra("idHotel");
        imageHotel = getIntent().getStringExtra("imageHotel");
        nameHotel = getIntent().getStringExtra("nameHotel");
        anhxa();
        get_hotel();
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btn_book_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hotel hotel = new Hotel();
                Intent intent = new Intent(view.getContext(), BookingActivity.class);
                intent.putExtra("idHotel", idHotel);
                intent.putExtra("idNguoiDang", id_nguoi_dang);
                intent.putExtra("imageHotel", imageHotel);
                intent.putExtra("nameHotel", nameHotel);
                startActivity(intent);
            }
        });
        img_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String idUser = user.getUid();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("HouseBooking");

                boolean isLiked = img_save.isSelected();
                reference.child(idHotel).child("isSaved").child(idUser).setValue(isLiked);

            }
        });

    }
    private void anhxa(){
        img_back = findViewById(R.id.img_back);
        img = findViewById(R.id.img);
        tv_name = findViewById(R.id.tv_name);
        tv_diachi = findViewById(R.id.tv_diachi);
        tv_description = findViewById(R.id.description);
        tv_email = findViewById(R.id.tv_email);
        tv_phone = findViewById(R.id.tv_phone);
        tv_price = findViewById(R.id.tv_price);
        btn_book_now = findViewById(R.id.btn_book_now);
        tv_priceKM = findViewById(R.id.tv_priceKM);
        img_save = findViewById(R.id.img_save);
        cl6 = findViewById(R.id.cl6);
    }
    private void get_hotel(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("HouseBooking");
        Query query = databaseReference.orderByChild("id_hotel").equalTo(idHotel);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    DataSnapshot dataSnapshot = snapshot.getChildren().iterator().next();
                    Hotel hotel = dataSnapshot.getValue(Hotel.class);
                    String giaKM = hotel.getPriceKM_hotel();

                    if (giaKM == null){
                        cl6.setVisibility(View.GONE);
                    } else {
                        tv_priceKM.setText(hotel.getPriceKM_hotel());
                    }
                    id_nguoi_dang = hotel.getId_user();
                    tv_priceKM.setText(hotel.getPriceKM_hotel());
                    tv_name.setText(hotel.getName_hotel());
                    tv_diachi.setText(hotel.getAddress_hotel());
                    tv_description.setText(hotel.getDescription_hotel());
                    tv_email.setText(hotel.getGmail_hotel());
                    tv_phone.setText(hotel.getPhone_hotel());
                    tv_price.setText(hotel.getPrice_hotel());
                    Url_anh = hotel.getImage_hotel();
                    Glide.with(DetailActivity.this).load(Url_anh).into(img);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}