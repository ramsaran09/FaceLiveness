package com.muthuram.faceliveness.studentfacial.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.muthuram.faceliveness.R
import com.muthuram.faceliveness.helper.ProgressCircle

@Composable
fun OngoingAttendanceScreen(
    studentPresent: Int,
    totalStudent: Int,
    pendingStudentsAttendanceUiModel: List<StudentAttendanceUiModel?>,
    markedStudentsAttendanceUiModel: List<StudentAttendanceUiModel?>,
    onStopClicked: () -> Unit,
    onCloseClicked: () -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        bottomBar = {
            RetakeAttendanceBottom(
                onStopClicked = onStopClicked
            )
        },
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = colorResource(R.color.white),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_close),
                        contentDescription = "Back Arrow",
                        tint = Color.Black,
                        modifier = Modifier
                            .clickable { onCloseClicked() }
                            .padding(horizontal = 10.dp),
                    )
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = it.calculateBottomPadding()
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            ) {
                val present = studentPresent.toDouble()
                val total = totalStudent.toDouble()
                val percentageCount = (present / total) * 100
                AttendanceMarkedWithTimer(
                    hasReachedEnd = studentPresent == totalStudent,
                    percentageCount = percentageCount.toInt(),
                    presentCount = studentPresent,
                    totalCount = totalStudent,
                )
                BuzzReportStatusTitle()
                if (pendingStudentsAttendanceUiModel.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp, horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Pending (${pendingStudentsAttendanceUiModel.size})",
                            color = colorResource(id = R.color.text_grey_color),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 5.dp)
                        )
                    }
                    pendingStudentsAttendanceUiModel.forEach { student ->
                        StudentItemScreen(
                            studentName = student?.studentName ?: "",
                            rollNumber = student?.rollNumber ?: "",
                            status = student?.status ?: "",
                        )
                    }
                }
                if (markedStudentsAttendanceUiModel.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp, horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Marked Attendance (${markedStudentsAttendanceUiModel.size})",
                            color = colorResource(id = R.color.text_grey_color),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 5.dp)
                        )
                    }
                    markedStudentsAttendanceUiModel.forEach { student ->
                        StudentItemScreen(
                            studentName = student?.studentName ?: "",
                            rollNumber = student?.rollNumber ?: "",
                            status = student?.status ?: "",
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StudentItemScreen(
    studentName: String,
    rollNumber: String,
    status: String,
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = studentName,
                    color = colorResource(R.color.hint_color),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Start,
                )
                Text(
                    text = rollNumber,
                    color = colorResource(R.color.student_id_text_color),
                    fontSize = 11.sp,
                    textAlign = TextAlign.Start,
                )
            }
            Column(modifier = Modifier.weight(.30f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .height(24.dp)
                        .padding(start = 6.dp, top = 6.dp, bottom = 6.dp)
                ) {
                    IconButton(onClick = {}) {
                        if (status == "present") {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = "Back Arrow",
                                tint = Color.Green
                            )
                        } else {
                            Icon(
                                modifier = Modifier.rotate(180f),
                                imageVector = Icons.Filled.Info,
                                contentDescription = "Back Arrow",
                                tint = colorResource(id = R.color.thumb_switch_color)
                            )
                        }
                    }
                }
            }
        }
        Divider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = colorResource(id = R.color.divider_grey)
        )
    }
}

@Composable
fun AttendanceMarkedWithTimer(
    hasReachedEnd: Boolean,
    presentCount: Int,
    totalCount: Int,
    percentageCount: Int,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorResource(id = R.color.white))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(150.dp)
                .height(150.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                Modifier
                    .width(150.dp)
                    .height(150.dp)
                    .clip(CircleShape)
                    .border(
                        width = 15.dp,
                        color = colorResource(id = R.color.circle_clr),
                        shape = CircleShape
                    )
            ) {}
            AndroidView(
                factory = { cont ->
                    ProgressCircle(cont).apply {
                        this.update(
                            progressToApply = percentageCount,
                            hasReachedEnd = hasReachedEnd
                        )
                    }
                },
                update = {
                    it.update(progressToApply = percentageCount, hasReachedEnd = hasReachedEnd)
                },
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .width(125.dp)
                    .height(125.dp)
            ) {
                Text(
                    text = "${presentCount}/${totalCount}",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    color = if (hasReachedEnd) colorResource(R.color.present_clr)
                    else colorResource(R.color.thumb_switch_color)
                )

                Text(
                    text = "Student Marked Attendance",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    color = colorResource(R.color.trigger_buzzer_cancel_clr)
                )

            }
        }
        Spacer(modifier = Modifier.padding(top = 4.dp))
    }
}

@Composable
fun BuzzReportStatusTitle() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(color = colorResource(id = R.color.bg_status_white)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f),
            text = "Student Info",
            color = colorResource(R.color.hint_color),
            fontSize = 13.sp,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Normal,
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(end = 20.dp)
        ) {
            Text(
                text = "Status",
                color = colorResource(R.color.hint_color),
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Composable
fun RetakeAttendanceBottom(
    onStopClicked: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = colorResource(R.color.buzzer_light_blue)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(0.7f),
            ) {
                Text(
                    text = "Attendance running in background...",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = colorResource(R.color.trigger_buzzer_cancel_clr)
                )
            }
            Button(
                modifier = Modifier.padding(horizontal = 16.dp).weight(0.3f),
                onClick = { onStopClicked() },
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.red_A700)),
                shape = RoundedCornerShape(12.dp),
            ) {
                Text(
                    text = "Stop",
                    color = Color.White,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

data class StudentAttendanceUiModel(
    val id: String?,
    val studentName: String?,
    val rollNumber: String?,
    var status: String?,
)