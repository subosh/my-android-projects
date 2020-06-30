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
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.subosh.restauranttrack.R;
import com.example.subosh.restauranttrack.ownercontent.OwnerInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderMaintanenceFragment extends Fragment {
   Toolbar toolbar;
    FirebaseAuth firebaseAuth;
    DatabaseReference getOwnerordersdatabaseReference;
    RecyclerView recyclerView;
    AdminViewOwnerOrderAdapter adminViewOwnerOrderAdapter;
    ArrayList<OwnerInformation> list1;
    ArrayAdapter<String> adapter;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
  OwnerInformation ownerInformation;
    String adminname,orderDeliveryRequestStatus;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.order_maintanence_fragment,container,false);
        admin admin=(admin) getActivity();
        recyclerView=view.findViewById(R.id.admin_view_shops_recycle);
        firebaseAuth= FirebaseAuth.getInstance();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        list1 = new ArrayList<OwnerInformation>();
        firebaseDatabase= FirebaseDatabase.getInstance();
        getOwnerordersdatabaseReference = firebaseDatabase.getReference().child("OWNERS");
        adminname=admin.getAdminname();
        getOwnerordersdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list1.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    ownerInformation=ds.getValue(OwnerInformation.class);
                    list1.add(ownerInformation);
                }
                adminViewOwnerOrderAdapter=new AdminViewOwnerOrderAdapter(getActivity(),list1,adminname);
                recyclerView.setAdapter(adminViewOwnerOrderAdapter);
                adminViewOwnerOrderAdapter.notifyDataSetChanged();
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
