package com.example.subosh.restauranttrack.customerscontent;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class CustomersViewPagerAdapter extends FragmentStatePagerAdapter {
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
