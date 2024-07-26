package com.proyecto.mallnav.application;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.proyecto.mallnav.utils.DimensionUtils;
import com.navigine.sdk.Navigine;

public class NavigineApp extends Application implements DefaultLifecycleObserver {

    public static Context AppContext = null;

    @Override
    public void onCreate() {
        super.onCreate();

        AppContext = getApplicationContext();

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        DimensionUtils.setDisplayMetrics(displayMetrics);

        Navigine.initialize(getApplicationContext());

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @Override
    public void onStart(LifecycleOwner owner) {
        try {
            Navigine.setMode(Navigine.Mode.NORMAL);
        } catch (Throwable e) {
            Log.e("NavigineSDK", "Navigine SDK is not initialized yet");
        }
    }

    @Override
    public void onResume(LifecycleOwner owner) {
        try {
            Navigine.setMode(Navigine.Mode.NORMAL);
        } catch (Throwable e) {
            Log.e("NavigineSDK", "Navigine SDK is not initialized yet");
        }
    }

    @Override
    public void onPause(LifecycleOwner owner) {
        try {
            Navigine.setMode(Navigine.Mode.BACKGROUND);
        } catch (Throwable e) {
            Log.e("NavigineSDK", "Navigine SDK is not initialized yet");
        }
    }

    @Override
    public void onStop(LifecycleOwner owner) {
        try {
            Navigine.setMode(Navigine.Mode.BACKGROUND);
        } catch (Throwable e) {
            Log.e("NavigineSDK", "Navigine SDK is not initialized yet");
        }
    }

    @Override
    public void onDestroy(LifecycleOwner owner) {
        try {
            Navigine.setMode(Navigine.Mode.BACKGROUND);
        } catch (Throwable e) {
            Log.e("NavigineSDK", "Navigine SDK is not initialized yet");
        }
    }
}
