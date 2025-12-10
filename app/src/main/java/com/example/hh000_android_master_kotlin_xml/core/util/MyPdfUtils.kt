package com.example.hh000_android_master_kotlin_xml.core.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object MyPdfUtils {
    //----------------------------------------------------------------------------------------------
    interface PdfCompressEventListener {

        fun onPdfCompressStarted()
        fun onPdfCompressProgressUpdate(progress: Int)
        fun onPdfCompressFinished(outputFilePath: String)
        fun onPdfCompressFailed()
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

    fun getBitmapOfPdfDocumentPage(
        sourcePdfFilePath: String,
        pageIndex: Int
    ): Bitmap {
        val sourcePdfFile =
            File(sourcePdfFilePath)
        val pdfRenderer =
            PdfRenderer(
                ParcelFileDescriptor.open(
                    sourcePdfFile,
                    ParcelFileDescriptor.MODE_READ_WRITE
                )
            )
        val pdfPage =
            pdfRenderer.openPage(pageIndex)
        val bitmap =
            Bitmap.createBitmap(
                pdfPage.width,
                pdfPage.height,
                Bitmap.Config.ARGB_8888
            )
        val canvas =
            Canvas(bitmap)
        canvas.drawColor(Color.WHITE) // Set white background color
        pdfPage.render(
            bitmap,
            null,
            null,
            PdfRenderer.Page.RENDER_MODE_FOR_PRINT
        )
        pdfPage.close()
        pdfRenderer.close()
        return bitmap
    }

    fun getBitmapOfPdfDocumentPageWithDeviceWidthQuality(
        sourcePdfFilePath: String,
        pageIndex: Int,
        deviceWidth: Int
    ): Bitmap {
        val sourcePdfFile =
            File(sourcePdfFilePath)
        val pdfRenderer =
            PdfRenderer(
                ParcelFileDescriptor.open(
                    sourcePdfFile,
                    ParcelFileDescriptor.MODE_READ_WRITE
                )
            )
        val pdfPage =
            pdfRenderer.openPage(pageIndex)
        // Determine scale factor
        val scaleFactor =
            deviceWidth.toFloat() / pdfPage.width.toFloat()
        val bitmapHeight =
            (pdfPage.height * scaleFactor).toInt()
        val bitmap =
            Bitmap.createBitmap(
                deviceWidth,
                bitmapHeight,
                Bitmap.Config.ARGB_8888
            )
        val canvas =
            Canvas(bitmap)
        canvas.drawColor(Color.WHITE) // Set white background color
        val destRect =
            Rect(
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
    }

    fun createPdfDocumentWithBitmapList(bitmapArrayList: ArrayList<Bitmap>) {
        // Create a new PDF document
        val pdfDocument =
            PdfDocument()
        for (i in bitmapArrayList.indices) {
            // Start a new page
            val pageInfo =
                PdfDocument.PageInfo.Builder(
                    bitmapArrayList[i].width,
                    bitmapArrayList[i].height,
                    i
                )
                    .create()
            val page =
                pdfDocument.startPage(pageInfo)
            // Draw the bitmap onto the page
            val canvas =
                page.canvas
            canvas.drawBitmap(
                bitmapArrayList[i],
                0f,
                0f,
                null
            )
            // Finish the current page
            pdfDocument.finishPage(page)
        }
    }

    //----------------------------------------------------------------------------------------------
    fun compressPdfByUtilizingPdfRendererWorksForImagePDFX1(
        sourcePdfFilePath: String,
        destinationPdfFilePath: String,
        quality: Int,
        pdfCompressEventListener: PdfCompressEventListener
    ) {
        try {
            val sourcePdfFile =
                File(sourcePdfFilePath)
            val destinationPdfFile =
                File(destinationPdfFilePath)
            val destinationPdfDocument =
                PdfDocument()
            val sourcePdfRenderer =
                PdfRenderer(
                    ParcelFileDescriptor.open(
                        sourcePdfFile,
                        ParcelFileDescriptor.MODE_READ_ONLY
                    )
                )
            pdfCompressEventListener.onPdfCompressStarted()
            for (i in 0 until sourcePdfRenderer.pageCount) {
                val sourcePdfPage =
                    sourcePdfRenderer.openPage(i)
                val bitmapForSourcePdfPage =
                    Bitmap.createBitmap(
                        sourcePdfPage.width,
                        sourcePdfPage.height,
                        Bitmap.Config.ARGB_8888
                    )
                val canvasForSourcePdfPage =
                    Canvas(bitmapForSourcePdfPage)

                canvasForSourcePdfPage.drawColor(Color.WHITE) // Set white background color
                sourcePdfPage.render(
                    bitmapForSourcePdfPage,
                    null,
                    null,
                    PdfRenderer.Page.RENDER_MODE_FOR_PRINT
                )
                sourcePdfPage.close()
                val outputStreamForDestinationPdfPage =
                    ByteArrayOutputStream()
                bitmapForSourcePdfPage.compress(
                    Bitmap.CompressFormat.JPEG,
                    quality,
                    outputStreamForDestinationPdfPage
                )
                val byteArrayOfDestinationPdfPage =
                    outputStreamForDestinationPdfPage.toByteArray()
                outputStreamForDestinationPdfPage.close()
                val bitmapOfDestinationPdfPage =
                    BitmapFactory.decodeByteArray(
                        byteArrayOfDestinationPdfPage,
                        0,
                        byteArrayOfDestinationPdfPage.size
                    )
                val destinationPageInfo =
                    PdfDocument.PageInfo.Builder(
                        bitmapOfDestinationPdfPage.width,
                        bitmapOfDestinationPdfPage.height,
                        i
                    ).create()
                val destinationPage =
                    destinationPdfDocument.startPage(destinationPageInfo)
                val canvasOfDestinationPage =
                    destinationPage.canvas
                canvasOfDestinationPage.drawBitmap(
                    bitmapOfDestinationPdfPage,
                    0f,
                    0f,
                    null
                )

                destinationPdfDocument.finishPage(destinationPage)
                val progress =
                    (i / sourcePdfRenderer.pageCount) * 100
                pdfCompressEventListener.onPdfCompressProgressUpdate(progress)
            }
            pdfCompressEventListener.onPdfCompressProgressUpdate(100)
            val fileOutputStreamForDestinationPdfFile =
                FileOutputStream(destinationPdfFile)
            destinationPdfDocument.writeTo(fileOutputStreamForDestinationPdfFile)

            sourcePdfRenderer.close()
            destinationPdfDocument.close()
            pdfCompressEventListener.onPdfCompressFinished(destinationPdfFile.path)
        } catch (e: Exception) {
            pdfCompressEventListener.onPdfCompressFailed()
        }
    }

    //----------------------------------------------------------------------------------------------
    fun compressPdfByUtilizingPdfRendererWorksForImagePDFX2(
        sourcePdfFilePath: String,
        destinationPdfFilePath: String,
        quality: Int,
        pdfCompressEventListener: PdfCompressEventListener
    ) {
        try {
            pdfCompressEventListener.onPdfCompressStarted()
            val fileDescriptor =
                ParcelFileDescriptor.open(
                    File(sourcePdfFilePath),
                    ParcelFileDescriptor.MODE_READ_ONLY
                )
            val renderer =
                PdfRenderer(fileDescriptor)
            val totalPages =
                renderer.pageCount
            val pdfDocument =
                PdfDocument()

            for (i in 0 until totalPages) {
                val page =
                    renderer.openPage(i)
                val bitmap =
                    Bitmap.createBitmap(
                        page.width * quality / 100,
                        page.height * quality / 100,
                        Bitmap.Config.ARGB_8888
                    ) // TODO NOTE THIS LINE QUALITY ?? SCALE ??
                page.render(
                    bitmap,
                    null,
                    null,
                    PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                )
                val pageInfo =
                    PdfDocument.PageInfo.Builder(
                        bitmap.width,
                        bitmap.height,
                        i
                    ).create()
                val pdfPage =
                    pdfDocument.startPage(pageInfo)
                val canvas =
                    pdfPage.canvas
                canvas.drawBitmap(
                    bitmap,
                    0f,
                    0f,
                    null
                )
                pdfDocument.finishPage(pdfPage)

                page.close()
                val progress =
                    (i + 1) * 100 / totalPages
                pdfCompressEventListener.onPdfCompressProgressUpdate(progress)
            }
            val outputStream =
                FileOutputStream(destinationPdfFilePath)
            pdfDocument.writeTo(outputStream)
            pdfDocument.close()
            outputStream.close()

            pdfCompressEventListener.onPdfCompressFinished(destinationPdfFilePath)
        } catch (e: IOException) {
            pdfCompressEventListener.onPdfCompressFailed()
        }
    }
    //----------------------------------------------------------------------------------------------
}