package com.example.subosh.restauranttrack.customerscontent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class CustomerViewOwnerProductfragment extends Fragment implements View.OnClickListener
{
    String marketname;
    DatabaseReference marketproductsdatabaseReference;
    ArrayList<ProductlistSinglenton> fullproducts=new ArrayList<ProductlistSinglenton>();
    static ArrayList<CustomerOrdersPojo> customerproductslist=new ArrayList<CustomerOrdersPojo>();
    static ArrayList<String> customerproductspricelist=new ArrayList<>();
    ArrayList<String> shopownerproducts;
    CustomerViewProductsAdapter customerViewProductsAdapter;
    CustomerOrdersConfirmationFragment customerOrdersConfirmationFragment;
    RecyclerView shopproductsrecyclerview;
    ProductlistSinglenton productlistSinglenton;
    Toolbar toolbar;
    Button placeorder;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
   static String customername;
    private static ArrayList<CustomerOrdersPojo> testlist;
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.customer_view_owner_product_fragment,container,false);
        toolbar = view.findViewById(R.id.toolbar);
        placeorder=view.findViewById(R.id.place_order);
        placeorder.setOnClickListener(this);
        CustomerViewOwnerProduct getcustomerview=(CustomerViewOwnerProduct)getActivity();
        getcustomerview.setSupportActionBar(toolbar);
        getcustomerview.setTitle("List of Products");
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        marketname=getcustomerview.getMarketName();
        customername=getcustomerview.getCustomerName();
        shopproductsrecyclerview=view.findViewById(R.id.customer_view_ownerproduct_recycle);
        shopproductsrecyclerview.setHasFixedSize(true);
        shopproductsrecyclerview.setLayoutManager(new LinearLayoutManager(this.getContext()));
        if (testlist==null)
        {
            testlist=new ArrayList<CustomerOrdersPojo>();
        }
        getshopprofileproducts();

        return view;
    }
    public void getshopprofileproducts(){

        marketproductsdatabaseReference= FirebaseDatabase.getInstance().getReference("OWNERS");
        marketproductsdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fullproducts.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren()) {
                    if (ds.child("MARKETPRODUCTS").child(marketname).exists()) {
                        for (DataSnapshot dataSnapshot1 : ds.child("MARKETPRODUCTS").child(marketname).getChildren()) {
                            productlistSinglenton = dataSnapshot1.getValue(ProductlistSinglenton.class);
                            fullproducts.add(productlistSinglenton);
                        }
                    }
                }

                customerViewProductsAdapter=new CustomerViewProductsAdapter(getActivity(),fullproducts,marketname,customername);
                shopproductsrecyclerview.setAdapter(customerViewProductsAdapter);
                customerViewProductsAdapter.notifyDataSetChanged();
//                shopproductsrecyclerview.smoothScrollToPosition(fullproducts.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        }

    @Override
    public void onClick(View v) {
        if(v==placeorder){
            getcustomerproductlist();
            initializeCustomerOrdersConfirmation();
        }
    }
    public void getcustomerproductlist(){
       customerproductslist=customerViewProductsAdapter.getproductlistfromcustomer();
       for (int i=0;i<customerproductslist.size();i++)
        {
        testlist.add(customerproductslist.get(i));
    }
    }
    public void initializeCustomerOrdersConfirmation(){
        customerOrdersConfirmationFragment=new CustomerOrdersConfirmationFragment();
        FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.customer_view_owner_products_frame,customerOrdersConfirmationFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public static ArrayList<CustomerOrdersPojo> getCustomerproductslist() {
        return testlist;
    }

    public static String getCustomername() {
        return customername;
    }
}

