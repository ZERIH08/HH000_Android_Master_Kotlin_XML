package com.example.hh000_android_master_kotlin_xml.core.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

object MyFileProviderUtils {

    fun getContentUriFromFilePath(
        context: Context,
        filePath: String
    ): Uri {
        return FileProvider.getUriForFile(
            context,
            context.packageName + "." + "provider",
            File(filePath)
        )
    }
}