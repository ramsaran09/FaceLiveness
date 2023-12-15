package com.muthuram.faceliveness

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.muthuram.faceliveness.databinding.ActivitySelectionBinding
import com.muthuram.faceliveness.helper.RetrofitClient
import com.muthuram.faceliveness.helper.toJson
import com.muthuram.faceliveness.manager.PreferenceManager
import com.muthuram.faceliveness.models.CredentialModel
import com.muthuram.faceliveness.models.StudentItem
import com.muthuram.faceliveness.service.ApiInterface
import com.muthuram.faceliveness.studentfacial.ImageVerificationActivity
import kotlinx.coroutines.launch

class SelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectionBinding
    private val viewModel: SelectionViewModel by viewModels { SelectionViewModel.Factory }

    private val requestPermissionLauncher by lazy {
        registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
            ::onPermissionResult
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestPermissionLauncher
        setUpUi()
        setUpListeners()
    }

    private fun setUpUi() {
        viewModel.studentList.observe(this) {
            binding.uiProgressbar.visibility = View.GONE
            val intent = Intent(this@SelectionActivity, ImageVerificationActivity::class.java)
            intent.putExtra(KEY_STUDENT_LIST, it.toJson())
            startActivity(intent)
        }
    }

    private fun setUpListeners() {
        binding.uiBtnSingleFace.setOnClickListener {
            startActivity(Intent(this, FaceComparisonActivity::class.java))
        }

        binding.uiBtnTwoFace.setOnClickListener {
            startActivity(Intent(this, TwoFaceComparisonActivity::class.java))
        }
        binding.uiBtnMultiFace.setOnClickListener {
            requestCameraPermission()
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
        if (result) getStudentFaceFromServer()
    }

    private fun getStudentFaceFromServer() {
        binding.uiProgressbar.visibility = View.VISIBLE
        val retrofit = RetrofitClient.getInstance(this)
        val apiInterface = retrofit.create(ApiInterface::class.java)
        lifecycleScope.launch {
            try {
                val response = apiInterface.getStudentFace(SESSION_ID)
                if (response.isSuccessful && response.body() != null) {
                    if (response.body()?.status == true) {
                        onStudentFaceFetchedSuccessFully(response.body()?.data?.students)
                    } else {
                        Toast.makeText(
                            this@SelectionActivity,
                            response.body()?.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    if (response.code() == 401) {
                        getAccessTokenFromServer()
                    }
                }
            } catch (Ex: Exception) {
                Ex.localizedMessage?.let { Log.d("Error", it) }
            }
            binding.uiProgressbar.visibility = View.GONE
        }
    }

    private fun getAccessTokenFromServer() {
        val retrofit = RetrofitClient.getInstance(this)
        val apiInterface = retrofit.create(ApiInterface::class.java)
        lifecycleScope.launch {
            try {
                val response = apiInterface.login(getCredentialModel())
                if (response.isSuccessful && response.body() != null) {
                    if (response.body()?.status == true) {
                        val preferenceManager = PreferenceManager(this@SelectionActivity)
                        preferenceManager.setAccessToken(response.body()?.data?.tokens?.accessToken?.token)
                        getStudentFaceFromServer()
                    } else {
                        Toast.makeText(
                            this@SelectionActivity,
                            response.body()?.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (Ex: Exception) {
                Ex.localizedMessage?.let { Log.d("Error", it) }
            }
        }
    }

    private fun getCredentialModel() =
        CredentialModel(
            email = "dr.subanmd@digi-val.com",
            password = "12345678",
            deviceType = "android",
            userType = "staff",
        )


    private fun onStudentFaceFetchedSuccessFully(students: ArrayList<StudentItem>?) {
        if (!students.isNullOrEmpty()) {
            viewModel.convertStudentUrlToImage(students)
        }
    }

    companion object {
        const val SESSION_ID = "6538fabb373918c23303d57c"
        const val KEY_STUDENT_LIST = "key.student.item"
    }
}