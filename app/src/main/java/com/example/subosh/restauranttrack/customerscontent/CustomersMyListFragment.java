package com.example.subosh.restauranttrack.customerscontent;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.example.subosh.restauranttrack.R;
import com.example.subosh.restauranttrack.admincontent.ProductlistSinglenton;
import com.example.subosh.restauranttrack.ownercontent.AdapterUsers;
import com.example.subosh.restauranttrack.ownercontent.OwnerInformation;
import com.example.subosh.restauranttrack.ownercontent.Owners;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomersMyListFragment extends Fragment {
    CustomerViewProductsAdapter customerViewProductsAdapter;
    CustomerOrdersConfirmationFragment customerOrdersConfirmationFragment;
    RecyclerView shopproductsrecyclerview;
    ProductlistSinglenton productlistSinglenton;
    Toolbar toolbar;
    Button placeorder;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    CustomerMyListAdapter customerMyListAdapter;
    ArrayList<OwnerInformation> list1;
    ArrayAdapter<String> adapter;
    FirebaseDatabase firebaseDatabase;
    static String customername;
    OwnerInformation ownerInformation;
    ArrayList<CustomerInformation> getCustomerDetailsList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.customers_mylist_fragment,container,false);
        Owners getOwners=(Owners) getActivity();
        //getOwners.setSupportActionBar(toolbar);
        customername=getOwners.getCustomername();
        getCustomerDetailsList=getOwners.getCustomerDetailsList();
        getOwners.setTitle("List Of Shops");
        recyclerView=view.findViewById(R.id.customer_mylist_recycle);
        firebaseAuth= FirebaseAuth.getInstance();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        list1 = new ArrayList<OwnerInformation>();
        // customerInformation=new CustomerInformation();
        ownerInformation=new OwnerInformation();
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("OWNERS");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list1.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    ownerInformation=ds.getValue(OwnerInformation.class);
                    list1.add(ownerInformation);
                }
                customerMyListAdapter=new CustomerMyListAdapter(getActivity(),list1,customername);
                recyclerView.setAdapter(customerMyListAdapter);
                customerMyListAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(list1.size());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"error in showing profiles"+" "+databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}
