package com.example.subosh.restauranttrack.customerscontent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.subosh.restauranttrack.R;
import com.example.subosh.restauranttrack.admincontent.ProductlistSinglenton;
import com.example.subosh.restauranttrack.ownercontent.OwnerInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomerCreateListFragment extends Fragment implements View.OnClickListener {
    CustomerViewCreateListProductsAdapter customerViewProductsAdapter;
    DatabaseReference marketproductsdatabaseReference,monthlyListDatabaseReference;
    ArrayList<CustomerOrdersPojo> customerorderproductlist = new ArrayList<CustomerOrdersPojo>();
    View view;
    Toolbar toolbar;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    CustomerInformation customerInformation;
    static String customername,marketname;
    RecyclerView shopproductsrecyclerview;
   static ArrayList<CustomerOrdersPojo> testlist;
    ArrayList<ProductlistSinglenton> fullproducts=new ArrayList<ProductlistSinglenton>();
    ArrayList<String> productnamelist=new ArrayList<String>();
    static ArrayList<CustomerOrdersPojo> customerproductslist=new ArrayList<CustomerOrdersPojo>();
    static ArrayList<String> customerproductspricelist=new ArrayList<>();
    final boolean[] checker = {false};
    ArrayList<String> shopownerproducts;
    ProductlistSinglenton productlistSinglenton;
    Button getMonthlyListButton;
    CustomerMonthlyListFragment customerMonthlyListFragment;
    static ArrayList<CustomerInformation> customerInformationArrayList=new ArrayList<>();
    static float grandtotal,grandsum;
    float orderGrandTotal;
public CustomerCreateListFragment(){}


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view=inflater.inflate(R.layout.customer_create_list_fragment,container,false);
        initializeViews();
        getshopprofileproducts();
        getCustomerDetailFromFirebase();
        return view;
    }
public void initializeViews(){
    toolbar = view.findViewById(R.id.toolbar);
    getMonthlyListButton=view.findViewById(R.id.see_monthly_list_button);
    getMonthlyListButton.setOnClickListener(this);
    CustomerCreateListActivity customerCreateListActivity=(CustomerCreateListActivity) getActivity();
    customerCreateListActivity.setSupportActionBar(toolbar);
    customerCreateListActivity.setTitle("List of Products");
    firebaseAuth= FirebaseAuth.getInstance();
    firebaseUser=firebaseAuth.getCurrentUser();
    marketname=customerCreateListActivity.getMarketNameCreateList();
    customername=customerCreateListActivity.getCustomerNameCreateList();

    shopproductsrecyclerview=view.findViewById(R.id.customer_create_list_recycle);
    shopproductsrecyclerview.setHasFixedSize(true);
    shopproductsrecyclerview.setLayoutManager(new LinearLayoutManager(this.getContext()));
    if (testlist==null)
    {
        testlist=new ArrayList<CustomerOrdersPojo>();
    }
    customerorderproductlist = getCustomerproductslist();


}
    public void getshopprofileproducts(){

        marketproductsdatabaseReference= FirebaseDatabase.getInstance().getReference("OWNERS");
        marketproductsdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fullproducts.clear();
                productnamelist.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren()) {
                    if (ds.child("MARKETPRODUCTS").child(marketname).exists()) {
                        for (DataSnapshot dataSnapshot1 : ds.child("MARKETPRODUCTS").child(marketname).getChildren()) {
                            productlistSinglenton = dataSnapshot1.getValue(ProductlistSinglenton.class);
                            String productname=dataSnapshot1.child("productname").getValue(String.class);
                            if (dataSnapshot1.child("productname").getValue()!=null)
                            {
                                productnamelist.add(productname);
                                fullproducts.add(productlistSinglenton);
                            }
                            }
                    }
                }

                customerViewProductsAdapter=new CustomerViewCreateListProductsAdapter(getActivity(),fullproducts,marketname,customername,productnamelist);
                customerViewProductsAdapter.notifyDataSetChanged();
                shopproductsrecyclerview.setAdapter(customerViewProductsAdapter);
//                shopproductsrecyclerview.smoothScrollToPosition(productnamelist.size());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    public void getcustomerproductlist(){
        customerproductslist=customerViewProductsAdapter.getproductlistfromcustomer();
        for (int i=0;i<customerproductslist.size();i++)
        {
            testlist.add(customerproductslist.get(i));
        }
    }
    public void initializeCustomerMonthlyListFragment(){
        customerMonthlyListFragment=new CustomerMonthlyListFragment();
        FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.customer_create_list_frame,customerMonthlyListFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public static ArrayList<CustomerOrdersPojo> getCustomerproductslist() {
        return testlist ;
    }

    public static String getCustomername() {
        return customername;
    }

    @Override
    public void onClick(View v) {
     if (v==getMonthlyListButton){
        initializeCustomerMonthlyListFragment();
        }
    }
    public void getCustomerDetailFromFirebase(){
DatabaseReference databaseReference;
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("CUSTOMERS");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    customerInformation=ds.getValue(CustomerInformation.class);
                    if (!checker[0] &&customerInformation.getCustomername().equals(customername)) {
                        customerInformationArrayList.add(customerInformation);
                        checker[0] =true;
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"error in showing profiles"+" "+databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    public static ArrayList<CustomerInformation> getCustomerDetailsList(){
        return customerInformationArrayList;
    }
    public float orderAmount(){
        for (int i=0;i<customerorderproductlist.size();i++)
        {
            if (customerorderproductlist.get(i).getMeasure().equals("KG")) {
                grandtotal = grandtotal + (customerorderproductlist.get(i).getProductsquantity() * customerorderproductlist.get(i).getProductamount());
            }
            else if (customerorderproductlist.get(i).getMeasure().equals("Gram"))
            {
                grandtotal=grandtotal+((customerorderproductlist.get(i).getProductamount()/1000)*(customerorderproductlist.get(i).getProductsquantity()));
            }
        }
        grandsum=grandtotal;
        grandtotal=0;
        return grandsum;
    }




}
