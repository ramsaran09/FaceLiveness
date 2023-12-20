package com.muthuram.faceliveness.activityUi

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.DragHandle
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muthuram.faceliveness.R
import com.muthuram.faceliveness.activity.OptionUiModel

@Composable
fun ActivityExitAlertDialogue(
    onEditClick: () -> Unit,
    onDismissAlertDialog: () -> Unit,
    tittle: String,
) {
    val cancelButtonColor = ButtonDefaults.buttonColors(
        backgroundColor = colorResource(id = R.color.white),
        contentColor = colorResource(id = R.color.digi_txt_color)
    )
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        backgroundColor = Color.White,
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                text = tittle,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.box_text_color),
            )
            Spacer(modifier = Modifier.padding(4.dp))
            /*Text(
                modifier = Modifier.padding(start = 16.dp, top = 4.dp, end = 16.dp),
                text = stringResource(id = R.string.exit_popup, subTittle),
                fontSize = 14.sp,
                color = colorResource(id = R.color.box_text_color),
            )
            Spacer(modifier = Modifier.padding(4.dp))*/
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(horizontalArrangement = Arrangement.Center) {
                    OutlinedButton(
                        colors = cancelButtonColor,
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, color = colorResource(id = R.color.white)),
                        onClick = {
                            onDismissAlertDialog()
                        },
                        modifier = Modifier
                            .padding(start = 16.dp, end = 4.dp, top = 10.dp)
                            .width(120.dp),
                    ) {
                        Text(
                            text = "Cancel",
                            fontSize = 15.sp,
                            modifier = Modifier.align(Alignment.CenterVertically),
                            textAlign = TextAlign.Center,
                            color = colorResource(R.color.digi_txt_color),
                        )
                    }
                    Spacer(modifier = Modifier.padding(10.dp))
                    Button(
                        onClick = {
                            onEditClick()
                        },
                        modifier = Modifier
                            .padding(top = 10.dp, start = 4.dp, end = 16.dp)
                            .width(130.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.digi_blue))
                    ) {
                        Text(
                            text = "Yes",
                            fontSize = 15.sp,
                            color = colorResource(R.color.white),
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.padding(4.dp))
        }
    }
}


@Composable
fun AppTopBar(
    title: String,
    onBack: () -> Unit,
    onScheduleClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
) {
    TopAppBar(
        backgroundColor = colorResource(id = R.color.digi_blue),
        title = {
            Text(
                text = title,
                color = Color.White,
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { onBack() }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.padding(start = 20.dp)
                )
            }
        },
        actions = {
            OverflowMenu { item ->
                when (item) {
                    "Schedule" -> {
                        onScheduleClicked()
                    }

                    "Delete" -> {
                        onDeleteClicked()
                    }
                }
            }
        }
    )
}

@Composable
fun OverflowMenu(
    onClickAction: (String?) -> Unit,
) {
    var showMenu by remember { mutableStateOf(false) }

    IconButton(onClick = {
        showMenu = !showMenu
    }) {
        Icon(
            imageVector = Icons.Outlined.MoreVert,
            contentDescription = "More Info",
            tint = Color.White,
        )
    }
    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = { showMenu = false }
    ) {
        DropdownMenuItem(onClick = {
            onClickAction("Schedule")
            showMenu = false
        }, content = {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Schedule",
                tint = Color.Black
            )
            Text(
                text = "Schedule",
                maxLines = 1,
                color = colorResource(id = R.color.digi_txt_color),
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 4.dp),
            )
        })
        Divider()
        DropdownMenuItem(onClick = {
            onClickAction("Delete")
            showMenu = false
        }, content = {
            Icon(
                imageVector = Icons.Default.Download,
                contentDescription = "Delete",
                tint = Color.Black
            )
            Text(
                text = "Delete",
                maxLines = 1,
                color = colorResource(id = R.color.digi_txt_color),
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 4.dp),
            )
        })

    }
}

