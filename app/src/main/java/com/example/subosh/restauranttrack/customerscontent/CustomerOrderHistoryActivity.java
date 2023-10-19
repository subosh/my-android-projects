package com.example.subosh.restauranttrack.customerscontent;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.subosh.restauranttrack.R;

public class CustomerOrderHistoryActivity extends AppCompatActivity {
CustomerOrderHistoryFragment customerOrderHistoryFragment;
String customerName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order_history);
        Intent myintent=getIntent();
        if (myintent!=null){
            customerName=myintent.getStringExtra("customername");
        }
        initializeCustomerOrderHistoryFragment();
    }
    public void initializeCustomerOrderHistoryFragment(){
        customerOrderHistoryFragment=new CustomerOrderHistoryFragment();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.customer_order_history_frame,customerOrderHistoryFragment);
        fragmentTransaction.commit();
    }

    public String getCustomerName() {
        return customerName;
    }
}
