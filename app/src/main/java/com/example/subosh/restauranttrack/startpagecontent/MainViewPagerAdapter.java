package com.example.subosh.restauranttrack.startpagecontent;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.subosh.restauranttrack.admincontent.AdminSignInFragment;
import com.example.subosh.restauranttrack.customerscontent.UsersFragment;

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment mainFragment=null;

        switch (position){
            case 0:
                mainFragment=new UsersFragment();
                break;
            case 1:
                mainFragment=new MainFragment();
                break;

        }
//        if(position==0){
//            mainFragment=new UsersFragment();
//
//        }
//        if(position==1)
//        {
//            mainFragment=new MainFragment();
//
//        }
        return mainFragment;

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "CustomerLogin";
            case 1:
                return "ShopOwnersLogin";

        }
        return null;
    }
}
