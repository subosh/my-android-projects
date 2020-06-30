package com.example.subosh.restauranttrack.admincontent;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.subosh.restauranttrack.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShareLocationFragment extends Fragment implements OnMapReadyCallback{
    MapView sharelocationMapview;
    GoogleMap map;
    public static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final int REQUEZT_CODE_LOCATION_PERMISSION = 1;
public ShareLocationFragment(){

}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.sharelocationfragment,container,false);
      //  initializeGoogleMap(savedInstanceState);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharelocationMapview=(MapView) view.findViewById(R.id.google_map_view);
        if (sharelocationMapview!=null){
            sharelocationMapview.onCreate(null);
            sharelocationMapview.onResume();
            sharelocationMapview.getMapAsync(this);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        map=googleMap;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEZT_CODE_LOCATION_PERMISSION);
        }
        LatLng location=new LatLng(AdminViewOrderRequestSummaryAdapter.getCustomerCoordinateList().get(0).get("customerLatitude"),AdminViewOrderRequestSummaryAdapter.getCustomerCoordinateList().get(0).get("customerLongitude"));
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.addMarker(new MarkerOptions().position(location).title("Customer Door Step"));
        CameraPosition cameraPosition=CameraPosition.builder().target(location).zoom(16).bearing(0).tilt(45).build();
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        map.setMyLocationEnabled(true);
    }
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        Bundle mapViewBundle=outState.getBundle(MAPVIEW_BUNDLE_KEY);
//        if(mapViewBundle==null){
//            mapViewBundle=new Bundle();
//            outState.putBundle(MAPVIEW_BUNDLE_KEY,mapViewBundle);
//        }
//        sharelocationMapview.onSaveInstanceState(mapViewBundle);
//    }

//    @Override
//    public void onResume() {
//        sharelocationMapview.onResume();
//        super.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        sharelocationMapview.onPause();
//        super.onPause();
//    }
//
//    @Override
//    public void onLowMemory() {
//        sharelocationMapview.onLowMemory();
//        super.onLowMemory();
//    }
//
//    @Override
//    public void onDestroy() {
//        sharelocationMapview.onDestroy();
//        super.onDestroy();
//    }
//
//    @Override
//    public void onStop() {
//        sharelocationMapview.onStop();
//        super.onStop();
//    }
//
//    @Override
//    public void onStart() {
//        sharelocationMapview.onStart();
//        super.onStart();
//    }
}
