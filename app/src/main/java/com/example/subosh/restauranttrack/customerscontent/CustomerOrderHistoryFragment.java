package com.example.subosh.restauranttrack.customerscontent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.example.subosh.restauranttrack.R;
import com.example.subosh.restauranttrack.ownercontent.OrdersSummaryPojo;
import com.example.subosh.restauranttrack.ownercontent.OwnerInformation;
import com.example.subosh.restauranttrack.ownercontent.OwnerOrderHistoryAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class CustomerOrderHistoryFragment extends Fragment {
    android.support.v7.widget.Toolbar toolbar;
    RecyclerView customerorderHistoryRecyclerview;
    DatabaseReference ordershistoryDataBaseReference,customerordersDatabaseReference;
    OrdersSummaryPojo ordersSummaryPojo;
    ArrayList<OwnerInformation> ownerInformationArrayList1=new ArrayList<>();
    ArrayList<OwnerInformation> ownerInformationArrayList=new ArrayList<>();
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    ArrayList<OrdersSummaryPojo> ordersSummaryPojoArrayListcustomername=new ArrayList<OrdersSummaryPojo>();
    String customerName;
    CustomerOrderHistoryAdapter customerOrderHistoryAdapter;
    OwnerInformation getOwnerInformationAsList;
    FirebaseUser firebaseUser;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.customer_order_history_fragment,container,false);
        toolbar=view.findViewById(R.id.toolbar);
        customerorderHistoryRecyclerview=view.findViewById(R.id.customer_order_history_recylerview);
        CustomerOrderHistoryActivity customerOrderHistoryActivity=(CustomerOrderHistoryActivity)getActivity();
        customerOrderHistoryActivity.setSupportActionBar(toolbar);
        customerOrderHistoryActivity.setTitle("Your Orders History");
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        customerName=customerOrderHistoryActivity.getCustomerName();
        getOwnerDetailsFromFirebase();
        getCustomerOrdersHistoryFromFirebase(customerName);
        customerorderHistoryRecyclerview.setHasFixedSize(true);
        customerorderHistoryRecyclerview.setLayoutManager(new LinearLayoutManager(this.getContext()));
        return view;
    }
    public void getCustomerOrdersHistoryFromFirebase(String customerName){
        ordershistoryDataBaseReference=FirebaseDatabase.getInstance().getReference("CUSTOMERS");
        ordershistoryDataBaseReference.child(firebaseUser.getUid()).child(customerName).child("ORDERSLIST").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ordersSummaryPojoArrayListcustomername.clear();
//                ownerInformationArrayList1.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){

                    for (DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()){
                        String ownername=dataSnapshot1.getKey();
                        String ordernodes=dataSnapshot2.getKey();
                        for (int i=0;i<getOwnerInformationArrayList().size();i++){
                            if (getOwnerInformationArrayList().get(i).getOwnername().equals(ownername)){
                                getOwnerInformationAsList=getOwnerInformationArrayList().get(i);
                                break;
                            }
                        }
                        ownerInformationArrayList1.add(getOwnerInformationAsList);
                        GenericTypeIndicator<ArrayList<CustomerOrdersPojo>> t1 = new GenericTypeIndicator<ArrayList<CustomerOrdersPojo>>() {};
                        ArrayList<CustomerOrdersPojo> customerOrdersPojo=dataSnapshot2.child("PRODUCTNAME").getValue(t1);
                        String adminordercaretaker = dataSnapshot2.child("ORDERCARETAKER").getValue(String.class);
                        String orderstatus=dataSnapshot2.child("ORDERSTATUS").getValue(String.class);
                        Float orderamount=dataSnapshot2.child("ORDEREDAMOUNT").getValue(Float.class);
                        GenericTypeIndicator<ArrayList<CustomerInformation>> t2 = new GenericTypeIndicator<ArrayList<CustomerInformation>>() {};
                        ArrayList<CustomerInformation> customerDeliveryDetails=dataSnapshot2.child("DELIVERYADDRESS").getValue(t2);
                        String orderedDate=dataSnapshot2.child("ORDEREDDATE").getValue(String.class);
                        String orderedTime=dataSnapshot2.child("ORDEREDTIME").getValue(String.class);
                        String orderId=dataSnapshot2.child("PRODUCTID").getValue(String.class);
                        if (dataSnapshot2.child("ORDERSTATUS").getValue()!=null)
                        {
                        ordersSummaryPojo = new OrdersSummaryPojo(orderedDate,orderedTime,orderId,customerDeliveryDetails,customerOrdersPojo, ownername, adminordercaretaker, "not needed", ownerInformationArrayList1, orderstatus, ordernodes, orderamount);
                        ordersSummaryPojoArrayListcustomername.add(ordersSummaryPojo);
                        //ownerInformationArrayList1.clear();
                    }
                    }
                }
               customerOrderHistoryAdapter =new CustomerOrderHistoryAdapter(getContext(),ordersSummaryPojoArrayListcustomername);
                customerorderHistoryRecyclerview.setAdapter(customerOrderHistoryAdapter);
                customerOrderHistoryAdapter.notifyDataSetChanged();
                customerorderHistoryRecyclerview.smoothScrollToPosition(ordersSummaryPojoArrayListcustomername.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getOwnerDetailsFromFirebase(){
        firebaseUser=firebaseAuth.getCurrentUser();
        customerordersDatabaseReference= FirebaseDatabase.getInstance().getReference("OWNERS");
        customerordersDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    OwnerInformation ownerInformation=dataSnapshot1.getValue(OwnerInformation.class);
                    ownerInformationArrayList.add(ownerInformation);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public ArrayList<OwnerInformation> getOwnerInformationArrayList(){
        return ownerInformationArrayList;
    }
}
