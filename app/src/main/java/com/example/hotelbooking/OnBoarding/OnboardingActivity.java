package com.example.hotelbooking.OnBoarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.hotelbooking.Activity.Progress_Dialog;
import com.example.hotelbooking.Activity.RegisterActivity;
import com.example.hotelbooking.Activity.HomeActivity;
import com.example.hotelbooking.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import me.relex.circleindicator.CircleIndicator;

public class OnboardingActivity extends AppCompatActivity {
    private TextView tv_skip;
    private ViewPager viewPager;
    private AppCompatButton btn_next;
    private CircleIndicator circleIndicator;
    private Progress_Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        anhxa();

        OnboardinViewPagergAdapter viewPagergAdapter = new OnboardinViewPagergAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagergAdapter);
        circleIndicator.setViewPager(viewPager);

        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(2);
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewPager.getCurrentItem() < 2){
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                } else {
                    startActivity(new Intent(OnboardingActivity.this, RegisterActivity.class));
                    finishAffinity();
                }
            }
        });



        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2){
                    tv_skip.setText("Xong");
                    btn_next.setText("Bắt đầu");
                }
                else {
                    tv_skip.setText("Bỏ qua");
                    btn_next.setText("Tiếp theo");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        CheckLogin();


    }
    private void anhxa(){
        tv_skip = findViewById(R.id.tv_skip);
        viewPager = findViewById(R.id.view_pager_intro);
        circleIndicator = findViewById(R.id.circleIndicator);
        btn_next = findViewById(R.id.btn_next);
        tv_skip.setText("Bỏ qua");
        btn_next.setText("Tiếp theo");
        dialog = new Progress_Dialog(this);
    }
    private void CheckLogin(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Người dùng đã đăng nhập
            Intent intent = new Intent(OnboardingActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}