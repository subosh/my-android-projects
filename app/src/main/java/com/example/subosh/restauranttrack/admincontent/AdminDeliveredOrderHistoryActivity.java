package com.example.subosh.restauranttrack.admincontent;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.subosh.restauranttrack.R;

public class AdminDeliveredOrderHistoryActivity extends AppCompatActivity {
AdminDeliveredOrderHistoryShopListFragment adminDeliveredOrderHistoryShopListFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_delivered_order_history);
        initializeAdminDeliveredOrderFragment();
    }
    public void initializeAdminDeliveredOrderFragment(){
        adminDeliveredOrderHistoryShopListFragment=new AdminDeliveredOrderHistoryShopListFragment();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.admin_delivered_order_frame,adminDeliveredOrderHistoryShopListFragment);
        fragmentTransaction.commit();

    }
}
