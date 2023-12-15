package com.muthuram.faceliveness.studentfacial

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.muthuram.faceliveness.R
import com.muthuram.faceliveness.databinding.FragmentImageSpoofingBinding
import com.muthuram.faceliveness.helper.fromJson
import com.muthuram.faceliveness.helper.replaceFragment

class SpoofFragment : Fragment() {

    private lateinit var binding : FragmentImageSpoofingBinding
    private val viewModel : MultiFaceComparisonViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageSpoofingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        processIntent(arguments)
        setUpListeners()
    }

    private fun processIntent(intent: Bundle?) {
        intent ?: return
        val images = Gson().fromJson(intent.getString(SAVED_IMAGE_PATH)) as List<Bitmap>?
        if (!images.isNullOrEmpty()) {
            val bitmap = images[0].copy(images[0].config,true)
            Log.d("TAG", "beforeScale: ${bitmap.width} * ${bitmap.height} : ${bitmap.config.name}")
            setUpImage(bitmap)
        }
    }

    private fun setUpImage(image : Bitmap) {
        Glide.with(this)
            .load(image)
            .into(binding.uiIvCapturedImage)
    }

    private fun setUpListeners() {
        binding.uiBtnRetry.setOnClickListener {
            replaceFragment(
                R.id.navHostCamera,
                CameraFragment.newInstance(),
            )
        }
        binding.uiIvClose.setOnClickListener {
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