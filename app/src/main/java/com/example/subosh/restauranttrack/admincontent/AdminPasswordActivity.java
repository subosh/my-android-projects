package com.example.subosh.restauranttrack.admincontent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.subosh.restauranttrack.R;
import com.example.subosh.restauranttrack.newownercontent.NameActivity;
import com.example.subosh.restauranttrack.newownercontent.PasswordActivity;
import com.google.firebase.auth.FirebaseAuth;

public class AdminPasswordActivity extends AppCompatActivity implements View.OnClickListener{
    String email,adminphone,adminname;
    FirebaseAuth auth;
    EditText password;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_password);
        password=(EditText)findViewById(R.id.admin_signup_password);
        next=(Button)findViewById(R.id.admin_signup_next);
        next.setOnClickListener(this);
        Intent myintent=getIntent();
        if(myintent!=null){
            email=myintent.getStringExtra("email");
            adminphone=myintent.getStringExtra("adminphonenumber");
            adminname=myintent.getStringExtra("adminname");
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
            Intent intent=new Intent(AdminPasswordActivity.this,AdminNameActivity.class);
            intent.putExtra("email",email);
            intent.putExtra("password",password.getText().toString());
            intent.putExtra("adminphonenumber",adminphone);
            intent.putExtra("adminname",adminname);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(),"Please eneter password length more than 5",Toast.LENGTH_LONG).show();
        }
    }

}
