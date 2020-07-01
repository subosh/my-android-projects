package com.example.subosh.restauranttrack.ownercontent;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.subosh.restauranttrack.MarketOwners;
import com.example.subosh.restauranttrack.admincontent.PreferenceUtils;
import com.example.subosh.restauranttrack.admincontent.ProductlistSinglenton;
import com.example.subosh.restauranttrack.startpagecontent.MainActivity;
import com.example.subosh.restauranttrack.startpagecontent.MainFragment;
import com.example.subosh.restauranttrack.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class ownerproducts extends AppCompatActivity {
    static DatabaseReference marketproductsdatabaseReference,ordersreference,marketproductsdatabaseReferencepush,wholeproductsdatabasereference,customerordersDatabaseReference;
    FirebaseDatabase firebaseDatabase;
    //FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    static String[] productlistarray,productImageListarray;
    String marketname,productaddingtime,productaddingdate,productId;
    Pair<String[],boolean[]> productlistarray1;
    View customLayout;
    String productName,productPrice;
    OwnerProductsPojo ownerProductsPojo;
    //MainFragment mainFragment;
    ListView listView;
    public static boolean[] checkeditems;
    ArrayList<Integer> itemarraylist = new ArrayList<>();
    static ArrayList<String> dummylist = new ArrayList<String>();
    static ArrayList<String> productImageDownloadPathList = new ArrayList<String>();
    ArrayList<OwnerInformation> ownerInformationArrayList=new ArrayList<>();
    ArrayList<ProductlistSinglenton> getOwnerproductslist=new ArrayList<ProductlistSinglenton>();;
    OwnerProductsFragment ownerProductsFragment;
    OwnerViewCustomerOrdersFragment ownerViewCustomerOrdersFragment;
    OwnerOrderHistoryFragment ownerOrderHistoryFragment;
    int len,productimagepathlength;
    RecyclerView shopproductsrecyclerview;
    String test;
    ProductlistSinglenton productlistSinglenton;
    EditText setprice;
    //FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference ownerDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ownerproducts);
        wholeproductsdatabasereference = FirebaseDatabase.getInstance().getReference("WHOLE_PRODUCTS");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        Intent myintent = getIntent();
        if (myintent.getStringExtra("MARKETNAME") != null) {
            marketname = myintent.getStringExtra("MARKETNAME");
        } else {
            marketname = PreferenceUtils.getName(this);
        }
        getOwnerDetailsFromFirebase();
        marketproductsdatabaseReference = FirebaseDatabase.getInstance().getReference("OWNERS");
        marketproductsdatabaseReferencepush = marketproductsdatabaseReference.push();
        starttask();
        initilizefragment();
    }
    public void initilizefragment(){
        ownerProductsFragment=new OwnerProductsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.owner_products_frame,ownerProductsFragment)
                .commit();
    }

    public static Void getWholeproductslists() {

        wholeproductsdatabasereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dummylist.clear();
                productImageDownloadPathList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String testString =ds.child("productname").getValue(String.class);
                    String downloadpath=ds.child("productImageDownloadPath").getValue(String.class);
                    if (ds.child("productname").getValue()!=null){
                        dummylist.add(testString);
                        productImageDownloadPathList.add(downloadpath);
                        System.out.println(dummylist);
                    } }
                    }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