@Composable
fun ActivityBottomBar(
    onPublish: () -> Unit,
    saveAsDraft: () -> Unit
) {
    val cancelButtonColor = ButtonDefaults.buttonColors(
        backgroundColor = colorResource(id = R.color.white),
        contentColor = colorResource(id = R.color.digi_blue)
    )
    val saveButtonColor = ButtonDefaults.buttonColors(
        backgroundColor = colorResource(id = R.color.digi_blue),
        contentColor = colorResource(id = R.color.white)
    )
    Card(
        backgroundColor = Color.White,
        elevation = 5.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 8.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedButton(
                colors = cancelButtonColor,
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, color = colorResource(id = R.color.digi_blue)),
                onClick = {
                    saveAsDraft()
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .weight(0.5f)
            ) {
                Text(
                    text = "Save As Draft",
                    color = colorResource(id = R.color.digi_blue),
                    style = TextStyle(fontWeight = FontWeight.Normal),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                )

            }
            Button(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .weight(0.5f),
                colors = saveButtonColor,
                shape = RoundedCornerShape(50),
                onClick = {
                    onPublish()
                },
            ) {
                Text(
                    text = "Publish",
                    color = Color.White,
                    style = TextStyle(fontWeight = FontWeight.Normal),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizGroupNamePreview() {
    QuizGroupName(
        quizTitle = "Quiz for Anatomy Basics . L1,L2",
        onEditClick = {},
        onPreviewClicked = {},
    )
}

@Composable
fun QuizGroupName(
    quizTitle: String,
    onEditClick: () -> Unit,
    onPreviewClicked: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color.White,
        elevation = 5.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Text(
                modifier = Modifier.weight(1f).padding(start = 8.dp),
                text = quizTitle,
                color = Color.Black,
                fontSize = 16.sp,
            )
            Icon(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .clickable { onEditClick() },
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = "edit",
            )
            Icon(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .clickable { onPreviewClicked() },
                imageVector = Icons.Default.Visibility,
                contentDescription = "edit",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FeedbackContentInfoPreview() {
    FeedbackContentInfo(
        questionText = "Differentiate active and passive transport mechanism across cell membrane",
        isGeneral = true,
        rightAnswerFeedback = "Right answer Feedback in less then 50 words\n" +
                "which will be shown once student Submit \n" +
                "the answer",
        wrongAnswerFeedback = "Right answer Feedback in less then 50 words\n" +
                "which will be shown once student Submit \n" +
                "the answer",
        generalFeedback = "Right answer Feedback in less then 50 words\n" +
                "which will be shown once student Submit \n" +
                "the answer",
        onEditClicked = {},
    )
}

@Composable
fun FeedbackContentInfo(
    questionText: String,
    isGeneral: Boolean,
    rightAnswerFeedback: String,
    wrongAnswerFeedback: String,
    generalFeedback: String,
    onEditClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFF9FAFB),
                )
                .padding(4.dp),
            text = questionText,
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.roboto_regular)),
            fontWeight = FontWeight(400),
            color = Color(0xFF4B5563),
        )
        Spacer(modifier = Modifier.padding(8.dp))
        if (isGeneral) {
            Text(
                text = "General Feedback",
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontWeight = FontWeight(400),
                color = Color(0x61000000),
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = generalFeedback,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontWeight = FontWeight(400),
                color = Color(0xDE000000),
            )
        } else {
            Text(
                text = "Wrong Answer Feedback",
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontWeight = FontWeight(400),
                color = Color(0x61000000),
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = wrongAnswerFeedback,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontWeight = FontWeight(400),
                color = Color(0xDE000000),
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = "Right Answer Feedback",
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontWeight = FontWeight(400),
                color = Color(0x61000000),
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = rightAnswerFeedback,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontWeight = FontWeight(400),
                color = Color(0xDE000000),
            )
        }
        Spacer(modifier = Modifier.padding(4.dp))
        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            onClick = {
                onEditClicked()
            },
            shape = CircleShape,
            border = BorderStroke(1.dp, colorResource(id = R.color.digi_blue)),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = colorResource(id = R.color.digi_blue)
            )
        ) {
            Text(
                text = "Edit",
                color = colorResource(id = R.color.digi_blue),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddFeedbackBottomSheetPreview() {
    AddFeedbackBottomSheet(
        title = "Add FeedBack",
        isGeneral = true,
        generalFeedback = "",
        rightAnswerFeedback = "",
        wrongAnswerFeedback = "",
        onClosedClicked = {},
        onGeneralSelected = {},
        onGeneralFeedBackEntered = {},
        onRightAnswerFeedbackEntered = {},
        onWrongAnswerFeedbackEntered = {},
        onCancelClicked = {},
        onSaveClicked = {},
    )
}

@Composable
fun AddFeedbackBottomSheet(
    title: String,
    isGeneral: Boolean,
    generalFeedback: String,
    rightAnswerFeedback: String,
    wrongAnswerFeedback: String,
    onClosedClicked: () -> Unit,
    onGeneralSelected: (Boolean) -> Unit,
    onGeneralFeedBackEntered: (String) -> Unit,
    onRightAnswerFeedbackEntered: (String) -> Unit,
    onWrongAnswerFeedbackEntered: (String) -> Unit,
    onCancelClicked: () -> Unit,
    onSaveClicked: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
        color = Color.White,
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular))
                )
                IconButton(onClick = { onClosedClicked() }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear",
                        tint = colorResource(id = R.color.box_text_color)
                    )
                }
            }
            Spacer(modifier = Modifier.padding(4.dp))
            //content starts
            Text(
                text = "Feedback For",
                fontSize = 12.sp,
                color = colorResource(id = R.color.grey_natural)
            )
            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                RadioButton(
                    selected = !isGeneral,
                    onClick = {
                        onGeneralSelected(false)
                    }
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Right/Wrong Answer",
                    color = Color.Black,
                    modifier = Modifier
                        .clickable {
                            onGeneralSelected(false)
                        }
                )
            }
            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                RadioButton(
                    selected = isGeneral,
                    onClick = {
                        onGeneralSelected(true)
                    }
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "General",
                    color = Color.Black,
                    modifier = Modifier
                        .clickable {
                            onGeneralSelected(true)
                        }
                )
            }
            Spacer(modifier = Modifier.padding(4.dp))
            Divider()
            Spacer(modifier = Modifier.padding(4.dp))
            if (isGeneral) {
                FeedbackDescriptionComponent(
                    title = "General",
                    enteredText = generalFeedback,
                    onTitleEntered = onGeneralFeedBackEntered,
                )
            } else {
                FeedbackDescriptionComponent(
                    title = "Right Answer Feedback",
                    enteredText = rightAnswerFeedback,
                    onTitleEntered = onRightAnswerFeedbackEntered,
                )
                Spacer(modifier = Modifier.padding(4.dp))
                FeedbackDescriptionComponent(
                    title = "Wrong Answer Feedback",
                    enteredText = wrongAnswerFeedback,
                    onTitleEntered = onWrongAnswerFeedbackEntered,
                )
            }
            Spacer(modifier = Modifier.padding(4.dp))
            //content ends
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
                        .fillMaxWidth()
                        .padding(end = 8.dp),
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
}


