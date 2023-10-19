package com.example.subosh.restauranttrack.admincontent;

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
import java.util.Map;

public class AdminOwnerOrderRequestFragment extends Fragment {
    String adminname,marketname;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ownerdatabaserefernceforadmin;
    AdminViewOrdersSummaryPojo adminViewOrdersSummaryPojo;
    AdminViewOrderRequestSummaryAdapter adminViewOrderRequestSummaryAdapter;
    ArrayList<OwnerInformation> ownerInformationArrayList=new ArrayList<>();
    RecyclerView recyclerView;
    String deliveryRequestStatus;
    Toolbar toolbar;
    static String marketnamenode;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    ArrayList<AdminViewOrdersSummaryPojo> ordersSummaryPojoArrayListcustomername=new ArrayList<AdminViewOrdersSummaryPojo>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.admin_owner_order_fragment,container,false);
        toolbar = view.findViewById(R.id.toolbar);
        AdminOwnerOrderRequestActivity adminOwnerOrderRequestActivity=(AdminOwnerOrderRequestActivity) getActivity();
        adminOwnerOrderRequestActivity.setSupportActionBar(toolbar);
        adminOwnerOrderRequestActivity.setTitle("Request From Owners");
        adminname=adminOwnerOrderRequestActivity.getAdminname();
        marketname=AdminViewOwnerOrderRequestAdapter.getMarketNametoAdminFragment();
       // firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        recyclerView=view.findViewById(R.id.admin_request_recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        Toast.makeText(getContext(),adminname+" "+marketname,Toast.LENGTH_SHORT).show();
        getCustomerOrderToRequestfromFirebase();
        return view;
    }
    public void getCustomerOrderToRequestfromFirebase()
    {
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
    }
    public String getOwnerOrderToAdmin(String snapshotstring){
        ownerdatabaserefernceforadmin.child(snapshotstring).child("CUSTOMERSORDERS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ordersSummaryPojoArrayListcustomername.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    for (DataSnapshot dataSnapshot2:dataSnapshot1.getChildren())
                    {
                        String customername=dataSnapshot1.getKey();
                        String ordernodes=dataSnapshot2.getKey();

                        deliveryRequestStatus=getOwnerInformationArrayList().get(0).getOwnerDeliveryRequestStatus();
                        GenericTypeIndicator<ArrayList<CustomerOrdersPojo>> t1 = new GenericTypeIndicator<ArrayList<CustomerOrdersPojo>>() {};
                        ArrayList<CustomerOrdersPojo> customerOrdersPojo=dataSnapshot2.child("PRODUCTNAME").getValue(t1);
                        String orderstatus=dataSnapshot2.child("ORDERSTATUS").getValue(String.class);
                        Float orderamount=dataSnapshot2.child("ORDEREDAMOUNT").getValue(Float.class);
                        GenericTypeIndicator<ArrayList<Map<String,Double>>> coordinates = new GenericTypeIndicator<ArrayList<Map<String,Double>>> () {};
                        ArrayList<Map<String,Double>> coordinatesDataList=dataSnapshot2.child("CUSTOMERLOCATIONCOORDINATES").getValue(coordinates);

                        GenericTypeIndicator<ArrayList<CustomerInformation>> t2 = new GenericTypeIndicator<ArrayList<CustomerInformation>>() {};
                        ArrayList<CustomerInformation> customerDeliveryDetails=dataSnapshot2.child("DELIVERYADDRESS").getValue(t2);
                        String orderedDate=dataSnapshot2.child("ORDEREDDATE").getValue(String.class);
                        String orderedTime=dataSnapshot2.child("ORDEREDTIME").getValue(String.class);
                        String orderId=dataSnapshot2.child("PRODUCTID").getValue(String.class);
                       if (dataSnapshot2.child("ORDERSTATUS").getValue()!=null){
                        if(!orderstatus.equals("DELIVERED")){
                            adminViewOrdersSummaryPojo=new AdminViewOrdersSummaryPojo(orderedDate,orderedTime,orderId,customerOrdersPojo,customername,orderstatus,ordernodes,orderamount,customerDeliveryDetails,deliveryRequestStatus,coordinatesDataList);
                            ordersSummaryPojoArrayListcustomername.add(adminViewOrdersSummaryPojo);
                        }}
                        }
                }
                adminViewOrderRequestSummaryAdapter=new AdminViewOrderRequestSummaryAdapter(getContext(),ordersSummaryPojoArrayListcustomername,adminname,marketname);
                recyclerView.setAdapter(adminViewOrderRequestSummaryAdapter);
                adminViewOrderRequestSummaryAdapter.notifyDataSetChanged();
                //recyclerView.smoothScrollToPosition(ordersSummaryPojoArrayListcustomername.size());





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return snapshotstring;
    }
    public ArrayList<OwnerInformation> getOwnerInformationArrayList(){
        return ownerInformationArrayList;
    }

    public static String getMarketnamenode() {
        return marketnamenode;
    }
}
