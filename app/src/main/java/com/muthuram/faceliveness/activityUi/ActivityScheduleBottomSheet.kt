package com.muthuram.faceliveness.activityUi

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.muthuram.faceliveness.R
import com.muthuram.faceliveness.activity.StudentGroupUiModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Preview(showBackground = true)
@Composable
fun ScheduleActivityBottomSheetPreview() {
    ScheduleActivityBottomSheet(
        startDate = "",
        endDate = "",
        startTime = "",
        endTime = "",
        selectedSessionName = "",
        sessions = listOf(),
        studentGroupList = listOf(),
        onCancelClicked = {},
        onSaveClicked = {},
        onSessionSelected = {},
        onStartDateSelected  = {},
        onStartTimeSelected  = {},
        onEndDateSelected  = {},
        onEndTimeSelected  = {},
        onSaveSelectedStudentGroup  = {},
        onStudentGroupSelected = {_,_ ->},
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScheduleActivityBottomSheet(
    startDate : String,
    endDate : String,
    startTime : String,
    endTime : String,
    sessions : List<String>,
    studentGroupList : List<StudentGroupUiModel>,
    selectedSessionName : String,
    onStartDateSelected : (String) -> Unit,
    onStartTimeSelected : (String) -> Unit,
    onEndDateSelected : (String) -> Unit,
    onEndTimeSelected : (String) -> Unit,
    onSessionSelected : (Int) -> Unit,
    onStudentGroupSelected : (Int,Boolean) -> Unit,
    onCancelClicked : () -> Unit,
    onSaveClicked : () -> Unit,
    onSaveSelectedStudentGroup : () -> Unit,
) {
    val sessionDialog = remember { mutableStateOf(false) }
    val studentGroupDialog = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        if (sessionDialog.value) {
            Dialog(
                onDismissRequest = { sessionDialog.value = false },
            ) {
                SelectSession(
                    sessions = sessions,
                    onSessionClicked = {
                        onSessionSelected(it)
                        sessionDialog.value = false
                    }
                )
            }
        }
        if (studentGroupDialog.value) {
            Dialog(
                onDismissRequest = { studentGroupDialog.value = false },
            ) {
                StudentGroup(
                    studentGroupList = studentGroupList,
                    onStudentGroupSelected = onStudentGroupSelected,
                    onCancelSelected = { studentGroupDialog.value = false },
                    onSaveClicked = {
                        studentGroupDialog.value = false
                        onSaveSelectedStudentGroup()
                    },
                )
            }
        }
        Text(
            text = "Enable Scheduling For This Quiz",
            fontSize = 16.sp,
            fontWeight = FontWeight.Black,
        )
        Spacer(modifier = Modifier.padding(2.dp))
        Text(
            text = "This will allow you to start and end this quiz automatically",
            fontSize = 14.sp,
            color = colorResource(id = R.color.hint_text_color),
        )
        Spacer(modifier = Modifier.padding(top = 25.dp))
        Text(
            text = "Schedule",
            fontSize = 16.sp,
            fontWeight = FontWeight.Black,
        )
        Spacer(modifier = Modifier.padding(top = 4.dp))
        Text(
            text = "Start By",
            fontSize = 13.sp,
            color = colorResource(id = R.color.grey_600),
        )
        Row(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth()
        ) {
            SelectedDate(
                selectedDate = startDate,
                onDateSelected = onStartDateSelected,
            )
            Spacer(modifier = Modifier.weight(1f))
            SetTimerDropDown(
                onSelectTime = onStartTimeSelected,
                time = startTime,
            )
        }
        Spacer(modifier = Modifier.padding(top = 8.dp))
        Text(
            text = "End By",
            fontSize = 13.sp,
            color = colorResource(id = R.color.grey_600),
        )
        Row(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth()
        ) {
            SelectedDate(
                selectedDate = endDate,
                onDateSelected = onEndDateSelected,
            )
            Spacer(modifier = Modifier.weight(1f))
            SetTimerDropDown(
                onSelectTime = onEndTimeSelected,
                time = endTime,
            )
        }
        Spacer(modifier = Modifier.padding(top = 10.dp))
        Text(
            text = "For Who Would you like to execute this quiz?",
            fontSize = 14.sp,
            color = colorResource(id = R.color.grey_new),
        )
        Spacer(modifier = Modifier.padding(top = 10.dp))
        Text(
            text = "Select Session",
            fontSize = 13.sp,
            color = colorResource(id = R.color.grey_600),
        )
        Spacer(modifier = Modifier.padding(top = 2.dp))
        OutlinedTextField(
            value = selectedSessionName,
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth()
                .clickable {
                },
            onValueChange = {},
            placeholder = {
                Text(
                    text = "Choose a Session",
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = Color.LightGray
                    )
                )
            },
            readOnly = true,
            interactionSource = remember { MutableInteractionSource() }
                .also { interactionSource ->
                    LaunchedEffect(interactionSource) {
                        interactionSource.interactions.collect {
                            if (it is PressInteraction.Release) {
                                sessionDialog.value = true
                            }
                        }
                    }
                },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = colorResource(id = R.color.buzz_txt_color_100),
                unfocusedBorderColor = colorResource(id = R.color.buzz_txt_color_100),
                focusedBorderColor = colorResource(id = R.color.colorGray),
            ),
            shape = RoundedCornerShape(60.dp),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(
                expanded = false,
                onIconClick ={
                    sessionDialog.value = true
                }
            ) },
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = "Select Student Group",
            fontSize = 13.sp,
            color = colorResource(id = R.color.grey_600),
        )
        Spacer(modifier = Modifier.padding(top = 2.dp))
        OutlinedTextField(
            value = studentGroupList.filter { it.isSelected }.joinToString{ it.studentGroupText },
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth(),
            onValueChange = {},
            interactionSource = remember { MutableInteractionSource() }
                .also { interactionSource ->
                    LaunchedEffect(interactionSource) {
                        interactionSource.interactions.collect {
                            if (it is PressInteraction.Release) {
                                studentGroupDialog.value = true
                            }
                        }
                    }
                },
            placeholder = {
                Text(
                    text = "Choose Student Group",
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = Color.LightGray
                    )
                )
            },
            readOnly = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = colorResource(id = R.color.buzz_txt_color_100),
                unfocusedBorderColor = colorResource(id = R.color.buzz_txt_color_100),
                focusedBorderColor = colorResource(id = R.color.colorGray),
            ),
            shape = RoundedCornerShape(60.dp),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = false,
                    onIconClick ={
                        studentGroupDialog.value = true
                    }
                )
            },
        )
        Row(
            modifier = Modifier.padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(end = 8.dp),
                onClick = {
                    onCancelClicked()
                },
                shape = CircleShape,
                border = BorderStroke(1.dp, colorResource(id = R.color.grey_bg)),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = colorResource(id = R.color.grey_bg)
                )
            ) {
                Text(
                    text = "Cancel"
                )
            }
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            Button(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(id = R.color.upload_icon_color),
                    contentColor = Color.White
                ),
                onClick = {
                    onSaveClicked()
                },
            ) {
                Text(
                    text = "Save"
                )
            }
        }
    }
}

