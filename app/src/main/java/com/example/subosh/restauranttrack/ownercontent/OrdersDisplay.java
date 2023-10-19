package com.example.subosh.restauranttrack.ownercontent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.subosh.restauranttrack.R;

public class OrdersDisplay extends AppCompatActivity {
OrderSummaryListFragment orderSummaryListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_display);
        initilizeOrdersDisplay();
    }
    public void initilizeOrdersDisplay(){
        orderSummaryListFragment=new OrderSummaryListFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.owner_display_frame,orderSummaryListFragment)
                .commit();
    }
}