@Composable
fun FeedbackDescriptionComponent(
    title: String,
    enteredText: String,
    onTitleEntered: (String) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White)
    ) {
        DisCussForumTitleText(text = title)
        OutlinedTextField(
            modifier =
            Modifier
                .height(150.dp)
                .fillMaxWidth()
                .padding(top = 5.dp),
            value = enteredText,
            onValueChange = {
                onTitleEntered(it)
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color(0xFFD1D5DB),
                focusedBorderColor = colorResource(R.color.digi_blue),
                backgroundColor = Color.Transparent,
                cursorColor = colorResource(R.color.digi_blue)
            ),
            placeholder = {
                Text(
                    text = "Type Feedback in less than 50 words",
                    color = Color.LightGray,
                    fontSize = 12.sp
                )
            }
        )
    }
}

@Composable
fun DisCussForumTitleText(
    text: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = text,
            color = Color(0XFF6B7280),
            fontSize = 14.sp,
            fontWeight = FontWeight(400),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChooseAnswerForShortAnswerPreview() {
    ChooseAnswerForShortAnswer(
        questionText = "Differentiate active and passive transport mechanism across cell membrane",
        benchMark = 5,
        isExactTextMatches = false,
        onExactTextMatches = {},
        keywordUiModel = listOf(
            ShortAnswerKeyWordUiModel("", "", ""),
        ),
        onKeywordTextEntered = { _, _ -> },
        onKeywordVariantTextEntered = { _, _ -> },
        onKeywordMarkEntered = { _, _ -> },
        onRemoveKeyword = {},
        onAddKeywordTextField = {},
        isNonEdit = false,
        onTotalBenchMarkSelected = {},
    )
}

@Composable
fun ChooseAnswerForShortAnswer(
    isNonEdit: Boolean,
    questionText: String,
    benchMark: Int,
    isExactTextMatches: Boolean,
    onExactTextMatches: (Boolean) -> Unit,
    keywordUiModel: List<ShortAnswerKeyWordUiModel>,
    onKeywordTextEntered: (String, Int) -> Unit,
    onKeywordVariantTextEntered: (String, Int) -> Unit,
    onKeywordMarkEntered: (String, Int) -> Unit,
    onRemoveKeyword: (Int) -> Unit,
    onAddKeywordTextField: () -> Unit,
    onTotalBenchMarkSelected: (Int) -> Unit,
) {
    var benchMarkExpand by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFF9FAFB),
                )
                .padding(4.dp),
            text = questionText,
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.roboto_regular)),
            fontWeight = FontWeight(400),
            color = Color(0xFF4B5563),
        )
        Spacer(modifier = Modifier.padding(4.dp))
        ValidatingAnswerForShortAnswer(
            isExactTextMatches = isExactTextMatches,
            onExactTextMatches = onExactTextMatches,
            isNonEdit = isNonEdit,
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = "Benchmark(Max - 5)",
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.roboto_bold)),
            fontWeight = FontWeight(500),
            color = Color(0xFF374151),
        )
        Spacer(modifier = Modifier.padding(4.dp))
        if (!isNonEdit) {
            Text(
                text = "Select mark",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontWeight = FontWeight(400),
                color = Color(0xFF6B7280),
            )
        }
        Row(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = Color(0xFFD1D5DB),
                    shape = RoundedCornerShape(size = 4.dp)
                )
                .width(100.dp)
                .height(48.dp)
                .background(
                    color = Color.White, shape = RoundedCornerShape(size = 4.dp)
                )
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier
                    .width(46.dp),
                text = if (benchMark == 0) "00" else benchMark.toString(),
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontWeight = FontWeight(400),
                color = Color(0xFF4B5563),
            )
            if (!isNonEdit) {
                Icon(
                    modifier = Modifier
                        .clickable { benchMarkExpand = true }
                        .padding(1.dp)
                        .size(24.dp),
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Drop down",
                )
                if (benchMarkExpand) {
                    DropdownMenu(
                        expanded = true,
                        onDismissRequest = { benchMarkExpand = false },
                        offset = DpOffset(200.dp, 0.dp)
                    ) {
                        (1..5).forEach {
                            DropdownMenuItem(
                                onClick = {
                                    onTotalBenchMarkSelected(it)
                                    benchMarkExpand = false
                                }
                            ) {
                                Text("$it")
                            }
                            Divider(
                                modifier = Modifier.fillMaxWidth(),
                                color = Color(0xFFEEEEEE),
                                thickness = 1.dp
                            )
                        }
                    }
                }
            }
        }
        AddAnswerKeyWordsText(
            isNonEdit = isNonEdit,
            isExactTextCorrection = isExactTextMatches,
            keywordUiModel = keywordUiModel,
            onKeywordTextEntered = onKeywordTextEntered,
            onKeywordVariantTextEntered = onKeywordVariantTextEntered,
            onKeywordMarkEntered = onKeywordMarkEntered,
            onRemoveKeyword = onRemoveKeyword,
            onAddKeywordTextField = onAddKeywordTextField,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ValidatingAnswerForShortAnswerPreview() {
    ValidatingAnswerForShortAnswer(
        isNonEdit = true,
        isExactTextMatches = true,
        onExactTextMatches = {}
    )
}

@Composable
fun ValidatingAnswerForShortAnswer(
    isNonEdit: Boolean,
    isExactTextMatches: Boolean,
    onExactTextMatches: (Boolean) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
    ) {
        Text(
            text = "Select Answer key matching type",
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.roboto_bold)),
            fontWeight = FontWeight(500),
            color = Color(0xFF374151),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
        ) {
            RadioButton(
                selected = isExactTextMatches,
                onClick = {
                    if (!isNonEdit) onExactTextMatches(true)
                }
            )
            Text(
                text = "if a responder's input matches this exact text, it's considered correct.",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontWeight = FontWeight(400),
                color = Color(0xFF374151),
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
        ) {
            RadioButton(
                selected = !isExactTextMatches,
                onClick = {
                    if (!isNonEdit) onExactTextMatches(false)
                }
            )
            Text(
                text = "A fuzzy search searches for text that matches a term closely instead of exactly.",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontWeight = FontWeight(400),
                color = Color(0xFF374151),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddAnswerKeyWordsTextPreview() {
    AddAnswerKeyWordsText(
        isNonEdit = true,
        isExactTextCorrection = true,
        keywordUiModel = listOf(
            ShortAnswerKeyWordUiModel("", "", ""),
        ),
        { _, _ -> },
        { _, _ -> },
        { _, _ -> },
        {},
        {},
    )
}

@Composable
fun AddAnswerKeyWordsText(
    isNonEdit: Boolean,
    isExactTextCorrection: Boolean,
    keywordUiModel: List<ShortAnswerKeyWordUiModel>,
    onKeywordTextEntered: (String, Int) -> Unit,
    onKeywordVariantTextEntered: (String, Int) -> Unit,
    onKeywordMarkEntered: (String, Int) -> Unit,
    onRemoveKeyword: (Int) -> Unit,
    onAddKeywordTextField: () -> Unit,
) {
    Column(
        modifier = if (!isExactTextCorrection) Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .horizontalScroll(rememberScrollState())
        else Modifier.fillMaxWidth(),
    ) {
        if (isExactTextCorrection) {
            AnswerKeywordsCaptionForExactText()
        } else {
            AnswerKeywordsCaptionForNonExactText()
        }
        keywordUiModel.forEachIndexed { index, keyWordUiModel ->
            if (isExactTextCorrection) {
                AddAnswerKeyWordsTextForExactText(
                    isNonEdit = isNonEdit,
                    index = index,
                    keywordText = keyWordUiModel.keyword,
                    keywordMark = keyWordUiModel.keywordMark,
                    onKeywordTextEntered = { onKeywordTextEntered(it, index) },
                    onKeywordMarkEntered = { onKeywordMarkEntered(it, index) },
                    onRemoveKeyword = { onRemoveKeyword(index) },
                )
            } else {
                AddAnswerKeyWordsTextForNonExactText(
                    isNonEdit = isNonEdit,
                    index = index,
                    keywordText = keyWordUiModel.keyword,
                    keywordTextVariant = keyWordUiModel.keywordVariant,
                    keywordMark = keyWordUiModel.keywordMark,
                    onKeywordTextEntered = { onKeywordTextEntered(it, index) },
                    onKeywordVariantTextEntered = { onKeywordVariantTextEntered(it, index) },
                    onKeywordMarkEntered = { onKeywordMarkEntered(it, index) },
                    onRemoveKeyword = { onRemoveKeyword(index) },
                )
            }
        }
        if (!isNonEdit) {
            Row(
                Modifier
                    .clickable {
                        onAddKeywordTextField()
                    }
                    .padding(vertical = 4.dp)
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Default.Add,
                    contentDescription = "image description",
                    tint = colorResource(id = R.color.digi_blue),
                )
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = "Add Key",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_bold)),
                    fontWeight = FontWeight(500),
                    color = colorResource(id = R.color.digi_blue),
                )
            }
        }
    }
}

