package com.muthuram.faceliveness.manager

import android.content.Context
import androidx.core.content.edit

class PreferenceManager(context: Context) {

    companion object {
        private const val KEY_STAFF_ID = "key.user.staff.id"
        private const val KEY_STUDENT_FACE_DATA = "key.student.face.data"

    }

    private val appPreference = context.getSharedPreferences("AppData", Context.MODE_PRIVATE)

    fun clearAll() {
        appPreference.edit { clear() }
    }

    fun setAccessToken(staffId: String?) {
        appPreference.edit {
            putString(KEY_STAFF_ID, staffId)
        }
    }

    fun getAccessToken() = appPreference.getString(KEY_STAFF_ID,"")

    fun getStudentFaceData() = appPreference.getString(KEY_STUDENT_FACE_DATA,"")

    fun setStudentFaceData(faceData : String) {
        appPreference.edit {
            putString(KEY_STUDENT_FACE_DATA, faceData)
        }
    }
}