package com.example.subosh.restauranttrack.admincontent;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.subosh.restauranttrack.R;
import com.example.subosh.restauranttrack.ownercontent.OwnerProductsFragment;

public class AdminViewOwnerOrderActivity extends AppCompatActivity {
AdminViewOwnerOrderFragment adminViewOwnerOrderFragment;
String adminname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_owner_order);
        Intent myintent=getIntent();
        if(myintent!=null){
            adminname=myintent.getStringExtra("adminname");
        }
        initializeAdminViewOwnerOrderFragment();
    }
    public  void initializeAdminViewOwnerOrderFragment(){
        adminViewOwnerOrderFragment=new AdminViewOwnerOrderFragment();
        FragmentManager  fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.admin_frame,adminViewOwnerOrderFragment);
        fragmentTransaction.commit();

    }
    public String getAdminname(){
        return adminname;
    }
}
