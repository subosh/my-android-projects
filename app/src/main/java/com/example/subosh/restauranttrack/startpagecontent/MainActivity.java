package com.example.subosh.restauranttrack.startpagecontent;

import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.subosh.restauranttrack.MainClass;
import com.example.subosh.restauranttrack.R;
import com.example.subosh.restauranttrack.admincontent.AdminSignInActivity;
import com.example.subosh.restauranttrack.admincontent.admin;
import com.example.subosh.restauranttrack.customerscontent.UsersFragment;
import com.example.subosh.restauranttrack.ownercontent.OwnerMainActivity;
import com.example.subosh.restauranttrack.ownercontent.Owners;

public class MainActivity extends AppCompatActivity {

private Toolbar toolbar;
private ViewPager viewPager;
private MainViewPagerAdapter mainViewPagerAdapter;
private TabLayout tabLayout;
private UsersFragment  usersFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intializeviews();
        initializeMainFragment();
        }
        public void intializeviews(){
toolbar=findViewById(R.id.toolbar);
setSupportActionBar(toolbar);
//viewPager=findViewById(R.id.main_view_pager);
//mainViewPagerAdapter=new MainViewPagerAdapter(getSupportFragmentManager());
//viewPager.setAdapter(mainViewPagerAdapter);
//tabLayout=findViewById(R.id.tabs);
//tabLayout.setupWithViewPager(viewPager);
}



//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus&&glSurfaceView.getVisibility()==View.GONE){
//            glSurfaceView.setVisibility(View.VISIBLE);
//        }
//    }

    public void initializeMainFragment(){
     usersFragment=new UsersFragment();
    FragmentManager fragmentManager=getSupportFragmentManager();
    FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
    fragmentTransaction.replace(R.id.mainactivity_frame,usersFragment);
    fragmentTransaction.commit();
}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainactivity_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_owner_login_page) {
           Intent intent = new Intent(MainActivity.this, OwnerMainActivity.class);
            startActivity(intent);
        }
        if (id == R.id.menu_home) {

        }
        if (id == R.id.menu_admin_page) {
            Intent intent = new Intent(MainActivity.this, AdminSignInActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);

    }
}

//        auth=FirebaseAuth.getInstance();
//        progressDialog=new ProgressDialog(this);
//        name=(EditText)findViewById(R.id.name);
//        password=(EditText)findViewById(R.id.password);
//        etmarket=(EditText)findViewById(R.id.etmarketname);
//        signin=(Button)findViewById(R.id.signin);
//        signup=(Button)findViewById(R.id.signup);
//        gotouserpage=(TextView) findViewById(R.id.goto_users_page);
//        auth=FirebaseAuth.getInstance();
//
//
//      if(auth.getCurrentUser()!=null){
//         // Intent intent=new Intent(MainActivity.this,MarketOwners.class);
//          //startActivity(intent);
//          startActivity(new Intent(getApplicationContext(),MarketOwners.class));
//          finish();
//      }
//
//      signup.setOnClickListener(this);
//signin.setOnClickListener(this);
//gotouserpage.setOnClickListener(this);




//    @Override
//    public void onClick(View v) {
//         if(v==signin){
//             register();
//         }
//         if(v==signup){
//             //finish();
//             Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
//             startActivity(intent);
//         }
//         if (v==gotouserpage)
//         {
//             Intent intent=new Intent(MainActivity.this,Users.class);
//             startActivity(intent);
//
//
//
//         }
//    }

//
//    public void register(){
//        //final
//        user=name.getText().toString().trim();
//       pass=password.getText().toString().trim();
//       marketname=etmarket.getText().toString().trim();
//        if(TextUtils.isEmpty(user))
//        {
//            Toast.makeText(MainActivity.this,"enter username",Toast.LENGTH_LONG).show();
//            return;
//        }
//        if(TextUtils.isEmpty(marketname))
//        {
//            Toast.makeText(MainActivity.this,"enter MarketName",Toast.LENGTH_LONG).show();
//            return;
//        }
//        if(TextUtils.isEmpty(pass))
//        {
//            Toast.makeText(MainActivity.this,"enter password",Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        progressDialog.setMessage("loging In");
//        progressDialog.show();
//       auth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if (task.isComplete()) {
//                        //FirebaseUser user1=auth.getCurrentUser();
//                        progressDialog.dismiss();
//                        finish();
//                       Intent intent = new Intent(MainActivity.this, ownerproducts.class);
//
//                        intent.putExtra("MARKETNAME",marketname);
//                        startActivity(intent);
//                        //startActivity(new Intent(getApplicationContext(),ownerproducts.class));
//
//                       // Toast.makeText(getApplicationContext(), "user succesfully logged In", Toast.LENGTH_LONG).show();
//                    }
//                    else{
//                        Toast.makeText(getApplicationContext(),"Please give correct Credentials ",Toast.LENGTH_LONG).show();
//
//                    }
//                }
//
//            });
//
//
//
//



