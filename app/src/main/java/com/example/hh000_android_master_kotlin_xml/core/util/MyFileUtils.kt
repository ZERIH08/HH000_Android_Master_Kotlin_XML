package com.example.hh000_android_master_kotlin_xml.core.util

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Locale

object MyFileUtils {
    //----------------------------------------------------------------------------------------------
    interface FileCopyEventListener {

        fun onCopyStarted()
        fun onCopyProgressUpdate(progress: Int)
        fun onCopyFinished(outputFilePath: String)
        fun onCopyFailed()
    }

    interface FileScanEventListener {

        fun onScanStarted()
        fun onScannedFile(filePath: String)
        fun onScanFinished()
        fun onScanFailed()
    }

    //----------------------------------------------------------------------------------------------
    data class MyFileInfo(
        var name: String = "",
        var size: String = "0KB",
        var path: String = ""
    )

    data class MyFolderInfo(
        var name: String = "",
        var size: String = "0KB",
        var path: String = "",
        var items: Int = 0
    )

    //----------------------------------------------------------------------------------------------
    fun getFileInfoNameSizePathFromFilePath(filePath: String): MyFileInfo {
        val file =
            File(filePath)
        return MyFileInfo().apply {
            name =
                file.name
            size =
                getSizeStringFromBytesLength(file.length())
            path =
                filePath
        }
    }

    fun getFolderInfoNameSizePathItemsFromFilePath(folderPath: String): MyFolderInfo {
        val folder = File(folderPath)
        return MyFolderInfo().apply {
            name = folder.name
            size = getSizeStringFromBytesLength(
                if (folder.listFiles() != null) {
                    var length = 0L
                    for (file in folder.listFiles()!!) {
                        length += file.length()
                    }
                    length
                } else {
                    folder.length()
                }
            )
            path = folder.path
            items = if (folder.listFiles() != null) {
                folder.listFiles()!!.size
            } else {
                0
            }
        }
    }

