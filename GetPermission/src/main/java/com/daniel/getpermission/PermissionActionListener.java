package com.daniel.getpermission;

import java.util.ArrayList;

public interface PermissionActionListener {
    void onPermissionsGranted(int permissionCode, ArrayList<String> acceptedPermissionList);

    void onPermissionsDenied(int permissionCode, ArrayList<String> deniedPermissionList);
}