data class ShortAnswerKeyWordUiModel(
    val keyword: String,
    val keywordVariant: String,
    val keywordMark: String,
)

@Composable
fun AnswerKeywordsCaptionForNonExactText() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Variant 1",
            fontSize = 12.sp,
            fontFamily = FontFamily(Font(R.font.roboto_regular)),
            fontWeight = FontWeight(400),
            color = Color(0xFF6B7280),
        )
        Spacer(modifier = Modifier.padding(horizontal = 72.dp))
        Text(
            text = "Variant 2",
            fontSize = 12.sp,
            fontFamily = FontFamily(Font(R.font.roboto_regular)),
            fontWeight = FontWeight(400),
            color = Color(0xFF6B7280),
        )
        Spacer(modifier = Modifier.padding(horizontal = 60.dp))
        Text(
            text = "Marks (Total - 05)",
            fontSize = 12.sp,
            fontFamily = FontFamily(Font(R.font.roboto_regular)),
            fontWeight = FontWeight(400),
            color = Color(0xFF6B7280),
        )
        Spacer(modifier = Modifier.padding(horizontal = 50.dp))
    }
}

@Composable
fun AnswerKeywordsCaptionForExactText() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Answer key",
            fontSize = 12.sp,
            fontFamily = FontFamily(Font(R.font.roboto_regular)),
            fontWeight = FontWeight(400),
            color = Color(0xFF6B7280),
        )
        Spacer(modifier = Modifier.weight(0.4f))
        Text(
            text = "Marks (Total - 05)",
            fontSize = 12.sp,
            fontFamily = FontFamily(Font(R.font.roboto_regular)),
            fontWeight = FontWeight(400),
            color = Color(0xFF6B7280),
        )
        Spacer(modifier = Modifier.weight(0.1f))
    }
}

