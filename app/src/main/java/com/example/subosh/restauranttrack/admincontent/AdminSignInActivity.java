package com.example.subosh.restauranttrack.admincontent;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.subosh.restauranttrack.R;

public class AdminSignInActivity extends AppCompatActivity {
AdminSignInFragment adminSignInFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signin_activity);
        initializeAdminSignInFragment();
    }
    public void initializeAdminSignInFragment(){
        adminSignInFragment=new AdminSignInFragment();
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.admin_signin_frame,adminSignInFragment);
        fragmentTransaction.commit();

    }
}
