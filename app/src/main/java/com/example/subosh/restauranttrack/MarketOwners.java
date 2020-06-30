package com.example.subosh.restauranttrack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.subosh.restauranttrack.admincontent.PreferenceUtils;
import com.example.subosh.restauranttrack.startpagecontent.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MarketOwners extends AppCompatActivity implements View.OnClickListener{
FirebaseAuth firebaseAuth;
DatabaseReference databaseReference;
//RecyclerView recyclerView=null;
Button logout;
TextView logedas;
    String email;

FirebaseUser user;
//@BindView(R.id.market_owner)RecyclerView
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_owners);
        logout=(Button)findViewById(R.id.logout);
        logedas=(TextView)findViewById(R.id.logedas);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("CUSTOMERS");
        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()==null)
        {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            Toast.makeText(getApplicationContext(),"Not registered first register your market",Toast.LENGTH_LONG).show();
        }

            user = firebaseAuth.getCurrentUser();
        email = user.getEmail();
     logedas.setText(email);
     logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==logout){
            firebaseAuth=FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            PreferenceUtils.saveName("",this);
            PreferenceUtils.saveLoginType("",this);
                finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            Toast.makeText(getApplicationContext(),"Successfully You Logged Out",Toast.LENGTH_SHORT).show();

        }

        }

    }


