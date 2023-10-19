package com.example.subosh.restauranttrack.customerscontent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class CustomerRegisterActivity extends AppCompatActivity implements View.OnClickListener{
    EditText email,customerphone,customer;
    Button register;
    FirebaseAuth auth;
    ProgressDialog dialog;
    SharedPreferences sharedPreferences;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_register);
        email=(EditText)findViewById(R.id.email);
        register=(Button)findViewById(R.id.register);
        customerphone=(EditText)findViewById(R.id.ownerphone);
        customer=(EditText)findViewById(R.id.marketownername);
toolbar=findViewById(R.id.toolbar);
setSupportActionBar(toolbar);
getSupportActionBar().setTitle("Customer Registration Page");
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
    public void gotopasswordactivity()
    {
        String emailid=email.getText().toString().trim();
        final String customerphonenumber=customerphone.getText().toString().trim();
        final String customername=customer.getText().toString().trim();
        if(TextUtils.isEmpty(emailid))
        {
            Toast.makeText(CustomerRegisterActivity.this,"enter username",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(customerphonenumber))
        {
            Toast.makeText(CustomerRegisterActivity.this,"enter password",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(customername))
        {
            Toast.makeText(CustomerRegisterActivity.this,"enter your Market Name",Toast.LENGTH_LONG).show();
            return;
        }
        dialog.setMessage("Cheacking for Customer has already account");
        dialog.show();
        auth.fetchSignInMethodsForEmail(emailid).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (task.isComplete()) {
                    dialog.dismiss();
                    boolean userexitence=!task.getResult().getSignInMethods().isEmpty();
                    if(!userexitence){

                        Intent intent=new Intent(CustomerRegisterActivity.this,customerpasswordactivity.class);
                        intent.putExtra("email",email.getText().toString());
                        intent.putExtra("customerphonenumber",customerphonenumber);
                        intent.putExtra("customername",customername);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Account is already registered",Toast.LENGTH_LONG).show();
                        finish();
                        //Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                        //startActivity(intent);
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