    fun getFileInfoNameSizeFromUri(
        contentResolver: ContentResolver,
        uri: Uri
    ): MyFileInfo? {
        return contentResolver.query(
            uri,
            null,
            null,
            null,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val myFileInfo =
                    MyFileInfo().apply {
                        val indexDisplayName =
                            cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        if (indexDisplayName != -1) {
                            name =
                                cursor.getString(indexDisplayName)
                        }
                        val indexSize =
                            cursor.getColumnIndex(OpenableColumns.SIZE)
                        if (indexSize != -1) {
                            size =
                                getSizeStringFromBytesLength(cursor.getLong(indexSize))
                        }
                    }
                myFileInfo
            } else null
        }
    }

    //----------------------------------------------------------------------------------------------
    fun getFileNameWithExtensionFromUri(
        context: Context,
        uri: Uri
    ): String? {
        var fileName: String? =
            null
        try {
            val cursor =
                context.contentResolver.query(
                    uri,
                    null,
                    null,
                    null,
                    null
                )
            cursor?.use { c ->
                if (c.moveToFirst()) {
                    val nameIndex =
                        c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIndex != -1) {
                        fileName =
                            c.getString(nameIndex)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(
                "818181",
                "Exception $e: ",
            )
        }
        return fileName
    }

    fun getFileNameWithoutExtensionFromUri(
        context: Context,
        uri: Uri
    ): String? {
        var fileName: String? =
            null
        val contentResolver: ContentResolver =
            context.contentResolver
        val cursor =
            contentResolver.query(
                uri,
                null,
                null,
                null,
                null
            )
        cursor?.use { c ->
            if (c.moveToFirst()) {
                val nameIndex =
                    c.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    fileName =
                        c.getString(nameIndex)
                }
            }
        }
        val fileExtensionFromMimeType =
            MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri))
        if (fileExtensionFromMimeType != null && fileName != null) {
            val extensionLength =
                fileExtensionFromMimeType.length
            if (fileName!!.endsWith(".$fileExtensionFromMimeType")) {
                fileName =
                    fileName!!.substring(
                        0,
                        fileName!!.length - extensionLength - 1
                    )
            }
        }
        return fileName
    }

    fun getFileExtensionFromUri(
        context: Context,
        uri: Uri
    ): String? {
        val contentResolver: ContentResolver =
            context.contentResolver
        val mimeType: String? =
            contentResolver.getType(uri)
        return mimeType?.let { type ->
            MimeTypeMap.getSingleton().getExtensionFromMimeType(type)
        }
    }

    //----------------------------------------------------------------------------------------------
    fun deleteFolder(file: File) {
        if (file.isDirectory) {
            val files =
                file.listFiles()
            if (files != null) {
                for (childFile in files) {
                    deleteFolder(childFile)
                }
            }
        }
        file.delete()
    }

    //----------------------------------------------------------------------------------------------
    fun getRenamedFileIfFileOfSameNameExistsInDestinationFolder(
        destinationFolder00: File,
        fileNameWithoutExtension00: String,
        fileExtension00: String
    ): File {
        var destinationFile = File(
            destinationFolder00,
            "$fileNameWithoutExtension00.$fileExtension00"
        )

        fun rename(
            destinationFolder: File,
            fileNameWithoutExtension: String,
            fileExtension: String
        ) {
            destinationFile =
                File(
                    destinationFolder,
                    "$fileNameWithoutExtension.$fileExtension"
                )
            if (destinationFile.exists()) {
                rename(
                    destinationFolder,
                    fileNameWithoutExtension + "_Renamed_${(Math.random() * 1000000).toInt()}",
                    fileExtension
                )
            }
        }
        rename(
            destinationFolder00,
            fileNameWithoutExtension00,
            fileExtension00
        )
        return destinationFile
    }

    fun getRenamedFolderIfNonEmptyFolderOfSameNameExistsInDestinationFolder(
        destinationFolder00: File,
        subFolderName00: String,
    ): File {
        var destinationSubFolder = File(
            destinationFolder00,
            subFolderName00
        )

        fun rename(
            destinationFolder: File,
            subFolderName: String,
        ) {
            destinationSubFolder = File(
                destinationFolder,
                subFolderName
            )
            if (destinationSubFolder.exists()) {
                val x = if (destinationSubFolder.listFiles() != null) {
                    destinationFolder.listFiles()!!.size
                } else {
                    0
                }
                if (x > 0) {
                    rename(
                        destinationFolder,
                        subFolderName + "_Renamed_${(Math.random() * 1000000).toInt()}",
                    )
                }
            }
        }
        rename(
            destinationFolder00,
            subFolderName00,
        )
        if (!destinationSubFolder.exists()) {
            destinationSubFolder.mkdirs()
        }
        return destinationSubFolder
    }

    //----------------------------------------------------------------------------------------------
    fun copyFileFromUriToDestinationFolder(
        context: Context,
        uri: Uri,
        destinationFolder: File,
        fileCopyEventListener: FileCopyEventListener?
    ) {
        fun copyWithFileName(filenameX: String) {
            val inputStream =
                context.contentResolver.openInputStream(uri)
            val outputFile =
                File(
                    destinationFolder,
                    filenameX
                )
            val outputStream =
                FileOutputStream(outputFile)
            try {
                val totalSize: Long =
                    (inputStream?.available() ?: 0).toLong()
                val buffer =
                    ByteArray(4 * 1024) // 4 KB buffer size
                var bytesRead: Int
                var copiedBytes =
                    0L
                fileCopyEventListener?.onCopyStarted()
                while (inputStream?.read(buffer)
                        .also {
                            bytesRead =
                                it!!
                        } != -1
                ) {
                    outputStream.write(
                        buffer,
                        0,
                        bytesRead
                    )
                    copiedBytes += bytesRead
                    val progress =
                        (copiedBytes * 100 / totalSize).toInt()
                    fileCopyEventListener?.onCopyProgressUpdate(progress)
                }
                fileCopyEventListener?.onCopyFinished(outputFile.absolutePath)
            } catch (e: IOException) {
                fileCopyEventListener?.onCopyFailed()
                e.printStackTrace()
            } finally {
                try {
                    inputStream?.close()
                    outputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        val fileName =
            getFileNameWithExtensionFromUri(
                context,
                uri
            )
        if (fileName != null) {
            copyWithFileName(fileName)
        } else {
            copyWithFileName("Unknown.pdf")
        }
    }

    //----------------------------------------------------------------------------------------------
    fun scanDeviceToFindFilesOfGivenExtension(
        extension: String,
        fileScanEventListener: FileScanEventListener
    ) {
        var extensionWithoutDot =
            extension
        if (extension.startsWith(".")) {
            extensionWithoutDot =
                extension.drop(1)
        }
        val externalStorage =
            Environment.getExternalStorageDirectory()

        fun searchFiles(
            dir: File,
            extensionWithoutDot: String
        ) {
            dir.listFiles()?.forEach { file ->
                if (file.isDirectory) {
                    searchFiles(
                        file,
                        extensionWithoutDot
                    )
                } else if (file.extension.equals(
                        other = extensionWithoutDot,
                        ignoreCase = true
                    )
                ) {
                    fileScanEventListener.onScannedFile(file.path)
                }
            }
        }
        fileScanEventListener.onScanStarted()
        try {
            searchFiles(
                externalStorage,
                extensionWithoutDot
            )
            fileScanEventListener.onScanFinished()
        } catch (exception: Exception) {
            fileScanEventListener.onScanFailed()
        }
    }

    //----------------------------------------------------------------------------------------------
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
        var unitIndex =
            0
        var size =
            bytesLength.toDouble()
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
}