return  null;
    }
    public static ArrayList<String> getDummylist(){
        return dummylist;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ownerside_menu, menu);
        return true;

    }

    @Override
    public void onBackPressed() {
        Fragment ownerordersfragment=getSupportFragmentManager().findFragmentByTag("ownerordersfragment");
        Fragment ownerdeliveredorderhistoryfragment =getSupportFragmentManager().findFragmentByTag("orderHistoryFragment");
        if(ownerordersfragment!=null||ownerdeliveredorderhistoryfragment!=null){
            super.onBackPressed();
        }
    }
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        productlistarray=traversedummlist();
        productImageListarray=traverseProductImageList();
        int id = item.getItemId();
        if (id == R.id.ownerside_menu_add_products) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ownerproducts.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
            builder.setTitle("List Of products");
            builder.setSingleChoiceItems( productlistarray,-1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, final int position) {
                   productlistSinglenton= new ProductlistSinglenton(productlistarray[position],Float.parseFloat("0"),productImageListarray[position]);
                   getOwnerproductslist.add(productlistSinglenton);

                          }});
            builder.setCancelable(false);
            builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    addproducttocatalogtodatabase();
                    getOwnerproductslist.clear();


                }
            });
            builder.setNegativeButton("Clear Product", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int position) {
                    getshopprofileproducts();
                    }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        if (id == R.id.ownerside_delivery_request) {
            createDialogBoxForDeliveryRequest();
        }




        if (id == R.id.ownerside_getOrder_details) {
            initilizeOwnerViewCustomerOrdersFragment();
        }
        if(id==R.id.owner_logout){
//            Intent intent=new Intent(ownerproducts.this,MarketOwners.class);
//            startActivity(intent);
//            this.finish();
            initializeLogoutConfirmationDialog();
        }
        if (id==R.id.ownerside_getOrder_history)
        {
     initializeOwnerOrderHistoryFragment();
        }
        return super.onOptionsItemSelected(item);
    }
    public void initializeLogoutConfirmationDialog(){
        final String No="NO";
        AlertDialog.Builder builder= new AlertDialog.Builder(ownerproducts.this,R.style.Theme_AppCompat_DayNight_Dialog_Alert);

        builder.setMessage("Next Time You need To Log in again newly")
                .setTitle("Logging Out From Your Account?")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, final int which) {
                        firebaseAuth=FirebaseAuth.getInstance();
                        firebaseAuth.signOut();
                        PreferenceUtils.saveName("",ownerproducts.this);
                        PreferenceUtils.saveLoginType("",ownerproducts.this);
                        finish();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        Toast.makeText(getApplicationContext(),"Successfully You Logged Out",Toast.LENGTH_SHORT).show();

                    }
                });
        builder.setCancelable(false);
        builder.setNegativeButton(No, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        dialog.dismiss();
                    }
                }
        );

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public ArrayList<OwnerInformation> getOwnerDetailsFromFirebase(){
        firebaseUser=firebaseAuth.getCurrentUser();
        customerordersDatabaseReference= FirebaseDatabase.getInstance().getReference("OWNERS");
        customerordersDatabaseReference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                OwnerInformation ownerInformation=dataSnapshot.getValue(OwnerInformation.class);
                ownerInformationArrayList.add(ownerInformation);
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

    public void initilizeOwnerViewCustomerOrdersFragment(){
        ownerViewCustomerOrdersFragment=new OwnerViewCustomerOrdersFragment();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.owner_products_frame,ownerViewCustomerOrdersFragment,"ownerordersfragment");
        fragmentTransaction.addToBackStack("ownerordersfragment");
        fragmentTransaction.commit();
    }
    public void initializeOwnerOrderHistoryFragment(){
        ownerOrderHistoryFragment=new OwnerOrderHistoryFragment();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.owner_products_frame,ownerOrderHistoryFragment,"orderHistoryFragment");
        fragmentTransaction.addToBackStack("orderHistoryFragment");
        fragmentTransaction.commit();

    }
