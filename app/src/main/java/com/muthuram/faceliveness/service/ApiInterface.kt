package com.muthuram.faceliveness.service

import com.muthuram.faceliveness.models.AttendanceReportResponse
import com.muthuram.faceliveness.models.CredentialModel
import com.muthuram.faceliveness.models.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiInterface {
    @GET("v1/sessions/session_report_with_face/{sessionId}")
    suspend fun getStudentFace(
        @Path("sessionId") sessionId: String,
    ): Response<AttendanceReportResponse?>

    @POST("v1/digiclass/user/authLogin")
    suspend fun login(
        @Body credentials: CredentialModel,
    ): Response<LoginResponse?>
}