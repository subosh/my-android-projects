package com.example.subosh.restauranttrack.ownercontent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.subosh.restauranttrack.R;
import com.example.subosh.restauranttrack.startpagecontent.MainFragment;

public class OwnerMainActivity extends AppCompatActivity {
MainFragment mainFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_main);
        initializeMainFragment();
    }
    public void initializeMainFragment()
    {
        mainFragment=new MainFragment();
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.owner_mainactivity_frame,mainFragment);
        fragmentTransaction.commit();
    }
}
