package com.example.subosh.restauranttrack.ownercontent;

import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.subosh.restauranttrack.R;
import com.example.subosh.restauranttrack.customerscontent.CustomerOrdersPojo;

import java.util.ArrayList;

public class OrderSummaryListFragment extends DialogFragment {
   // Toolbar toolbar;
    ArrayList<CustomerOrdersPojo> customerOrdersPojoArrayList;
    RecyclerView recyclerView;
    OrdersDialogAdapter ordersDialogAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.orders_summary_list_fragment,container);
        //toolbar=view.findViewById(R.id.toolbar);
        recyclerView=view.findViewById(R.id.order_display_recycle);
       // TextView textView=view.findViewById(R.id.order_summary_detail);
//        OrdersDisplay getownerproductsactivity=(OrdersDisplay) getActivity();
//        getownerproductsactivity.setSupportActionBar(toolbar);
//        getownerproductsactivity.setTitle("Orders Summary");
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
       // ordersDialogAdapter=new OrdersDialogAdapter(getContext(),getCustomerOrderDetails());
        recyclerView.setAdapter(ordersDialogAdapter);
//        DisplayMetrics displayMetrics=getResources().getDisplayMetrics();
//        int width=displayMetrics.widthPixels;
//        int height=displayMetrics.heightPixels;
//        Dialog dialog=getDialog();
//
//        dialog.getWindow().setLayout((6*width)/7,(4*height)/5);
       // this.getDialog().setTitle("Order Details");
        ordersDialogAdapter.notifyDataSetChanged();
//        getCustomerOrderDetails();
//        for (int i=0;i<getCustomerOrderDetails().size();i++){
//            Toast.makeText(getContext(),"Hello"+" "+getCustomerOrderDetails().get(i),Toast.LENGTH_SHORT).show();
//        }
       // Toast.makeText(getContext(), (CharSequence) getCustomerOrderDetails(),Toast.LENGTH_SHORT).show();

        return  view;
    }
    public ArrayList<CustomerOrdersPojo> getCustomerOrderDetails(){
        customerOrdersPojoArrayList=OrdersSummaryAdapter.customerOrders();
        return customerOrdersPojoArrayList;
    }


}
