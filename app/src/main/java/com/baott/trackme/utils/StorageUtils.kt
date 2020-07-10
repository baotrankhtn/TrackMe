package com.baott.trackme.utils

import android.content.Context
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception


/* 
 * Created by baotran on 2020-07-09 
 */

object StorageUtils {
    private val FOLDER_ROOT_INTERNAL = "image"

    /**
     * Create image file name from timestamp
     */
    fun createInternalImageFilePath(context: Context, fileName: String): String {
        var pathRoot = ""

        // Create root folder
        try {
            pathRoot = context.filesDir.absolutePath + File.separator + FOLDER_ROOT_INTERNAL
            val resourceFolder = File(pathRoot)
            if (!resourceFolder.exists())
                resourceFolder.mkdirs()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return pathRoot + File.separator + fileName + ".jpg"
    }

    /**
     * Save bitmap to internal storage
     */
    fun saveBitmapToInternal(context: Context, bitmap: Bitmap, fileName: String) {
        val filePath = createInternalImageFilePath(context, fileName)
        val file = File(filePath)
        val outputStream = FileOutputStream(file)
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            outputStream.close()
        }
    }
}