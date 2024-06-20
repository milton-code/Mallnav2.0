package com.proyecto.mallnav.ui.fragments;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.navigine.idl.java.IconMapObject;
import com.navigine.idl.java.Location;
import com.navigine.idl.java.LocationListener;
import com.navigine.view.LocationView;
import com.proyecto.mallnav.R;

import com.proyecto.mallnav.ui.activities.MainActivity;
import com.proyecto.mallnav.utils.NavigineSdkManager;

public class NavigationFragment extends Fragment {

    LocationView mLocationView;
    IconMapObject mPosition = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
        mLocationView = view.findViewById(R.id.location_view);
        mPosition = mLocationView.getLocationWindow().addIconMapObject();
        mPosition.setSize(30, 30);
        mPosition.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_current_point));
        NavigineSdkManager.LocationManager.addLocationListener(new LocationListener() {
            @Override
            public void onLocationLoaded(Location location) {
                NavigineSdkManager.LocationManager.setLocationId(1563);
                int id = location.getId();
                String ide = Integer.toString(id);
                Toast.makeText(getContext(),ide,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLocationFailed(int i, Error error) {

            }

            @Override
            public void onLocationUploaded(int i) {

            }
        });

        return view;
    }
}