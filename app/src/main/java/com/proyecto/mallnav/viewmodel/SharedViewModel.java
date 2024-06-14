package com.proyecto.mallnav.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.navigine.idl.java.Location;
import com.navigine.idl.java.LocationListener;


public class SharedViewModel extends ViewModel {

    public MutableLiveData<Location> mLocation = new MutableLiveData<>(null);
}
