package com.muthuram.faceliveness.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class LoginResponse(
    @SerializedName("status_code")
    val statusCode: Int?,

    @SerializedName("status")
    val status: Boolean?,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: LoginDataModel?,
)

@Keep
data class LoginDataModel(
    @SerializedName("tokens")
    val tokens: LoginTokenModel?,
)

@Keep
data class LoginTokenModel(
    @SerializedName("access")
    val accessToken: TokenModel?,
    @SerializedName("refresh")
    val refreshToken: TokenModel?,
)

@Keep
data class TokenModel(
    @SerializedName("token")
    val token: String?,
    @SerializedName("expires")
    val expires: String?,
)