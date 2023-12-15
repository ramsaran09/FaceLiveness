package com.muthuram.faceliveness.studentfacial

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.muthuram.faceliveness.R
import com.muthuram.faceliveness.databinding.ActivityImageProcesingBinding
import com.muthuram.faceliveness.helper.fromJson
import com.muthuram.faceliveness.helper.replaceFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ImageProcessingFragment : Fragment() {

    private lateinit var binding : ActivityImageProcesingBinding
    private val viewModel: MultiFaceComparisonViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityImageProcesingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        processIntent(arguments)
        setUpUi()
        setUpListeners()
    }

    private fun setUpUi() {
        viewModel.verificationStatus.observe(viewLifecycleOwner) {
            onImageVerificationStatusUpdated(it)
        }
        viewModel.cropImage.observe(viewLifecycleOwner) {
            setUpImage(it)
        }

    }
    private fun setUpListeners() {
        binding.uiBtnRetry.setOnClickListener {
            viewModel.changeVerificationStatus()
            replaceFragment(
                R.id.navHostCamera,
                CameraFragment.newInstance(),
            )
        }
    }

    private fun processIntent(intent: Bundle?) {
        intent ?: return
        val images = Gson().fromJson(intent.getString(SAVED_IMAGE_PATH)) as List<Bitmap>?
        if (!images.isNullOrEmpty()) {
            val bitmap = images[0].copy(images[0].config,true)
            Log.d("TAG", "beforeScale: ${bitmap.width} * ${bitmap.height} : ${bitmap.config.name}")
            setUpImage(bitmap)
            val activity = activity as ImageVerificationActivity
            activity.verifyImage(bitmap)
        }
    }

    private fun setUpImage(image : Bitmap) {
        Glide.with(this)
            .load(image)
            .into(binding.uiIvCapturedImage)
    }

    private fun onImageVerificationStatusUpdated(verificationStatus: VerificationStatus) {
        when (verificationStatus) {
            is VerificationStatus.Success -> imageVerifiedSuccessfully(verificationStatus.userId)
            is VerificationStatus.Failure -> imageVerifyFailed()
            is VerificationStatus.Loading -> imageVerificationInProgress()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun imageVerifiedSuccessfully(studentName : String) {
        binding.uiBtnRetry.visibility = View.GONE
        binding.uiTvProgressStatus.apply {
            text = "Authentication Success for $studentName"
            setTextColor(ContextCompat.getColor(requireContext(), R.color.digi_green))
        }
        binding.uiIvImageVerification.visibility = View.VISIBLE
        binding.uiIvImageVerification.setImageResource(R.drawable.ic_authenticationsuccess_icon)
        binding.uiCvCapturedImage.strokeColor = ContextCompat.getColor(requireContext(),
            R.color.digi_green
        )
        updateNavigation()
    }

    @SuppressLint("SetTextI18n")
    private fun imageVerifyFailed() {
        binding.uiBtnRetry.visibility = View.VISIBLE
        binding.uiTvProgressStatus.apply {
            text = "Authentication Failed"
            setTextColor(ContextCompat.getColor(requireContext(), R.color.digi_red))
        }
        binding.uiIvImageVerification.visibility = View.VISIBLE
        binding.uiIvImageVerification.setImageResource(R.drawable.ic_cancel_small)
        binding.uiCvCapturedImage.strokeColor = ContextCompat.getColor(requireContext(),
            R.color.digi_red
        )
    }

    @SuppressLint("SetTextI18n")
    private fun imageVerificationInProgress() {
        binding.uiBtnRetry.visibility = View.GONE
        binding.uiTvProgressStatus.apply {
            text = "Authentication In-Progress"
            setTextColor(ContextCompat.getColor(requireContext(), R.color.hint_text_color))
        }
        binding.uiIvImageVerification.visibility = View.GONE
        binding.uiCvCapturedImage.strokeColor = ContextCompat.getColor(requireContext(),
            R.color.white
        )
    }

    private fun updateNavigation() {
        lifecycleScope.launch(Dispatchers.Main) {
            delay(2000)
            viewModel.changeVerificationStatus()
            replaceFragment(
                R.id.navHostCamera,
                CameraFragment.newInstance(),
            )
        }
    }

    companion object {
        private const val SAVED_IMAGE_PATH = "image_path"

        fun newInstance(image: List<Bitmap>): ImageProcessingFragment {
            val imageProcessingFragment = ImageProcessingFragment()
            imageProcessingFragment.arguments = Bundle().apply {
                putString(SAVED_IMAGE_PATH, Gson().toJson(image))
            }
            return imageProcessingFragment
        }
    }
}