package com.example.subosh.restauranttrack.ownercontent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.subosh.restauranttrack.R;
import com.example.subosh.restauranttrack.customerscontent.CustomerInformation;
import com.example.subosh.restauranttrack.customerscontent.CustomerOrdersPojo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OwnerOrderHistoryFragment extends Fragment {
    Toolbar toolbar;
    RecyclerView ownerorderhistoryRecyclerview;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String orderStatus;
    DatabaseReference customerordersDatabaseReference,ordersreference;
    String deliveredConstant="DELIVERED";
    OrdersSummaryPojo ordersSummaryPojo;
    OwnerOrderHistoryAdapter ownerOrderHistoryAdapter;
    ArrayList<OrdersSummaryPojo> ordersSummaryPojoArrayListcustomername=new ArrayList<OrdersSummaryPojo>();
    ArrayList<OwnerInformation> ownerInformationArrayList=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.ownerorderhistoryfragment,container,false);
        toolbar = view.findViewById(R.id.toolbar);
        ownerorderhistoryRecyclerview=view.findViewById(R.id.ownerorderhistoryrecyclerview);
        ownerproducts getownerproductsactivity=(ownerproducts)getActivity();
        getownerproductsactivity.setSupportActionBar(toolbar);
        getownerproductsactivity.setTitle("Delivered Orders History");
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        getOwnerDetailsFromFirebase();
        getOrderHistoryFromFirebase();
        ownerorderhistoryRecyclerview.setHasFixedSize(true);
        ownerorderhistoryRecyclerview.setLayoutManager(new LinearLayoutManager(this.getContext()));
        return view;
    }
    public void getOrderHistoryFromFirebase(){
        firebaseUser=firebaseAuth.getCurrentUser();
        customerordersDatabaseReference= FirebaseDatabase.getInstance().getReference("OWNERS");
        ordersreference=customerordersDatabaseReference.child(firebaseUser.getUid()).child("CUSTOMERSORDERS");
        ordersreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ordersSummaryPojoArrayListcustomername.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot : ds.getChildren()) {
                        String customername = ds.getKey();
                        String ordernodestring = snapshot.getKey();
                        orderStatus = snapshot.child("ORDERSTATUS").getValue(String.class);
                        if ( snapshot.child("ORDERSTATUS").getValue()!=null)
                        {
                            if (orderStatus.equals(deliveredConstant))
                            {
                                GenericTypeIndicator<ArrayList<CustomerOrdersPojo>> t = new GenericTypeIndicator<ArrayList<CustomerOrdersPojo>>() {
                                };
                                ArrayList<CustomerOrdersPojo> customerOrdersPojo = snapshot.child("PRODUCTNAME").getValue(t);
                                String adminordercaretaker = snapshot.child("ORDERCARETAKER").getValue(String.class);
                                String orderstatus = snapshot.child("ORDERSTATUS").getValue(String.class);
                                Float orderAmount = snapshot.child("ORDEREDAMOUNT").getValue(Float.class);
                                GenericTypeIndicator<ArrayList<CustomerInformation>> t2 = new GenericTypeIndicator<ArrayList<CustomerInformation>>() {};
                                ArrayList<CustomerInformation> customerDeliveryDetails=snapshot.child("DELIVERYADDRESS").getValue(t2);
                                String delivereddate=snapshot.child("DELIVEREDDATE").getValue(String.class);
                                String deliveredtime=snapshot.child("DELIVEREDTIME").getValue(String.class);
                                String orderId=snapshot.child("PRODUCTID").getValue(String.class);
                                ordersSummaryPojo = new OrdersSummaryPojo(delivereddate,deliveredtime,orderId,customerDeliveryDetails,customerOrdersPojo, customername, adminordercaretaker, "not needed", getOwnerInformationArrayList(), orderstatus, ordernodestring, orderAmount);
                                ordersSummaryPojoArrayListcustomername.add(ordersSummaryPojo);
                            }
                        }

                    }
                }
                ownerOrderHistoryAdapter=new OwnerOrderHistoryAdapter(getContext(),ordersSummaryPojoArrayListcustomername);
                ownerorderhistoryRecyclerview.setAdapter(ownerOrderHistoryAdapter);
                ownerOrderHistoryAdapter.notifyDataSetChanged();
                ownerorderhistoryRecyclerview.smoothScrollToPosition(ordersSummaryPojoArrayListcustomername.size());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }
    public void getOwnerDetailsFromFirebase(){
        firebaseUser=firebaseAuth.getCurrentUser();
        customerordersDatabaseReference= FirebaseDatabase.getInstance().getReference("OWNERS");
        customerordersDatabaseReference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                OwnerInformation ownerInformation=dataSnapshot.getValue(OwnerInformation.class);
                ownerInformationArrayList.add(ownerInformation);
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
