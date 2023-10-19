package com.example.subosh.restauranttrack.newownercontent;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Looper;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.subosh.restauranttrack.R;
import com.example.subosh.restauranttrack.ownercontent.OwnerInformation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
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

import java.util.List;
import java.util.Locale;

import static com.example.subosh.restauranttrack.newownercontent.NewOwnerConstants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.example.subosh.restauranttrack.newownercontent.NewOwnerConstants.PERMISSIONS_REQUEST_ENABLE_GPS;

public class InviteActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {
    //FirebaseUser user;
    TextView ownerAddressTextview;
    String email, code_s, share, date, code1, mydate, password, ownerphone,ownername;
    Button invite;
    Uri imageUri;
    FirebaseAuth auth;
    FirebaseUser user;
    ProgressDialog progressDialog;
private  StorageReference firebaseStorage;
    UploadTask uploadTask;
    DatabaseReference database;
    private String downloadpath;
    private static final int REQUEZT_CODE_LOCATION_PERMISSION = 1;
    String customerAddress;
    String owneraddressfinal="";
    List<Address> addressList = null;
    private FusedLocationProviderClient fusedLocationProviderClient;
    double customerlatitude, customerlongitude;
    private GoogleApiClient googleApiClient;
    private boolean mLocationPermissionGranted=false;
    LocationRequest locationRequest;
    PendingResult<LocationSettingsResult> resultPendingResult;
    ImageView addlocationimageview;
    private Location ownerLocationCoordinates=new Location("StoreToFireBase");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        ownerAddressTextview=findViewById(R.id.owner_address_textview);
        invite = (Button) findViewById(R.id.owner_invite);
        addlocationimageview=findViewById(R.id.adding_owner_location);
        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance().getReference("Customer_profile_images");
         user=auth.getCurrentUser();
         database = FirebaseDatabase.getInstance().getReference().child("OWNERS");
        Intent myintent = getIntent();
        if (myintent != null) {
            email = myintent.getStringExtra("email");
            code_s = myintent.getStringExtra("code");
            password = myintent.getStringExtra("password");
            share = myintent.getStringExtra("isSharing");
            imageUri = myintent.getParcelableExtra("imageUri");
            date = myintent.getStringExtra("date");
            ownerphone = myintent.getStringExtra("ownerphonenumber");
            ownername=myintent.getStringExtra("ownername");

        }


