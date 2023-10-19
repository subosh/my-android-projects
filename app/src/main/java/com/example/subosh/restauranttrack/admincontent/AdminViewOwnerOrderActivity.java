package com.example.subosh.restauranttrack.admincontent;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.subosh.restauranttrack.R;

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
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.admin_frame,adminViewOwnerOrderFragment);
        fragmentTransaction.commit();

    }
    public String getAdminname(){
        return adminname;
    }
}
