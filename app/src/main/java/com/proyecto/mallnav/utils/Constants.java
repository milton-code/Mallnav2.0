package com.proyecto.mallnav.utils;

import com.proyecto.mallnav.BuildConfig;

public class Constants {

    public static final String TAG = "MallnavDemo.LOG";

    // notifications
    public static final String NOTIFICATION_CHANNEL_ID   = BuildConfig.APPLICATION_ID + ".PUSH";
    public static final String NOTIFICATION_CHANNEL_NAME = "NAVIGINE_PUSH";
    // anim image sizes
    public static final int   SIZE_SUCCESS         = 52;
    public static final int   SIZE_FAILED          = 32;
    public static final float CHECK_FRAME_SELECTED = 1f;

    public static final String LOCATION_CHANGED = "LOCATION_CHANGED";

    public static final String ENDPOINT_GET_USER     = "/mobile/v1/users/get?userHash=";
}
