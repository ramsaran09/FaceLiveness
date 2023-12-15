package com.muthuram.faceliveness.studentfacial

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.WindowMetrics
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.muthuram.faceliveness.R
import com.muthuram.faceliveness.databinding.ActivityMultiFaceComparisonBinding
import com.muthuram.faceliveness.helper.getFile
import com.muthuram.faceliveness.helper.replaceFragment
import com.muthuram.faceliveness.helper.rotate
import com.muthuram.faceliveness.studentfacial.compose.OngoingAttendanceScreen
import com.muthuram.faceliveness.studentfacial.compose.RegisteredFaceScreen
import com.muthuram.faceliveness.studentfacial.compose.StudentAttendanceUiModel
import com.muthuram.faceliveness.studentfacial.helper.ClassifierListener
import com.muthuram.faceliveness.studentfacial.helper.ImageClassifierHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


class CameraFragment : Fragment() , ClassifierListener {

    private lateinit var binding: ActivityMultiFaceComparisonBinding

    private val viewModel : MultiFaceComparisonViewModel by activityViewModels()
    private var scope = CoroutineScope(Dispatchers.Main)
    private var isTimerActive = false
    private var timeMills = 0L
    private var lastTimeTamp = 0L

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

    private lateinit var bitmapBuffer: Bitmap
    private lateinit var detectedBitmap: Bitmap
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private val spoofList =  arrayListOf<Bitmap>()
    private lateinit var faceDetector: FaceDetector
    private var isProcessingFrame: Boolean = false
    private var computingDetection: Boolean = false
    private var postInferenceCallback: Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityMultiFaceComparisonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
        setUpListeners()
        startTimer()
    }

    private fun setUpListeners() {
        binding.uiClContainer.setOnClickListener { viewModel.showAttendanceView(true) }
        binding.uiCameraCapture.setOnClickListener {
            binding.uiCameraCapture.isEnabled = false
            onImageCapture()
        }
        binding.uiIvInformation.setOnClickListener { viewModel.showRegisteredFaceView(true) }
    }

    private fun setUpUi() {
        viewModel.showAttendanceInfo.observe(viewLifecycleOwner) {
            if (it) showAttendanceInfoView()
            else binding.uiComposeView.visibility = View.GONE
        }
        viewModel.showRegisteredFaceInfo.observe(viewLifecycleOwner) {
            if (it) showRegisteredFaceInfo()
            else binding.uiComposeView.visibility = View.GONE
        }
        viewModel.attendanceCount.observe(viewLifecycleOwner) {
            binding.uiTvAttendanceCount.text = it
            updateStudentCount()
        }
        viewModel.initCamera.observe(viewLifecycleOwner) {
            binding.uiCiLoadedCount.visibility = View.GONE
            Toast.makeText(requireContext(), "Image Loaded", Toast.LENGTH_LONG).show()
            setUpCamera()
        }
        viewModel.hasBlinked.observe(viewLifecycleOwner) {
            binding.uiCameraCapture.visibility =  if (it) View.VISIBLE else View.GONE
        }
        viewModel.imageLoadedCount.observe(viewLifecycleOwner) {
            showProgressCount(it)
        }
    }

    private fun showProgressCount(count : Int) {
        binding.uiCiLoadedCount.progress =
            ((count.toDouble() / viewModel.studentList.size.toDouble()) * 100).toInt()
    }

    private fun updateStudentCount() {
        val presentCount = viewModel.studentList.count { it.status == "present" }
        binding.uiCiProgress.progress =
            ((presentCount.toDouble() / viewModel.studentList.size.toDouble()) * 100).toInt()
    }

    private fun showAttendanceInfoView() {
        binding.uiComposeView.apply {
            visibility = View.VISIBLE
            setContent {
                OngoingAttendanceScreen(
                    studentPresent = viewModel.studentList.count { it.status != "pending" },
                    totalStudent = viewModel.studentList.size,
                    pendingStudentsAttendanceUiModel = viewModel.studentList.filter {
                        it.status != "present"
                    }.map {
                        StudentAttendanceUiModel(
                            id = it.userId,
                            studentName = it.name?.first + "" + it.name?.last,
                            rollNumber = it.userId,
                            status = it.status,
                        )
                    },
                    markedStudentsAttendanceUiModel = viewModel.studentList.filter {
                        it.status == "present"
                    }.map {
                        StudentAttendanceUiModel(
                            id = it.userId,
                            studentName = it.name?.first + "" + it.name?.last,
                            rollNumber = it.userId,
                            status = it.status,
                        )
                    },
                    onStopClicked = {},
                    onCloseClicked = {
                        viewModel.showAttendanceView(false)
                    },
                )
            }
        }
    }

    private fun showRegisteredFaceInfo() {
        binding.uiComposeView.apply {
            visibility = View.VISIBLE
            setContent {
                RegisteredFaceScreen(
                    studentFaceList = viewModel.registeredStudentFaces,
                    onCloseClicked = {
                        viewModel.showRegisteredFaceView(false)
                    }
                )
            }
        }
    }

    private fun startTimer() {
        if (isTimerActive) return
        scope.launch {
            System.currentTimeMillis()
            isTimerActive = true
            while (isTimerActive) {
                delay(10L)
                timeMills += System.currentTimeMillis() - lastTimeTamp
                lastTimeTamp = System.currentTimeMillis()
                binding.uiTvTimer.text = formatTime(timeMills)
            }
        }
    }

    @SuppressLint("NewApi")
    private fun formatTime(timeMills: Long): String {
        val localDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(timeMills),
            ZoneId.of("UTC")
        )
        val formatter = DateTimeFormatter.ofPattern(
            "mm:ss",
        )
        return localDateTime.format(formatter)
    }

    private fun stopTimer() {
        isTimerActive = false
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
        cameraExecutor.shutdown()
    }

    private fun onImageCapture() {
        imageCapture?.let { imageCapture ->
            val photoFile = getFile(requireContext())
            val metadata = ImageCapture.Metadata().apply {
                isReversedHorizontal = lensFacing == CameraSelector.LENS_FACING_FRONT
            }
            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
                .setMetadata(metadata)
                .build()
            imageCapture.takePicture(
                outputOptions, cameraExecutor, object : ImageCapture.OnImageSavedCallback {
                    override fun onError(exc: ImageCaptureException) {
                        Log.e(TAG, "Photo capture failed: ${exc.message}")
                    }

                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
                        Log.d(TAG, "Photo capture succeeded: $savedUri")
                        activity?.runOnUiThread {
                            val images = arrayListOf<Bitmap>()
                            images.add(detectedBitmap)
                            replaceFragment(
                                R.id.navHostCamera,
                                ImageProcessingFragment.newInstance(images),
                            )
                            binding.uiCameraCapture.isEnabled = true
                        }
                    }
                }
            )
        }
    }

    private fun setUpCamera() {
        binding.uiCameraPreview.visibility = View.VISIBLE
        displayId = binding.uiCameraPreview.display?.displayId ?: -1
        imageClassifierHelper =
            ImageClassifierHelper(context = requireContext(), imageClassifierListener = this)
        val highAccuracyOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()

        faceDetector = FaceDetection.getClient(highAccuracyOpts)
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({

            // CameraProvider
            cameraProvider = cameraProviderFuture.get()

            // Select lensFacing depending on the available cameras
            if (!hasFrontCamera() && !hasBackCamera()) {
                return@addListener
            }
            lensFacing = when {
                hasFrontCamera() -> CameraSelector.LENS_FACING_FRONT
                hasBackCamera() -> CameraSelector.LENS_FACING_BACK
                else -> {
                    Toast.makeText(requireContext(),"camera disabled for this application", Toast.LENGTH_SHORT).show()
                    activity?.finish()
                    -1
                }
            }

            // Build and bind the camera use cases
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(requireContext()))
    }


    @SuppressLint("NewApi")
    private fun bindCameraUseCases() {

        val size = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) getScreenSizeForApi30AndAbove()
        else getScreenSize()

        val screenAspectRatio = aspectRatio(size.width, size.height)
        Log.d(TAG, "Preview aspect ratio: $screenAspectRatio")

        val rotation = binding.uiCameraPreview.display?.rotation ?: Surface.ROTATION_0

        // CameraProvider
        val cameraProvider = cameraProvider
            ?: throw IllegalStateException("Camera initialization failed.")

        // CameraSelector
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        // Preview
        preview = Preview.Builder()
            // We request aspect ratio but no resolution
            .setTargetAspectRatio(screenAspectRatio)
            // Set initial target rotation
            .setTargetRotation(rotation)
            .build()

        // ImageCapture
        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            // We request aspect ratio but no resolution to match preview config, but letting
            // CameraX optimize for whatever specific resolution best fits our use cases
            .setTargetAspectRatio(screenAspectRatio)
            // Set initial target rotation, we will have to call this again if rotation changes
            // during the lifecycle of this use case
            .setTargetRotation(rotation)
            .build()
        // ImageAnalysis
        imageAnalyzer = ImageAnalysis.Builder()
            // We request aspect ratio but no resolution
            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
            // Set initial target rotation, we will have to call this again if rotation changes
            // during the lifecycle of this use case
            .setTargetRotation(rotation)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setOutputImageFormat(
                ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888
            )
            .build()
            // The analyzer can then be assigned to the instance
            .also {
                it.setAnalyzer(
                    cameraExecutor,
                ) { image ->
                    if (isProcessingFrame) {
                        image.close()
                        return@setAnalyzer
                    }
                    isProcessingFrame = true
                    postInferenceCallback = Runnable {
                        image.close()
                        isProcessingFrame = false
                    }
                    onFaceDetect(image, rotation)
                }
            }

        // Must unbind the use-cases before rebinding them
        cameraProvider.unbindAll()

        try {
            // A variable number of use-cases can be passed here -
            // camera provides access to CameraControl & CameraInfo
            camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageCapture, imageAnalyzer
            )

            // Attach the viewfinder's surface provider to preview use case
            preview?.setSurfaceProvider(binding.uiCameraPreview.surfaceProvider)
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @Suppress("DEPRECATION")
    private fun getScreenSize(): Size {
        val display = requireContext().getSystemService(WindowManager::class.java).defaultDisplay
        val metrics = if (display != null) DisplayMetrics().also { display.getRealMetrics(it) }
        else Resources.getSystem().displayMetrics
        return Size(metrics.widthPixels, metrics.heightPixels)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun getScreenSizeForApi30AndAbove(): Size {
        val metrics: WindowMetrics = requireContext().getSystemService(WindowManager::class.java).currentWindowMetrics
        return Size(metrics.bounds.width(), metrics.bounds.height())
    }

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    private fun hasBackCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) ?: false
    }

    private fun hasFrontCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) ?: false
    }

    private fun onFaceDetect(image: ImageProxy, rotation: Int) {
        if (!::bitmapBuffer.isInitialized) {
            // The image rotation and RGB image buffer are initialized only once
            // the analyzer has started running
            bitmapBuffer = Bitmap.createBitmap(
                image.width,
                image.height,
                Bitmap.Config.ARGB_8888
            )
        }
        classifyImage(image,rotation)
    }

    private fun classifyImage(image: ImageProxy, rotation: Int) {
        // Copy out RGB bits to the shared bitmap buffer
        image.use {
            val buffer = image.planes[0].buffer
            buffer.rewind()
            bitmapBuffer.copyPixelsFromBuffer(buffer)
        }
            binding.uiCameraPreview.isEnabled = false
            imageClassifierHelper.classify(bitmapBuffer, rotation)
    }

    private fun findCategoryAndIndex(results: List<Classifications>?, image: Bitmap) {
        if (!results.isNullOrEmpty()) {
            if (results[0].categories.isNotEmpty()) {
                if (results[0].categories[0].index == 0 && results[0].categories[0].score >= 0.99) {
                    Log.d(
                        "TAG",
                        "findCategoryAndIndex: index: ${results[0].categories?.get(0)?.index} - score: ${
                            results[0].categories?.get(0)?.score
                        }"
                    )
                    val rotatedImage = image.rotate(270f)
                    spoofList.add(rotatedImage)
                    lifecycleScope.launchWhenResumed {
                        replaceFragment(
                            R.id.navHostCamera,
                            SpoofFragment.newInstance(
                                image = spoofList,
                            ),
                        )
                    }
                } else validateImageThroughMlKit(image)
            }
        }
    }

    private fun validateImageThroughMlKit(image: Bitmap) {
        if (computingDetection) {
            readyForNextImage()
            return
        }
        computingDetection = true
        readyForNextImage()
        onFaceDetected(image.rotate(270f))
    }

    private fun readyForNextImage() {
        if (postInferenceCallback != null) {
            postInferenceCallback?.run()
        }
    }

    private fun onFaceDetected(image: Bitmap) {
        faceDetector.process(InputImage.fromBitmap(image, 0))
            .addOnSuccessListener { faces ->
                faces.forEach { face ->
                    Log.d(
                        "TAG",
                        "analyze: ${face.leftEyeOpenProbability}   ${face.rightEyeOpenProbability}"
                    )
                    viewModel.setBlink(true)
                }
                computingDetection = false
            }.addOnFailureListener {
                Log.d(
                    "TAG",
                    "face detection : ${it.message}"
                )
                viewModel.setBlink(true)
            }
        detectedBitmap = image
    }

    companion object {
        private const val TAG = "MultiFaceActivity"
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0

        fun newInstance() = CameraFragment()
    }

    override fun onError(error: String) {
        activity?.runOnUiThread {
            Log.d(TAG, "onError: $error")
        }
    }

    override fun onResults(results: List<Classifications>?, inferenceTime: Long, image: Bitmap) {
        activity?.runOnUiThread {
            findCategoryAndIndex(results,image)
        }
    }
}