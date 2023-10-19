package com.example.subosh.restauranttrack.customerscontent;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.subosh.restauranttrack.R;

public class CustomerCreateListActivity extends AppCompatActivity {
CustomerCreateListFragment customerCreateListFragment;
String customername,marketname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_create_list_activity);
        Intent myintent=getIntent();
        if(myintent!=null){
            marketname=myintent.getStringExtra("MARKETNAME");
        }
        if(myintent!=null){
            customername=myintent.getStringExtra("customername");
        }
        initializeCustomerCreateListFragment();
    }
    public String getMarketNameCreateList(){
        return marketname;
    }
    public String getCustomerNameCreateList(){
        return customername;
    }
    public void initializeCustomerCreateListFragment(){
        customerCreateListFragment=new CustomerCreateListFragment();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.customer_create_list_frame,customerCreateListFragment);
        fragmentTransaction.commit();
    }
}
