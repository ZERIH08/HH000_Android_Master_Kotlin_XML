package com.example.hh000_android_master_kotlin_xml.core.util

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.view.WindowMetrics
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.math.roundToInt

object MyBaseUtils {

    //----------------------------------------------------------------------------------------------
    fun shareThisApp(context: Context, appName: String) {
        try {
            val shareMessage =
                "\nLet me recommend you this application\n\n" + "https://play.google.com/store/apps/details?id=" + context.applicationContext.packageName + ""
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "text/plain"
            shareIntent.putExtra(
                Intent.EXTRA_SUBJECT,
                appName
            )
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                shareMessage
            )
            context.startActivity(
                Intent.createChooser(
                    shareIntent,
                    "Choose one"
                )
            )
        } catch (e: Exception) {
            //e.toString();
        }
    }

    fun rateThisApp(context: Context) {
        val uriString = "market://details?id=${context.applicationContext.packageName}"
        val uri = Uri.parse(uriString)
        val myAppLinkToMarket = Intent(
            Intent.ACTION_VIEW,
            uri
        )

        try {
            context.startActivity(myAppLinkToMarket)
        } catch (e: ActivityNotFoundException) {
            try {
                val webUriString =
                    "http://play.google.com/store/apps/details?id=${context.applicationContext.packageName}"
                val webUri = Uri.parse(webUriString)
                val myAppLinkToWebMarket = Intent(
                    Intent.ACTION_VIEW,
                    webUri
                )
                context.startActivity(myAppLinkToWebMarket)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    context,
                    "Unable to find market app",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun openChromeCustomTabUrl(
        webUrl: String,
        context: Context
    ) {
        val packageNameOfBrowser = "com.android.chrome"

        val customTabsIntent = CustomTabsIntent.Builder().build().apply {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            if (context.packageManager.getLaunchIntentForPackage(packageNameOfBrowser) != null) {
                intent.setPackage(packageNameOfBrowser)
            }
        }

        try {
            customTabsIntent.launchUrl(
                context,
                Uri.parse(webUrl)
            )
        } catch (e: Exception) {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(webUrl)
            ).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            try {
                context.startActivity(browserIntent)
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "Unable to find internet browser",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    fun getVersionName(context: Context): String {
        var version = ""
        try {
            val packageInfo = context.packageManager.getPackageInfo(
                context.packageName,
                0
            )
            version = packageInfo.versionName.toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return version
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun getVersionCode(context: Context): Long {
        var versionCode: Long = 0
        try {
            val packageInfo = context.packageManager.getPackageInfo(
                context.packageName,
                0
            )
            versionCode = packageInfo.longVersionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionCode
    }

    //----------------------------------------------------------------------------------------------
    fun createExitAlertDialogForActivity(activity: Activity) {
        AlertDialog.Builder(activity)
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes") { _, _ ->
                activity.finish()
            }
            .setNegativeButton(
                "No",
                null
            )
            .show()
    }

    //----------------------------------------------------------------------------------------------
    fun showKeyBoard(
        context: Context,
        editTextSearchView: EditText
    ) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(
            editTextSearchView,
            InputMethodManager.SHOW_IMPLICIT
        )
    }

    fun hideKeyBoard(
        context: Context,
        editTextSearchView: EditText
    ) {
        val inputMethodManager =
            context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            editTextSearchView.windowToken,
            0
        )
    }

    fun hideKeyBoardX(context: Context) {
        if (context is Activity) {
            val activity: Activity = context
            val focusedView = activity.currentFocus
            if (focusedView != null) {
                focusedView.clearFocus()
                val inputMethodManager =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(
                    focusedView.windowToken,
                    0
                )
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    fun dpx(
        context: Context,
        dp: Int
    ): Int {
        return (dp * context.resources.displayMetrics.density).roundToInt()
    }

    fun pxDp(
        context: Context,
        dp: Int
    ): Int {
        return (dp * context.resources.displayMetrics.density).roundToInt()
    }

    fun getDateStringFromMilliSecondsSinceEpoch(milliSecondsSinceTheEpoch: Long): String {
        return SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss",
            Locale.getDefault()
        ).format(Date(milliSecondsSinceTheEpoch))
    }

    fun getSizeStringFromBytesLength(bytesLength: Long): String {
        val units =
            arrayOf(
                "B",
                "KB",
                "MB",
                "GB",
                "TB",
                "PB",
                "EB",
                "ZB",
                "YB"
            )
        var unitIndex = 0
        var size = bytesLength.toDouble()
        while (size >= 1024 && unitIndex < units.size - 1) {
            size /= 1024.0
            unitIndex++
        }
        return String.format(
            Locale.ENGLISH,
            "%.2f %s",
            size,
            units[unitIndex]
        )
    }

    //----------------------------------------------------------------------------------------------
    fun getMaxPossibleSideOfApplication(activity: Activity): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics: WindowMetrics = activity.windowManager.maximumWindowMetrics
            val height = abs(windowMetrics.bounds.height())
            val width = abs(windowMetrics.bounds.width())
            val maxSide =
                if (height >= width) {
                    height
                } else {
                    width
                }
            return maxSide
        }
        return 10000 // To do ??????/
    }

    //----------------------------------------------------------------------------------------------
    fun isDigitsOnly(input: String): Boolean {
        val regex = Regex("[0-9]+")
        return regex.matches(input)
    }

    //----------------------------------------------------------------------------------------------
    fun compressAndGetBitmap(
        bitmap: Bitmap,
        quality: Int
    ): Bitmap {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(
            Bitmap.CompressFormat.JPEG,
            quality,
            outputStream
        )
        val compressedByteArray = outputStream.toByteArray()
        outputStream.close()
        return BitmapFactory.decodeByteArray(
            compressedByteArray,
            0,
            compressedByteArray.size
        )
    }

    //----------------------------------------------------------------------------------------------
    fun getMimeType(filePath: String): String? {
        val extension = MimeTypeMap.getFileExtensionFromUrl(filePath)
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    }
    //----------------------------------------------------------------------------------------------
}