@Preview(showBackground = true)
@Composable
fun AddAnswerKeyWordsTextWithMarksPreview() {
    AddAnswerKeyWordsTextForExactText(
        isNonEdit = true,
        index = 0,
        keywordText = "",
        keywordMark = "",
        onKeywordTextEntered = {},
        onKeywordMarkEntered = {},
        onRemoveKeyword = {},
    )
}

@Composable
fun AddAnswerKeyWordsTextForExactText(
    isNonEdit: Boolean,
    index: Int,
    keywordText: String,
    keywordMark: String,
    onKeywordTextEntered: (String) -> Unit,
    onKeywordMarkEntered: (String) -> Unit,
    onRemoveKeyword: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Top,
    ) {
        TextField(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = if (isNonEdit) Color.White
                    else Color(0xFFD1D5DB),
                    shape = RoundedCornerShape(size = 4.dp)
                )
                .weight(1f)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(size = 4.dp)
                )
                .padding(horizontal = 4.dp, vertical = 4.dp),
            value = keywordText,
            onValueChange = {
                onKeywordTextEntered(it)
            },
            readOnly = isNonEdit,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color(0xFFD1D5DB),
                focusedBorderColor = colorResource(R.color.digi_blue),
                backgroundColor = Color.Transparent,
                cursorColor = colorResource(R.color.digi_blue)
            ),
            placeholder = {
                Text(
                    text = "Answer Key Text ${index + 1}",
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF374151),
                )
            }
        )
        Spacer(modifier = Modifier.padding(16.dp))
        TextField(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = if (isNonEdit) Color.White
                    else Color(0xFFD1D5DB),
                    shape = RoundedCornerShape(size = 4.dp)
                )
                .weight(0.35f)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(size = 4.dp)
                )
                .padding(horizontal = 4.dp, vertical = 4.dp),
            value = keywordMark,
            onValueChange = {
                onKeywordMarkEntered(it)
            },
            readOnly = isNonEdit,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color(0xFFD1D5DB),
                focusedBorderColor = colorResource(R.color.digi_blue),
                backgroundColor = Color.Transparent,
                cursorColor = colorResource(R.color.digi_blue)
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            placeholder = {
                Text(
                    text = "1",
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF374151),
                )
            }
        )
        if (!isNonEdit) {
            Spacer(modifier = Modifier.padding(8.dp))
            IconButton(onClick = { onRemoveKeyword() }) {
                Icon(
                    modifier = Modifier.padding(top = 8.dp),
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear",
                    tint = colorResource(id = R.color.box_text_color)
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun AddAnswerKeyWordsTextForNonExactTextPreview() {
    AddAnswerKeyWordsTextForNonExactText(
        isNonEdit = true,
        index = 0,
        keywordText = "",
        keywordTextVariant = "",
        keywordMark = "",
        onKeywordTextEntered = {},
        onKeywordVariantTextEntered = {},
        onKeywordMarkEntered = {},
        onRemoveKeyword = {},
    )
}

@Composable
fun AddAnswerKeyWordsTextForNonExactText(
    isNonEdit: Boolean,
    index: Int,
    keywordText: String,
    keywordTextVariant: String,
    keywordMark: String,
    onKeywordTextEntered: (String) -> Unit,
    onKeywordVariantTextEntered: (String) -> Unit,
    onKeywordMarkEntered: (String) -> Unit,
    onRemoveKeyword: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextField(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = if (isNonEdit) Color.White
                    else Color(0xFFD1D5DB),
                    shape = RoundedCornerShape(size = 4.dp)
                )
                .width(150.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(size = 4.dp)
                )
                .padding(horizontal = 4.dp, vertical = 4.dp),
            value = keywordText,
            onValueChange = {
                onKeywordTextEntered(it)
            },
            readOnly = isNonEdit,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color(0xFFD1D5DB),
                focusedBorderColor = colorResource(R.color.digi_blue),
                backgroundColor = Color.Transparent,
                cursorColor = colorResource(R.color.digi_blue)
            ),
            placeholder = {
                Text(
                    text = "Answer Key Text ${index + 1}",
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF374151),
                )
            }
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Box(contentAlignment = Alignment.Center) {
            DoubleEqualUi()
        }
        Spacer(modifier = Modifier.padding(4.dp))
        TextField(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = if (isNonEdit) Color.White
                    else Color(0xFFD1D5DB),
                    shape = RoundedCornerShape(size = 4.dp)
                )
                .width(150.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(size = 4.dp)
                )
                .padding(horizontal = 4.dp, vertical = 4.dp),
            value = keywordTextVariant,
            onValueChange = {
                onKeywordVariantTextEntered(it)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color(0xFFD1D5DB),
                focusedBorderColor = colorResource(R.color.digi_blue),
                backgroundColor = Color.Transparent,
                cursorColor = colorResource(R.color.digi_blue)
            ),
            readOnly = isNonEdit,
            placeholder = {
                Text(
                    text = "Answer Key Text ${index + 1}",
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF374151),
                )
            }
        )
        Spacer(modifier = Modifier.padding(8.dp))
        TextField(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = if (isNonEdit) Color.White
                    else Color(0xFFD1D5DB),
                    shape = RoundedCornerShape(size = 4.dp)
                )
                .width(60.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(size = 4.dp)
                )
                .padding(horizontal = 4.dp, vertical = 4.dp),
            value = keywordMark,
            onValueChange = {
                onKeywordMarkEntered(it)
            },
            readOnly = isNonEdit,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color(0xFFD1D5DB),
                focusedBorderColor = colorResource(R.color.digi_blue),
                backgroundColor = Color.Transparent,
                cursorColor = colorResource(R.color.digi_blue)
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            placeholder = {
                Text(
                    text = "1",
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF374151),
                )
            }
        )
        if (!isNonEdit) {
            Spacer(modifier = Modifier.padding(8.dp))
            IconButton(onClick = { onRemoveKeyword() }) {
                Icon(
                    modifier = Modifier.padding(top = 8.dp),
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear",
                    tint = colorResource(id = R.color.box_text_color)
                )
            }
        }
    }
}