        invite.setOnClickListener(this);
        addlocationimageview.setOnClickListener(this);
        setGoogleApiClient();
    }


    @Override
    public void onClick(View view) {
        if (view == invite) {
            getOwnerAddressManually();
            registering(getOwnerLocationCoordinates());
        }
        if (view==addlocationimageview)
        {
            enableGps();
            CheckPermissionAndgetOwnerAddress();
        }

    }
    public void CheckPermissionAndgetOwnerAddress(){
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED)
            {
                getLocation();

            }
            else {
                ActivityCompat.requestPermissions(
                        InviteActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEZT_CODE_LOCATION_PERMISSION);
            }

        }
        else {
            ActivityCompat.requestPermissions(
                    InviteActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEZT_CODE_LOCATION_PERMISSION);
        }
    }

    private void getLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        try{
            if (mLocationPermissionGranted)
            {
                LocationServices.getFusedLocationProviderClient(InviteActivity.this)
                        .requestLocationUpdates(locationRequest, new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                super.onLocationResult(locationResult);
                                LocationServices.getFusedLocationProviderClient(InviteActivity.this)
                                        .removeLocationUpdates(this);
                                if (locationResult != null && locationResult.getLocations().size() > 0) {
                                    int latestlocationIndex = locationResult.getLocations().size() - 1;
                                    double latitude = locationResult.getLocations().get(latestlocationIndex).getLatitude();
                                    double longitude = locationResult.getLocations().get(latestlocationIndex).getLongitude();
                                    Location location = new Location("providerNA");
                                    location.setLatitude(latitude);
                                    location.setLongitude(longitude);
                                  //  addressFromlatLong(location);
                                    ownerLocationCoordinates=getOwnerCoordinates(location);
                                    if(ownerLocationCoordinates.getLatitude()!=0&&ownerLocationCoordinates.getLongitude()!=0)
                                    {
                                        Toast.makeText(InviteActivity.this,"You Successfully added Your Location",Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }, Looper.getMainLooper());


            }
        }

        catch(SecurityException e){
            e.printStackTrace();

        }

    }



    private Location getOwnerCoordinates(Location location){
        return location;
    }
    private void addressFromlatLong(Location location){
        String errormessage="";
        Geocoder geocoder=new Geocoder(this, Locale.getDefault());
        List<Address> addresses=null;
        try{
            addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
        }
        catch (Exception exception){
            errormessage=exception.getMessage();
            Toast.makeText(this,errormessage,Toast.LENGTH_SHORT).show();
        }
        String addressline=addresses.get(0).getAddressLine(0);
        owneraddressfinal=getOwnerAddress(addressline);
        Toast.makeText(this,addressline,Toast.LENGTH_SHORT).show();
    }
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    public String getOwnerAddress(String customerAddress){
        return customerAddress;
    }
    public void getOwnerAddressManually(){
        String owneraddress=ownerAddressTextview.getText().toString().trim();
        if(!owneraddress.equals(""))
        {
            owneraddressfinal=getOwnerAddress(owneraddress);
        }
    }
    public String owneraddressfinal(){
        return owneraddressfinal;
    }
    private Location getOwnerLocationCoordinates()
    {
        return ownerLocationCoordinates;
    }

    public void registering(final Location ownerLocationCoordinates) {
        if (owneraddressfinal().equals(""))
        {
            Toast.makeText(this,"Please Give Your Address details By entering Details",Toast.LENGTH_LONG).show();
            return;
        }
        if (ownerLocationCoordinates.getLatitude()==0&&ownerLocationCoordinates.getLongitude()==0){
            Toast.makeText(this,"Please Add Your location By clicking add location option",Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Please wait for your Market Registeration");
        progressDialog.show();
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isComplete()) {
                    user = auth.getCurrentUser();
                   OwnerInformation ownerInformation = new OwnerInformation(email, ownerphone, date, "na",ownername,owneraddressfinal(),"YES",ownerLocationCoordinates.getLatitude(),ownerLocationCoordinates.getLongitude());
                   database.child(user.getUid()).setValue(ownerInformation);
//                    DatabaseReference coordinatesreference=database.child(user.getUid()).child("OwnerCoordinates");
//                    coordinatesreference.child("latitude").setValue(ownerLocationCoordinates.getLatitude());
//                    coordinatesreference.child("longitude").setValue(ownerLocationCoordinates.getLongitude());
                   // DatabaseReference requestStatusReference=database.child(user.getUid()).child("DeliveryRequestStatus");
                    //requestStatusReference.child("OwnerRequestStatus").setValue("YES");
                    final StorageReference str=firebaseStorage.child(imageUri.getLastPathSegment()+".jpg");
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
                                   Toast.makeText(InviteActivity.this,"image uploaded and market registerd",Toast.LENGTH_LONG).show();
                                   Intent intent=new Intent(InviteActivity.this,NameActivity.class);
                                   intent.putExtra("date",date);
                                   startActivity(intent);
                               }
                           }) ;
                           }
                       }) ;
                        }
                    });
                   }
                   else{
                    Toast.makeText(getApplicationContext(),"sorry something problem",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        System.out.print("Onresume method is called");
        if(!mLocationPermissionGranted)
        {
            enableGps();
        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted=false;
        switch (requestCode){
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:{
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    mLocationPermissionGranted=true;
                }

            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PERMISSIONS_REQUEST_ENABLE_GPS:
                switch (resultCode)
                {
                    case Activity.RESULT_OK: {
                        mLocationPermissionGranted = true;
                        break;
                    }
                    case Activity.RESULT_CANCELED:
                    {
                        Toast.makeText(this,"You Doenst allowed Gps Enabling option",Toast.LENGTH_LONG).show();
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
                break;
        }
    }
    public void setGoogleApiClient()
    {
    if(googleApiClient==null){
        googleApiClient=new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(InviteActivity.this).build();
        googleApiClient.connect();
    }
}

    @Override
    public void onConnected(@Nullable Bundle bundle) {


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    public void enableGps()
    {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(30000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder=new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        resultPendingResult=LocationServices.SettingsApi.checkLocationSettings(googleApiClient,builder.build());
        resultPendingResult.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status=locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        mLocationPermissionGranted=true;
                        break;

                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {

                            status.startResolutionForResult(InviteActivity.this,PERMISSIONS_REQUEST_ENABLE_GPS);
                            // mLocationPermissionGranted=true;
                        }
                        catch (IntentSender.SendIntentException exception) {

                        }
                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }}
