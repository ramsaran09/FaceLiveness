package com.muthuram.faceliveness.models


data class DrawingAngles(val start: Float, val end: Float)

fun DrawingAngles.isInsideAngle(angle: Float) = angle > this.start && angle < this.start + this.end

data class ActivityDonutChartData(
    val count: Float,
    val questionAttendanceStatus: String,
)

data class ActivityDonutChartDataCollection(
    var items: List<ActivityDonutChartData> = listOf()
)
