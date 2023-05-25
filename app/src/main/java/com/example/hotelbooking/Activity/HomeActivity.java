package com.example.hotelbooking.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.hotelbooking.Fragment.ExploreFragment;
import com.example.hotelbooking.Fragment.ProfileFragment;
import com.example.hotelbooking.Fragment.WishlistFragment;
import com.example.hotelbooking.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        anhxa();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, new ExploreFragment());
        transaction.commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()){
                    case R.id.item_explore:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame_layout, new ExploreFragment())
                                .commit();
                        return true;
                    case R.id.item_wishlist:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame_layout, new WishlistFragment())
                                .commit();
                        return true;
                    case R.id.item_profile:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame_layout, new ProfileFragment())
                                .commit();
                        return true;
                }
                return false;
            }
        });




    }
    private void anhxa(){
        bottomNavigationView = findViewById(R.id.bottom_nav_home);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Ấn thoát lần nữa để thoát", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}