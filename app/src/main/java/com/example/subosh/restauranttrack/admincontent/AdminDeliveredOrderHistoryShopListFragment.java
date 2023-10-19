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

public class AdminDeliveredOrderHistoryShopListFragment extends Fragment {
    Toolbar toolbar;
    RecyclerView deliverdShopsListRecyclerview;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ownerdatabaserefernceforadmin,ownerInformationDatabaseReference;
    ArrayList<OwnerInformation> ownerInformationArrayList=new ArrayList<>();
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    AdminDeliveredShopsListAdapter adminDeliveredShopsListAdapter;
    ArrayList<OwnerInformation> ownerInformationArrayList1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.admin_delivered_order_history_shoplist_fragment,container,false);
        initializeView(view);
        return view;
    }
    public void initializeView(View view){
        toolbar=view.findViewById(R.id.toolbar);
        deliverdShopsListRecyclerview=view.findViewById(R.id.shop_lists);
        AdminDeliveredOrderHistoryActivity adminDeliveredOrderHistoryActivity=(AdminDeliveredOrderHistoryActivity)getActivity();
        adminDeliveredOrderHistoryActivity.setSupportActionBar(toolbar);
        adminDeliveredOrderHistoryActivity.setTitle("Delivered Shops List");
        getDeliverdOrdersShopOwnerInformationFromFirebase();
        deliverdShopsListRecyclerview.setHasFixedSize(true);
        deliverdShopsListRecyclerview.setLayoutManager(new LinearLayoutManager(this.getContext()));


    }
    public void getDeliverdOrdersShopOwnerInformationFromFirebase(){
        firebaseUser=firebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        ownerdatabaserefernceforadmin=firebaseDatabase.getReference("ADMINS");
        ownerdatabaserefernceforadmin.child(firebaseUser.getUid()).child("ADMINDELIVEREDORDERS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ownerInformationArrayList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    String marketName=dataSnapshot1.getKey();
                    ownerInformationArrayList1=getOwnerInformationFromFirebase(marketName);

                }

            }            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
public ArrayList<OwnerInformation> getOwnerInformationFromFirebase(final String marketName){
    firebaseDatabase=FirebaseDatabase.getInstance();
    ownerInformationDatabaseReference=firebaseDatabase.getReference("OWNERS");
    ownerInformationDatabaseReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                OwnerInformation ownerInformation=dataSnapshot1.getValue(OwnerInformation.class);
                if (ownerInformation.getOwnername().equals(marketName))
                {
                    ownerInformationArrayList.add(ownerInformation);
                }
            }
            adminDeliveredShopsListAdapter=new AdminDeliveredShopsListAdapter(getContext(),ownerInformationArrayList);
            deliverdShopsListRecyclerview.setAdapter(adminDeliveredShopsListAdapter);
            adminDeliveredShopsListAdapter.notifyDataSetChanged();
            deliverdShopsListRecyclerview.smoothScrollToPosition(ownerInformationArrayList.size());

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
