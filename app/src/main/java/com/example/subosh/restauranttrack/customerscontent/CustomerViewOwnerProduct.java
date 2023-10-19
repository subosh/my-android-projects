package com.example.subosh.restauranttrack.customerscontent;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.subosh.restauranttrack.R;

public class CustomerViewOwnerProduct extends AppCompatActivity {
CustomerViewOwnerProductfragment customerViewOwnerProductfragment;
String marketname,customername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view_owner_product);
        Intent myintent=getIntent();
        if(myintent!=null){
            marketname=myintent.getStringExtra("MARKETNAME");
        }
        if(myintent!=null){
            customername=myintent.getStringExtra("customername");
        }

        initializecustomerviewOwnerProducstfragment();



    }
    public String getMarketName(){
        return marketname;
    }
    public String getCustomerName(){
        return customername;
    }
    public void initializecustomerviewOwnerProducstfragment(){
        customerViewOwnerProductfragment=new CustomerViewOwnerProductfragment();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.customer_view_owner_products_frame,customerViewOwnerProductfragment);
                fragmentTransaction.commit();
    }
}
