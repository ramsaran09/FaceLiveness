package com.muthuram.faceliveness.activityUi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muthuram.faceliveness.R
import com.muthuram.faceliveness.activity.MatchQuestionUiModel
import com.muthuram.faceliveness.activity.OptionUiModel
import com.muthuram.faceliveness.activity.TrueOrFalseUiModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ActivityCorrectionScreen(
    activityName: String,
    studentName: String,
    userId: String,
    attendedQuestion: Int,
    totalQuestion: Int,
    overallScoredMark: Int,
    totalMark: Int,
    courseName: String,
    noOfQuestions: Int,
    //questionList: List<>,
    onBackIconPressed: () -> Unit,
    onShowStudentList: () -> Unit,
    onSaveResult: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    ModalBottomSheetLayout(
        sheetContent = {

        },
        sheetState = sheetState,
        scrimColor = Color.Black.copy(alpha = 0.5f),
        content = {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    ActivityViewResultTopBar(
                        tabTitles = arrayListOf(),
                        onTabSelected = {},
                        selectedTab = 0,
                        onBackIconPressed = onBackIconPressed,
                        activityName = activityName,
                        noOfQuestions = noOfQuestions,
                        courseName = courseName,
                    )
                },
                bottomBar = {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(id = R.color.upload_icon_color),
                            contentColor = Color.White
                        ),
                        onClick = {
                            onSaveResult()
                        },
                    ) {
                        Text(
                            text = "Save Result"
                        )
                    }
                }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color(0xFFF3F4F6))
                        .padding(
                            bottom = it.calculateBottomPadding()
                        )
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        IndividualQuizViewResult(
                            studentName = studentName,
                            userId = userId,
                            attendedQuestion = attendedQuestion,
                            totalQuestion = totalQuestion,
                            overallScoredMark = overallScoredMark,
                            totalMark = totalMark,
                            onShowStudentList = onShowStudentList,
                        )
                        LazyColumn {
                            //items()
                        }
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ActivityCorrectionQuestionCardPreview() {
    ActivityCorrectionQuestionCard(
        questionNumber = 1,
        questionText = "Explain what is multi-universe in Loki web series streaming on Disney Plus Hotstar",
        isExpanded = true,
        sessions = "L1",
        slo = "1.1",
        taxonomy = "Recall",
        isAnswered = true,
        answerText = "multi-universe in Loki web series was showing",
        questionType = ActivityQuestionType.MATCH,
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
            OptionUiModel(
                id = "",
                optionText = "Option 3",
                isAnswer = false,
                isAnswered = false,
            ),
        ),
        trueOrFalseUiModel = listOf(
            TrueOrFalseUiModel(
                id = "",
                text = "True",
                isAnswer = true,
                isAnswered = true,
            ),
            TrueOrFalseUiModel(
                id = "",
                text = "False",
                isAnswer = false,
                isAnswered = false,
            ),
        ),
        matchQuestionUiModel = listOf(
            MatchQuestionUiModel(
                id = "",
                text = "Chennai is capital of which state in India",
                answerPosition = 0,
            ),
            MatchQuestionUiModel(
                id = "",
                text = "Kochi",
                answerPosition = 1,
            ),
            MatchQuestionUiModel(
                id = "",
                text = "Mumbai",
                answerPosition = 2,
            ),
            MatchQuestionUiModel(
                id = "",
                text = "Bangalore",
                answerPosition = 3,
            ),
        ),
        isQuestionCorrected = true,
        isMarkedCorrect = false,
        onExpandChanged = {},
        onQuestionCorrected = {},
        questionTotalMark = 5,
    )
}

