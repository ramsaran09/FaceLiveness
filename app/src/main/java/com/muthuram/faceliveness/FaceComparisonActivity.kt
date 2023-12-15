package com.muthuram.faceliveness

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceLandmark
import com.muthuram.faceliveness.databinding.ActivityFaceComparisonBinding
import com.muthuram.faceliveness.helper.bitmapResizer
import com.muthuram.faceliveness.helper.getTransformationMatrix
import com.muthuram.faceliveness.helper.rotate
import com.muthuram.faceliveness.helper.simulate3DTransformation
import com.muthuram.faceliveness.helper.toJson
import com.muthuram.faceliveness.tensorHelper.Recognition
import com.muthuram.faceliveness.tensorHelper.SimilarityClassifier
import com.muthuram.faceliveness.tensorHelper.TFLiteObjectDetectionAPIModel
import kotlinx.coroutines.launch
import java.util.LinkedList


class FaceComparisonActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFaceComparisonBinding

    private val requestPermissionLauncher by lazy {
        registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
            ::onPermissionResult
        )
    }

    private val requestActivityLauncher by lazy {
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ::onActivityResult
        )
    }

    private val requestDocumentLauncher by lazy {
        registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ::onContentResult
        )
    }

    private var isCameraPermission: Boolean = false

    private var bitmap1: Bitmap? = null
    private var bitmap2: Bitmap? = null
    private var faceNo: Int = 0

    private var detector: SimilarityClassifier? = null
    private lateinit var faceDetector: FaceDetector
    private var previewWidth = 480
    private var previewHeight = 640
    private var frameToCropTransform: Matrix? = null
    private var cropToFrameTransform: Matrix? = null
    private var portraitBmp: Bitmap? = null
    private var faceBmp: Bitmap? = null
    private var croppedBitmap: Bitmap? = null
    private var faceName: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaceComparisonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestPermissionLauncher
        requestActivityLauncher
        requestDocumentLauncher
        requestCameraPermission()
        setUpControls()
        setUpListeners()

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

    private fun setUpListeners() {
        binding.uiIvFace1.setOnClickListener {
            attachedBottomSheetDialog(
                onGallerySelected = {
                    showAddFaceDialog {
                        faceNo = 1
                        requestDocumentLauncher.launch("image/*")
                    }
                },
                onCameraSelected = {
                    showAddFaceDialog {
                        val intent = Intent(this, CameraActivity::class.java)
                        intent.putExtra("face", "bitmap1")
                        requestActivityLauncher.launch(intent)
                    }
                },
            )
        }
        binding.uiIvFace2.setOnClickListener {
            attachedBottomSheetDialog(
                onGallerySelected = {
                        faceNo = 2
                        requestDocumentLauncher.launch("image/*")
                },
                onCameraSelected = {
                    val intent = Intent(this, CameraActivity::class.java)
                    intent.putExtra("face", "bitmap2")
                    requestActivityLauncher.launch(intent)
                },
            )

        }
        binding.uiBtnVerify.setOnClickListener {
            lifecycleScope.launch {

            }
        }
        binding.uiBtnClear.setOnClickListener {
            clearImage()
        }
    }

    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            onPermissionResult(true)
        } else requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private fun onPermissionResult(result: Boolean) {
        isCameraPermission = result
    }

    private fun onActivityResult(activityResult: ActivityResult) {
        if (activityResult.resultCode == RESULT_OK) {
            if (activityResult.data?.getStringExtra("bitmap1") != null) {
                val filePath = activityResult.data?.getStringExtra("bitmap1")
                Log.d("CameraActivity", "onActivityResult: $filePath")
                bitmap1 = filePath?.let {
                    checkForBitmapRotation(it)
                }
                setUpImage(bitmap1, binding.uiIvFace1)
                binding.uiBtStart.text = ""
                Log.d("CameraActivity", "ImageSize: ${bitmap1?.width} * ${bitmap1?.height}")
                bitmap1 = if (bitmap1!!.width < bitmap1!!.height) bitmapResizer(bitmap1!!, previewWidth, previewHeight) else bitmap1
                registerFace(bitmap1!!)
            } else if (activityResult.data?.getStringExtra("bitmap2") != null) {
                val filePath = activityResult.data?.getStringExtra("bitmap2")
                Log.d("CameraActivity", "onActivityResult: $filePath")
                bitmap2 = filePath?.let { checkForBitmapRotation(it) }
                setUpImage(bitmap2, binding.uiIvFace2)
                binding.uiBtStart.text = ""
                Log.d("CameraActivity", "ImageSize: ${bitmap2?.width} * ${bitmap2?.height}")
                bitmap2 = bitmapResizer(bitmap2!!, previewWidth, previewHeight)
                registerFace(bitmap2!!, true)
            }
        }
    }

    private fun onContentResult(uri: Uri?) {
        uri ?: return
        Log.d("CameraActivity", "onContentResult: $uri")
        if (faceNo == 1) {
            val bitmap = BitmapFactory.decodeStream(
                contentResolver.openInputStream(uri)
            )
            bitmap1 = bitmap.copy(Bitmap.Config.ARGB_8888, true)
            setUpImage(bitmap1, binding.uiIvFace1)
            binding.uiBtStart.text = ""
            Log.d("CameraActivity", "ImageSize: ${bitmap1?.width} * ${bitmap1?.height}")
            bitmap1 = if (bitmap1!!.width < bitmap1!!.height) bitmapResizer(bitmap1!!, previewWidth, previewHeight) else bitmap1
            registerFace(bitmap1!!)
        } else if (faceNo == 2) {
            val bitmap = BitmapFactory.decodeStream(
                contentResolver.openInputStream(uri)
            )
            bitmap2 = bitmap.copy(Bitmap.Config.ARGB_8888, true)
            setUpImage(bitmap2, binding.uiIvFace2)
            binding.uiBtStart.text = ""
            Log.d("CameraActivity", "ImageSize: ${bitmap2?.width} * ${bitmap2?.height}")
            bitmap2 = bitmapResizer(bitmap2!!, previewWidth, previewHeight)
            registerFace(bitmap2!!, true)
        }
    }

    private fun setUpImage(images: Bitmap?, imageView: AppCompatImageView) {
        Glide.with(this)
            .load(images)
            .into(imageView)
    }

    private fun registerFace(scaledBitmap: Bitmap, isVerify: Boolean = false) {
        val canvas = Canvas(croppedBitmap!!)
        canvas.drawBitmap(scaledBitmap, frameToCropTransform!!, null)
        onFaceDetected(croppedBitmap!!, scaledBitmap, isVerify)
    }

    private fun onFaceDetected(image: Bitmap, scaledBitmap: Bitmap, isVerify: Boolean) {
        faceDetector.process(InputImage.fromBitmap(image, 0))
            .addOnSuccessListener { faces ->
                Log.d(
                    "TAG",
                    "face detection started: ${faces.size}"
                )
                if (faces.size == 0) {
                    runOnUiThread {
                        binding.uiBtStart.setTextColor(
                            ContextCompat.getColor(
                                this,
                                android.R.color.holo_red_light
                            )
                        )
                        binding.uiBtStart.text = "Face Match failed"
                    }
                    return@addOnSuccessListener
                }
                val leftEye = faces[0].getLandmark(FaceLandmark.LEFT_EYE)
                val rightEye = faces[0].getLandmark(FaceLandmark.RIGHT_EYE)

                if (leftEye != null && rightEye != null) {
                    val leftEyePos = leftEye.position
                    val rightEyePos = rightEye.position

                    // Create rectangles around both eyes
                    val rect = android.graphics.Rect(
                        leftEyePos.x.toInt(),
                        leftEyePos.y.toInt(),
                        rightEyePos.x.toInt(),
                        rightEyePos.y.toInt()
                    )
                    val canvas = Canvas(scaledBitmap)
                    val paint = Paint()
                    paint.color = Color.BLACK
                    paint.style = Paint.Style.STROKE
                    paint.strokeWidth = 2f
                    canvas.drawRect(rect, paint)
                    setUpImage(scaledBitmap, binding.uiIvFace1)
                }
                onFacesDetected(faces[0], scaledBitmap, isVerify)
            }.addOnFailureListener {
                Log.d(
                    "TAG",
                    "face detection : ${it.message}"
                )
            }
    }

    private fun onFacesDetected(face: Face, scaledBitmap: Bitmap, isVerify: Boolean) {
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
        var cosine = -1f
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
                confidence = result.distance
                cosine = result.cosineValue
                if (confidence < 6.0f) {
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
                cosineValue = cosine,
                location = boundingBox,
                color = color,
                extra = extra as Array<FloatArray>,
                crop = crop,
            )
            mappedRecognitions.add(result)
        } else {
            binding.uiBtStart.setTextColor(
                ContextCompat.getColor(
                    this,
                    android.R.color.holo_red_light
                )
            )
            binding.uiBtStart.text = "Face Match failed"
        }

        if (!isVerify) updateResults(mappedRecognitions)
        else checkForComparison(mappedRecognitions)
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

    private fun updateResults(mappedRecognitions: List<Recognition>) {
        if (mappedRecognitions.isNotEmpty()) {
            val rec = mappedRecognitions[0]
            Log.d("TAG", "updateResults: ${rec.toJson()}")
            if (rec.extra.isNotEmpty()) {
                //setUpImage(faceBmp, binding.uiIvFace1)
                setUpImage(faceBmp?.simulate3DTransformation(false), binding.uiIvFace1)

                detector?.register(faceName ?: "", rec)
            }
        }
    }

    private fun checkForComparison(mappedRecognitions: MutableList<Recognition>) {
        if (mappedRecognitions.isNotEmpty()) {
            Log.d("TAG", "checkForComparison: ${mappedRecognitions.toJson()}")
            val rec = mappedRecognitions[0]
            setUpImage(rec.crop?.simulate3DTransformation(true), binding.uiIvFace2)
            var text = ""
            if (!rec.title.isNullOrEmpty()) {
                binding.uiBtStart.setTextColor(
                    ContextCompat.getColor(
                        this,
                        android.R.color.holo_green_light
                    )
                )
                text = "Face comparison results for ${rec.title} is Distance : ${rec.distance} and cosine ${rec.cosineValue}"
                showSnackBar(text, false)
            } else {
                binding.uiBtStart.setTextColor(
                    ContextCompat.getColor(
                        this,
                        android.R.color.holo_red_light
                    )
                )
                showSnackBar(text, true)
                text = "Face Match failed with Distance : ${rec.distance} and cosine ${rec.cosineValue}"
            }
            binding.uiBtStart.text = text
        }
    }

    private fun showSnackBar(message: String, isError: Boolean) {
        val snackBar = Snackbar.make(
            binding.uiBtnRegister,
            message,
            Snackbar.LENGTH_SHORT
        )
        if (isError) {
            snackBar.setBackgroundTint(ContextCompat.getColor(this, R.color.red_A700))
        } else snackBar.setBackgroundTint(ContextCompat.getColor(this, R.color.green_A700))
        snackBar.show()
    }

    private fun showAddFaceDialog(onNameEntered: () -> Unit) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout: View = inflater.inflate(R.layout.image_add_dialog, null)
        val etName = dialogLayout.findViewById<AppCompatEditText>(R.id.uiEtName)
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dlg, _ ->
            val name = etName.text.toString()
            if (name.isEmpty()) {
                return@OnClickListener
            }
            faceName = name
            onNameEntered()
            dlg.dismiss()
        })
        builder.setView(dialogLayout)
        builder.show()
    }

    private fun clearImage() {
        bitmap1 = null
        bitmap2 = null
        binding.uiIvFace1.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_account_circle
            )
        )
        binding.uiIvFace2.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_account_circle
            )
        )
        binding.uiBtStart.text = ""
        detector?.clearRegisteredFace()
        faceBmp = null
        faceBmp = Bitmap.createBitmap(
            TF_OD_API_INPUT_SIZE,
            TF_OD_API_INPUT_SIZE,
            Bitmap.Config.ARGB_8888
        )
    }

    private fun attachedBottomSheetDialog(
        onGallerySelected: () -> Unit,
        onCameraSelected: () -> Unit,
    ) {
        val bottomSheet =
            LayoutInflater.from(this).inflate(R.layout.bottom_sheet_attach_dialog, null)
        val dialog = BottomSheetDialog(this, R.style.BottomSheetDialog)
        dialog.setContentView(bottomSheet)
        dialog.show()
        bottomSheet.findViewById<ConstraintLayout>(R.id.uiCsDocument).setOnClickListener {
            onGallerySelected()
            dialog.dismiss()
        }
        bottomSheet.findViewById<ConstraintLayout>(R.id.uiCsCamera).setOnClickListener {
            onCameraSelected()
            dialog.dismiss()
        }

        bottomSheet.findViewById<AppCompatImageView>(R.id.uiIvClose).setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun checkForBitmapRotation(imagePath: String): Bitmap {
        var image = BitmapFactory.decodeFile(imagePath)
        val exifInterface = ExifInterface(imagePath)
        val rotation = exifInterface.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )
        Log.d("TAG", "checkForBitmapRotation: $rotation")
        image =
            when (rotation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> image.rotate(90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> image.rotate(180f)
                ExifInterface.ORIENTATION_ROTATE_270,
                ExifInterface.ORIENTATION_TRANSVERSE -> image.rotate(270f)

                else -> image
            }
        return image
    }

    companion object {

        private const val TF_OD_API_INPUT_SIZE = 112
        private const val TF_OD_API_IS_QUANTIZED = false
        private const val TF_OD_API_MODEL_FILE = "mobile_face_net.tflite"
        private const val TF_OD_API_LABELS_FILE = "file:///android_asset/labelmap.txt"
    }
}