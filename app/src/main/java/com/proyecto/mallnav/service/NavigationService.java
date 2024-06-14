package com.proyecto.mallnav.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class NavigationService extends Service {
    public static NavigationService INSTANCE = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
