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

public class OwnerViewCustomerOrdersFragment extends Fragment {
    Toolbar toolbar;
    RecyclerView ownersviewcustomerordersrecyclerview;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference customerordersDatabaseReference,ordersreference;
    OrdersSummaryAdapter ordersSummaryAdapter;
    OrdersSummaryPojo ordersSummaryPojo;
    ArrayList<OwnerInformation> ownerInformationArrayList=new ArrayList<>();

    ArrayList<OrdersSummaryPojo> ordersSummaryPojoArrayListcustomername=new ArrayList<OrdersSummaryPojo>();
    String marketname,productaddingtime,productaddingdate,productId;
    String deliveryRequestStatus,orderStatus;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.ownerviewcustomerordersfragment,container,false);
        toolbar = view.findViewById(R.id.toolbar);
        ownersviewcustomerordersrecyclerview=view.findViewById(R.id.ownersview_customer_orders_recycle);
        ownerproducts getownerproductsactivity=(ownerproducts)getActivity();
        getownerproductsactivity.setSupportActionBar(toolbar);
        getownerproductsactivity.setTitle("Orders Summary");
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        ownerInformationArrayList=getownerproductsactivity.getOwnerInformationArrayList();
        getCustomerOrders(ownerInformationArrayList);
        ownersviewcustomerordersrecyclerview.setHasFixedSize(true);
        ownersviewcustomerordersrecyclerview.setLayoutManager(new LinearLayoutManager(this.getContext()));

        return  view;
    }

    public void getCustomerOrders(final ArrayList<OwnerInformation> ownerDetailsFromFirebase){
        firebaseUser=firebaseAuth.getCurrentUser();
        customerordersDatabaseReference= FirebaseDatabase.getInstance().getReference("OWNERS");
        final DatabaseReference requestStatus=customerordersDatabaseReference.child(firebaseUser.getUid()).child("ownerDeliveryRequestStatus");
        ordersreference=customerordersDatabaseReference.child(firebaseUser.getUid()).child("CUSTOMERSORDERS");
        ordersreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ordersSummaryPojoArrayListcustomername.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren())
                { for (DataSnapshot snapshot:ds.getChildren())
                    {
                        String customername=ds.getKey();
                        String ordernodestring=snapshot.getKey();
                        orderStatus=snapshot.child("ORDERSTATUS").getValue(String.class);
                        deliveryRequestStatus=ownerDetailsFromFirebase.get(0).getOwnerDeliveryRequestStatus();
                        if(deliveryRequestStatus.equals("YES")&&snapshot.child("ORDERSTATUS").getValue()!=null)
                        {
                            if (orderStatus.equals("ORDERACCEPTEDBYADMIN"))
                            {
                                GenericTypeIndicator<ArrayList<CustomerOrdersPojo>> t = new GenericTypeIndicator<ArrayList<CustomerOrdersPojo>>() {
                                };
                                ArrayList<CustomerOrdersPojo> customerOrdersPojo = snapshot.child("PRODUCTNAME").getValue(t);
                                String orderstatus=snapshot.child("ORDERSTATUS").getValue(String.class);
                                Float orderAmount=snapshot.child("ORDEREDAMOUNT").getValue(Float.class);
                                String adminordercaretaker = snapshot.child("ORDERCARETAKER").getValue(String.class);
                                GenericTypeIndicator<ArrayList<CustomerInformation>> t2 = new GenericTypeIndicator<ArrayList<CustomerInformation>>() {};
                                ArrayList<CustomerInformation> customerDeliveryDetails=snapshot.child("DELIVERYADDRESS").getValue(t2);
                                String orderedDate=snapshot.child("ORDEREDDATE").getValue(String.class);
                                String orderedTime=snapshot.child("ORDEREDTIME").getValue(String.class);
                                String orderId=snapshot.child("PRODUCTID").getValue(String.class);
                                ordersSummaryPojo = new OrdersSummaryPojo(orderedDate,orderedTime,orderId,customerDeliveryDetails,customerOrdersPojo, customername, adminordercaretaker,deliveryRequestStatus,getOwnerInformationArrayList(),orderstatus,ordernodestring,orderAmount);
                                ordersSummaryPojoArrayListcustomername.add(ordersSummaryPojo);
                            }
                            }
                            else {
                            if(snapshot.child("ORDERSTATUS").getValue()!=null)
                            {
                            if (orderStatus.equals("ORDERACCEPTEDBYADMIN") ||orderStatus.equals("ORDERED")) {
                                GenericTypeIndicator<ArrayList<CustomerOrdersPojo>> t = new GenericTypeIndicator<ArrayList<CustomerOrdersPojo>>() {
                                };
                                ArrayList<CustomerOrdersPojo> customerOrdersPojo = snapshot.child("PRODUCTNAME").getValue(t);
                                String adminordercaretaker = snapshot.child("ORDERCARETAKER").getValue(String.class);
                                String orderstatus = snapshot.child("ORDERSTATUS").getValue(String.class);
                                Float orderAmount = snapshot.child("ORDEREDAMOUNT").getValue(Float.class);
                                GenericTypeIndicator<ArrayList<CustomerInformation>> t2 = new GenericTypeIndicator<ArrayList<CustomerInformation>>() {};
                                ArrayList<CustomerInformation> customerDeliveryDetails=snapshot.child("DELIVERYADDRESS").getValue(t2);
                                String orderedDate=snapshot.child("ORDEREDDATE").getValue(String.class);
                                String orderedTime=snapshot.child("ORDEREDTIME").getValue(String.class);
                                String orderId=snapshot.child("PRODUCTID").getValue(String.class);
                                ordersSummaryPojo = new OrdersSummaryPojo(orderedDate,orderedTime,orderId,customerDeliveryDetails,customerOrdersPojo, customername, adminordercaretaker, deliveryRequestStatus, getOwnerInformationArrayList(), orderstatus, ordernodestring, orderAmount);
                                ordersSummaryPojoArrayListcustomername.add(ordersSummaryPojo);
                            }
                            }
                        }
                    }
                }

                ordersSummaryAdapter=new OrdersSummaryAdapter(getActivity(),ordersSummaryPojoArrayListcustomername);
                ownersviewcustomerordersrecyclerview.setAdapter(ordersSummaryAdapter);
                ordersSummaryAdapter.notifyDataSetChanged();
//                ownersviewcustomerordersrecyclerview.smoothScrollToPosition(ordersSummaryPojoArrayListcustomername.size());



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }
    public ArrayList<OwnerInformation> getOwnerDetailsFromFirebase(){
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
return ownerInformationArrayList;
    }

    public ArrayList<OwnerInformation> getOwnerInformationArrayList(){
        return ownerInformationArrayList;
    }

}
