package com.example.subosh.restauranttrack.admincontent;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.subosh.restauranttrack.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminOwnerOrderRequestActivity extends AppCompatActivity {
AdminOwnerOrderRequestFragment adminOwnerOrderRequestFragment;
String adminname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_owner_order_request);
        Intent myintent=getIntent();
        if(myintent!=null){
            adminname=myintent.getStringExtra("adminname");
        }
        initializeAdminOwnerOrderRequestFragment();
    }
    public void initializeAdminOwnerOrderRequestFragment(){
        adminOwnerOrderRequestFragment=new AdminOwnerOrderRequestFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.admin_owner_order_request_fragment_frame,adminOwnerOrderRequestFragment)
                .commit();
    }

    public String getAdminname() {
        return adminname;
    }
}
