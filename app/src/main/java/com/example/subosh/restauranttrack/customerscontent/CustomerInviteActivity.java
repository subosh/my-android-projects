package com.example.subosh.restauranttrack.customerscontent;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Looper;
import android.os.ResultReceiver;

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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
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

import static com.example.subosh.restauranttrack.customerscontent.CustomerConstants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.example.subosh.restauranttrack.customerscontent.CustomerConstants.PERMISSIONS_REQUEST_ENABLE_GPS;

public class CustomerInviteActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{
    TextView customerAddressTextview;
    String email, code_s, share, date, code1, mydate, password, customerphone,customername;
    Button invite;
    Uri imageUri;
    FirebaseAuth auth;
    FirebaseUser user;
    ProgressDialog progressDialog;
    private StorageReference firebaseStorage;
    UploadTask uploadTask;
    DatabaseReference database;
    private String downloadpath;
    ImageView addlocationimageview;
    private static final int REQUEZT_CODE_LOCATION_PERMISSION = 1;
    private ResultReceiver resultReceiver;
    String customerAddress;
    String customeraddressfinal="";
    List<Address> addressList = null;
    private FusedLocationProviderClient fusedLocationProviderClient;
    double customerlatitude, customerlongitude;
    private GoogleApiClient googleApiClient;
    private boolean mLocationPermissionGranted=false;
LocationRequest locationRequest;
PendingResult<LocationSettingsResult> resultPendingResult;
    private Location customerLocationCoordinates=new Location("StoreToFireBase");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_invite);
        invite = (Button) findViewById(R.id.customer_invite);
        customerAddressTextview=findViewById(R.id.customer_address_textview);
        progressDialog = new ProgressDialog(this);
        addlocationimageview=findViewById(R.id.adding_location);
        //initializeGoogleMap(savedInstanceState);
       // resultReceiver = new AddressResultReceiver(new Handler());
        auth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance().getReference("customersss_images");
        user=auth.getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference().child("CUSTOMERS");
        Intent myintent = getIntent();
        if (myintent != null) {

            email = myintent.getStringExtra("email");
            code_s = myintent.getStringExtra("code");
            password = myintent.getStringExtra("password");
            share = myintent.getStringExtra("isSharing");
            imageUri = myintent.getParcelableExtra("imageUri");
            date = myintent.getStringExtra("date");
            customerphone = myintent.getStringExtra("customerphonenumber");
            customername=myintent.getStringExtra("customername");

        }

        invite.setOnClickListener(this);
        addlocationimageview.setOnClickListener(this);
