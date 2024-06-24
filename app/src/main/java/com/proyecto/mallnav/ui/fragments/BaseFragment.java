package com.proyecto.mallnav.ui.fragments;

import static com.proyecto.mallnav.utils.Constants.LOCATION_CHANGED;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.proyecto.mallnav.R;
import com.proyecto.mallnav.utils.PermissionUtils;

public class BaseFragment extends Fragment {

    private LocationManager locationManager  = null;
    private BluetoothManager bluetoothManager = null;
    private BluetoothAdapter bluetoothAdapter = null;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
