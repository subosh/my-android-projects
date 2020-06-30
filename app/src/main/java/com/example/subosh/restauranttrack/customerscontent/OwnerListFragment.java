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
import android.widget.Toast;

import com.example.subosh.restauranttrack.R;
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

public class OwnerListFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    AdapterUsers adapterUsers;
    ArrayList<OwnerInformation> list1;
    ArrayAdapter<String> adapter;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    //CustomerInformation customerInformation;
    OwnerInformation ownerInformation;
    Toolbar toolbar;
    View view;
    String customername;
    ArrayList<CustomerInformation> getCustomerDetailsList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.ownerlistfragment,container,false);
        //toolbar = view.findViewById(R.id.toolbar);
        Owners getOwners=(Owners) getActivity();
        //getOwners.setSupportActionBar(toolbar);
        customername=getOwners.getCustomername();
        getCustomerDetailsList=getOwners.getCustomerDetailsList();
        getOwners.setTitle("List Of Shops");

        recyclerView=view.findViewById(R.id.recycle);
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
                adapterUsers=new AdapterUsers(getActivity(),list1,customername);
                recyclerView.setAdapter(adapterUsers);
                adapterUsers.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"error in showing profiles"+" "+databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}
