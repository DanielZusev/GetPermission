package com.daniel.permissionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.daniel.getpermission.GetPermission;
import com.daniel.getpermission.PermissionActionListener;
import com.daniel.getpermission.PermissionTypes;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PermissionActionListener {

    private Button btnSinglePerm, btnMultiPerm, btnOpenSettings;
    private RelativeLayout layoutGithub;

    private GetPermission getPermission;

    private static final int REQUEST_MULTIPLE_PERMISSION = 101;
    private static final int REQUEST_SINGLE_PERMISSION = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSinglePerm = findViewById(R.id.btnSinglePerm);
        btnMultiPerm = findViewById(R.id.btnMultiPerm);
        btnOpenSettings = findViewById(R.id.btnOpenSettings);


        btnSinglePerm.setOnClickListener(view ->
                requestSinglePermission()
        );

        btnMultiPerm.setOnClickListener(view ->
                requestMultiplePermission()
        );

        btnOpenSettings.setOnClickListener(view ->
                openAppPermissionSettings()
        );


    }

    private void requestSinglePermission() {
        getPermission = GetPermission.Builder()
                .with(this)
                .requestCode(REQUEST_SINGLE_PERMISSION)
                .setPermissionResultCallback(this)
                .askForPermission(PermissionTypes.CONTACTS)
                .build();
        getPermission.requestPermissions();
    }

    private void requestMultiplePermission() {
        getPermission = GetPermission.Builder()
                .with(this)
                .requestCode(REQUEST_MULTIPLE_PERMISSION)
                .setPermissionResultCallback(this)
                .askForPermission(PermissionTypes.CALENDAR, PermissionTypes.CAMERA, PermissionTypes.FINE_LOCATION)
                .message("These Permissions are required to work app with all functions.")
                .build();
        getPermission.requestPermissions();
    }

    private void openAppPermissionSettings(){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }


    public void onPermissionsGranted(int requestCode, ArrayList<String> acceptedPermissionList) {
        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();

    }

    public void onPermissionsDenied(int requestCode, ArrayList<String> deniedPermissionList) {
        Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        getPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
