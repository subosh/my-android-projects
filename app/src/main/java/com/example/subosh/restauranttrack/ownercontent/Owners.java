package com.example.subosh.restauranttrack.ownercontent;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.subosh.restauranttrack.MarketOwners;
import com.example.subosh.restauranttrack.admincontent.PreferenceUtils;
import com.example.subosh.restauranttrack.customerscontent.CustomerInformation;
import com.example.subosh.restauranttrack.customerscontent.CustomerOrderHistoryActivity;
import com.example.subosh.restauranttrack.customerscontent.CustomersViewPagerAdapter;
import com.example.subosh.restauranttrack.customerscontent.OwnerListFragment;
import com.example.subosh.restauranttrack.startpagecontent.MainActivity;
import com.example.subosh.restauranttrack.R;
import com.example.subosh.restauranttrack.startpagecontent.MainViewPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.app.PendingIntent.getActivity;

public class Owners extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    AdapterUsers adapterUsers;
    ArrayList<CustomerInformation> list1;
    ArrayAdapter<String> adapter;
    FirebaseDatabase firebaseDatabase;
    ListView listView;
    //FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    CustomerInformation customerInformation;
OwnerListFragment ownerListFragment;
String customername;
ArrayList<CustomerInformation> customerInformationArrayList;
CustomersViewPagerAdapter customersViewPagerAdapter;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owners);
        Intent myintent=getIntent();
        if(myintent.getStringExtra("customername")!=null){
            customername=myintent.getStringExtra("customername");
            customerInformationArrayList=(ArrayList<CustomerInformation>) myintent.getSerializableExtra("customerDetailsList");
        }
        else {
            customername= PreferenceUtils.getName(this);
        }
        intializeviews();
       // initializeshopownerslistfragment();
        }
    public void intializeviews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = findViewById(R.id.customers_view_pager);
        customersViewPagerAdapter = new CustomersViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(customersViewPagerAdapter);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
    public String getCustomername(){
        return customername;
    }

    public ArrayList<CustomerInformation> getCustomerDetailsList() {
        return customerInformationArrayList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.customer_order_history_menu){
            Intent intent=new Intent(Owners.this, CustomerOrderHistoryActivity.class);
            intent.putExtra("customername",customername);
            startActivity(intent);
        }
        if (id==R.id.customer_logout){
//            Intent intent=new Intent(Owners.this,MarketOwners.class);
//            startActivity(intent);
//            this.finish();
            initializeLogoutConfirmationDialog();
        }

        return super.onOptionsItemSelected(item);
    }
    public void initializeLogoutConfirmationDialog(){
        final String No="NO";
        AlertDialog.Builder builder= new AlertDialog.Builder(Owners.this,R.style.Theme_AppCompat_DayNight_Dialog_Alert);

        builder.setMessage("Logging Out From Your Account?")
                .setTitle("Next Time You need To Log in again newly")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, final int which) {
                        firebaseAuth=FirebaseAuth.getInstance();
                        firebaseAuth.signOut();
                        PreferenceUtils.saveName("",Owners.this);
                        PreferenceUtils.saveLoginType("",Owners.this);
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}