@Composable
fun DoubleEqualUi() {
    Column(
        modifier = Modifier.size(24.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Outlined.DragHandle,
            contentDescription = "remove",
            tint = Color(0xFF9CA3AF)
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Icon(
            imageVector = Icons.Outlined.DragHandle,
            contentDescription = "remove",
            tint = Color(0xFF9CA3AF)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ChooseAnswerForMCQPreview() {
    ChooseAnswerForMCQ(
        questionText = "Differentiate active and passive transport mechanism across cell membrane",
        options = listOf(
            OptionUiModel(
                id = "",
                optionText = "Option 1",
                isAnswer = true,
                isAnswered = true,
            ),
            OptionUiModel(
                id = "",
                optionText = "Option 2",
                isAnswer = false,
                isAnswered = false,
            ),
            OptionUiModel(
                id = "",
                optionText = "Option 3",
                isAnswer = false,
                isAnswered = false,
            ),
        ),
        onOptionSelected = { _, _ -> },
        isNonEdit = false,
        onEditClicked = {},
    )
}

@Composable
fun ChooseAnswerForMCQ(
    questionText: String,
    options: List<OptionUiModel>,
    onOptionSelected: (Int, Boolean) -> Unit,
    isNonEdit: Boolean,
    onEditClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFF9FAFB),
                )
                .padding(4.dp),
            text = questionText,
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.roboto_regular)),
            fontWeight = FontWeight(400),
            color = Color(0xFF4B5563),
        )
        Spacer(modifier = Modifier.padding(4.dp))
        options.forEachIndexed { index, optionUiModel ->
            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clickable {
                        if (!optionUiModel.isAnswer && !isNonEdit) {
                            optionUiModel.isAnswer = true
                            onOptionSelected(index, true)
                        }
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                RadioButton(
                    selected = optionUiModel.isAnswer,
                    onClick = {
                        if (!optionUiModel.isAnswer && !isNonEdit) {
                            optionUiModel.isAnswer = true
                            onOptionSelected(index, true)
                        }
                    }
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = optionUiModel.optionText,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF374151),
                )
            }
        }
        Spacer(modifier = Modifier.padding(4.dp))
        if (isNonEdit) {
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                onClick = {
                    onEditClicked()
                },
                shape = CircleShape,
                border = BorderStroke(1.dp, colorResource(id = R.color.digi_blue)),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = colorResource(id = R.color.digi_blue)
                )
            ) {
                Text(
                    text = "Edit",
                    color = colorResource(id = R.color.digi_blue),
                )
            }
        }
    }
}
