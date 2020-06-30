package com.example.subosh.restauranttrack.newownercontent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.subosh.restauranttrack.R;
import com.example.subosh.restauranttrack.startpagecontent.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
EditText email,ownerphone,marketname;
Button register;
FirebaseAuth auth;
ProgressDialog dialog;
public  String ownername;
SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
       // getSupportActionBar().setTitle("Shop Owner Registration");
        email=(EditText)findViewById(R.id.email);
        register=(Button)findViewById(R.id.register);
        ownerphone=(EditText)findViewById(R.id.ownerphone);
        marketname=(EditText)findViewById(R.id.marketownername);

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
       final String ownerphonenumber=ownerphone.getText().toString().trim();
       final String ownername=marketname.getText().toString().trim();
      //sharedPreferences=getSharedPreferences("marketname", Context.MODE_PRIVATE);
      //SharedPreferences.Editor editor=sharedPreferences.edit();
      //editor.putString("marketname",ownername);
      //editor.apply();
      //sharedPreferences.getString("value",ownername);
        if(TextUtils.isEmpty(emailid))
        {
            Toast.makeText(RegisterActivity.this,"enter username",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(ownerphonenumber))
        {
            Toast.makeText(RegisterActivity.this,"enter password",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(ownername))
        {
            Toast.makeText(RegisterActivity.this,"enter your Market Name",Toast.LENGTH_LONG).show();
            return;
        }
        dialog.setMessage("Cheacking for user has already account");
        dialog.show();
        auth.fetchProvidersForEmail(emailid).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                if (task.isComplete()) {
                    dialog.dismiss();
            boolean userexitence=!task.getResult().getProviders().isEmpty();
            if(!userexitence){

                Intent intent=new Intent(RegisterActivity.this,PasswordActivity.class);
                intent.putExtra("email",email.getText().toString());
                intent.putExtra("ownerphonenumber",ownerphonenumber);
                intent.putExtra("ownername",ownername);
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
