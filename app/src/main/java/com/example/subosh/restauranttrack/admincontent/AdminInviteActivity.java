package com.example.subosh.restauranttrack.admincontent;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.subosh.restauranttrack.R;
import com.example.subosh.restauranttrack.newownercontent.InviteActivity;
import com.example.subosh.restauranttrack.newownercontent.NameActivity;
import com.example.subosh.restauranttrack.ownercontent.OwnerInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AdminInviteActivity extends AppCompatActivity implements View.OnClickListener {
    TextView emailid;
    TextView date1;
    TextView code;
    TextView resulturi;
    String email, code_s, share, date, code1, mydate, password, adminphone, adminname;
    Button invite;
    Uri imageUri;
    FirebaseAuth auth;
    FirebaseUser user;
    ProgressDialog progressDialog;
    private StorageReference firebaseStorage;
    UploadTask uploadTask;
    DatabaseReference database;
    private String downloadpath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_invite);
        emailid = (TextView) findViewById(R.id.emailid);
        resulturi = (TextView) findViewById(R.id.resulturi);
        code = (TextView) findViewById(R.id.code);
        date1 = (TextView) findViewById(R.id.date1);


        invite = (Button) findViewById(R.id.invite);
        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance().getReference("Admin_profile_images");
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference().child("ADMINS");
        Intent myintent = getIntent();
        if (myintent != null) {
            email = myintent.getStringExtra("email");
            code_s = myintent.getStringExtra("code");
            password = myintent.getStringExtra("password");
            share = myintent.getStringExtra("isSharing");
            imageUri = myintent.getParcelableExtra("imageUri");
            date = myintent.getStringExtra("date");
            adminphone = myintent.getStringExtra("adminphonenumber");
            adminname = myintent.getStringExtra("adminname");

        }


        invite.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == invite) {
            registering();

        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void registering() {
        progressDialog.setMessage("Please wait for Admin Registeration");
        progressDialog.show();

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override

            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isComplete()) {

                    user = auth.getCurrentUser();
                    AdminInformation adminInformation = new AdminInformation(email, adminphone, date, "na", adminname);
                    database.child(user.getUid()).setValue(adminInformation);
                    final StorageReference str = firebaseStorage.child(imageUri.getLastPathSegment() + ".jpg");
                    str.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            str.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    database.child(user.getUid()).child("downloadpath").setValue(String.valueOf(uri)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressDialog.dismiss();
                                            Toast.makeText(AdminInviteActivity.this, "image uploaded and market registerd", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(AdminInviteActivity.this, AdminNameActivity.class);
                                            intent.putExtra("date", date);
                                            startActivity(intent);
                                        }
                                    });
                                }
                            });
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "sorry something problem", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
