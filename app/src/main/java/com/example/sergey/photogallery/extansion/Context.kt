package com.example.sergey.photogallery.extansion

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat.requestPermissions
import android.support.v4.content.ContextCompat.checkSelfPermission
import android.widget.Toast

fun Context?.isPermissionIsDenied(permission: String) = runCatching {
    this != null && checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED
}.getOrDefault(true)

fun Activity.requestPermissions(permissions: List<String>, requestCode: Int) =
        requestPermissions(this, permissions.toTypedArray(), requestCode)

fun Context?.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
