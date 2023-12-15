package com.muthuram.faceliveness

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Surface
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.muthuram.faceliveness.databinding.ActivityCameraBinding
import com.muthuram.faceliveness.helper.*
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private var displayId: Int = -1
    private var lensFacing: Int = CameraSelector.LENS_FACING_FRONT
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null

    private val cameraExecutor: ExecutorService by lazy {
        Executors.newSingleThreadExecutor()
    }
    private var type : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        type = intent.getStringExtra("face")
        setContentView(binding.root)
        setUpUiView()
        setListeners()
    }

    private fun setListeners() {
        binding.uiIvClose.setOnClickListener { finish() }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpUiView() {
        displayId = binding.uiCameraPreview.display?.displayId ?: -1
        // Build UI controls
        updateCameraUi()
        // Set up the camera and its use cases
        setUpCamera()

    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Redraw the camera UI controls
        updateCameraUi()
    }

    private fun setUpCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({

            // CameraProvider
            cameraProvider = cameraProviderFuture.get()

            // Select lensFacing depending on the available cameras
            lensFacing = when {
                hasFrontCamera() -> CameraSelector.LENS_FACING_FRONT
                hasBackCamera() -> CameraSelector.LENS_FACING_BACK
                else -> throw IllegalStateException("Back and front camera are unavailable")
            }

            // Build and bind the camera use cases
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindCameraUseCases() {

        // Get screen metrics used to setup camera for full screen resolution
        val metrics = DisplayMetrics().also { binding.uiCameraPreview.display?.getRealMetrics(it) }
        Log.d(TAG, "Screen metrics: ${metrics.widthPixels} x ${metrics.heightPixels}")

        val screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)
        Log.d(TAG, "Preview aspect ratio: $screenAspectRatio")

        val rotation = binding.uiCameraPreview.display?.rotation ?: Surface.ROTATION_0

        val cameraProvider = cameraProvider
            ?: throw IllegalStateException("Camera initialization failed.")

        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        preview = Preview.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .build()

        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setTargetRotation(rotation)
            .build()

        imageAnalyzer = ImageAnalysis.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .build()
            .also {
                it.setAnalyzer(
                    cameraExecutor,
                    LuminosityAnalyzer {}
                )
            }

        cameraProvider.unbindAll()

        try {
            camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageCapture, imageAnalyzer
            )
            preview?.setSurfaceProvider(binding.uiCameraPreview.surfaceProvider)
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    private fun updateCameraUi() {
        binding.uiCameraCapture.setOnClickListener {
            binding.uiCameraCapture.isEnabled = false
            imageCapture?.let { imageCapture ->
                val photoFile = getFile(this)
                val metadata = ImageCapture.Metadata().apply {
                    isReversedHorizontal = lensFacing == CameraSelector.LENS_FACING_FRONT
                }

                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
                    .setMetadata(metadata)
                    .build()

                imageCapture.takePicture(
                    outputOptions, cameraExecutor, object : ImageCapture.OnImageSavedCallback {
                        override fun onError(exc: ImageCaptureException) {
                            binding.uiCameraCapture.isEnabled = true
                            Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                        }

                        @RequiresApi(Build.VERSION_CODES.O)
                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
                            Log.d(TAG, "Photo capture succeeded: $savedUri")
                            runOnUiThread {
                                binding.uiCameraCapture.isEnabled = true
                                val intent = Intent()
                                intent.putExtra(type,photoFile.absolutePath)
                                setResult(RESULT_OK,intent)
                                finish()
                            }
                        }
                    }
                )
            }
        }
    }

    private fun hasBackCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) ?: false
    }

    private fun hasFrontCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) ?: false
    }

    private class LuminosityAnalyzer(
        val onFaceDetected: (Face) -> Unit,
    ) :
        ImageAnalysis.Analyzer {

        val highAccuracyOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()

        val faceDetector = FaceDetection.getClient(highAccuracyOpts)

        @SuppressLint("UnsafeExperimentalUsageError", "UnsafeOptInUsageError")
        override fun analyze(image: ImageProxy) {
            image.image?.let {
                faceDetector.process(InputImage.fromMediaImage(it, image.imageInfo.rotationDegrees))
                    .addOnSuccessListener { faces ->
                        faces.forEach { face ->
                            Log.d(
                                "TAG",
                                "analyze: ${face.leftEyeOpenProbability}   ${face.rightEyeOpenProbability}"
                            )
                            onFaceDetected(face)
                        }
                        image.close()
                    }.addOnFailureListener {
                        image.close()
                    }
            }
        }
    }

    companion object {
        private const val TAG = "cameraActivity"
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0

    }
}

