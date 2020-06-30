package com.example.subosh.restauranttrack.admincontent;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.subosh.restauranttrack.R;
import com.example.subosh.restauranttrack.newownercontent.InviteActivity;
import com.example.subosh.restauranttrack.newownercontent.NameActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminNameActivity extends AppCompatActivity implements View.OnClickListener {
    String email, password,mydate,code,adminphone,adminname;
    EditText name;
    Button profile1;
    TextView bug;
    CircleImageView circleImageView;
    //Uri imagetUri;
    Uri resultUri;
    //Uri resultUri1;

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_name);
        name = (EditText) findViewById(R.id.admin_name_image);
        profile1 = (Button) findViewById(R.id.profile_upload_button);
        //logout=(Button)findViewById(R.id.logout);
        circleImageView = (CircleImageView) findViewById(R.id.circleImageView);

        bug=(TextView)findViewById(R.id.bug);
        auth=FirebaseAuth.getInstance();
        Intent myintent = getIntent();
        if (myintent != null) {
            email=myintent.getStringExtra("email");
            password=myintent.getStringExtra("password");
            adminphone=myintent.getStringExtra("adminphonenumber");
            adminname=myintent.getStringExtra("adminname");

        }
        bug.setText(email);
        profile1.setOnClickListener(this);
        circleImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == circleImageView) {
            selectimage();
        }
        if (v == profile1) {
            generatecode();
        }

    }
    public void generatecode() {
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat format1=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        mydate = format1.format(calendar.getTime());
        Random random = new Random();

        int n = 100000 + random.nextInt(900000);
        code= String.valueOf(n);

        if (resultUri != null) {
            Intent myintent = new Intent(AdminNameActivity.this, AdminInviteActivity.class);
            myintent.putExtra("email", email);
            myintent.putExtra("password", password);
            myintent.putExtra("date", mydate);
            //myintent.putExtra("isSharing", "false");
            myintent.putExtra("code", code);
            myintent.putExtra("imageUri", resultUri);
            myintent.putExtra("adminphonenumber",adminphone);
            myintent.putExtra("adminname",adminname);
            startActivity(myintent);
            finish();

        }
        else
        {
            Toast.makeText(getApplicationContext(), "please choose image", Toast.LENGTH_LONG).show();
        }
    }

    public void selectimage() {

        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(i,12);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 12 && resultCode == RESULT_OK&&data!=null&&data.getData()!=null)
        {
            resultUri = data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK)
            {
                resultUri = result.getUri();
                // resultUri1=data.getData();
                circleImageView.setImageURI(resultUri);


            }
            else if(resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error=result.getError();
            }


        }


    }
}
