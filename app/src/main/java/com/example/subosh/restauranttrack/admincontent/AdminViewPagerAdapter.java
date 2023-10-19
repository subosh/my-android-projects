package com.example.subosh.restauranttrack.admincontent;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class AdminViewPagerAdapter extends FragmentStatePagerAdapter {
    private  String adminname;
    public AdminViewPagerAdapter(FragmentManager supportFragmentManager, String adminname) {
        super(supportFragmentManager);
        this.adminname=adminname;

    }

    @Override
    public Fragment getItem(int position) {
        Fragment mainFragment=null;
        switch (position){
            case 0:
                mainFragment=new OrderMaintanenceFragment();
                break;
            case 1:
               mainFragment=new AdminRequestFragment();
                break;

        }
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
                return adminname +" "+"Home Page";
            case 1:
                return "Admin Requests";

        }
        return null;
    }
}
