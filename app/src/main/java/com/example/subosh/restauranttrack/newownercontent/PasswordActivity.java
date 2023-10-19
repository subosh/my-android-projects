package com.example.subosh.restauranttrack.newownercontent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.subosh.restauranttrack.R;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordActivity extends AppCompatActivity implements View.OnClickListener{
String email,ownerphone,ownername;
FirebaseAuth auth;
EditText password;
Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        password=(EditText)findViewById(R.id.password);
        next=(Button)findViewById(R.id.next);
        next.setOnClickListener(this);
        Intent myintent=getIntent();
        if(myintent!=null){
            email=myintent.getStringExtra("email");
            ownerphone=myintent.getStringExtra("ownerphonenumber");
            ownername=myintent.getStringExtra("ownername");
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
         Intent intent=new Intent(PasswordActivity.this,NameActivity.class);
        intent.putExtra("email",email);
        intent.putExtra("password",password.getText().toString());
        intent.putExtra("ownerphonenumber",ownerphone);
        intent.putExtra("ownername",ownername);
        startActivity(intent);
        finish();
    }
    else{
        Toast.makeText(getApplicationContext(),"Please eneter password length more than 5",Toast.LENGTH_LONG).show();
    }
    }


}
