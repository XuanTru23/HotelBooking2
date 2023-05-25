package com.example.hotelbooking.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.hotelbooking.R;

public class BookingConfirmActivity extends AppCompatActivity {
    private AppCompatButton btn_ok;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirm);
        anhxa();
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BookingConfirmActivity.this, HomeActivity.class));
                finishAffinity();
            }
        });
    }
    private void anhxa(){
        btn_ok = findViewById(R.id.btn_ok);
    }
}