package com.example.subosh.restauranttrack.customerscontent;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.subosh.restauranttrack.R;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class CustomerMonthlyListFragment extends Fragment implements View.OnClickListener {
    String customername,marketname;
    CustomerMonthlyListAdapter customerMonthlyListAdapter;
    RecyclerView recyclerView;
    DatabaseReference monthlyListDatabaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    CustomerOrdersPojo customerOrdersPojo;
    DatabaseReference customerordersDatabaseReferencecustomerview;
    ArrayList<CustomerMonthlyListPojo> customerMonthlyListPojoArrayList=new ArrayList<>();
    ArrayList<CustomerOrdersPojo> orginalcustomerProductList=new ArrayList<CustomerOrdersPojo>();
    ArrayList<CustomerInformation> customerInformationArrayList=new ArrayList<>();
    ArrayList<Map<String,Double>> customerCoordinatesList=new ArrayList<Map<String, Double>>();
    Map<String,Double> customercoordinatesMap=new HashMap<>();
    ArrayList<String> productnamearraylist=new ArrayList<>();
    ArrayList<String> getproduct=new ArrayList<>();
ProgressDialog progressDialog;
    View view;
   Button placeOrderMylistButton,showTotalButton;
   CustomerMonthlyListPojo customerMonthlyListPojo;
    Toolbar toolbar;
    CustomerInformation customerInformation;
    final boolean[] checker = {false};
String productaddingdate;
    String productaddingtime,productIdGeneration;
    String code;
    String productId,productIdArray[];
    Date checkTime;
   static TextView setGrandTotalTextview;
    boolean check;
    Float grandTotal=(float)0;
    Float grandSum;
    ArrayList<String> dummyproductslist;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view=inflater.inflate(R.layout.customer_monthly_list_fragment,container,false);
        initializeViews();

        return view;
    }
    public void initializeViews(){
        toolbar=view.findViewById(R.id.toolbar);
        placeOrderMylistButton=view.findViewById(R.id.place_order_monthly_list);
        //showTotalButton=view.findViewById(R.id.show_total_button);
        setGrandTotalTextview=view.findViewById(R.id.setGrandTotal);
        placeOrderMylistButton.setOnClickListener(this);
        progressDialog=new ProgressDialog(this.getContext());
        //showTotalButton.setOnClickListener(this);
        CustomerCreateListActivity customerCreateListActivity=(CustomerCreateListActivity) getActivity();
        customerCreateListActivity.setSupportActionBar(toolbar);
        customername=customerCreateListActivity.getCustomerNameCreateList();
        marketname=customerCreateListActivity.getMarketNameCreateList();
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseDatabase= FirebaseDatabase.getInstance();
        checkProductAvailabilityFromCustomer();
        customerCreateListActivity.setTitle("My Grocery List");
        getCustomerDetailFromFirebase();
        dummyproductslist=CustomerViewCreateListProductsAdapter.getProductnameArraylist();
        getCustomerMonthlyListFromFirebase();
        recyclerView=view.findViewById(R.id.customer_monthly_list_recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
      //  setGrandTotalTextview.setText(""+CustomerMonthlyListAdapter.getGrandTotal());


    }

    @Override
    public void onResume() {
        super.onResume();


    }

    public void getCustomerMonthlyListFromFirebase(){

        monthlyListDatabaseReference = firebaseDatabase.getReference().child("CUSTOMERS");
        monthlyListDatabaseReference.child(firebaseUser.getUid()).child(customername).child("MYLIST").child(marketname).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customerMonthlyListPojoArrayList.clear();
                orginalcustomerProductList.clear();
              //  checkProductAvailabilityFromCustomer();
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    String ordersNodeString=ds.getKey();
                    check= false;
                    GenericTypeIndicator<ArrayList<CustomerOrdersPojo>> t1 = new GenericTypeIndicator<ArrayList<CustomerOrdersPojo>>() {};
                    customerOrdersPojo= ds.child("PRODUCTNAME").getValue(t1).get(0);
                    String productName=customerOrdersPojo.getProductname();
                    for (int i=0;i<dummyproductslist.size();i++){
                        if (dummyproductslist.get(i).equals(productName))
                            {
                              check=true;
                              break;
                            }
                        }
                        if(ds.child("PRODUCTNAME").getValue()!=null)
                        {
                        orginalcustomerProductList.add(customerOrdersPojo);
                    customerMonthlyListPojo=new CustomerMonthlyListPojo(ordersNodeString,check);
                    customerMonthlyListPojoArrayList.add(customerMonthlyListPojo);
                    }
                }
                    customerMonthlyListAdapter=new CustomerMonthlyListAdapter(getActivity(),orginalcustomerProductList,customerMonthlyListPojoArrayList,customername,marketname,(float)0);
                customerMonthlyListAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(customerMonthlyListAdapter);
               // recyclerView.smoothScrollToPosition(customerMonthlyListPojoArrayList.size());


                }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"error in showing profiles"+" "+databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }
    public ArrayList<String> checkProductAvailabilityFromCustomer(){
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseDatabase= FirebaseDatabase.getInstance();
         final DatabaseReference monthlyListDatabaseReference = firebaseDatabase.getReference().child("OWNERS");
        monthlyListDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    OwnerInformation ownerInformation=dataSnapshot1.getValue(OwnerInformation.class);
                    String ownernode=dataSnapshot1.getKey();
                    if(ownerInformation.getOwnername().equals(marketname))
                    {
                  getproduct=getproductaFromFirebase(ownernode,monthlyListDatabaseReference);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
return getproduct;
    }
    public ArrayList<String> getproductaFromFirebase(String ownernode, DatabaseReference monthlyListDatabaseReference){
        monthlyListDatabaseReference.child(ownernode).child("MARKETPRODUCTS").child(marketname).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String checkproductname = snapshot.child("productname").getValue(String.class);
                    productnamearraylist.add(checkproductname);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return productnamearraylist;
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

    public void storeCustomerOrderMonthlyListToFirebase(final String productId, final String productaddingdate, final String productaddingtime){
        final boolean[] checker1 = {false};
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
    firebaseDatabase= FirebaseDatabase.getInstance();
    monthlyListDatabaseReference = firebaseDatabase.getReference().child("OWNERS");

        if(getCheckedMonthlyList().isEmpty()){
        Toast.makeText(getContext(),"Please Select Items To Make Order",Toast.LENGTH_LONG).show();
        return;
    }
    monthlyListDatabaseReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot ds:dataSnapshot.getChildren()) {
                OwnerInformation ownerinformation = ds.getValue(OwnerInformation.class);
                if (!getCheckedMonthlyList().isEmpty() && !checker1[0]&&ownerinformation.getOwnername().equals(marketname)) {
                    final DatabaseReference customersorders = monthlyListDatabaseReference.child(ds.getKey()).child("CUSTOMERSORDERS")
                            .child(customername).push();
                    customersorders.child("PRODUCTID").setValue(productId);
                    customersorders.child("ORDEREDDATE").setValue(productaddingdate);
                    customersorders.child("ORDEREDTIME").setValue(productaddingtime);
                    customersorders.child("DELIVERYADDRESS").setValue(getCustomerDetailsList());
                    customersorders.child("CUSTOMERLOCATIONCOORDINATES").setValue(getCustomerCoordinatesList());
                    customersorders.child("PRODUCTNAME").setValue(getCheckedMonthlyList());
                    customersorders.child("ORDEREDAMOUNT").setValue(CustomerMonthlyListAdapter.getGrandTotal());
                    customersorders.child("ORDERSTATUS").setValue("ORDERED");
                    checker1[0]=true;



                }
            }


        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getContext(),"error in showing profiles"+" "+databaseError.getMessage(),Toast.LENGTH_LONG).show();
        }
    });

}

    public Float getGrandTotal() {
        for (CustomerOrdersPojo customerOrdersPojo:customerMonthlyListAdapter.checkedMonthlyList){
            grandTotal=grandTotal+customerOrdersPojo.getProductamount();

        }
        grandSum=grandTotal;
        grandTotal=(float)0;
        return grandSum;
    }

    public ArrayList<CustomerOrdersPojo> getCheckedMonthlyList(){
        return customerMonthlyListAdapter.checkedMonthlyList;
}
public ArrayList<String> getProductnamearraylist(){
        return productnamearraylist;
}
    @Override
    public void onClick(View v) {
        if(v==placeOrderMylistButton) {
            createDialogBoxForOrderConfirmation();

        }

    }
    public void createDialogBoxForOrderConfirmation(){
        final String Yes="YES";
        final String No="NO";
        AlertDialog.Builder builder= new AlertDialog.Builder(getContext(),R.style.Theme_AppCompat_DayNight_Dialog_Alert);

        builder.setMessage("Order Confirmation")
                .setTitle("Do You Want To Place Your Order?")
                .setPositiveButton(Yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, final int which) {
                        String productId=getProductId();
                        storeCustomerOrderMonthlyListToFirebase(productId,getProductaddingdate(),getProductaddingtime());
                        storeCustomerOrderstoCustomerView(productId,getProductaddingdate(),getProductaddingtime(),getMyTime());

                    }
                });
        builder.setNegativeButton(No, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                       //dialog.dismiss();
                    }
                }
        );

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public String getProductId() {
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat savecurrentdate=new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
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
    public void storeCustomerOrderstoCustomerView(final String productId, final String productaddingdate, final String productaddingtime, final String[] myTime) {
        customerordersDatabaseReferencecustomerview = FirebaseDatabase.getInstance().getReference("CUSTOMERS");

        final DatabaseReference databaseReference=customerordersDatabaseReferencecustomerview.child(firebaseUser.getUid()).child(customername).child("ORDERSLIST")
                .child(marketname).push();
progressDialog.setMessage("Please Wait We are Making Your Order To Shop...Wait");
progressDialog.show();
        databaseReference.child("PRODUCTNAME").setValue(getCheckedMonthlyList()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    databaseReference.child("ORDEREDDATE").setValue(productaddingdate);
                    databaseReference.child("ORDEREDTIME").setValue(productaddingtime);
                    databaseReference.child("ORDEREDAMOUNT").setValue(CustomerMonthlyListAdapter.getGrandTotal());
                    databaseReference.child("DELIVERYADDRESS").setValue(getCustomerDetailsList());
                    databaseReference.child("ORDERSTATUS").setValue("ORDERED");
                    databaseReference.child("PRODUCTID").setValue(productId);
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Successfully You ordered", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
