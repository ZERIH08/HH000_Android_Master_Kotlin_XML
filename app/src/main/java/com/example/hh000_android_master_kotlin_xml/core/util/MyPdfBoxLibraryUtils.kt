package com.example.hh000_android_master_kotlin_xml.core.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.pdmodel.encryption.AccessPermission
import com.tom_roush.pdfbox.pdmodel.encryption.StandardProtectionPolicy
import com.tom_roush.pdfbox.pdmodel.graphics.image.LosslessFactory
import com.tom_roush.pdfbox.pdmodel.graphics.image.PDImageXObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import androidx.core.graphics.createBitmap
import androidx.core.graphics.scale

// TODO NOTE //implementation 'com.tom-roush:pdfbox-android:2.0.27.0'
object MyPdfBoxLibraryUtils {

    //----------------------------------------------------------------------------------------------
    enum class MyPdfCompressionType {
        COMPRESS_WITH_GOOD_FONT_QUALITY,
        COMPRESS_WITH_FLATTING_PAGES_TO_IMAGE_PDF,
        COMPRESS_WITH_FLATTING_PAGES_TO_IMAGE_PDF_AND_DOWN_SCALING
    }

    //----------------------------------------------------------------------------------------------
    interface PdfCompressEventByPdfBoxLibraryListener {
        fun onPdfCompressStarted()
        fun onPdfCompressProgressUpdate(progress: Int)
        fun onPdfCompressFinished(outputFilePath: String)
        fun onPdfCompressFailed()
    }

    interface PdfOperationByPdfBoxLibraryListener {
        fun onSuccess()
        fun onFailure()
    }

    interface PdfOperationWithProgressByPdfBoxLibraryListener {
        fun onSuccess()
        fun onProgress(progress: Int)
        fun onFailure()
    }

    //----------------------------------------------------------------------------------------------
    fun isPdfEncrypted(filePath: String): Boolean {
        var pdfDocument: PDDocument? = null
        var isEncrypted: Boolean
        try {
            pdfDocument = PDDocument.load(File(filePath))
            isEncrypted = pdfDocument.isEncrypted
        } catch (e: IOException) {
            e.printStackTrace()
            isEncrypted = true
        } finally {
            pdfDocument?.close()
        }
        return isEncrypted
    }

    fun isPdfPasswordCorrect(
        filePath: String,
        password: String = ""
    ): Boolean {
        var pdfDocument: PDDocument? = null
        var isPasswordCorrect: Boolean
        try {
            pdfDocument = PDDocument.load(
                File(filePath),
                password
            )
            isPasswordCorrect = true
        } catch (e: IOException) {
            e.printStackTrace()
            isPasswordCorrect = false
        } finally {
            pdfDocument?.close()
        }
        return isPasswordCorrect
    }

    //----------------------------------------------------------------------------------------------
    fun getNumberOfPagesInPDF(sourcePdfFilePath: String): Int {
        val sourcePdfFile =
            File(sourcePdfFilePath)
        val pdfRenderer =
            PdfRenderer(
                ParcelFileDescriptor.open(
                    sourcePdfFile,
                    ParcelFileDescriptor.MODE_READ_WRITE
                )
            )
        return pdfRenderer.pageCount
    }

    //----------------------------------------------------------------------------------------------
    fun getBitmapOfPdfDocumentPageOrNull(
        sourcePdfFilePath: String,
        pageIndex: Int
    ): Bitmap? {
        return try {
            val sourcePdfFile = File(sourcePdfFilePath)
            val pdfRenderer = PdfRenderer(
                ParcelFileDescriptor.open(
                    sourcePdfFile,
                    ParcelFileDescriptor.MODE_READ_WRITE
                )
            )
            val pdfPage = pdfRenderer.openPage(pageIndex)
            val bitmap = createBitmap(pdfPage.width, pdfPage.height)
            val canvas = Canvas(bitmap)
            canvas.drawColor(Color.WHITE) // Set white background color
            pdfPage.render(
                bitmap,
                null,
                null,
                PdfRenderer.Page.RENDER_MODE_FOR_PRINT
            )
            pdfPage.close()
            pdfRenderer.close()
            bitmap
        } catch (e: Exception) {
            null
        }
    }

