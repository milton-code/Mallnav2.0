package com.proyecto.mallnav.ui.fragments;


import android.graphics.BitmapFactory;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.navigine.idl.java.IconMapObject;
import com.navigine.idl.java.Location;
import com.navigine.idl.java.LocationListener;
import com.navigine.idl.java.LocationManager;
import com.navigine.view.LocationView;
import com.proyecto.mallnav.R;


import com.proyecto.mallnav.utils.NavigineSdkManager;




public class NavigationFragment extends Fragment {


    LocationView mLocationView;
    IconMapObject mPosition = null;
    LocationListener mLocationListener;
    LocationManager mLocationManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initNavigation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
        mLocationView = view.findViewById(R.id.location_view);
        mPosition = mLocationView.getLocationWindow().addIconMapObject();
        mPosition.setSize(30, 30);
        mPosition.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_current_point));

        return view;
    }

    public void initNavigation(){
        mLocationManager = NavigineSdkManager.LocationManager;
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationLoaded(Location location) {
                mLocationView.getLocationWindow().setSublocationId(location.getSublocations().get(0).getId());
            }

            @Override
            public void onLocationFailed(int i, Error error) {
                Toast.makeText(getContext(), "failed load location", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLocationUploaded(int i) {

            }
        };

        mLocationManager.addLocationListener(mLocationListener);
        mLocationManager.setLocationId(1563);
    }


}