public void createDialogBoxForDeliveryRequest(){
    final String Yes="YES";
    final String No="NO";
        AlertDialog.Builder builder= new AlertDialog.Builder(this,R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        builder.setMessage("If you Don't want Admins to TakeCare of Your Orders to Deliver To Your Customers,..If You are Ok then Press 'NO' (your orders will be restricted from admin) orElse Press 'YES'")
            .setTitle("Delivery Request")
            .setPositiveButton(Yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, final int which) {
                   setOrderReaquesToFireBase(Yes);
                }
            });
    builder.setNegativeButton(No, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int position) {
            setOrderReaquesToFireBase(No);
        }
    }
    );
            builder.setCancelable(false);
    AlertDialog dialog = builder.create();
    dialog.show();
}
public void setOrderReaquesToFireBase(final String deliveryRequestStatus){
    final boolean[] checkFordata = {false};
    marketproductsdatabaseReference=FirebaseDatabase.getInstance().getReference("OWNERS");
//OwnerInformation ownerInformation=new OwnerInformation();
    marketproductsdatabaseReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot ds:dataSnapshot.getChildren()) {
                String ownerRootNode=ds.getKey();
                OwnerInformation ownerInformation=ds.getValue(OwnerInformation.class);
                if(!checkFordata[0] &&ownerInformation.getOwnername().equals(marketname)){
                    //ownerInformation.setOwnerDeliveryRequestStatus(deliveryRequestStatus);
                    marketproductsdatabaseReference.child(ownerRootNode).child("ownerDeliveryRequestStatus").setValue(deliveryRequestStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isComplete()){
                                Toast.makeText(getApplicationContext(),"Successfully You updated Delivery Request ",Toast.LENGTH_LONG).show();
                                checkFordata[0] =true;
                            }
                        }
                    });

                    }
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getBaseContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
        }
    });
}
    public String getMarketName(){
return marketname;
    }
    public void addproducttocatalogtodatabase(){
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        //ownerProductsPojo=new OwnerProductsPojo(getOwnerproductslist);
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat savecurrentdate=new SimpleDateFormat("dd/MM/yyyy");
        productaddingdate= savecurrentdate.format(calendar.getTime());
        SimpleDateFormat savecurrenttime=new SimpleDateFormat("HH:mm:ss a");
        productaddingtime= savecurrenttime.format(calendar.getTime());
        productId=productaddingdate+" "+productaddingtime;
//        HashMap<String,Object> cart= new HashMap<>();
//        cart.put("productName",getOwnerproductslist);
//      //  cart.put("price",productPrice);
        Random random = new Random();

        int n = 100000 + random.nextInt(900000);
        //code= String.valueOf(n);

        marketproductsdatabaseReference.child(firebaseUser.getUid()).child("MARKETPRODUCTS").child(marketname).push().setValue(productlistSinglenton).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
           if (task.isComplete())
           {
               Toast.makeText(getBaseContext(), "Successfully you Added this product  to your shop profile", Toast.LENGTH_SHORT).show();

           }
           else {
               Toast.makeText(getBaseContext(),task.getException().toString(),Toast.LENGTH_LONG).show();
           }
            }
        });
        //productlistSinglenton=new ProductlistSinglenton();
    }
    public String[] traversedummlist() {
        len = dummylist.size();

        productlistarray = new String[len];
        if(checkeditems==null){
            checkeditems=new boolean[productlistarray.length];
        }
        for (int i = 0; i < len; i++) {
            productlistarray[i] = dummylist.get(i);
        }

        return productlistarray;
    }
    public String[] traverseProductImageList(){
        productimagepathlength = productImageDownloadPathList.size();

        productImageListarray = new String[productimagepathlength];
        if(checkeditems==null){
            checkeditems=new boolean[productImageListarray.length];
        }
        for (int i = 0; i < productimagepathlength; i++) {
            productImageListarray[i] = productImageDownloadPathList.get(i);
        }

        return productImageListarray;
    }
    public boolean[] checkbox(){
        return checkeditems;
    }
    private void getshopprofileproducts(){

        marketproductsdatabaseReference=FirebaseDatabase.getInstance().getReference("OWNERS");

        marketproductsdatabaseReference.child(firebaseUser.getUid()).child("MARKETPRODUCTS").child(marketname).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Boolean ischeckedclearcartproduct=false;
                for (DataSnapshot ds:dataSnapshot.getChildren()) {
                    if (!getOwnerproductslist.isEmpty()&&getOwnerproductslist.get(0).getProductname().equals(ds.child("productname").getValue())) {
                        marketproductsdatabaseReference.child(firebaseUser.getUid()).child("MARKETPRODUCTS").child(marketname).child(ds.getKey()).removeValue();
                        getOwnerproductslist.clear();
                        }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getBaseContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }
class Async extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... Params) {
        return getWholeproductslists();
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