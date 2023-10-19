package com.example.subosh.restauranttrack.startpagecontent;

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
import com.example.subosh.restauranttrack.admincontent.PreferenceUtils;
import com.example.subosh.restauranttrack.newownercontent.RegisterActivity;
import com.example.subosh.restauranttrack.ownercontent.OwnerInformation;
import com.example.subosh.restauranttrack.ownercontent.OwnerMainActivity;
import com.example.subosh.restauranttrack.ownercontent.ownerproducts;
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

public class MainFragment extends Fragment implements View.OnClickListener {
    View view;
    FirebaseAuth auth;
    String user,pass,marketname;
    EditText name,password,etmarket;
    Button signin,signup;
    ListView productslistview;
    ProgressDialog progressDialog;
    DatabaseReference marketverifyDatabaseReference;
    OwnerInformation ownerInformation;
    FirebaseUser firebaseUser;
    ArrayList<String> marketownersnameslist=new ArrayList<>();
    boolean validation;
    boolean test;
    Toolbar toolbar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    view=inflater.inflate(R.layout.main_fragment,container,false);
    initializeViews();
    verifyvalidMarket();
//    if(auth.getCurrentUser()!=null){
////             Intent intent=new Intent(getActivity(),MarketOwners.class);
////            startActivity(intent);
////            getActivity().finish();
//            Intent intent = new Intent(getActivity(), ownerproducts.class);
//            intent.putExtra("MARKETNAME",marketname);
//            startActivity(intent);
//            getActivity().finish();
//            Toast.makeText(getActivity(), "user succesfully logged In as owner", Toast.LENGTH_LONG).show();
//
//
//        }
        signup.setOnClickListener(this);
        signin.setOnClickListener(this);
        return view;
    }
public void initializeViews(){
    auth= FirebaseAuth.getInstance();
    progressDialog=new ProgressDialog(this.getContext());
    name=(EditText)view.findViewById(R.id.name);
    password=(EditText)view.findViewById(R.id.password);
    etmarket=(EditText)view.findViewById(R.id.etmarketname);
    signin=(Button)view.findViewById(R.id.signin);
    signup=(Button)view.findViewById(R.id.signup);
    toolbar=view.findViewById(R.id.toolbar);
    OwnerMainActivity ownerMainActivity=(OwnerMainActivity)getActivity();
    ownerMainActivity.setSupportActionBar(toolbar);
    ownerMainActivity.setTitle("Owners Login Page");
    auth=FirebaseAuth.getInstance();
    firebaseUser=auth.getCurrentUser();
}
    @Override
    public void onClick(View v) {
        if(v==signin){
            register();
        }
        if(v==signup){
            //finish();
            Intent intent=new Intent(getActivity(),RegisterActivity.class);
            startActivity(intent);
        }

    }
    public void register(){
        auth=FirebaseAuth.getInstance();
        user=name.getText().toString().trim();
        pass=password.getText().toString().trim();
        marketname=etmarket.getText().toString().trim();
        if(TextUtils.isEmpty(user))
        {
            Toast.makeText(getActivity(),"enter username",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(marketname))
        {
            Toast.makeText(getActivity(),"enter MarketName",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(pass))
        {
            Toast.makeText(getActivity(),"enter password",Toast.LENGTH_LONG).show();
            return;
        }
        PreferenceUtils.saveName(marketname,this.getContext());
        PreferenceUtils.saveLoginType("OWNER",this.getContext());
        progressDialog.setMessage("loging In");
        progressDialog.show();

if(checkownernameslist())
{
    auth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isComplete()) {
                progressDialog.dismiss();
                firebaseUser=auth.getCurrentUser();

                Intent intent = new Intent(getActivity(), ownerproducts.class);
                intent.putExtra("MARKETNAME",marketname);
                getActivity().finish();
                startActivity(intent);
                Toast.makeText(getActivity(), "user succesfully logged In", Toast.LENGTH_LONG).show();
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
            if(marketownersnameslist.get(i).equals(marketname))
            {
                validation=true;
                break;
            }
        }
        return validation;

    }
    public void  verifyvalidMarket(){
        marketverifyDatabaseReference= FirebaseDatabase.getInstance().getReference("OWNERS");
        marketverifyDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
    ownerInformation = dataSnapshot1.getValue(OwnerInformation.class);
   marketownersnameslist.add(ownerInformation.getOwnername());
      }
}

@Override
public void onCancelled(@NonNull DatabaseError databaseError) {
                }
        });

    }
}

