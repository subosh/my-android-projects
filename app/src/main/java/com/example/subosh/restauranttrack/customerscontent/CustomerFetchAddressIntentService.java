package com.example.subosh.restauranttrack.customerscontent;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CustomerFetchAddressIntentService extends IntentService {
    private ResultReceiver resultReceiver;
    public CustomerFetchAddressIntentService(){
        super("CustomerFetchAddressIntentService");
    }

    @SuppressLint("NewApi")
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent!=null){
            String errormessage="";
            resultReceiver=intent.getParcelableExtra(CustomerConstants.RECEIVER);
            Location location=intent.getParcelableExtra(CustomerConstants.LOCATION_DATA_EXTRA);
            if (location==null){
                return;
            }
            Geocoder geocoder=new Geocoder(this, Locale.getDefault());
            List<Address> addresses=null;
            try{
                addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            }
            catch (Exception exception){
                errormessage=exception.getMessage();
            }
            if (addresses==null||addresses.isEmpty()){
                deliverResultToReceiver(CustomerConstants.FAILURE_RESULT,errormessage);
            }
            else {
                Address address=addresses.get(0);
                ArrayList<String> addressfragment=new ArrayList<>();
                for (int i=0;i<=address.getMaxAddressLineIndex();i++){
                    addressfragment.add(address.getAddressLine(i));
                }
                deliverResultToReceiver(CustomerConstants.SUCCESS_RESULT, TextUtils.join(Objects.requireNonNull(System.getProperty("line.separator")),addressfragment)
                );
            }
        }

    }
    private void deliverResultToReceiver(int resultcode,String addressMessage){
        Bundle bundle=new Bundle();
        bundle.putString(CustomerConstants.RESULT_DATA_KEY,addressMessage);
        resultReceiver.send(resultcode,bundle);

    }
}
