package com.muthuram.faceliveness.tensorHelper

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.RectF

interface SimilarityClassifier {

    fun register(name: String, recognition: Recognition)

    fun recognizeImage(bitmap: Bitmap, getExtra: Boolean): List<Recognition>

    fun clearRegisteredFace()
}

data class Recognition(
    var id : String?,
    var title : String?,
    var distance : Float = -1f,
    var cosineValue : Float = -1f,
    var color : Int = Color.BLUE,
    var location : RectF? = null,
    var extra : Array<FloatArray> = arrayOf(),
    var crop : Bitmap? = null,
)