setGoogleApiClient();

    }


    @Override
    public void onClick(View v) {
        if (v == invite) {
            getCustomerAddressManually();
                registering(getCustomerLocationCoordinates());
                }
        if (v==addlocationimageview)
        {
            enableGps();
            CheckPermissionAndgetCustomerAddress();
        }

    }
    public void CheckPermissionAndgetCustomerAddress(){
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
           if (ContextCompat.checkSelfPermission(this.getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED)
           {
               //mLocationPermissionGranted=true;
               getLocation();

           }
           else {
               ActivityCompat.requestPermissions(
                       CustomerInviteActivity.this,
                       new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                       REQUEZT_CODE_LOCATION_PERMISSION);
           }

        }
        else {
            ActivityCompat.requestPermissions(
                    CustomerInviteActivity.this,
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
                LocationServices.getFusedLocationProviderClient(CustomerInviteActivity.this)
                           .requestLocationUpdates(locationRequest, new LocationCallback() {
                               @Override
                               public void onLocationResult(LocationResult locationResult) {
                                   super.onLocationResult(locationResult);
                                   LocationServices.getFusedLocationProviderClient(CustomerInviteActivity.this)
                                           .removeLocationUpdates(this);
                                   if (locationResult != null && locationResult.getLocations().size() > 0) {
                                       int latestlocationIndex = locationResult.getLocations().size() - 1;
                                       double latitude = locationResult.getLocations().get(latestlocationIndex).getLatitude();
                                       double longitude = locationResult.getLocations().get(latestlocationIndex).getLongitude();
                                       Location location = new Location("providerNA");
                                       location.setLatitude(latitude);
                                       location.setLongitude(longitude);
                                     //  addressFromlatLong(location);
                                       customerLocationCoordinates=getCustomerCoordinates(location);
                                       if(customerLocationCoordinates.getLatitude()!=0&&customerLocationCoordinates.getLongitude()!=0)
                                       {
                                           Toast.makeText(CustomerInviteActivity.this,"You Successfully added Your Location",Toast.LENGTH_LONG).show();
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
    customeraddressfinal=getCustomerAddress(addressline);
    Toast.makeText(this,addressline,Toast.LENGTH_SHORT).show();
}
private Location getCustomerCoordinates(Location location){
        return location;
}
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    public String getCustomerAddress(String customerAddress){
        return customerAddress;
    }
    public void getCustomerAddressManually(){
        String customeraddress=customerAddressTextview.getText().toString().trim();
       if(!customeraddress.equals(""))
       {
        customeraddressfinal=getCustomerAddress(customeraddress);
       }
    }
    public String customeraddressfinal(){
        return customeraddressfinal;
    }
    private Location getCustomerLocationCoordinates()
    {
        return customerLocationCoordinates;
    }
    public void registering(final Location customerLocationCoordinates) {
        if (customeraddressfinal().equals(""))
        {
            Toast.makeText(this,"Please Give Your Address details By entering Your Details",Toast.LENGTH_LONG).show();
            return;
        }
        if (customerLocationCoordinates.getLatitude()==0&&customerLocationCoordinates.getLongitude()==0){
            Toast.makeText(this,"Please Add Your location By clicking add location option",Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Please wait for Customer Registeration");
        progressDialog.show();

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override

            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isComplete()) {
                    user = auth.getCurrentUser();
                    CustomerInformation customerInformation= new CustomerInformation(email, customerphone, date, "na",customername,customeraddressfinal());
                    database.child(user.getUid()).setValue(customerInformation);
                    DatabaseReference coordinatesreference=database.child(user.getUid()).child("CustomerCoordinates");
                    coordinatesreference.child("latitude").setValue(customerLocationCoordinates.getLatitude());
                    coordinatesreference.child("longitude").setValue(customerLocationCoordinates.getLongitude());
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
                                            Toast.makeText(CustomerInviteActivity.this,"image uploaded and Customer can purchase products",Toast.LENGTH_LONG).show();
                                            Intent intent=new Intent(CustomerInviteActivity.this,CustomerNameActivity.class);
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
//        if(checkMapServices()){
//           mLocationPermissionGranted=true;

//
//        }
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
//            if (!mLocationPermissionGranted){
//                        checkMapServices();
//                    }
//                    else {
//                        mLocationPermissionGranted=true;
//                    }
//
//                }
                    case Activity.RESULT_OK: {
                        mLocationPermissionGranted = true;
                        break;
                    }
                    case Activity.RESULT_CANCELED:
                    {
                        //checkMapServices();
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

    private boolean checkMapServices(){
      if(isServicesOk()){
          if (isMapEnabled()){
              return true;
          }
      }
      return false;
    }
    public boolean isServicesOk(){
     int available= GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(CustomerInviteActivity.this);
     if(available== ConnectionResult.SUCCESS){
         return  true;
     }
     else if (GoogleApiAvailability.getInstance().isUserResolvableError(available))
     {
         Dialog dialog=GoogleApiAvailability.getInstance().getErrorDialog(CustomerInviteActivity.this,available,CustomerConstants.ERROR_DIALOG_REQUEST);
         dialog.show();
     }
     else {
         Toast.makeText(this,"You Cant request to Store Your location ",Toast.LENGTH_SHORT).show();
     }
        return false;
    }
    public  boolean isMapEnabled(){
        final LocationManager manager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }
    private  void buildAlertMessageNoGps(){
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("This Application requires Gps to Turn On,Please Enable it")
                .setCancelable(false)
                .setPositiveButton("Turn On", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        Intent enableGpsIntent=new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                })
                .setCancelable(false)
                .show();

    }
    public void setGoogleApiClient()
    {
        if(googleApiClient==null){
            googleApiClient=new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(CustomerInviteActivity.this).build();
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

                                status.startResolutionForResult(CustomerInviteActivity.this,PERMISSIONS_REQUEST_ENABLE_GPS);
                               // mLocationPermissionGranted=true;
                            }
                            catch (SendIntentException exception) {

                            }
                            break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
                }
            });
    }}
