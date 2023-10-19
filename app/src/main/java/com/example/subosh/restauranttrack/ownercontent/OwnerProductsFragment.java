package com.example.subosh.restauranttrack.ownercontent;

import android.os.AsyncTask;
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
import com.example.subosh.restauranttrack.admincontent.ProductlistSinglenton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OwnerProductsFragment extends Fragment {
    View view;
    DatabaseReference marketproductsdatabaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    String marketname;
    Toolbar toolbar;
    RecyclerView shopproductsrecyclerview;
    OwnerProductsAdapter ownerProductsAdapter;
    String productlistarray[];
    ArrayList<ProductlistSinglenton> fullproducts=new ArrayList<ProductlistSinglenton>();

public OwnerProductsFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.ownerproductsfragment,container,false);
        toolbar = view.findViewById(R.id.toolbar);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        ownerproducts getownerproductsactivity=(ownerproducts)getActivity();
        getownerproductsactivity.setSupportActionBar(toolbar);
        shopproductsrecyclerview=view.findViewById(R.id.owner_products_recycle);
        shopproductsrecyclerview.setHasFixedSize(true);
        shopproductsrecyclerview.setLayoutManager(new LinearLayoutManager(this.getContext()));
        getownerproductsactivity.setTitle("Owner Products");
        marketname=getownerproductsactivity.getMarketName();
        starttask();
        //getshopprofileproducts();
        return view;
    }
    public Void getshopprofileproducts(){
    marketproductsdatabaseReference=FirebaseDatabase.getInstance().getReference("OWNERS");
        marketproductsdatabaseReference.child(firebaseUser.getUid()).child("MARKETPRODUCTS").child(marketname).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fullproducts.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    ProductlistSinglenton productlistSinglenton=ds.getValue( ProductlistSinglenton.class);
                    if (ds.getValue()!=null)
                    {
                        fullproducts.add(productlistSinglenton);
                    }
                    }
                    ownerProductsAdapter=new OwnerProductsAdapter(getActivity(),fullproducts,marketname);
                    shopproductsrecyclerview.setAdapter(ownerProductsAdapter);
                    ownerProductsAdapter.notifyDataSetChanged();
                    //shopproductsrecyclerview.smoothScrollToPosition(fullproducts.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        return null;
    }
    class Async extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... Params) {
            return getshopprofileproducts();
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);

        }
    }
    public void starttask(){
        Async async=new Async();
        async.execute();

    }
}
