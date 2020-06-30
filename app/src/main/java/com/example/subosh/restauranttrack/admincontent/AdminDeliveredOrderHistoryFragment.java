package com.example.subosh.restauranttrack.admincontent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.subosh.restauranttrack.R;
import com.example.subosh.restauranttrack.customerscontent.CustomerInformation;
import com.example.subosh.restauranttrack.customerscontent.CustomerOrdersPojo;
import com.example.subosh.restauranttrack.ownercontent.OrdersSummaryPojo;
import com.example.subosh.restauranttrack.ownercontent.OwnerInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminDeliveredOrderHistoryFragment extends Fragment {
    android.support.v7.widget.Toolbar toolbar;
    RecyclerView recyclerView;
    OwnerInformation ownerInformationObject;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    OrdersSummaryPojo ordersSummaryPojo;
    AdminDeliveredOrderHistoryAdapter adminDeliveredOrderHistoryAdapter;
    ArrayList<OrdersSummaryPojo> ordersSummaryPojoArrayListcustomername=new ArrayList<OrdersSummaryPojo>();
    DatabaseReference ownerdatabaserefernceforadmin,ownerInformationDatabaseReference;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.admin_delivered_order_history_fragment,container,false);
        initializeView(view);
        return view;
    }
    public void initializeView(View view){
        toolbar=view.findViewById(R.id.toolbar);
        recyclerView=view.findViewById(R.id.orders_list);
        AdminDeliveredOrderHistoryActivity adminDeliveredOrderHistoryActivity=(AdminDeliveredOrderHistoryActivity)getActivity();
        adminDeliveredOrderHistoryActivity.setSupportActionBar(toolbar);
        adminDeliveredOrderHistoryActivity.setTitle("Delivered Orders");
        ownerInformationObject=(OwnerInformation)getArguments().getSerializable("ownerInformation");
        getOwnerDelivereddOrdersToAdminFromFrirebase();
        Toast.makeText(getContext(),ownerInformationObject.getOwnerAddress()+ownerInformationObject.getOwnername(),Toast.LENGTH_LONG).show();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

    }
public void getOwnerDelivereddOrdersToAdminFromFrirebase(){
    firebaseUser=firebaseAuth.getInstance().getCurrentUser();
    firebaseDatabase= FirebaseDatabase.getInstance();
    ownerdatabaserefernceforadmin=firebaseDatabase.getReference("ADMINS");
    ownerdatabaserefernceforadmin.child(firebaseUser.getUid())
            .child("ADMINDELIVEREDORDERS")
            .child(ownerInformationObject.getOwnername())
            .addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            ordersSummaryPojoArrayListcustomername.clear();
            for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
            {
                for (DataSnapshot dataSnapshot2:dataSnapshot1.getChildren())
                {
                    String customername=dataSnapshot1.getKey();
                    String ordernodestring=dataSnapshot2.getKey();
                GenericTypeIndicator<ArrayList<CustomerOrdersPojo>> t = new GenericTypeIndicator<ArrayList<CustomerOrdersPojo>>() {
                };
                ArrayList<CustomerOrdersPojo> customerOrdersPojo = dataSnapshot2.child("PRODUCTNAME").getValue(t);
                String orderstatus=dataSnapshot2.child("ORDERSTATUS").getValue(String.class);
                Float orderAmount=dataSnapshot2.child("ORDEREDAMOUNT").getValue(Float.class);
                String adminordercaretaker = dataSnapshot2.child("ORDERCARETAKER").getValue(String.class);
                GenericTypeIndicator<ArrayList<CustomerInformation>> t2 = new GenericTypeIndicator<ArrayList<CustomerInformation>>() {};
                ArrayList<CustomerInformation> customerDeliveryDetails=dataSnapshot2.child("DELIVERYADDRESS").getValue(t2);
                    String deliveredDate=dataSnapshot2.child("DELIVEREDDATE").getValue(String.class);
                    String deliveredTime=dataSnapshot2.child("DELIVEREDTIME").getValue(String.class);
                    String orderId=dataSnapshot2.child("PRODUCTID").getValue(String.class);
                if(dataSnapshot2.child("ORDERSTATUS").getValue()!=null)
                {
                ordersSummaryPojo = new OrdersSummaryPojo(deliveredDate,deliveredTime,orderId,customerDeliveryDetails,customerOrdersPojo, customername, adminordercaretaker,"not needed",ownerInformationObject,orderstatus,ordernodestring,orderAmount);
                ordersSummaryPojoArrayListcustomername.add(ordersSummaryPojo);
            }}
        }
        adminDeliveredOrderHistoryAdapter=new AdminDeliveredOrderHistoryAdapter(getContext(),ordersSummaryPojoArrayListcustomername);
            recyclerView.setAdapter(adminDeliveredOrderHistoryAdapter);
            adminDeliveredOrderHistoryAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(ordersSummaryPojoArrayListcustomername.size());
        }


        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });

}

}