@Composable
fun SelectedDate(
    selectedDate: String,
    onDateSelected : (String) -> Unit,
) {
    Card(
        modifier = Modifier
            .wrapContentSize()
            .width(150.dp)
            .height(40.dp)
            .background(Color.White),
        border = BorderStroke(0.5.dp, colorResource(id = R.color.text_disabled_color)),
        backgroundColor = Color.White,
    ) {
        Row(
            modifier = Modifier
                .width(150.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SelectedDateText(
                selectedDate = selectedDate
            )
            CalendarIcon(
                onSelectDateFromDatePicker = onDateSelected,
            )
        }
    }
}

@Composable
fun SelectedDateText(
    selectedDate: String,
) {
    Text(
        text = selectedDate,
        modifier = Modifier.padding(start = 4.dp),
        color = Color.Black
    )
}

@Composable
fun CalendarIcon(
    onSelectDateFromDatePicker: (String) -> Unit,
) {
    val context = LocalContext.current
    Icon(
        ImageVector.vectorResource(id = R.drawable.ic_calendar),
        contentDescription = "All Sessions",
        modifier = Modifier
            .padding(2.dp)
            .clickable {
                showDatePickerDialog(
                    context,
                ) {
                    onSelectDateFromDatePicker(it)
                }
            },
        tint = colorResource(id = R.color.digi_blue)
    )
}

fun showDatePickerDialog(
    context: Context,
    datePicked: (String) -> Unit,
) {
    val year: Int
    val month: Int
    val day: Int
    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)
    val datePickerDialog = DatePickerDialog(
        context, R.style.DialogTheme,
        { _: DatePicker, yearPick: Int, monthPick: Int, dayOfMonth: Int ->
            val days = if (dayOfMonth < 10) "0$dayOfMonth"
            else "$dayOfMonth"
            val date = "$yearPick-${monthPick + 1}-$days"
            datePicked(date)
        }, year, month, day
    )
    datePickerDialog.show()
}

