package com.muthuram.faceliveness.activityUi

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muthuram.faceliveness.R
import com.muthuram.faceliveness.activity.OptionUiModel
import com.muthuram.faceliveness.activity.TrueOrFalseUiModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StudentAnswerQuizScreen(
    isLoading: Boolean,
    quizTitle: String,
    questions: List<QuestionsUi?>,
    formatTime: String,
    onOptionSelected: (String, String) -> Unit,
    onSubmitClick: () -> Unit,
    isAllOptionClicked: Boolean,
    isEnableSubmitButton: Boolean,
    onCloseClicked: () -> Unit,
    onBackPressed: () -> Unit,
    onImagePreview: (String) -> Unit,
    onUploadUrl: (String, String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val signedUrl = remember { mutableStateOf(0) }

    ModalBottomSheetLayout(
        modifier = Modifier
            .fillMaxSize(),
        sheetState = sheetState,
        sheetContent = {
            QuizBottomSheet(
                onCloseClicked = onCloseClicked,
            )
        },
        scrimColor = Color.Black.copy(alpha = 0.5f),
        content = {
            LaunchedEffect(isAllOptionClicked) {
                scope.launch {
                    sheetState.show()
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                AnswerQuizTopBar(
                    questionsCount = questions.size,
                    quizTitle = quizTitle,
                    formatTime = formatTime,
                    onBackPressed = onBackPressed,
                )
                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Spacer(modifier = Modifier.padding(top = 20.dp))
                        CircularProgressIndicator(
                            color = colorResource(R.color.digi_blue)
                        )
                    }
                }
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    LazyColumn {
                        itemsIndexed(questions) { index, question ->

                        }
                    }
                }
                SubmitButton(
                    isEnableSubmitButton = isEnableSubmitButton,
                    onSubmitClick = onSubmitClick,
                )
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun AnswerQuestionCardPreview() {
    AnswerQuestionCard(
        questionText = "What is newton's 3rd law?",
        optionsUiModel = listOf(
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
        ),
        trueOrFalseUiModel = listOf(
            TrueOrFalseUiModel(
                id = "",
                text = "Option 1",
                isAnswer = true,
                isAnswered = true,
            ),
            TrueOrFalseUiModel(
                id = "",
                text = "Option 2",
                isAnswer = false,
                isAnswered = false,
            ),
        ),
        isMandatory = true,
        questionType = ActivityQuestionType.MCQ,
        questionTotalMark = 5,
        answerText = "Human-Computer Interaction for AI Systems Design course offered by Edureka, in collaboration with University of Cambridge Online equips students with skills to create user-friendly experiences for AI-powered systems and focuses on HCI principles.",
        onOptionSelected = {},
        onAnswerTextChanged = {},
        isMaximumCharacterReached = false,
        isMinimumCharacterReached = true,
    )
}

@Composable
fun AnswerQuestionCard(
    questionText: String,
    optionsUiModel: List<OptionUiModel>,
    trueOrFalseUiModel: List<TrueOrFalseUiModel>,
    isMandatory: Boolean,
    questionType: ActivityQuestionType,
    questionTotalMark: Int,
    answerText: String,
    onOptionSelected: (String) -> Unit,
    onAnswerTextChanged: (String) -> Unit,
    isMaximumCharacterReached: Boolean,
    isMinimumCharacterReached: Boolean,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(color = Color.White, shape = RoundedCornerShape(size = 8.dp)),
        backgroundColor = Color.White,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = questionText,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF374151),
                )
                if (isMandatory) {
                    Text(
                        text = "*",
                        fontSize = 22.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFFDC2626),
                    )
                }
            }
            when (questionType) {
                ActivityQuestionType.MCQ -> {
                    val radioButtonColors = RadioButtonDefaults.colors(
                        selectedColor = colorResource(id = R.color.digi_blue),
                        unselectedColor = colorResource(id = R.color.hint_color)
                    )
                    optionsUiModel.forEach { option ->
                        Row(
                            modifier = Modifier
                                .padding(top = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                        ) {
                            RadioButton(
                                modifier = Modifier.offset((-12).dp),
                                selected = option.isAnswer,
                                onClick = {
                                    onOptionSelected(option.id)
                                },
                                colors = radioButtonColors,
                            )
                            Text(
                                text = option.optionText,
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                                fontWeight = FontWeight(400),
                                color = Color(0xFF374151),
                            )
                        }
                    }
                }
                ActivityQuestionType.TRUE_OR_FALSE -> {
                    val radioButtonColors = RadioButtonDefaults.colors(
                        selectedColor = colorResource(id = R.color.digi_blue),
                        unselectedColor = colorResource(id = R.color.hint_color)
                    )
                    trueOrFalseUiModel.forEach { option ->
                        Row(
                            modifier = Modifier
                                .padding(top = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                        ) {
                            RadioButton(
                                modifier = Modifier.offset((-12).dp),
                                selected = option.isAnswer,
                                onClick = {
                                    onOptionSelected(option.id)
                                },
                                colors = radioButtonColors,
                            )
                            Text(
                                text = option.text,
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                                fontWeight = FontWeight(400),
                                color = Color(0xFF374151),
                            )
                        }
                    }
                }
                else -> {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = answerText,
                        onValueChange = onAnswerTextChanged,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            unfocusedBorderColor = Color(0xFFD1D5DB),
                            focusedBorderColor = colorResource(R.color.digi_blue),
                            backgroundColor = Color.Transparent,
                            cursorColor = colorResource(R.color.digi_blue)
                        ),
                        placeholder = {
                            Text(
                                text = "Your Answer",
                                color = Color(0xFF9CA3AF),
                                fontSize = 14.sp
                            )
                        },
                        textStyle = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.roboto_regular)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFF9CA3AF),
                        )
                    )
                    if (isMaximumCharacterReached || isMinimumCharacterReached) {
                        val text = if (isMaximumCharacterReached) "Maximum"
                        else "Minimum"
                        Text(
                            text = "$text - character reached",
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.roboto_regular)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFFF59E0B),
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = when (questionType) {
                        ActivityQuestionType.MCQ -> "Multiple Choice"
                        ActivityQuestionType.TRUE_OR_FALSE -> "True Or False"
                        else -> "Short Answer"
                    },
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF9CA3AF),
                )
                Text(
                    text = "Mark : ",
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF9CA3AF),
                )
                Text(
                    text = questionTotalMark.toString(),
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF6B7280),
                )
            }
        }
    }
}

