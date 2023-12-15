package com.muthuram.faceliveness.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CredentialModel(
    @SerializedName("email")
    val email: String?,
    @SerializedName("password")
    val password: String?,
    @SerializedName("device_type")
    val deviceType: String,
    @SerializedName("user_type")
    val userType: String,
)