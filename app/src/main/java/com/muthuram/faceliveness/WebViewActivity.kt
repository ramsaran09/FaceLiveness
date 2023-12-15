package com.muthuram.faceliveness

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.ConsoleMessage
import android.webkit.SslErrorHandler
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.muthuram.faceliveness.databinding.ActivityWebViewBinding
import com.muthuram.faceliveness.helper.toJson

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding : ActivityWebViewBinding
    var uploadMessage: ValueCallback<Array<Uri>>? = null

    private val requestActivityResultLauncher by lazy {
        registerForActivityResult(
            ActivityResultContracts.GetMultipleContents(),
            ::onGetContent
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        requestActivityResultLauncher
        
        binding.uiWebView.settings.javaScriptEnabled = true
        binding.uiWebView.settings.setSupportMultipleWindows(true)
        binding.uiWebView.settings.allowFileAccess = true
        binding.uiWebView.settings.domStorageEnabled = true
        //binding.uiWebView.settings.loadWithOverviewMode = true
        binding.uiWebView.settings.useWideViewPort = true
        binding.uiWebView.settings.javaScriptCanOpenWindowsAutomatically = true
        binding.uiWebView.loadUrl(WEB_URL)
        binding.uiWebView.webViewClient = ChromeWebViewClient(binding.uiProgressOverlay)
        binding.uiWebView.webChromeClient = object : WebChromeClient() {
            // For 3.0+ Devices (Start)
            override fun onConsoleMessage(message: ConsoleMessage): Boolean {
                Log.d(
                    "MyApplication", "${message.message()} -- From line " +
                            "${message.lineNumber()} of ${message.sourceId()}"
                )
                return true
            }
            // For Lollipop 5.0+ Devices
            override fun onShowFileChooser(
                mWebView: WebView,
                filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                uploadMessage = filePathCallback
                try {
                    val intent = Intent()
                    intent.putExtra(KEY_SELECT_FILE, REQUEST_SELECT_FILE)
                    requestActivityResultLauncher.launch("image/*")
                    //startActivityForResult(intent, REQUEST_SELECT_FILE)
                } catch (e: ActivityNotFoundException) {
                    uploadMessage = null
                    return false
                }
                return true
            }
        }
        binding.uiWebView.loadUrl(WEB_URL)

        title = "ASSIGNMENT"
        onBackPressedDispatcher.addCallback(this, onBackPressed)
    }

    private val onBackPressed = object : OnBackPressedCallback(true ) {
        override fun handleOnBackPressed() {
            if (binding.uiWebView.canGoBack()) {
                binding.uiWebView.goBack()
            } else {
                finish()
            }
        }
    }

    private class ChromeWebViewClient(val uiProgressOverlay: FrameLayout) : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onPageCommitVisible(view: WebView?, url: String?) {
            super.onPageCommitVisible(view, url)
            uiProgressOverlay.visibility = View.GONE
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            Log.d("WebViewActivity", "onPageFinished: $url")
            val inputModel = AssignmentInputModel().toJson()
            Log.d("WebViewActivity", "input: $inputModel")
            view?.evaluateJavascript("window.postMessage('$inputModel', '*');") {}
        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
            uiProgressOverlay.visibility = View.GONE
        }

        override fun onReceivedHttpError(
            view: WebView?,
            request: WebResourceRequest?,
            errorResponse: WebResourceResponse?
        ) {
            super.onReceivedHttpError(view, request, errorResponse)
            uiProgressOverlay.visibility = View.GONE
        }

        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            super.onReceivedSslError(view, handler, error)
            uiProgressOverlay.visibility = View.GONE
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
        }

    }

    private fun onGetContent(uri: List<Uri>?) {
        if (uploadMessage == null) return
        if (uri != null) uploadMessage!!.onReceiveValue(uri.toTypedArray())
    }


    companion object {
        private const val REQUEST_SELECT_FILE = 100
        private const val KEY_SELECT_FILE = "key.select.file"
        private const val WEB_URL = "http://192.168.1.55:3000/mobileFlow/assignment/student"
    }

    data class AssignmentInputModel(
        val from : String = "dataFromMobile",
        val access_token : String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI5MDAwMTAiLCJpYXQiOjE2OTcxNzgzNjUsImV4cCI6MTY5NzIxNDM2NX0.0HDChOmtDLQkZVM9O0GEPbm9_PkFm_vVNU2eB877jfQ",
        val refresh_token: String = "eeyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI5MDAwMTAiLCJpYXQiOjE2OTcxNzgzNjUsImV4cCI6MTY5NzIzMjM2NX0.vq8KfczlFHmBhvmrpFqL4IG-y37yjRvOdUiJVxz40_U",
        val user_id : String = "62babe4b58e9580f791fc6a4",
        val institutionCalendar: String = "6493e87fa2cad15a75b7ddea",
        val user_type : String = "student"
    )

}