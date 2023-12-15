package com.muthuram.faceliveness.helper

import android.content.ContentResolver
import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.util.Base64OutputStream
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs


const val ANIMATION_FAST_MILLIS = 50L

fun View.simulateClick(delay: Long = ANIMATION_FAST_MILLIS) {
    performClick()
    isPressed = true
    invalidate()
    postDelayed({
        invalidate()
        isPressed = false
    }, delay)
}

fun getFile(context: Context?): File {
    val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
    val date: String = formatter.format(Date()).toString()
    return File(context?.filesDir, "$date.png")
}

fun View?.removeSelf() {
    this ?: return
    val parentView = parent as? ViewGroup ?: return
    parentView.removeView(this)
}


@RequiresApi(Build.VERSION_CODES.O)
fun Context.fileUriToBase64(uri: Uri): String? {
    var encodedBase64: String? = ""
    try {
        val bytes = readBytes(uri, contentResolver)
        encodedBase64 = String(Base64.getEncoder().encode(bytes))
    } catch (e1: IOException) {
        e1.printStackTrace()
    }
    return encodedBase64
}

/**
 * Read bytes.
 *
 * @param uri
 * the uri
 * @param resolver
 * the resolver
 * @return the byte[]
 * @throws IOException
 * Signals that an I/O exception has occurred.
 */
@Throws(IOException::class)
private fun readBytes(uri: Uri, resolver: ContentResolver): ByteArray {
    // this dynamically extends to take the bytes you read
    val inputStream: InputStream? = resolver.openInputStream(uri)
    val byteBuffer = ByteArrayOutputStream()

    // this is storage overwritten on each iteration with bytes
    val bufferSize = 1024
    val buffer = ByteArray(bufferSize)

    // we need to know how may bytes were read to write them to the
    // byteBuffer
    var len = 0
    while (inputStream?.read(buffer).also {
            if (it != null) {
                len = it
            }
        } != -1) {
        byteBuffer.write(buffer, 0, len)
    }

    // and then we can return your byte array.
    return byteBuffer.toByteArray()
}

@Throws(IOException::class)
fun readBytes(context: Context, uri: Uri): ByteArray? =
    context.contentResolver.openInputStream(uri)?.buffered()?.use { it.readBytes() }

fun convertImageFileToBase64(imageFile: File): String {
    return ByteArrayOutputStream().use { outputStream ->
        Base64OutputStream(outputStream, 0).use { base64FilterStream ->
            imageFile.inputStream().use { inputStream ->
                inputStream.copyTo(base64FilterStream)
            }
        }
        return@use outputStream.toString()
    }
}

fun getTransformationMatrix(
    srcWidth: Int,
    srcHeight: Int,
    dstWidth: Int,
    dstHeight: Int,
    applyRotation: Int,
    maintainAspectRatio: Boolean
): Matrix {
    val matrix = Matrix()
    if (applyRotation != 0) {

        // Translate so center of image is at origin.
        matrix.postTranslate(-srcWidth / 2.0f, -srcHeight / 2.0f)

        // Rotate around origin.
        matrix.postRotate(applyRotation.toFloat())
    }

    // Account for the already applied rotation, if any, and then determine how
    // much scaling is needed for each axis.
    val transpose = (abs(applyRotation) + 90) % 180 == 0
    val inWidth = if (transpose) srcHeight else srcWidth
    val inHeight = if (transpose) srcWidth else srcHeight

    // Apply scaling if necessary.
    if (inWidth != dstWidth || inHeight != dstHeight) {
        val scaleFactorX = dstWidth / inWidth.toFloat()
        val scaleFactorY = dstHeight / inHeight.toFloat()
        if (maintainAspectRatio) {
            // Scale by minimum factor so that dst is filled completely while
            // maintaining the aspect ratio. Some image may fall off the edge.
            val scaleFactor = scaleFactorX.coerceAtLeast(scaleFactorY)
            matrix.postScale(scaleFactor, scaleFactor)
        } else {
            // Scale exactly to fill dst from src.
            matrix.postScale(scaleFactorX, scaleFactorY)
        }
    }
    if (applyRotation != 0) {
        // Translate back from origin centered reference to destination frame.
        matrix.postTranslate(dstWidth / 2.0f, dstHeight / 2.0f)
    }
    return matrix
}


fun bitmapResize(bitmap: Bitmap, scale: Float): Bitmap {
    val width = bitmap.width
    val height = bitmap.height
    val matrix = Matrix()
    matrix.postScale(scale, scale)
    return Bitmap.createBitmap(
        bitmap, 0, 0, width, height, matrix, true
    )
}

@Throws(IOException::class)
fun loadModelFile(assets: AssetManager, modelFilename: String): MappedByteBuffer {
    val fileDescriptor = assets.openFd(modelFilename)
    val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
    val fileChannel = inputStream.channel
    val startOffset = fileDescriptor.startOffset
    val declaredLength = fileDescriptor.declaredLength
    return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
}

fun Bitmap.rotate(degrees: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

fun <T> T.toJson(): String = Gson().toJson(this)

inline fun <reified T> Gson.fromJson(json: String?): T? {
    json ?: return null
    return fromJson<T>(json, object : TypeToken<T>() {}.type)
}

fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray {
    val size: Int = bitmap.rowBytes * bitmap.height
    val byteBuffer = ByteBuffer.allocate(size)
    bitmap.copyPixelsToBuffer(byteBuffer)
    return byteBuffer.array()
}

@RequiresApi(Build.VERSION_CODES.O)
fun ByteArray.toBase64(): String =
    String(Base64.getEncoder().encode(this))

fun Fragment.replaceFragment(navId: Int, fragment: Fragment, addToBackStack: Boolean = false) {
    try {
        val replace =
            parentFragmentManager.beginTransaction().replace(navId, fragment, "TAG")
        if (addToBackStack) {
            replace.addToBackStack("AddToStack")
        }
        replace.commit()
    } catch (exp: IllegalStateException) {
        Log.e("TAG", "replaceFragment: ", exp)
    } catch (exp: Exception) {
        Log.e("TAG", "replaceFragment: ", exp)
    }
}

fun bitmapResizer(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap? {
    val scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888)
    val ratioX = newWidth / bitmap.width.toFloat()
    val ratioY = newHeight / bitmap.height.toFloat()
    val middleX = newWidth / 2.0f
    val middleY = newHeight / 2.0f
    val scaleMatrix = Matrix()
    scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
    val canvas = Canvas(scaledBitmap)
    canvas.setMatrix(scaleMatrix)
    canvas.drawBitmap(
        bitmap,
        middleX - bitmap.width / 2,
        middleY - bitmap.height / 2,
        Paint(Paint.FILTER_BITMAP_FLAG)
    )
    return scaledBitmap
}

fun Bitmap.simulate3DTransformation(topAngle: Boolean): Bitmap {
    val width = this.width
    val height = this.height

    val resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)


    for (y in 0 until height) {
        for (x in 0 until width) {
            val newX = x.toFloat()
            val newY = if (topAngle) {
                // Simulate top angle transformation
                y - 0.1f * x
            } else {
                // Simulate bottom angle transformation
                y + 0.1f * x
            }

            if (newY >= 0 && newY < height) {
                val color = this.getPixel(x, newY.toInt())
                resultBitmap.setPixel(newX.toInt(), newY.toInt(), color)
            }
        }
    }

    return resultBitmap
}