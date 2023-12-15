package com.muthuram.faceliveness.models

import android.graphics.Bitmap
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AttendanceReportResponse(
    @SerializedName("status_code")
    var statusCode: Int? = null,

    @SerializedName("status")
    var status: Boolean? = null,

    @SerializedName("message")
    var message: String? = null,

    @SerializedName("data")
    var data: AttendanceReportDataModel? = null,
)

data class AttendanceReportDataModel(

    @SerializedName("students")
    var students: ArrayList<StudentItem>? = null,
)

@Keep
data class StudentItem(

    @SerializedName("userId")
    var userId: String? = null,

    @SerializedName("name")
    var name: NameModel? = null,

    var status: String? = null,

    @SerializedName("face")
    val faceUrl: String? = null,

    var studentImage : String? = null,
)

@Keep
class NameModel {

    @SerializedName("first")
    var first: String? = null

    @SerializedName("last")
    var last: String? = null
}