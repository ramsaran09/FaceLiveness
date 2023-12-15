package com.muthuram.faceliveness.studentfacial

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muthuram.faceliveness.models.StudentItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import java.util.TreeMap

class MultiFaceComparisonViewModel : ViewModel() {

    private val _showAttendanceInfo = MutableLiveData<Boolean>()
    val showAttendanceInfo: LiveData<Boolean> = _showAttendanceInfo

    private val _showRegisteredFaceInfo = MutableLiveData<Boolean>()
    val showRegisteredFaceInfo: LiveData<Boolean> = _showRegisteredFaceInfo

    private val _attendanceCount = MutableLiveData<String>()
    val attendanceCount: LiveData<String> = _attendanceCount

    private val _image = MutableLiveData<Pair<Bitmap, String>>()
    val image: LiveData<Pair<Bitmap, String>> = _image

    private val _initCamera = MutableLiveData<Unit>()
    val initCamera: LiveData<Unit> = _initCamera

    private val _verificationStatus = MutableLiveData<VerificationStatus>()
    val verificationStatus : LiveData<VerificationStatus> = _verificationStatus

    private val _cropImage = MutableLiveData<Bitmap>()
    val cropImage : LiveData<Bitmap> = _cropImage

    private val _hasBlinked = MutableLiveData<Boolean>()
    val hasBlinked: LiveData<Boolean> = _hasBlinked

    val studentList = arrayListOf<StudentItem>()
    val registeredStudentFaces = arrayListOf<Bitmap>()
    private val downloadFailedImages = arrayListOf<String>()
    private val studentImages = TreeMap<String, Bitmap> ()

    private val _imageLoadedCount = MutableLiveData<Int>()
    val imageLoadedCount : LiveData<Int> = _imageLoadedCount

    private var failedImage : Int = 0
    private var registeredImage : Int = 0
    private var isInitialImage : Boolean = false

    fun showAttendanceView(isAttendanceView: Boolean) {
        _showAttendanceInfo.value = isAttendanceView
    }

    fun showRegisteredFaceView(isFaceView : Boolean) {
        _showRegisteredFaceInfo.value = isFaceView
    }

    fun setAttendanceCount() {
        val present = studentList.count { it.status == "present" }
        _attendanceCount.value = "${present}/${studentList.size}"
    }

    fun loadUrlToBitmap(url: String, userId: String) {
        viewModelScope.launch {
            val bitmap = async { getBitmapFromUrl(url) }
            val image = bitmap.await()
            if (image != null) {
                studentImages[userId] = image
                Log.d("TAG", "loadUrlToBitmap: ${studentImages.size}")
                if (!isInitialImage || studentImages.size == 1) {
                    _image.value = image to userId
                    isInitialImage = true
                }
            } else {
                downloadFailedImages.add(userId)
                failedImage += 1
            }
        }
    }

    private suspend fun getBitmapFromUrl(faceUrl: String): Bitmap? {
        return try {
            withContext(Dispatchers.IO) {
                val url = URL(faceUrl)
                BitmapFactory.decodeStream(url.openConnection().getInputStream())
            }
        } catch (e: Exception) {
            Log.d("Exception", "getBitmapFromUrl: ${e.message}")
            null
        }
    }

    fun imageVerifyFailed() {
        _verificationStatus.value = VerificationStatus.Failure
    }

    fun imageVerifiedSuccessfully(result : String) {
        _verificationStatus.value = VerificationStatus.Success(result)
        studentList.find { it.userId == result }?.status = "present"
        setAttendanceCount()
    }

    fun showImage(bitmap: Bitmap) {
        _cropImage.value = bitmap
    }

    fun saveRegisteredFace(bitmap: Bitmap) {
        registeredStudentFaces.add(bitmap)
    }

    fun setBlink(isBlinked : Boolean) {
        _hasBlinked.value = isBlinked
    }

    fun sendNextImage(userId: String) {
        _imageLoadedCount.value = registeredImage++
        if (studentImages.isNotEmpty()) {
            val image = studentImages.firstEntry()?.value
            val id = studentImages.firstEntry()?.key
            studentImages.remove(userId)
            if (id != null) _image.value = image!! to id
        }
        if (registeredImage == studentList.size - failedImage) {
            _initCamera.value = Unit
        }

    }

    fun changeVerificationStatus() {
        _verificationStatus.value = VerificationStatus.Loading
    }
}

sealed class VerificationStatus{

    data class Success(val userId : String) : VerificationStatus()

    object Failure : VerificationStatus()

    object Loading : VerificationStatus()
}