package com.example.subosh.restauranttrack.admincontent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.subosh.restauranttrack.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminSignInFragment extends Fragment implements View.OnClickListener {
    FirebaseAuth auth;
    String emailString,pass,adminname;
    EditText email,password,etmarket;
    Button signin,signup;
    ListView productslistview;
    ProgressDialog progressDialog;
    DatabaseReference marketverifyDatabaseReference;
    AdminInformation adminInformation;
    FirebaseUser firebaseUser;
    Toolbar toolbar;
    ArrayList<String> marketownersnameslist=new ArrayList<>();
    boolean validation;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.admin_sign_fragment,container,false);
        toolbar=view.findViewById(R.id.toolbar);
        AdminSignInActivity adminSignInActivity=(AdminSignInActivity)getActivity();
        adminSignInActivity.setSupportActionBar(toolbar);
        adminSignInActivity.setTitle("Admin Login Page");
        auth= FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this.getContext());
        email=(EditText)view.findViewById(R.id.admin_email);
        password=(EditText)view.findViewById(R.id.admin_signin_password);
        etmarket=(EditText)view.findViewById(R.id.etadminname);
        signin=(Button)view.findViewById(R.id.admin_signin_button);
        signup=(Button)view.findViewById(R.id.admin_signup_button);
        //gotouserpage=(TextView)view.findViewById(R.id.goto_users_page);
        auth=FirebaseAuth.getInstance();
        firebaseUser=auth.getCurrentUser();
        verifyvalidMarket();

//        if(auth.getCurrentUser()!=null){
//            Intent intent=new Intent(getActivity(),MarketOwners.class);
//            startActivity(intent);
//            getActivity().finish();
//
////            Intent intent = new Intent(getActivity(), admin.class);
////            intent.putExtra("ADMINNAME",adminname);
////            startActivity(intent);
////            Toast.makeText(getActivity(), "user succesfully logged In"+firebaseUser.getUid(), Toast.LENGTH_LONG).show();
////            getActivity().finish();
//
//
//        }
        signup.setOnClickListener(this);
        signin.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v==signin){
            register();
        }
        if(v==signup){
            Intent intent=new Intent(getActivity(),AdminRegisterActivity.class);
            startActivity(intent);
        }
    }
    public void register(){
        auth=FirebaseAuth.getInstance();
        emailString=email.getText().toString().trim();
        pass=password.getText().toString().trim();
        adminname=etmarket.getText().toString().trim();
        if(TextUtils.isEmpty(emailString))
        {
            Toast.makeText(getActivity(),"Please enter Registered Email Id",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(adminname))
        {
            Toast.makeText(getActivity(),"enter AdminName",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(pass))
        {
            Toast.makeText(getActivity(),"enter Admin password",Toast.LENGTH_LONG).show();
            return;
        }
        PreferenceUtils.saveName(adminname,this.getContext());
        PreferenceUtils.saveLoginType("ADMIN",this.getContext());
        progressDialog.setMessage("loging In");
        progressDialog.show();
        if(checkownernameslist())
        {
            auth.signInWithEmailAndPassword(emailString, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isComplete()) {
                        firebaseUser=auth.getCurrentUser();
                        progressDialog.dismiss();
                        Intent intent = new Intent(getActivity(), admin.class);
                        intent.putExtra("ADMINNAME",adminname);
                        getActivity().finish();
                        startActivity(intent);
                        Toast.makeText(getActivity(), "user succesfully logged In"+firebaseUser.getUid(), Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getActivity(),"Please give correct Credentials ",Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
        else {
            progressDialog.dismiss();
            Toast.makeText(getActivity(),"Please enter correct name registered market",Toast.LENGTH_SHORT).show();
        }

    }
    public boolean checkownernameslist(){
        for (int i=0;i<marketownersnameslist.size();i++){
            if(marketownersnameslist.get(i).equals(adminname))
            {
                validation=true;
                break;
            }
        }
        return validation;

    }
    public void  verifyvalidMarket(){
        marketverifyDatabaseReference= FirebaseDatabase.getInstance().getReference("ADMINS");
        marketverifyDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                    adminInformation= dataSnapshot1.getValue(AdminInformation.class);
                    marketownersnameslist.add(adminInformation.getAdminname());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
}
