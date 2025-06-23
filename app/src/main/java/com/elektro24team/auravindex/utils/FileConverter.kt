package com.elektro24team.auravindex.utils

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

object FileConverter {

    fun getMultipartBodyPart(
        context: Context,
        imageUri: Uri,
        formFieldName: String = "user_img"
    ): MultipartBody.Part? {
        if (imageUri == null) return null
        return try {
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(imageUri) ?: return null
            val tempFile = File.createTempFile("temp_image", ".jpg", context.cacheDir)

            inputStream?.use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            val requestFile = tempFile.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData(formFieldName, tempFile.name, requestFile)
        }catch (e:Exception){
            e.printStackTrace()
            null
        }
    }
}