package com.example.subosh.restauranttrack.customerscontent;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class CustomersViewPagerAdapter extends FragmentStatePagerAdapter{
    public CustomersViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment mainFragment=null;
        switch (position){
            case 0:
                mainFragment=new OwnerListFragment();
                break;
            case 1:
                mainFragment=new CustomersMyListFragment();
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
                return "Purchase Page";
            case 1:
                return "MyGrocery List";

        }
        return null;
    }
}
