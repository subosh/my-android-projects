package com.example.subosh.restauranttrack.startpagecontent;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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