@Composable
fun SetTimerDropDown(
    onSelectTime: (String) -> Unit,
    time: String,
) {
    val context = LocalContext.current
    val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val currentTime = Calendar.getInstance().time
    var selectedTime by remember { mutableStateOf<Date?>(null) }
    if (selectedTime == null && time.isNotBlank()) {
        selectedTime = dateFormat.parse(time)
    }
    val selectedTimeText =
        if (selectedTime != null) selectedTime?.let { dateFormat.format(it) } ?: dateFormat.format(
            currentTime
        ) else time
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .wrapContentSize()
            .width(150.dp)
            .height(40.dp)
            .background(Color.White),
        border = BorderStroke(0.5.dp, colorResource(id = R.color.text_disabled_color)),
        backgroundColor = Color.White,
    ) {
        Row(
            modifier = Modifier
                .width(150.dp)
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SelectedDateText(
                selectedDate = selectedTimeText ?: ""
            )
            Icon(
                imageVector = Icons.Default.Schedule,
                modifier = Modifier.clickable {
                    expanded = !expanded
                },
                contentDescription = "Drawer Menu",
                tint = colorResource(id = R.color.digi_blue)
            )
        }

        if (expanded) {
            ShowTimePickerDialog(
                context = context,
                selectedTime = selectedTime,
                onTimeSelected = { newTime ->
                    selectedTime = newTime
                },
                onSelectTime = {
                    onSelectTime(selectedTime?.let { dateFormat.format(it) } ?: dateFormat.format(
                        currentTime
                    ))
                },
                onExpandClose = {
                    expanded = false
                }
            )
        }
    }
}

@Composable
fun ShowTimePickerDialog(
    context: Context,
    selectedTime: Date?,
    onTimeSelected: (Date) -> Unit,
    onSelectTime: () -> Unit,
    onExpandClose: () -> Unit,
) {
    val calendar = Calendar.getInstance()
    selectedTime?.let { calendar.time = it }
    val initialHour = calendar.get(Calendar.HOUR_OF_DAY)
    val initialMinute = calendar.get(Calendar.MINUTE)

    var dialogShownState by remember {
        mutableStateOf(false)
    }

    if (!dialogShownState) {
        // Mark the dialog as shown to prevent multiple invocations
        dialogShownState = true

        DisposableEffect(context) {
            val timePickerDialog = TimePickerDialog(
                context,
                { _, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    onTimeSelected(calendar.time)
                    dialogShownState = false // Reset the dialog state
                },
                initialHour,
                initialMinute,
                false
            )

            // Show the dialog when the Composable is first drawn
            timePickerDialog.show()

            onDispose {
                // Dismiss the dialog when the Composable is removed from the screen
                onSelectTime()
                onExpandClose()
                timePickerDialog.dismiss()
            }
        }
    }
}

@Composable
fun SelectSession(
    sessions: List<String>,
    onSessionClicked: (Int) -> Unit,
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = "SelectSession",
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
            )
            Column(modifier = Modifier.padding(start = 20.dp, top = 20.dp)) {
                sessions.forEachIndexed { index, session ->
                    Text(
                        text = session,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSessionClicked(index) },
                        fontSize = 14.sp,
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}

@Composable
fun StudentGroup(
    studentGroupList : List<StudentGroupUiModel>,
    onStudentGroupSelected : (Int, Boolean) -> Unit,
    onSaveClicked : () -> Unit,
    onCancelSelected : () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        val backButtonColor = ButtonDefaults.buttonColors(
            backgroundColor = colorResource(id = R.color.white),
            contentColor = colorResource(id = R.color.white)
        )
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = "Choose Student Group",
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 10.dp, start = 20.dp),
                fontSize = 18.sp,
            )
            Column(modifier = Modifier.padding(top = 20.dp)) {
                studentGroupList.forEachIndexed { index, studentGroup ->
                    FilterCheckbox(
                        studentGroupName = studentGroup.studentGroupText,
                        onCheckedChanged = {
                            onStudentGroupSelected(index,it)
                        },
                        isSelected = studentGroup.isSelected,
                    )
                }
            }
            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .background(Color.White)
                    .fillMaxWidth()
                    .height(65.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    colors = backButtonColor,
                    onClick = onCancelSelected,
                    modifier = Modifier
                        .width(120.dp)
                        .padding(8.dp),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(
                        width = 0.5.dp,
                        color = colorResource(id = R.color.colorGray)
                    )
                ) {
                    Text(
                        text = "Cancel",
                        color = colorResource(id = R.color.digi_blue),
                        fontSize = 16.sp,
                    )
                }
                Button(
                    colors = backButtonColor,
                    onClick = onSaveClicked,
                    modifier = Modifier
                        .width(120.dp)
                        .padding(8.dp),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(
                        width = 0.5.dp,
                        color = colorResource(id = R.color.colorGray)
                    )
                ) {
                    Text(
                        text = "Save",
                        color = colorResource(id = R.color.digi_blue),
                        fontSize = 16.sp,
                    )
                }
            }
        }
    }
}


@Composable
fun FilterCheckbox(
    studentGroupName: String,
    onCheckedChanged: (Boolean) -> Unit,
    isSelected: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Checkbox(
            checked = isSelected,
            modifier = Modifier.padding(start = 12.dp),
            onCheckedChange = { onCheckedChanged(it) },
            colors = CheckboxDefaults.colors(
                checkedColor = colorResource(id = R.color.digi_blue),
                uncheckedColor = colorResource(id = R.color.digi_blue),
            )
        )
        Text(
            text = studentGroupName,
            modifier = Modifier.padding(top = 12.dp),
            fontWeight = FontWeight.Normal
        )
    }
}