package com.example.hotelbooking.OnBoarding;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.hotelbooking.OnBoarding.OnboardingFragment1;
import com.example.hotelbooking.OnBoarding.OnboardingFragment2;
import com.example.hotelbooking.OnBoarding.OnboardingFragment3;

public class OnboardinViewPagergAdapter extends FragmentStatePagerAdapter {
    public OnboardinViewPagergAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new OnboardingFragment1();
            case 1:
                return new OnboardingFragment2();
            case 2:
                return new OnboardingFragment3();
            default:
                return new OnboardingFragment1();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