    fun getBitmapOfPdfDocumentPageWithDeviceWidthQualityOrNull(
        sourcePdfFilePath: String,
        pageIndex: Int,
        deviceWidth: Int
    ): Bitmap? {
        try {
            val sourcePdfFile = File(sourcePdfFilePath)
            val pdfRenderer = PdfRenderer(
                ParcelFileDescriptor.open(
                    sourcePdfFile,
                    ParcelFileDescriptor.MODE_READ_WRITE
                )
            )
            val pdfPage = pdfRenderer.openPage(pageIndex)
            // Determine scale factor
            val scaleFactor = deviceWidth.toFloat() / pdfPage.width.toFloat()
            val bitmapHeight = (pdfPage.height * scaleFactor).toInt()
            val bitmap = createBitmap(deviceWidth, bitmapHeight)
            val canvas = Canvas(bitmap)
            canvas.drawColor(Color.WHITE) // Set white background color
            val destRect = Rect(
                0,
                0,
                deviceWidth,
                bitmapHeight
            )
            pdfPage.render(
                bitmap,
                destRect,
                null,
                PdfRenderer.Page.RENDER_MODE_FOR_PRINT
            )

            pdfPage.close()
            pdfRenderer.close()
            return bitmap
        } catch (e: Exception) {
            return null
        }
    }

    //----------------------------------------------------------------------------------------------
    fun convertPdfPagesToImagesAndSaveToDestinationFolderPath(
        sourcePdfFile: File,
        destinationFolderOfImages: File,
        dpiOfImage: Float = 72f,
        pdfOperationWithProgressByPdfBoxLibraryListener: PdfOperationWithProgressByPdfBoxLibraryListener
    ) {
        try {
            val fileDescriptor = ParcelFileDescriptor.open(
                sourcePdfFile,
                ParcelFileDescriptor.MODE_READ_ONLY
            )
            val pdfRenderer = PdfRenderer(fileDescriptor)

            val numDigits = pdfRenderer.pageCount.toString().length

            for (page in 0 until pdfRenderer.pageCount) {
                val pdfPage = pdfRenderer.openPage(page)

                // Calculate the bitmap size based on the desired DPI
                val bitmapWidth = (pdfPage.width * dpiOfImage / 72f).toInt()
                val bitmapHeight = (pdfPage.height * dpiOfImage / 72f).toInt()

                val bitmap = createBitmap(bitmapWidth, bitmapHeight)

                val bitmapWithBG = createBitmap(bitmap.width, bitmap.height)
                val canvas = Canvas(bitmapWithBG)
                canvas.drawColor(Color.WHITE)
                pdfPage.render(
                    bitmap,
                    null,
                    null,
                    PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                )
                canvas.drawBitmap(
                    bitmap,
                    0f,
                    0f,
                    null
                )

                val pageNumber = String.format(
                    "%0${numDigits}d",
                    page + 1
                )
                val destinationFile = File(
                    destinationFolderOfImages,
                    "page_$pageNumber.png"
                )

                val outputStream = FileOutputStream(destinationFile)
                bitmapWithBG.compress(
                    Bitmap.CompressFormat.JPEG,
                    100,
                    outputStream
                )
                outputStream.close()

                pdfPage.close()

                pdfOperationWithProgressByPdfBoxLibraryListener.onProgress(
                    (((page + 1f) / pdfRenderer.pageCount) * 100f).toInt()
                )
            }
            pdfRenderer.close()
            pdfOperationWithProgressByPdfBoxLibraryListener.onSuccess()
        } catch (e: Exception) {
            pdfOperationWithProgressByPdfBoxLibraryListener.onFailure()
        }
    }

    //----------------------------------------------------------------------------------------------
    fun removePdfPasswordAndSaveToDestinationFilePath(
        sourcePdfFilePath: String,
        destinationPdfFilePath: String,
        passwordShouldBeCorrect: String,
        pdfOperationByPdfBoxLibraryListener: PdfOperationByPdfBoxLibraryListener
    ) {
        try {
            val document = PDDocument.load(
                File(sourcePdfFilePath),
                passwordShouldBeCorrect
            )
            document.isAllSecurityToBeRemoved = true
            document.save(destinationPdfFilePath)
            document.close()
            pdfOperationByPdfBoxLibraryListener.onSuccess()
        } catch (e: IOException) {
            pdfOperationByPdfBoxLibraryListener.onFailure()
        }
    }

