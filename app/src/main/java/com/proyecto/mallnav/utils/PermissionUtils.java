package com.proyecto.mallnav.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.proyecto.mallnav.R;

public class PermissionUtils {

    private static final int RC_PERMISSION_BACKGROUND_LOCATION = 101;
    private static AlertDialog mAlertLocationDialog = null;


    public static boolean hasLocationPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static boolean hasLocationBackgroundPermission(Context context) {
        return hasLocationPermission(context)
                && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static void showBackgroundPermissionRationale(Activity context) {
        if (mAlertLocationDialog == null) {
            mAlertLocationDialog = new MaterialAlertDialogBuilder(context, R.style.Theme_Mallnav_MaterialAlertDialog_Rounded)
                    .setMessage(R.string.permission_rationale_message_bg_access)
                    .setPositiveButton(R.string.dialog_action_ok, (dialog, which) ->
                            ActivityCompat.requestPermissions(context,
                                    new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, RC_PERMISSION_BACKGROUND_LOCATION))
                    .create();
        }
        mAlertLocationDialog.show();
    }


}
