package com.example.subosh.restauranttrack.admincontent;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.subosh.restauranttrack.MarketOwners;
import com.example.subosh.restauranttrack.R;
import com.example.subosh.restauranttrack.ownercontent.Owners;
import com.example.subosh.restauranttrack.ownercontent.ownerproducts;
import com.example.subosh.restauranttrack.startpagecontent.MainActivity;
import com.example.subosh.restauranttrack.startpagecontent.MainViewPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
public class admin extends AppCompatActivity {
//    Toolbar toolbar;
    static String productlistarray[];
    DatabaseReference databaseReference;
    public static boolean[] checkeditems;
    ArrayList<Integer> itemarraylist = new ArrayList<>();
    AddProductFragment addProductFragment;
    ArrayList<String> getOwnerproductslist=new ArrayList<>();
    OrderMaintanenceFragment orderMaintanenceFragment;
    static ArrayList<String> dummylist = new ArrayList<>();
    int len;
    private  Async task;
    String adminname;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private AdminViewPagerAdapter adminViewPagerAdapter;
    private TabLayout tabLayout;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Intent myintent=getIntent();
        if (myintent.getStringExtra("ADMINNAME")!=null){
            adminname=myintent.getStringExtra("ADMINNAME");
        }
        else {
            adminname=PreferenceUtils.getName(this);
        }
       // firebaseAuth=FirebaseAuth.getInstance();
//        authStateListener=new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                if (firebaseAuth.getCurrentUser()!=null){
//                    firebaseUser=firebaseAuth.getCurrentUser();
//
//                }
//            }
//        };
//        Toast.makeText(getApplicationContext(),firebaseUser.getUid(),Toast.LENGTH_LONG).show();
        initializeviews();
        databaseReference = FirebaseDatabase.getInstance().getReference("WHOLE_PRODUCTS");
        startTask();

        }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
    }

    private void initializeviews() {
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager=findViewById(R.id.admin_view_pager);
        adminViewPagerAdapter=new AdminViewPagerAdapter(getSupportFragmentManager(),adminname);
        viewPager.setAdapter(adminViewPagerAdapter);
        tabLayout=findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
    public  String getAdminname(){
        return adminname;
    }
        public void initializefragment() {
        addProductFragment = new AddProductFragment();
            final FragmentManager fragmentManager=getSupportFragmentManager();
            addProductFragment.show(fragmentManager,"Add_products_as_wish");
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.product_adding_menu, menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        productlistarray =traversedummlist();

        int id = item.getItemId();
        if (id == R.id.menu_add_products) {
            AlertDialog.Builder builder = new AlertDialog.Builder(admin.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
            builder.setTitle("List Of products");
            builder.setMultiChoiceItems(productlistarray, checkeditems, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                    if (isChecked) {
                        if (!itemarraylist.contains(position)) {
                            getOwnerproductslist.add(productlistarray[position]);
                            itemarraylist.add(position);

                        } else if(itemarraylist.contains(position)){
                            getOwnerproductslist.remove(productlistarray[position]);
                            itemarraylist.remove(position);

                        }
                    }
                }

            });
//            builder.setCancelable(false);
//            builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Toast.makeText(admin.this, "succesfullyaddded", Toast.LENGTH_SHORT).show();
//
//
//                }
//            });
//            builder.setNegativeButton("Clear All", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int position) {
//
//                        for (int i = 0; i < checkeditems.length; i++) {
//                            checkeditems[i] = false;
//                            itemarraylist.clear();
//
//                    }
//                }
//            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        if (id == R.id.menu_home) {
            Intent intent=new Intent(this,AdminDeliveredOrderHistoryActivity.class);
            startActivity(intent);
        }
        if (id == R.id.menu_add_products_wish) {
            initializefragment();
        }
        if (id==R.id.menu_admin_logogout){
//            Intent intent=new Intent(admin.this,MarketOwners.class);
//            startActivity(intent);
//            this.finish();
            initializeLogoutConfirmationDialog();
        }
        return super.onOptionsItemSelected(item);
    }
    public void initializeLogoutConfirmationDialog(){
        final String No="NO";
        AlertDialog.Builder builder= new AlertDialog.Builder(admin.this,R.style.Theme_AppCompat_DayNight_Dialog_Alert);

        builder.setMessage("Logging Out From Your Account?")
                .setTitle("Next Time You need To Logging again newly")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, final int which) {
                        firebaseAuth=FirebaseAuth.getInstance();
                        firebaseAuth.signOut();
                        PreferenceUtils.saveName("",admin.this);
                        PreferenceUtils.saveLoginType("",admin.this);
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
    public Void getWholeproductslists() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dummylist.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String testString = ds.child("productname").getValue(String.class);
                    if (ds.child("productname").getValue()!=null)
                    {
                        dummylist.add(testString);
                        System.out.println(dummylist);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return null;
    }
    public  String[] traversedummlist() {
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
    class Async extends AsyncTask<Void, Void, Void> {
        @Override
        public Void doInBackground(Void... voids) {
            return getWholeproductslists();
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
            }
    }
    public void startTask() {
        task = new Async();
        task.execute();


    }

}