@Composable
fun ActivityCorrectionQuestionCard(
    questionNumber: Int,
    questionText: String,
    isExpanded: Boolean,
    sessions: String,
    slo: String,
    taxonomy: String,
    isAnswered: Boolean,
    answerText: String,
    questionType: ActivityQuestionType,
    isMarkedCorrect: Boolean,
    isQuestionCorrected: Boolean,
    questionTotalMark: Int,
    optionsUiModel: List<OptionUiModel>,
    trueOrFalseUiModel: List<TrueOrFalseUiModel>,
    matchQuestionUiModel: List<MatchQuestionUiModel>,
    onExpandChanged: (Boolean) -> Unit,
    onQuestionCorrected: (Boolean) -> Unit,
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
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "$questionNumber. $questionText?",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF374151),
                )
                IconButton(
                    modifier = Modifier.size(30.dp),
                    onClick = { onExpandChanged(!isExpanded) }) {
                    Icon(
                        imageVector = if (!isExpanded) Icons.Default.KeyboardArrowDown
                        else Icons.Default.KeyboardArrowUp,
                        contentDescription = "arrow",
                        tint = colorResource(id = R.color.grey_new)
                    )
                }
            }
            Text(
                modifier = Modifier.padding(vertical = 4.dp),
                text = "$sessions • SLO $slo • $taxonomy",
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontWeight = FontWeight(400),
                color = Color(0xFF9CA3AF),
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Divider()
            Spacer(modifier = Modifier.padding(8.dp))
            if (isExpanded) {
                when (questionType) {
                    ActivityQuestionType.MCQ -> {
                        val radioButtonColors = RadioButtonDefaults.colors(
                            selectedColor = Color(0xFF4B5563),
                            unselectedColor = Color.Black,
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
                                    onClick = {},
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
                        Spacer(modifier = Modifier.padding(8.dp))
                        Divider()
                        Spacer(modifier = Modifier.padding(8.dp))
                    }
                    ActivityQuestionType.TRUE_OR_FALSE -> {
                        val radioButtonColors = RadioButtonDefaults.colors(
                            selectedColor = Color(0xFF4B5563),
                            unselectedColor = Color.Black,
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
                                    onClick = {},
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
                        Spacer(modifier = Modifier.padding(8.dp))
                        Divider()
                        Spacer(modifier = Modifier.padding(8.dp))
                    }
                    ActivityQuestionType.MATCH -> {
                        matchQuestionUiModel.forEachIndexed { index, questionUiModel ->
                            MatchQuestionResultView(
                                index = index,
                                isCorrectingAnswer = true,
                                text = questionUiModel.text,
                                answerPosition = questionUiModel.answerPosition,
                            )
                        }
                        Spacer(modifier = Modifier.padding(8.dp))
                        Divider()
                        Spacer(modifier = Modifier.padding(8.dp))
                    }
                    else -> {
                        if (answerText.isNotEmpty() && isAnswered) {
                            Text(
                                text = answerText,
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                                fontWeight = FontWeight(400),
                                color = Color(0xFF4B5563),
                            )
                            Spacer(modifier = Modifier.padding(8.dp))
                            Divider()
                            Spacer(modifier = Modifier.padding(8.dp))
                        }
                    }
                }
                if (isAnswered) {
                    ValidateRightOrWrongCard(
                        isQuestionCorrected = isQuestionCorrected,
                        isMarkedCorrect = isMarkedCorrect,
                        onQuestionCorrected = onQuestionCorrected,
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    Divider()
                    Spacer(modifier = Modifier.padding(4.dp))
                } else {
                    NotRespondedCard()
                }
            }
            Spacer(modifier = Modifier.padding(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = when (questionType) {
                        ActivityQuestionType.SHORT_ANSWER -> "Short Answer Mark : "
                        ActivityQuestionType.TRUE_OR_FALSE -> "True Or False Mark : "
                        ActivityQuestionType.MATCH -> "Matching Mark : "
                        else -> "Multiple Choice Mark  : "
                    },
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF6B7280),
                )
                Text(
                    text = questionTotalMark.toString(),
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFF374151),
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotRespondedCardPreview() {
    NotRespondedCard()
}

@Composable
fun NotRespondedCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFF3F4F6),
                shape = RoundedCornerShape(size = 4.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),

        ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = "error",
                tint = Color(0xFFD97706)
            )
            Column {
                Text(
                    text = "Response Not Available",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF1F2937),
                )
                Text(
                    text = "Consider as a Wrong Answer",
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF6B7280),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ValidateRightOrWrongCardPreview() {
    ValidateRightOrWrongCard(
        isQuestionCorrected = true,
        isMarkedCorrect = false,
        onQuestionCorrected = {},
    )
}

@Composable
fun ValidateRightOrWrongCard(
    isQuestionCorrected: Boolean,
    isMarkedCorrect: Boolean,
    onQuestionCorrected: (Boolean) -> Unit,
) {
    val correctRadioButtonColors = RadioButtonDefaults.colors(
        selectedColor = Color(0xFF16A34A),
        unselectedColor = Color(0xFF16A34A),
    )
    val inCorrectRadioButtonColors = RadioButtonDefaults.colors(
        selectedColor = Color(0xFFDC2626),
        unselectedColor = Color(0xFFDC2626),
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(85.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            RadioButton(
                modifier = Modifier
                    .size(25.dp),
                selected = isMarkedCorrect && isQuestionCorrected,
                onClick = { onQuestionCorrected(true) },
                colors = correctRadioButtonColors,
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = "Correct",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontWeight = FontWeight(400),
                color = Color(0xFF16A34A),
            )
        }
        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            RadioButton(
                modifier = Modifier
                    .size(25.dp),
                selected = !isMarkedCorrect && isQuestionCorrected,
                onClick = { onQuestionCorrected(false) },
                colors = inCorrectRadioButtonColors,
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = "Incorrect",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontWeight = FontWeight(400),
                color = Color(0xFFDC2626),
            )
        }
    }
}