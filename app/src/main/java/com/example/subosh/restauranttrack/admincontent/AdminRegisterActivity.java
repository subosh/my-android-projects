package com.example.subosh.restauranttrack.admincontent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.subosh.restauranttrack.R;
import com.example.subosh.restauranttrack.newownercontent.PasswordActivity;
import com.example.subosh.restauranttrack.newownercontent.RegisterActivity;
import com.example.subosh.restauranttrack.startpagecontent.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class AdminRegisterActivity extends AppCompatActivity implements View.OnClickListener {
    EditText email,adminphone,adminnametextview;
    Button register;
    FirebaseAuth auth;
    ProgressDialog dialog;
    public  String ownername;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register);
        email=(EditText)findViewById(R.id.admin_email);
        register=(Button)findViewById(R.id.admin_next);
        adminphone=(EditText)findViewById(R.id.AdminphoneNumber);
        adminnametextview=(EditText)findViewById(R.id. Admin_name);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Admin Registration Page");
        auth=FirebaseAuth.getInstance();
        dialog=new ProgressDialog(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==register)
        {
            gotopasswordactivity();
        }
    }
    public void gotopasswordactivity(){
        String emailid=email.getText().toString().trim();
        final String adminphonenumber=adminphone.getText().toString().trim();
        final String adminname=adminnametextview.getText().toString().trim();
        if(TextUtils.isEmpty(emailid))
        {
            Toast.makeText(AdminRegisterActivity.this,"enter username",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(adminphonenumber))
        {
            Toast.makeText(AdminRegisterActivity.this,"enter Your Phone Number",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(adminname))
        {
            Toast.makeText(AdminRegisterActivity.this,"enter your Name",Toast.LENGTH_LONG).show();
            return;
        }
        dialog.setMessage("Cheacking for this Admin has already account");
        dialog.show();
        auth.fetchSignInMethodsForEmail(emailid).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (task.isComplete()) {
                    dialog.dismiss();
                    boolean userexitence=!task.getResult().getSignInMethods().isEmpty();
                    if(!userexitence){

                        Intent intent=new Intent(AdminRegisterActivity.this,AdminPasswordActivity.class);
                        intent.putExtra("email",email.getText().toString());
                        intent.putExtra("adminphonenumber",adminphonenumber);
                        intent.putExtra("adminname",adminname);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Account is already registered",Toast.LENGTH_LONG).show();
                        finish();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));

                    }
                }
                else{
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Registerationunsucess",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