    fun addPdfPasswordAndSaveToDestinationFilePath(
        sourcePdfFilePath: String,
        destinationPdfFilePath: String,
        passwordShouldBeCorrect: String,
        pdfOperationByPdfBoxLibraryListener: PdfOperationByPdfBoxLibraryListener
    ) {
        try {
            val document = PDDocument.load(File(sourcePdfFilePath))
            val accessPermission = AccessPermission()
            val policy = StandardProtectionPolicy(
                passwordShouldBeCorrect,
                passwordShouldBeCorrect,
                accessPermission
            )
            policy.encryptionKeyLength = 128
            policy.permissions = accessPermission
            document.protect(policy)
            document.save(destinationPdfFilePath)
            document.close()
            pdfOperationByPdfBoxLibraryListener.onSuccess()
        } catch (e: IOException) {
            pdfOperationByPdfBoxLibraryListener.onFailure()
        }
    }

    fun changePdfPasswordAndSaveToDestinationFilePath(
        sourcePdfFilePath: String,
        destinationPdfFilePath: String,
        passwordOldShowBeCorrect: String,
        passwordNewValid: String,
        pdfOperationByPdfBoxLibraryListener: PdfOperationByPdfBoxLibraryListener
    ) {
        try {
            val document = PDDocument.load(
                File(sourcePdfFilePath),
                passwordOldShowBeCorrect
            )
            val accessPermission = AccessPermission()
            val policy = StandardProtectionPolicy(
                passwordNewValid,
                passwordNewValid,
                accessPermission
            )
            policy.encryptionKeyLength = 128
            policy.permissions = accessPermission
            document.protect(policy)
            document.save(destinationPdfFilePath)
            document.close()
            pdfOperationByPdfBoxLibraryListener.onSuccess()
        } catch (e: IOException) {
            pdfOperationByPdfBoxLibraryListener.onFailure()
        }

    }

    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    fun compressPDF0(
        sourcePdfFilePath: String,
        destinationPdfFilePath: String,
        quality: Int = 80,
        isPasswordProtected: Boolean = false,
        isToKeepPassword: Boolean = false,
        passwordShouldBeCorrect: String = "",
        myPdfCompressionType: MyPdfCompressionType = MyPdfCompressionType.COMPRESS_WITH_GOOD_FONT_QUALITY,
        pdfCompressEventByPdfBoxLibraryListener: PdfCompressEventByPdfBoxLibraryListener
    ) {
        when (myPdfCompressionType) {
            MyPdfCompressionType.COMPRESS_WITH_GOOD_FONT_QUALITY -> {
                compressPDF1(
                    sourcePdfFilePath = sourcePdfFilePath,
                    destinationPdfFilePath = destinationPdfFilePath,
                    quality = quality,
                    isPasswordProtected = isPasswordProtected,
                    isToKeepPassword = isToKeepPassword,
                    passwordShouldBeCorrect = passwordShouldBeCorrect,
                    pdfCompressEventByPdfBoxLibraryListener = pdfCompressEventByPdfBoxLibraryListener
                )
            }

            MyPdfCompressionType.COMPRESS_WITH_FLATTING_PAGES_TO_IMAGE_PDF -> {
                compressPDF2(
                    sourcePdfFilePath = sourcePdfFilePath,
                    destinationPdfFilePath = destinationPdfFilePath,
                    quality = quality,
                    isPasswordProtected = isPasswordProtected,
                    isToKeepPassword = isToKeepPassword,
                    passwordShouldBeCorrect = passwordShouldBeCorrect,
                    pdfCompressEventByPdfBoxLibraryListener = pdfCompressEventByPdfBoxLibraryListener
                )
            }

            MyPdfCompressionType.COMPRESS_WITH_FLATTING_PAGES_TO_IMAGE_PDF_AND_DOWN_SCALING -> {
                compressPDF3(
                    sourcePdfFilePath = sourcePdfFilePath,
                    destinationPdfFilePath = destinationPdfFilePath,
                    quality = quality,
                    isPasswordProtected = isPasswordProtected,
                    isToKeepPassword = isToKeepPassword,
                    passwordShouldBeCorrect = passwordShouldBeCorrect,
                    pdfCompressEventByPdfBoxLibraryListener = pdfCompressEventByPdfBoxLibraryListener
                )
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    private fun compressPDF1(
        sourcePdfFilePath: String,
        destinationPdfFilePath: String,
        quality: Int,
        isPasswordProtected: Boolean,
        isToKeepPassword: Boolean,
        passwordShouldBeCorrect: String,
        pdfCompressEventByPdfBoxLibraryListener: PdfCompressEventByPdfBoxLibraryListener
    ) {
        try {
            pdfCompressEventByPdfBoxLibraryListener.onPdfCompressStarted()
            val document =
                if (isPasswordProtected) {
                    val document0 = PDDocument.load(
                        File(sourcePdfFilePath),
                        passwordShouldBeCorrect
                    )
                    document0.isAllSecurityToBeRemoved = true
                    document0
                } else {
                    PDDocument.load(File(sourcePdfFilePath))
                }
            val pdfPages = document.pages
            val totalPages = pdfPages.count
            for ((index, page) in pdfPages.withIndex()) {
                val resources = page.resources
                val xObjectMap = resources.xObjectNames
                val xObjectMapSize = xObjectMap.toList().size

                for ((x, name) in xObjectMap.withIndex()) {
                    val xObject = resources.getXObject(name)
                    if (xObject is PDImageXObject) {
                        val image = xObject.image
                        val downscaledImage = image.scale(
                            (image.width * quality / 100),
                            (image.height * quality / 100)
                        )
                        val downscaledXObject = LosslessFactory.createFromImage(
                            document,
                            downscaledImage
                        )
                        resources.put(
                            name,
                            downscaledXObject
                        )
                    }
                    val progressX1 = ((x.toFloat() + 1) / xObjectMapSize.toFloat())
                    val progressX2 = ((index + progressX1) * 100) / totalPages
                    pdfCompressEventByPdfBoxLibraryListener.onPdfCompressProgressUpdate(progressX2.toInt())
                }
                val progressX0 = (index + 1) * 100 / totalPages
                pdfCompressEventByPdfBoxLibraryListener.onPdfCompressProgressUpdate(progressX0)
            }
            if (isPasswordProtected && isToKeepPassword) {
                // Before saving the document, reapply the security
                val accessPermission = AccessPermission()
                val policy = StandardProtectionPolicy(
                    passwordShouldBeCorrect,
                    passwordShouldBeCorrect,
                    accessPermission
                )
                policy.encryptionKeyLength = 128
                policy.permissions = accessPermission
                document.protect(policy)
            }

            document.save(destinationPdfFilePath)
            document.close()

            pdfCompressEventByPdfBoxLibraryListener.onPdfCompressFinished(destinationPdfFilePath)
        } catch (e: IOException) {
            pdfCompressEventByPdfBoxLibraryListener.onPdfCompressFailed()
        }
    }

    //----------------------------------------------------------------------------------------------
    private fun compressPDF2( // TODO : COMPRESS CODE FOR PASSWORD PROTECTED PDF
        sourcePdfFilePath: String,
        destinationPdfFilePath: String,
        quality: Int,
        isPasswordProtected: Boolean,
        isToKeepPassword: Boolean,
        passwordShouldBeCorrect: String,
        pdfCompressEventByPdfBoxLibraryListener: PdfCompressEventByPdfBoxLibraryListener
    ) {
        try {
            val sourcePdfFile = File(sourcePdfFilePath)
            val destinationPdfFile = File(destinationPdfFilePath)
            val destinationPdfDocument = PdfDocument()
            val sourcePdfRenderer =
                PdfRenderer(
                    ParcelFileDescriptor.open(
                        sourcePdfFile,
                        ParcelFileDescriptor.MODE_READ_ONLY
                    )
                )
            pdfCompressEventByPdfBoxLibraryListener.onPdfCompressStarted()
            for (i in 0 until sourcePdfRenderer.pageCount) {
                val sourcePdfPage = sourcePdfRenderer.openPage(i)
                val bitmapForSourcePdfPage = createBitmap(sourcePdfPage.width, sourcePdfPage.height)
                val canvasForSourcePdfPage = Canvas(bitmapForSourcePdfPage)

                canvasForSourcePdfPage.drawColor(Color.WHITE) // Set white background color
                sourcePdfPage.render(
                    bitmapForSourcePdfPage,
                    null,
                    null,
                    PdfRenderer.Page.RENDER_MODE_FOR_PRINT
                )
                sourcePdfPage.close()
                val outputStreamForDestinationPdfPage = ByteArrayOutputStream()
                bitmapForSourcePdfPage.compress(
                    Bitmap.CompressFormat.JPEG,
                    quality,
                    outputStreamForDestinationPdfPage
                )
                val byteArrayOfDestinationPdfPage = outputStreamForDestinationPdfPage.toByteArray()
                outputStreamForDestinationPdfPage.close()
                val bitmapOfDestinationPdfPage = BitmapFactory.decodeByteArray(
                    byteArrayOfDestinationPdfPage,
                    0,
                    byteArrayOfDestinationPdfPage.size
                )
                val destinationPageInfo = PdfDocument.PageInfo.Builder(
                    bitmapOfDestinationPdfPage.width,
                    bitmapOfDestinationPdfPage.height,
                    i
                ).create()
                val destinationPage = destinationPdfDocument.startPage(destinationPageInfo)
                val canvasOfDestinationPage = destinationPage.canvas
                canvasOfDestinationPage.drawBitmap(
                    bitmapOfDestinationPdfPage,
                    0f,
                    0f,
                    null
                )

                destinationPdfDocument.finishPage(destinationPage)
                val progress = ((i + 1) / sourcePdfRenderer.pageCount) * 100
                pdfCompressEventByPdfBoxLibraryListener.onPdfCompressProgressUpdate(progress)
            }
            val fileOutputStreamForDestinationPdfFile = FileOutputStream(destinationPdfFile)
            destinationPdfDocument.writeTo(fileOutputStreamForDestinationPdfFile)

            sourcePdfRenderer.close()
            destinationPdfDocument.close()
            pdfCompressEventByPdfBoxLibraryListener.onPdfCompressFinished(destinationPdfFile.path)
        } catch (e: Exception) {
            pdfCompressEventByPdfBoxLibraryListener.onPdfCompressFailed()
        }
    }

    //----------------------------------------------------------------------------------------------
    private fun compressPDF3( // TODO : COMPRESS CODE FOR PASSWORD PROTECTED PDF
        sourcePdfFilePath: String,
        destinationPdfFilePath: String,
        quality: Int,
        isPasswordProtected: Boolean,
        isToKeepPassword: Boolean,
        passwordShouldBeCorrect: String,
        pdfCompressEventByPdfBoxLibraryListener: PdfCompressEventByPdfBoxLibraryListener
    ) {
        try {
            pdfCompressEventByPdfBoxLibraryListener.onPdfCompressStarted()
            val fileDescriptor = ParcelFileDescriptor.open(
                File(sourcePdfFilePath),
                ParcelFileDescriptor.MODE_READ_ONLY
            )
            val renderer = PdfRenderer(fileDescriptor)
            val totalPages = renderer.pageCount
            val pdfDocument = PdfDocument()

            for (i in 0 until totalPages) {
                val page = renderer.openPage(i)
                val bitmap = createBitmap(page.width * quality / 100, page.height * quality / 100) // TODO NOTE THIS LINE QUALITY ?? SCALE ??
                page.render(
                    bitmap,
                    null,
                    null,
                    PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                )
                val pageInfo = PdfDocument.PageInfo.Builder(
                    bitmap.width,
                    bitmap.height,
                    i
                ).create()
                val pdfPage = pdfDocument.startPage(pageInfo)
                val canvas = pdfPage.canvas
                canvas.drawBitmap(
                    bitmap,
                    0f,
                    0f,
                    null
                )
                pdfDocument.finishPage(pdfPage)
                page.close()
                val progress =
                    ((i + 1) / totalPages) * 100
                pdfCompressEventByPdfBoxLibraryListener.onPdfCompressProgressUpdate(progress)
            }
            val outputStream = FileOutputStream(destinationPdfFilePath)
            pdfDocument.writeTo(outputStream)
            pdfDocument.close()
            outputStream.close()

            pdfCompressEventByPdfBoxLibraryListener.onPdfCompressFinished(destinationPdfFilePath)
        } catch (e: IOException) {
            pdfCompressEventByPdfBoxLibraryListener.onPdfCompressFailed()
        }
    }
    //----------------------------------------------------------------------------------------------
}