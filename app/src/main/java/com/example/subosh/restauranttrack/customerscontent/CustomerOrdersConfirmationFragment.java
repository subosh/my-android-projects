package com.example.subosh.restauranttrack.customerscontent;

import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.subosh.restauranttrack.R;
import com.example.subosh.restauranttrack.ownercontent.OrderSummaryListFragment;
import com.example.subosh.restauranttrack.ownercontent.OrdersSummaryAdapter;
import com.example.subosh.restauranttrack.ownercontent.OrdersSummaryPojo;
import com.example.subosh.restauranttrack.ownercontent.OwnerInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import static com.example.subosh.restauranttrack.customerscontent.CustomerViewOwnerProductfragment.getCustomerproductslist;

public class CustomerOrdersConfirmationFragment extends Fragment implements View.OnClickListener {
    CustomerViewOwnerProductfragment customerViewOwnerProductfragment;
    ArrayList<CustomerOrdersPojo> customerorderproductlist = new ArrayList<CustomerOrdersPojo>();
    RecyclerView cartlistrecyclerview;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    Toolbar toolbar;
    TextView textView;
    Button continueshoppingbutton, confirmorderbutton;
    DatabaseReference customerordersDatabaseReferencecustomerview, customerordersDatabaseReferenceownersview,customerordersDatabaseReferenceownersview1;
    CustomerCartViewAdapter customerCartViewAdapter;
    FirebaseUser user;
    String customername, marketname;
    String productaddingtime,productaddingdate,productId;
    String productIdGeneration;
    String code;
    String productIdArray[];
    CustomerInformation customerInformation=new CustomerInformation();
    ArrayList<CustomerInformation> customerInformationArrayList=new ArrayList<>();
    ArrayList<Map<String,Double>> customerCoordinatesList=new ArrayList<Map<String, Double>>();
    Map<String,Double> customercoordinatesMap=new HashMap<>();
    FirebaseAuth firebaseAuth;
    final boolean[] checker = {false};
TextView grandtotaltextview;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_order_confirmation_fragment, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        //toolbar = view.findViewById(R.id.toolbar);
        grandtotaltextview=view.findViewById(R.id.grand_total_textview);
        continueshoppingbutton = view.findViewById(R.id.continue_shopping_button);
        confirmorderbutton = view.findViewById(R.id.confirm_order_button);
        cartlistrecyclerview = view.findViewById(R.id.cartlist_recycler);
        cartlistrecyclerview.setHasFixedSize(true);
        cartlistrecyclerview.setLayoutManager(new LinearLayoutManager(this.getContext()));
        //textView.setOnClickListener(this);
        continueshoppingbutton.setOnClickListener(this);
        confirmorderbutton.setOnClickListener(this);
        CustomerViewOwnerProduct customerViewOwnerProduct = (CustomerViewOwnerProduct) getActivity();
        customerViewOwnerProduct.setSupportActionBar(toolbar);
        customerViewOwnerProduct.setTitle("Cart list");
        getCustomerDetailFromFirebase();
        marketname = customerViewOwnerProduct.getMarketName();
        customername = CustomerViewOwnerProductfragment.getCustomername();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        customerorderproductlist = getCustomerproductslist();
        customerCartViewAdapter = new CustomerCartViewAdapter(getContext(), customerorderproductlist,0);
        cartlistrecyclerview.setAdapter(customerCartViewAdapter);
        customerCartViewAdapter.notifyDataSetChanged();
        final Handler handler=new Handler();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        grandtotaltextview.setText("" + CustomerCartViewAdapter.getGrandTotal());

                    }
                });

            }
        };
        new Thread(runnable).start();

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == confirmorderbutton) {
            if (!getCustomerproductslist().isEmpty()) {
                float total=CustomerCartViewAdapter.grandsum;
                String productId=getProductId();
                storeCustomerOrderstoCustomerView(total,productId,getProductaddingdate(),getProductaddingtime());
                storeCustomerOrderstoOwnersView(productId,getProductaddingdate(),getProductaddingtime());


            } else {
                Toast.makeText(getContext(), "Please First Select product For Order Confirmation", Toast.LENGTH_SHORT).show();
            }

        }
        if (v == continueshoppingbutton) {
            initializecustomerviewOwnerProducstfragment();
        }
    }

    public void initializecustomerviewOwnerProducstfragment() {
        customerViewOwnerProductfragment = new CustomerViewOwnerProductfragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.customer_view_owner_products_frame, customerViewOwnerProductfragment);
        fragmentTransaction.commit();
    }
    public String getProductId() {
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat savecurrentdate=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        productaddingdate= savecurrentdate.format(calendar.getTime());
        SimpleDateFormat savecurrenttime=new SimpleDateFormat("HH:mm:ss a", Locale.getDefault());
        SimpleDateFormat savecurrenttimeforproductId=new SimpleDateFormat("HH:mm:ss ", Locale.getDefault());
        productaddingtime= savecurrenttime.format(calendar.getTime());
        productIdGeneration=savecurrenttimeforproductId.format(calendar.getTime());
        Random random = new Random();
        int n = 1000 + random.nextInt(9000);
        code= String.valueOf(n);
        productIdArray=productIdGeneration.split(":");
        productId=code+productIdArray[0]+productIdArray[1]+productIdArray[2];
        return productId;
    }

    public String getProductaddingdate() {
        return productaddingdate;
    }

    public String getProductaddingtime() {
        return productaddingtime;
    }
    public String[] getMyTime(){
        return productIdArray;
    }
    public void storeCustomerOrderstoCustomerView(final float total, final String productId, final String productaddingdate, final String productaddingtime) {
        customerordersDatabaseReferencecustomerview = FirebaseDatabase.getInstance().getReference("CUSTOMERS");

       final DatabaseReference databaseReference=customerordersDatabaseReferencecustomerview.child(user.getUid()).child(customername).child("ORDERSLIST")
               .child(marketname).push();

        databaseReference.child("PRODUCTNAME").setValue(customerorderproductlist).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    //customerordersDatabaseReferencecustomerview.child(user.getUid()).child(customername).child("ORDERSLIST").child(marketname).child("PRODUCTNAME").child("ORDEREDGRANDTOTAL").setValue(customerCartViewAdapter.getGrandTotal());
                    databaseReference.child("ORDEREDDATE").setValue(productaddingdate);
                    databaseReference.child("ORDEREDTIME").setValue(productaddingtime);
                    databaseReference.child("PRODUCTID").setValue(productId);
                    databaseReference.child("ORDEREDAMOUNT").setValue(total);
                    databaseReference.child("DELIVERYADDRESS").setValue(getCustomerDetailsList());
                    databaseReference.child("ORDERSTATUS").setValue("ORDERED");
                    Toast.makeText(getContext(), "Succefully You ordered", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_LONG).show();
                }
            }

        });
    }
    public void getCustomerDetailFromFirebase(){

        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("CUSTOMERS");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    customerInformation=ds.getValue(CustomerInformation.class);
                    double customerLatitude= ds.child("CustomerCoordinates").child("latitude").getValue(Double.class);
                    double customerLongitude= ds.child("CustomerCoordinates").child("longitude").getValue(Double.class);
                    if (!checker[0] &&customerInformation.getCustomername().equals(customername)) {
                        customercoordinatesMap.put("customerLatitude",customerLatitude);
                        customercoordinatesMap.put("customerLongitude",customerLongitude);
                        customerCoordinatesList.add(customercoordinatesMap);
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
    public ArrayList<CustomerInformation> getCustomerDetailsList(){
        return customerInformationArrayList;
    }
    public ArrayList<Map<String,Double>> getCustomerCoordinatesList(){
        return customerCoordinatesList;
    }


    public void storeCustomerOrderstoOwnersView(final String productId, final String productaddingdate, final String productaddingtime) {
        customerordersDatabaseReferenceownersview = FirebaseDatabase.getInstance().getReference("OWNERS");
        customerordersDatabaseReferenceownersview.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    OwnerInformation ownerinformation = ds.getValue(OwnerInformation.class);
                    if (!getCustomerproductslist().isEmpty()&&ownerinformation.getOwnername().equals(marketname)) {
                        DatabaseReference customersorders=customerordersDatabaseReferenceownersview.child(ds.getKey()).child("CUSTOMERSORDERS").child(customername).push();
                        customersorders.child("ORDEREDDATE").setValue(productaddingdate);
                        customersorders.child("ORDEREDTIME").setValue(productaddingtime);
                        customersorders.child("PRODUCTID").setValue(productId);
                        customersorders.child("DELIVERYADDRESS").setValue(getCustomerDetailsList());
                        customersorders.child("CUSTOMERLOCATIONCOORDINATES").setValue(getCustomerCoordinatesList());
                        customersorders.child("PRODUCTNAME").setValue(customerorderproductlist);
                        customersorders.child("ORDEREDAMOUNT").setValue(CustomerCartViewAdapter.getGrandTotal());
                        customersorders.child("ORDERSTATUS").setValue("ORDERED");
                        getCustomerproductslist().clear();
                        customerCartViewAdapter.notifyDataSetChanged();
                        CustomerCartViewAdapter.grandsum=(float)0.0;
                        grandtotaltextview.setText("0.0");



                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }


}


