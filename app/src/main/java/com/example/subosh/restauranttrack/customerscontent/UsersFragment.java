package com.example.subosh.restauranttrack.customerscontent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.subosh.restauranttrack.MarketOwners;
import com.example.subosh.restauranttrack.R;
import com.example.subosh.restauranttrack.admincontent.PreferenceUtils;
import com.example.subosh.restauranttrack.ownercontent.Owners;
import com.example.subosh.restauranttrack.startpagecontent.MainActivity;
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

public class UsersFragment extends Fragment implements View.OnClickListener{
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    String user,pass,customername;
    EditText name,password,customerName;
    Button signin,signup;
    ListView productslistview;

    FirebaseUser firebaseUser;
    Toolbar toolbar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.usersfragment,null,false);
        progressDialog=new ProgressDialog(this.getContext());
        name=(EditText)view.findViewById(R.id.name);
        customerName=(EditText)view.findViewById(R.id.customerName);
        password=(EditText)view.findViewById(R.id.password);
        signin=(Button)view.findViewById(R.id.customer_sign_in);
        signup=(Button)view.findViewById(R.id.customer_sign_up);
        toolbar=view.findViewById(R.id.toolbar);
//        MainActivity mainActivity=(MainActivity)getActivity();
//        mainActivity.setSupportActionBar(toolbar);
//        mainActivity.setTitle("Customer login");


        //gotouserpage=(TextView)view.findViewById(R.id.goto_users_page);
        auth=FirebaseAuth.getInstance();


//        if(auth.getCurrentUser()!=null){
//            Intent intent=new Intent(getActivity(),Owners.class);
//            startActivity(intent);
//            getActivity().finish();
//            Toast.makeText(getContext(),"Successfully as Customer",Toast.LENGTH_SHORT).show();
//
//
//        }

        signup.setOnClickListener(this);
        signin.setOnClickListener(this);
        //gotouserpage.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v==signin){
            register();
        }
        if(v==signup){
            //finish();
            Intent intent=new Intent(getActivity(),CustomerRegisterActivity.class);
            startActivity(intent);
        }
    }
    public void register()
    {
        auth= FirebaseAuth.getInstance();
        user=name.getText().toString().trim();
        pass=password.getText().toString().trim();
        customername=customerName.getText().toString().trim();
        if(TextUtils.isEmpty(user))
        {
            Toast.makeText(getActivity(),"Please enter Customer Id",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(customername))
        {
            Toast.makeText(getActivity(),"Please enter Customer Name",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(pass))
        {
            Toast.makeText(getActivity(),"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage(customername+"is loging In Please Wait Moment");
        progressDialog.show();
        PreferenceUtils.saveName(customername,this.getContext());
        PreferenceUtils.saveLoginType("CUSTOMER",this.getContext());

        auth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isComplete()) {
                    firebaseUser=auth.getCurrentUser();
                    progressDialog.dismiss();
                    Intent intent = new Intent(getActivity(), Owners.class);
                    intent.putExtra("customername",customername);
                  // intent.putExtra("customerDetailsList",getCustomerDetailsList());
                    getActivity().finish();
                    startActivity(intent);
                    Toast.makeText(getActivity(), "Customer succesfully logged In", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getActivity(),"Please give correct Credentials ",Toast.LENGTH_LONG).show();

                }
            }
        });
    }

}
