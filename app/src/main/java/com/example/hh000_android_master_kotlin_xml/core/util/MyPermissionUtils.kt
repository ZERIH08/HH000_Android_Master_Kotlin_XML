package com.example.hh000_android_master_kotlin_xml.core.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object MyPermissionUtils {

    fun isAllPermissionGrantedIfNotRequestAll(
        context: Context,
        permissions: Array<String>,
        permsRequestCode: Int = 818181
    ): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.e(
                    "818181",
                    "isAllPermissionGrantedIfNotRequestAll: permission = $permission is not granted"
                )
                ActivityCompat.requestPermissions(
                    context as Activity,
                    permissions,
                    permsRequestCode
                )
                return false
            } else {
                Log.e(
                    "818181",
                    "isAllPermissionGrantedIfNotRequestAll: permission = $permission is granted"
                )
            }
        }
        return true
    }

    fun isAllPermissionGranted(
        context: Context,
        permissions: Array<String>
    ): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.e(
                    "818181",
                    "isAllPermissionGranted: permission = $permission is not granted"
                )
                return false
            } else {
                Log.e(
                    "818181",
                    "isAllPermissionGranted: permission = $permission is granted"
                )
            }
        }
        return true
    }

    fun checkAllFileAccessPermissionAndGO(
        mContext: Context,
        myCallBackRun: MyInterfaceUtils.MyCallbackRun,
        dialogTitle: String = "All Files Access Permission needed",
        dialogMessage: String = "This app needs your permission to manage external storage.Without This permission app may not work as intended because app's core functionality depends on it."
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                myCallBackRun.onCallbackRun()
            } else {
                val dialog =
                    MaterialAlertDialogBuilder(mContext).setTitle(dialogTitle)
                        .setMessage(dialogMessage).setPositiveButton("Allow") { dialog, _ ->
                            dialog.dismiss()
                            val intent =
                                Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                            intent.addCategory(Intent.CATEGORY_DEFAULT)
                            intent.data =
                                Uri.parse("package:${mContext.packageName}")
                            mContext.startActivity(intent)
                        }.setNegativeButton("Cancel") { dialog, _ ->
                            dialog.dismiss()
                        }.create()
                dialog.show()
            }
        } else {
            myCallBackRun.onCallbackRun()
        }
    }
}