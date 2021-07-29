package com.daniel.getpermission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class GetPermission {

    private PermissionActionListener permissionListener;
    private Activity activity;
    private Fragment fragment;
    private Context context;

    private ArrayList<String> permissionsToAsk;
    private String message;

    private boolean isFragment;
    private boolean shouldShowRationaleDialog;
    private int requestCode;

    private GetPermission() {
        throw new AssertionError("Default Constructor is Not Allowed Use Builder");
    }

    private GetPermission(Builder builder) {
        this.fragment = builder.fragment;
        this.activity = builder.activity;
        this.context = builder.context;

        this.requestCode = builder.requestCode;
        this.message = builder.message;
        this.permissionsToAsk = builder.permissionsToAsk;
        this.permissionListener = builder.permissionListener;
        this.isFragment = builder.isFragment;
    }


//   return instance of Builder

    public static IWith Builder() {
        return new Builder();
    }

    /**
     * Method that invokes permission dialog, if permission is already granted or
     * denied (with never asked ticked) then the result is delivered without showing any dialog.
     */
    public void requestPermissions() {
        if (!hasPermissions(context, permissionsToAsk.toArray(new String[0]))) {
            if (shouldShowRationale(permissionsToAsk.toArray(new String[0])) && message != null) {
                CustomAlert
                        .getInstance()
                        .showAlertDialog(context,
                                null,
                                0,
                                message,
                                "OK",
                                (dialog, which) -> request(),
                                "Cancel",
                                true);
            } else {
                request();
            }
        } else {
            permissionListener.onPermissionsGranted(requestCode, permissionsToAsk);
        }
    }

    /* Shows a dialog for permission */
    private void request() {
        if (isFragment) {
            fragment.requestPermissions(permissionsToAsk.toArray(new String[0]), requestCode);
        } else {
            ActivityCompat.requestPermissions(activity, permissionsToAsk.toArray(new String[0]), requestCode);
        }
    }

    /* Check whether any permission is denied before, if yes then we show a rational dialog for explanation */
    private boolean shouldShowRationale(String... permissions) {
        if (isFragment) {
            for (String permission : permissions) {
                if (fragment.shouldShowRequestPermissionRationale(permission)) {
                    shouldShowRationaleDialog = true;
                }
            }
        } else {
            for (String permission : permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    shouldShowRationaleDialog = true;
                }
            }
        }

        return shouldShowRationaleDialog;
    }

    /* Check if we already have the permission */
    private boolean hasPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Called by the user when he gets the call in Activity/Fragment
     *
     * @param reqCode      Request Code
     * @param permissions  List of permissions
     * @param grantResults Permission grant result
     */
    public void onRequestPermissionsResult(int reqCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == reqCode) {
            ArrayList<String> grantedPermissionList = new ArrayList<>();
            ArrayList<String> deniedPermissionList = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                int grantResult = grantResults[i];
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    grantedPermissionList.add(permissions[i]);
                } else {
                    deniedPermissionList.add(permissions[i]);
                }
            }

            if (!grantedPermissionList.isEmpty()) {
                permissionListener.onPermissionsGranted(requestCode, grantedPermissionList);
            }
            if (!deniedPermissionList.isEmpty()) {
                permissionListener.onPermissionsDenied(requestCode, deniedPermissionList);
            }
        }
    }


    public static class Builder implements IWith, IRequestCode, IPermissionResultCallback, IAskFor, IBuild {
        ArrayList<String> permissionsToAsk;
        String message;
        PermissionActionListener permissionListener;
        Activity activity;
        Fragment fragment;
        Context context;
        int requestCode = -1;
        boolean isFragment;

        @Override
        public IRequestCode with(Activity activity) {
            this.activity = activity;
            this.context = activity;
            isFragment = false;
            return this;
        }

        @Override
        public IRequestCode with(Fragment fragment) {
            this.fragment = fragment;
            this.context = fragment.getActivity();
            isFragment = true;
            return this;
        }

        @Override
        public IPermissionResultCallback requestCode(int requestCode) {
            this.requestCode = requestCode;
            return this;
        }

        @Override
        public IAskFor setPermissionResultCallback(PermissionActionListener permissionListener) {
            this.permissionListener = permissionListener;
            return this;
        }

        @Override
        public IBuild askForPermission(PermissionTypes... permission) {
            permissionsToAsk = new ArrayList<>();
            for (PermissionTypes mPermission : permission) {
                switch (mPermission) {
                    case CALENDAR:
                        permissionsToAsk.add(Manifest.permission.WRITE_CALENDAR);
                        break;
                    case CAMERA:
                        permissionsToAsk.add(Manifest.permission.CAMERA);
                        break;
                    case CONTACTS:
                        permissionsToAsk.add(Manifest.permission.READ_CONTACTS);
                        break;
                    case FINE_LOCATION:
                        permissionsToAsk.add(Manifest.permission.ACCESS_FINE_LOCATION);
                        break;
                    case COARSE_LOCATION:
                        permissionsToAsk.add(Manifest.permission.ACCESS_COARSE_LOCATION);
                        break;
                    case MICROPHONE:
                        permissionsToAsk.add(Manifest.permission.RECORD_AUDIO);
                        break;
                    case PHONE:
                        permissionsToAsk.add(Manifest.permission.CALL_PHONE);
                        break;
                    case SENSORS:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                            permissionsToAsk.add(Manifest.permission.BODY_SENSORS);
                        }
                        break;
                    case SMS:
                        permissionsToAsk.add(Manifest.permission.SEND_SMS);
                        break;
                    case STORAGE:
                        permissionsToAsk.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        break;
                    case CALL_LOG:
                        permissionsToAsk.add(Manifest.permission.READ_CALL_LOG);
                        permissionsToAsk.add(Manifest.permission.WRITE_CALL_LOG);
                        break;
                }
            }
            return this;
        }

        @Override
        public GetPermission.IBuild message(String message) {
            this.message = message;
            return this;
        }

        @Override
        public GetPermission build() {
            if (this.permissionListener == null) {
                throw new NullPointerException("Permission listener can not be null");
            } else if (this.context == null) {
                throw new NullPointerException("Context can not be null");
            } else if (this.permissionsToAsk.size() == 0) {
                throw new IllegalArgumentException("Not asking for any permission. At least one permission is expected before calling build()");
            } else if (this.requestCode == -1) {
                throw new IllegalArgumentException("Request code is missing");
            } else {
                return new GetPermission(this);
            }
        }
    }

    /*Interfaces for builder to make some methods must/required*/

    public interface IWith {
        IRequestCode with(Activity activity);

        IRequestCode with(Fragment fragment);
    }

    public interface IRequestCode {
        IPermissionResultCallback requestCode(int requestCode);
    }

    public interface IPermissionResultCallback {
        IAskFor setPermissionResultCallback(PermissionActionListener permissionListener);
    }

    public interface IAskFor {
        IBuild askForPermission(PermissionTypes... permission);
    }

    public interface IBuild {
        IBuild message(String message);

        GetPermission build();
    }
}

