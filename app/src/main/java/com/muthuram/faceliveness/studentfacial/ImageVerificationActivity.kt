package com.muthuram.faceliveness.studentfacial

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.muthuram.faceliveness.R
import com.muthuram.faceliveness.SelectionActivity
import com.muthuram.faceliveness.databinding.ActivityImageVerificationBinding
import com.muthuram.faceliveness.helper.bitmapResizer
import com.muthuram.faceliveness.helper.fromJson
import com.muthuram.faceliveness.helper.getTransformationMatrix
import com.muthuram.faceliveness.helper.toJson
import com.muthuram.faceliveness.models.StudentItem
import com.muthuram.faceliveness.tensorHelper.Recognition
import com.muthuram.faceliveness.tensorHelper.SimilarityClassifier
import com.muthuram.faceliveness.tensorHelper.TFLiteObjectDetectionAPIModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.LinkedList

class ImageVerificationActivity : AppCompatActivity() {

    private lateinit var binding : ActivityImageVerificationBinding
    private val viewModel: MultiFaceComparisonViewModel by viewModels()
    private var detector: SimilarityClassifier? = null
    private lateinit var faceDetector: FaceDetector
    private var previewWidth = 480
    private var previewHeight = 640
    private var frameToCropTransform: Matrix? = null
    private var cropToFrameTransform: Matrix? = null
    private var portraitBmp: Bitmap? = null
    private var faceBmp: Bitmap? = null
    private var croppedBitmap: Bitmap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpUi()
        setUpControls()
        processIntent(intent)
        replaceFragment(R.id.navHostCamera, CameraFragment.newInstance())
    }

    private fun processIntent(intent: Intent?) {
        intent ?: return
        val data = intent.getStringExtra(SelectionActivity.KEY_STUDENT_LIST)
        if (data != null) {
            val studentList = Gson().fromJson<ArrayList<StudentItem>>(data)
            Log.d("MultiFace", "processIntent: $studentList")
            if (studentList != null) {
                viewModel.studentList.addAll(studentList)
                registerStudentFace()
            }
        }
        viewModel.setAttendanceCount()
    }

    private fun setUpUi() {
        viewModel.image.observe(this) {
            registerFace(it.first, it.second)
        }
    }

    private fun setUpControls() {
        val highAccuracyOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()

        faceDetector = FaceDetection.getClient(highAccuracyOpts)

        detector = TFLiteObjectDetectionAPIModel.create(
            assets,
            TF_OD_API_MODEL_FILE,
            TF_OD_API_LABELS_FILE,
            TF_OD_API_INPUT_SIZE,
            TF_OD_API_IS_QUANTIZED
        )
        val cropW: Int = (previewWidth / 2.0).toInt()
        val cropH: Int = (previewHeight / 2.0).toInt()

        croppedBitmap = Bitmap.createBitmap(cropW, cropH, Bitmap.Config.ARGB_8888)

        portraitBmp = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.ARGB_8888)
        faceBmp = Bitmap.createBitmap(
            TF_OD_API_INPUT_SIZE,
            TF_OD_API_INPUT_SIZE,
            Bitmap.Config.ARGB_8888
        )

        frameToCropTransform = getTransformationMatrix(
            previewWidth, previewHeight,
            cropW, cropH,
            0, false
        )
        cropToFrameTransform = Matrix()
        frameToCropTransform?.invert(cropToFrameTransform)
    }

    fun verifyImage(bitmap: Bitmap) {
        val canvas = Canvas(croppedBitmap!!)
        canvas.drawBitmap(bitmap, frameToCropTransform!!, null)
        viewModel.showImage(croppedBitmap!!)
        onFaceDetected(croppedBitmap!!, bitmap,true,null)
    }

    private fun onFaceDetected(image: Bitmap, scaledBitmap: Bitmap,isVerify: Boolean,userId: String?) {
        faceDetector.process(InputImage.fromBitmap(image,0))
            .addOnSuccessListener { faces ->
                Log.d(
                    "TAG",
                    "face detection started: ${faces.size}"
                )
                if (faces.size == 0) {
                    runOnUiThread {
                        viewModel.imageVerifyFailed()
                    }
                    return@addOnSuccessListener
                }
                onFacesDetected(faces[0], scaledBitmap,isVerify,userId)
            }.addOnFailureListener {
                Log.d("TAG", "face detection : ${it.message}")
            }
    }

    private fun onFacesDetected(face: Face, scaledBitmap: Bitmap,isVerify : Boolean,userId: String?) {
        val mappedRecognitions: MutableList<Recognition> = LinkedList<Recognition>()
        val sourceW = scaledBitmap.width
        val sourceH = scaledBitmap.height
        val targetW = portraitBmp!!.width
        val targetH = portraitBmp!!.height
        val transform: Matrix = createTransform(
            sourceW,
            sourceH,
            targetW,
            targetH,
        )
        val cv = Canvas(portraitBmp!!)

        // draws the original image in portrait mode.
        cv.drawBitmap(scaledBitmap, transform, null)
        val cvFace = Canvas(faceBmp!!)
        val boundingBox = RectF(face.boundingBox)
        cropToFrameTransform?.mapRect(boundingBox)
        val faceBB = RectF(boundingBox)
        transform.mapRect(faceBB)
        val sx = TF_OD_API_INPUT_SIZE.toFloat() / faceBB.width()
        val sy = TF_OD_API_INPUT_SIZE.toFloat() / faceBB.height()
        val matrix = Matrix()
        matrix.postTranslate(-faceBB.left, -faceBB.top)
        matrix.postScale(sx, sy)
        cvFace.drawBitmap(portraitBmp!!, matrix, null)

        var label = ""
        var confidence = -1f
        var color = Color.BLUE
        var extra: Any? = null
        if (
            faceBB.left.toInt() > 0 &&
            faceBB.top.toInt() > 0 &&
            (faceBB.top.toInt() + faceBB.height().toInt()) < portraitBmp!!.height &&
            (faceBB.left.toInt() + faceBB.width().toInt()) < portraitBmp!!.width
        ) {
            val crop: Bitmap = Bitmap.createBitmap(
                portraitBmp!!,
                faceBB.left.toInt(),
                faceBB.top.toInt(),
                faceBB.width().toInt(),
                faceBB.height().toInt()
            )
            val resultsAux: List<Recognition>? = detector?.recognizeImage(
                faceBmp!!, true
            )
            if (resultsAux?.isNotEmpty() == true) {
                val result: Recognition = resultsAux[0]
                Log.d("TAG", "recognizeImage: ${result.toJson()}")
                extra = result.extra
                val conf = result.distance
                if (conf < 6.0f) {
                    confidence = conf
                    label = result.title ?: ""
                    color = if (result.id.equals("0")) {
                        Color.GREEN
                    } else {
                        Color.RED
                    }
                }
            }
            val flip = Matrix()
            flip.postScale(-1f, 1f, previewWidth / 2.0f, previewHeight / 2.0f)
            flip.mapRect(boundingBox)
            val result = Recognition(
                id = "0",
                title = label,
                distance = confidence,
                location = boundingBox,
                color = color,
                extra = extra as Array<FloatArray>,
                crop = crop,
            )
            if (userId != null) viewModel.saveRegisteredFace(crop)
            mappedRecognitions.add(result)
        } else {
            Log.d("TAG", "faceLeft: ${faceBB.left.toInt()}")
            Log.d("TAG", "faceTop: ${faceBB.top.toInt()}")
            Log.d("TAG", "faceHeight: ${(faceBB.top.toInt() + faceBB.height().toInt())}")
            Log.d("TAG", "faceWidth: ${(faceBB.left.toInt() + faceBB.width().toInt())}")
            viewModel.imageVerifyFailed()
        }

        if (isVerify) checkForComparison(mappedRecognitions)
        else if (userId != null) {
            updateResults(mappedRecognitions,userId)
        }
    }

    private fun checkForComparison(mappedRecognitions: MutableList<Recognition>) {
        if (mappedRecognitions.isNotEmpty()) {
            Log.d("TAG", "checkForComparison: ${mappedRecognitions.toJson()}")
            val rec = mappedRecognitions[0]
            if (!rec.title.isNullOrEmpty()) {
                viewModel.imageVerifiedSuccessfully(rec.title.toString())
            } else viewModel.imageVerifyFailed()
        }
    }

    private fun updateResults(mappedRecognitions: List<Recognition>, userId: String) {
        Log.d("TAG", "updateResults: $userId : $mappedRecognitions")
        if (mappedRecognitions.isNotEmpty()) {
            val rec = mappedRecognitions[0]
            Log.d("TAG", "userId: $userId ")
            if (rec.extra.isNotEmpty()) {
                detector?.register(userId, rec)
            }
        }

        viewModel.sendNextImage(userId)
    }

    private fun createTransform(
        srcWidth: Int,
        srcHeight: Int,
        dstWidth: Int,
        dstHeight: Int,
        applyRotation: Int = 0,
    ): Matrix {
        val matrix = Matrix()
        if (applyRotation != 0) {
            matrix.postTranslate(-srcWidth / 2.0f, -srcHeight / 2.0f)
            matrix.postRotate(applyRotation.toFloat())
        }
        if (applyRotation != 0) {
            matrix.postTranslate(dstWidth / 2.0f, dstHeight / 2.0f)
        }
        return matrix
    }

    private fun registerStudentFace() {
        viewModel.studentList.forEach { studentItem ->
            studentItem.studentImage?.let { image ->
                viewModel.loadUrlToBitmap(
                    url = image,
                    userId = studentItem.userId ?: "",
                )
            }
        }
    }

    private fun registerFace(scaledBitmap: Bitmap, userId: String?) {
        val bitmap = bitmapResizer(scaledBitmap, previewWidth, previewHeight)
        lifecycleScope.launch {
            val async = async {
                if (bitmap != null) {
                    val canvas = Canvas(croppedBitmap!!)
                    canvas.drawBitmap(
                        bitmap,
                        frameToCropTransform!!,
                        null
                    )
                    onFaceDetected(croppedBitmap!!, bitmap,false ,userId)
                }
            }
            async.await()
        }
    }

    private fun replaceFragment(navId: Int, fragment: Fragment, addToBackStack: Boolean = false) {
        try {
            val replace =
                supportFragmentManager.beginTransaction().replace(navId, fragment, "TAG")
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

    companion object {
        private const val TF_OD_API_INPUT_SIZE = 112
        private const val TF_OD_API_IS_QUANTIZED = false
        private const val TF_OD_API_MODEL_FILE = "mobile_face_net.tflite"
        private const val TF_OD_API_LABELS_FILE = "file:///android_asset/labelmap.txt"
    }
}