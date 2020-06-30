package com.example.subosh.restauranttrack.customerscontent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.subosh.restauranttrack.R;
import com.example.subosh.restauranttrack.newownercontent.NameActivity;
import com.example.subosh.restauranttrack.newownercontent.PasswordActivity;
import com.google.firebase.auth.FirebaseAuth;

public class customerpasswordactivity extends AppCompatActivity implements View.OnClickListener{
    String email,customerphone,customername;
    FirebaseAuth auth;
    EditText password;
    Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customerpasswordactivity);
        password=(EditText)findViewById(R.id.password);
        next=(Button)findViewById(R.id.next);
        next.setOnClickListener(this);
        Intent myintent=getIntent();
        if(myintent!=null){
            email=myintent.getStringExtra("email");
            customerphone=myintent.getStringExtra("customerphonenumber");
            customername=myintent.getStringExtra("customername");
        }

    }

    @Override
    public void onClick(View v) {
        if(v==next){
            gotonamepicactivity();
        }
    }
    public  void gotonamepicactivity(){
        if(password.getText().toString().length()>=5)
        {
            //pass=password.getText().toString();
            Intent intent=new Intent(customerpasswordactivity.this,CustomerNameActivity.class);
            intent.putExtra("email",email);
            intent.putExtra("password",password.getText().toString());
            intent.putExtra("customerphonenumber",customerphone);
            intent.putExtra("customername",customername);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(),"Please eneter password length more than 5",Toast.LENGTH_LONG).show();
        }
    }
}