@Composable
fun AnswerQuizTopBar(
    questionsCount: Int,
    quizTitle: String,
    formatTime: String,
    onBackPressed: () -> Unit,
) {
    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.digi_blue).copy(0.7f)),
        ) {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = quizTitle,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Start,
                            fontSize = 20.sp,
                        )
                        Row {
                            Text(
                                text = stringResource(
                                    id = R.string.surprise_quiz_timer,
                                    questionsCount
                                ),
                                color = Color.White,
                                fontSize = 14.sp,
                            )
                            Spacer(modifier = Modifier.padding(2.dp))
                            Text(
                                text = formatTime,
                                color = Color.White,
                                fontSize = 14.sp,
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onBackPressed() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.padding(start = 20.dp)
                        )
                    }
                },
                backgroundColor = Color.Transparent,
                modifier = Modifier.padding(10.dp),
                elevation = 0.dp
            )

        }
    }
}

@Composable
fun SubmitButton(
    isEnableSubmitButton: Boolean,
    onSubmitClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .height(60.dp),
        contentAlignment = Alignment.BottomCenter,

        ) {
        val submitButtonColor = ButtonDefaults.buttonColors(
            backgroundColor = colorResource(id = R.color.digi_blue),
            contentColor = colorResource(id = R.color.digi_blue)
        )
        Button(
            colors = submitButtonColor,
            onClick = {
                if (isEnableSubmitButton) onSubmitClick()
            },
            modifier = Modifier
                .padding(top = 8.dp, end = 16.dp, start = 16.dp, bottom = 8.dp)
                .fillMaxSize(),
            shape = RoundedCornerShape(20.dp),
        ) {
            Text(
                text = "Submit",
                color = colorResource(id = R.color.white),
                fontSize = 16.sp,
            )
        }
    }
}

@Composable
fun QuizBottomSheet(
    onCloseClicked: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(5.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(id = R.string.submited_successfully),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.padding(5.dp))
            Text(
                text = stringResource(id = R.string.your_answers_are_viewable),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                color = Color.Gray

            )
            Spacer(modifier = Modifier.padding(10.dp))
            Divider(
                color = colorResource(id = R.color.buzz_txt_color),
                thickness = 0.5.dp
            )
            Spacer(modifier = Modifier.padding(10.dp))
            val submitButtonColor = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(id = R.color.white),
                contentColor = colorResource(id = R.color.white)
            )
            Button(
                colors = submitButtonColor,
                border = BorderStroke(1.dp, colorResource(id = R.color.digi_blue)),
                onClick = {
                    onCloseClicked()
                },
                modifier = Modifier
                    .padding(top = 8.dp, end = 16.dp, start = 16.dp, bottom = 8.dp)
                    .width(362.dp)
                    .height(39.dp),
                shape = RoundedCornerShape(20.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.close),
                    color = colorResource(id = R.color.digi_blue),
                    fontSize = 16.sp,
                )
            }
            Spacer(modifier = Modifier.padding(5.dp))
        }
    }
}


data class QuestionsUi(
    val text: String,
    val id: String,
    val options: List<OptionUi>,
    var url: String,
)

data class OptionUi(
    val id: String,
    val text: String,
    var isSelected: Boolean,
)