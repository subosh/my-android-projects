package com.example.subosh.restauranttrack.admincontent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.subosh.restauranttrack.R;
import com.example.subosh.restauranttrack.customerscontent.CustomerInformation;
import com.example.subosh.restauranttrack.customerscontent.CustomerOrdersPojo;
import com.example.subosh.restauranttrack.ownercontent.OwnerInformation;
import com.example.subosh.restauranttrack.ownercontent.Owners;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class AdminViewOwnerOrderFragment extends Fragment {

    String adminname,marketname;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ownerdatabaserefernceforadmin;
    AdminViewOrdersSummaryPojo adminViewOrdersSummaryPojo;
AdminViewOrderSummaryAdapter adminViewOrderSummaryAdapter;
    ArrayList<OwnerInformation> ownerInformationArrayList=new ArrayList<>();
RecyclerView recyclerView;
    String deliveryRequestStatus;
Toolbar toolbar;
static String marketnamenode;
    ArrayList<AdminViewOrdersSummaryPojo> ordersSummaryPojoArrayListcustomername=new ArrayList<AdminViewOrdersSummaryPojo>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.admin_view_owner_order_fragment,container,false);
        toolbar = view.findViewById(R.id.toolbar);
        AdminViewOwnerOrderActivity adminViewOwnerOrderActivity=(AdminViewOwnerOrderActivity) getActivity();
        adminViewOwnerOrderActivity.setSupportActionBar(toolbar);
        adminViewOwnerOrderActivity.setTitle("OwnerOrders");
        adminname=adminViewOwnerOrderActivity.getAdminname();
        marketname=AdminViewOwnerOrderAdapter.getMarketNametoAdminFragment();


        Toast.makeText(getContext(),adminname+" "+marketname,Toast.LENGTH_SHORT).show();
        firebaseDatabase=FirebaseDatabase.getInstance();
        ownerdatabaserefernceforadmin=firebaseDatabase.getReference("OWNERS");
        ownerdatabaserefernceforadmin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    OwnerInformation ownerInformation=ds.getValue(OwnerInformation.class);
                    if(ownerInformation.getOwnername().equals(marketname)) {
                   ownerInformationArrayList.add(ownerInformation);
                        marketnamenode= getOwnerOrderToAdmin(ds.getKey());
                    break;
                    }
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        recyclerView=view.findViewById(R.id.admin_view_owner_order_recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        return  view;
    }
    public String getOwnerOrderToAdmin(String dataSnapshot){
        ownerdatabaserefernceforadmin.child(dataSnapshot).child("CUSTOMERSORDERS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ordersSummaryPojoArrayListcustomername.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
            for (DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()) {
                String customername = dataSnapshot1.getKey();
                String ordernodes = dataSnapshot2.getKey();
                deliveryRequestStatus = getOwnerInformationArrayList().get(0).getOwnerDeliveryRequestStatus();
                GenericTypeIndicator<ArrayList<CustomerOrdersPojo>> t1 = new GenericTypeIndicator<ArrayList<CustomerOrdersPojo>>() {
                };
                ArrayList<CustomerOrdersPojo> customerOrdersPojo = dataSnapshot2.child("PRODUCTNAME").getValue(t1);
                GenericTypeIndicator<ArrayList<Map<String, Double>>> coordinates = new GenericTypeIndicator<ArrayList<Map<String, Double>>>() {
                };
                ArrayList<Map<String, Double>> coordinatesDataList = dataSnapshot2.child("CUSTOMERCOORDINATES").child("0").getValue(coordinates);

                String orderstatus = dataSnapshot2.child("ORDERSTATUS").getValue(String.class);
                Float orderamount = dataSnapshot2.child("ORDEREDAMOUNT").getValue(Float.class);
                GenericTypeIndicator<ArrayList<CustomerInformation>> t2 = new GenericTypeIndicator<ArrayList<CustomerInformation>>() {
                };
                ArrayList<CustomerInformation> customerDeliveryDetails = dataSnapshot2.child("DELIVERYADDRESS").getValue(t2);
                String orderedDate=dataSnapshot2.child("ORDEREDDATE").getValue(String.class);
                String orderedTime=dataSnapshot2.child("ORDEREDTIME").getValue(String.class);
                String orderId=dataSnapshot2.child("PRODUCTID").getValue(String.class);
                if (dataSnapshot2.child("ORDERSTATUS").getValue() != null) {
                    if (!orderstatus.equals("DELIVERED")) {
                        adminViewOrdersSummaryPojo = new AdminViewOrdersSummaryPojo(orderedDate,orderedTime,orderId,customerOrdersPojo, customername, orderstatus, ordernodes, orderamount, customerDeliveryDetails, deliveryRequestStatus, coordinatesDataList);
                        ordersSummaryPojoArrayListcustomername.add(adminViewOrdersSummaryPojo);
                    }

                }
            }
                }
                adminViewOrderSummaryAdapter=new AdminViewOrderSummaryAdapter(getActivity(),ordersSummaryPojoArrayListcustomername,adminname);
                recyclerView.setAdapter(adminViewOrderSummaryAdapter);
                adminViewOrderSummaryAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(ordersSummaryPojoArrayListcustomername.size());

                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
return dataSnapshot;
    }
//    public void getOwnerDetailsFromFirebase(){
//        firebaseUser=firebaseAuth.getCurrentUser();
//        DatabaseReference customerordersDatabaseReference= FirebaseDatabase.getInstance().getReference("OWNERS");
//        customerordersDatabaseReference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                OwnerInformation ownerInformation=dataSnapshot.getValue(OwnerInformation.class);
//                ownerInformationArrayList.add(ownerInformation);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }

    public ArrayList<OwnerInformation> getOwnerInformationArrayList(){
        return ownerInformationArrayList;
    }

    public static String getMarketnamenode() {
        return marketnamenode;
    }
}
