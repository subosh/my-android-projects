package com.example.subosh.restauranttrack;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import com.example.subosh.restauranttrack.admincontent.PreferenceUtils;
import com.example.subosh.restauranttrack.admincontent.admin;
import com.example.subosh.restauranttrack.ownercontent.OwnerMainActivity;
import com.example.subosh.restauranttrack.ownercontent.Owners;
import com.example.subosh.restauranttrack.ownercontent.ownerproducts;
import com.example.subosh.restauranttrack.startpagecontent.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if (firebaseUser!=null&&PreferenceUtils.getLoginType(this)!=null){
            if(PreferenceUtils.getLoginType(this).equals("CUSTOMER"))
            {
                Intent intent=new Intent(MainClass.this, Owners.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"Successfully  Logged In Customer",Toast.LENGTH_SHORT).show();
            }
            if (PreferenceUtils.getLoginType(this).equals("OWNER")){
                Intent intent=new Intent(MainClass.this, ownerproducts.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                Toast.makeText(getApplicationContext(),"Successfully  Logged In Owner",Toast.LENGTH_SHORT).show();
            }
            if (PreferenceUtils.getLoginType(this).equals("ADMIN")){
                Intent intent=new Intent(MainClass.this, admin.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                Toast.makeText(getApplicationContext(),"Successfully  Logged In Admin",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
