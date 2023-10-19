package com.example.subosh.restauranttrack.admincontent;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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

public class AdminRequestFragment extends Fragment {
    Toolbar toolbar;
    FirebaseAuth firebaseAuth;
    DatabaseReference getOwnerordersdatabaseReference;
    RecyclerView recyclerView;
    AdminViewOwnerOrderRequestAdapter adminViewOwnerOrderRequestAdapter;
    ArrayList<OwnerInformation> list1;
    ArrayAdapter<String> adapter;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    OwnerInformation ownerInformation;
    String adminname,orderDeliveryRequestStatus;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.admin_request_fragment,container,false);
        admin admin=(admin) getActivity();
        recyclerView=view.findViewById(R.id.admin_view_owners_request_recycle);
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
                    if (ownerInformation.getOwnerDeliveryRequestStatus().equals("YES"))
                    {
                        list1.add(ownerInformation);
                    }
                }
                adminViewOwnerOrderRequestAdapter= new AdminViewOwnerOrderRequestAdapter(getActivity(),list1,adminname);
                recyclerView.setAdapter(adminViewOwnerOrderRequestAdapter);
                adminViewOwnerOrderRequestAdapter.notifyDataSetChanged();
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
