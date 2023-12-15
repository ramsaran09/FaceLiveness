package com.muthuram.faceliveness.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.muthuram.faceliveness.databinding.ActivityCreateNewActivityBinding

class CreateNewActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCreateNewActivityBinding
    private val viewModel : CreateNewActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNewActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpUi()
    }

    private fun setUpUi() {
        binding.uiComposeView.setContent {
            CreateNewActivityRouter(viewModel)
        }
    }
}