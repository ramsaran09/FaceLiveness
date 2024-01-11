package com.muthuram.faceliveness.activityUi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.muthuram.faceliveness.R
import com.muthuram.faceliveness.activity.MatchAnswerUiModel
import com.muthuram.faceliveness.activity.MatchQuestionUiModel
import com.muthuram.faceliveness.activity.OptionUiModel
import com.muthuram.faceliveness.activity.TrueOrFalseUiModel

@Preview(showBackground = true)
@Composable
fun StartActivityScreenPreview() {
    StartActivityScreen(
        quizTitle = "Quiz for Anatomy Basics",
        sessions = "Anatomy . L1,L2",
        questions = listOf(
            ActivityQuestionsUiModel(
                id = "",
                questionText = "what is newtons 3rd law?",
                questionType = ActivityQuestionType.SHORT_ANSWER,
                isMandatory = true,
                optionUiModel = listOf(),
                answerKeys = arrayListOf(
                    "answer key 1",
                    "answer key 2",
                    "answer key 3",
                    "answer key 4",
                ),
                questionTotalMark = 5,
                trueOrFalseUiModel = listOf(),
                matchQuestionUiModel = listOf(),
                matchAnswerUiModel = listOf(),
            ),
            ActivityQuestionsUiModel(
                id = "",
                questionText = "which of the following is capital of TamilNadu?",
                questionType = ActivityQuestionType.MCQ,
                isMandatory = true,
                optionUiModel = listOf(
                    OptionUiModel(
                        id = "",
                        optionText = "Chennai",
                        isAnswer = false,
                        isAnswered = true,
                    ),
                    OptionUiModel(
                        id = "",
                        optionText = "Kochi",
                        isAnswer = true,
                        isAnswered = false,
                    ),
                    OptionUiModel(
                        id = "",
                        optionText = "Trichy",
                        isAnswer = false,
                        isAnswered = false,
                    ),
                    OptionUiModel(
                        id = "",
                        optionText = "Madurai",
                        isAnswer = false,
                        isAnswered = false,
                    ),
                ),
                questionTotalMark = 5,
                trueOrFalseUiModel = listOf(),
                matchQuestionUiModel = listOf(),
                matchAnswerUiModel = listOf(),
            ),
            ActivityQuestionsUiModel(
                id = "",
                questionText = "which of the following is capital of TamilNadu?",
                questionType = ActivityQuestionType.TRUE_OR_FALSE,
                isMandatory = true,
                optionUiModel = listOf(),
                questionTotalMark = 5,
                trueOrFalseUiModel = listOf(
                    TrueOrFalseUiModel(
                        id = "",
                        text = "True",
                        isAnswer = false,
                        isAnswered = true,
                    ),
                    TrueOrFalseUiModel(
                        id = "",
                        text = "False",
                        isAnswer = true,
                        isAnswered = false,
                    ),
                ),
                matchQuestionUiModel = listOf(),
                matchAnswerUiModel = listOf(),
            ),
            ActivityQuestionsUiModel(
                id = "",
                questionText = "which of the following is capital of TamilNadu?",
                questionType = ActivityQuestionType.MATCH,
                isMandatory = true,
                optionUiModel = listOf(),
                questionTotalMark = 5,
                trueOrFalseUiModel = listOf(),
                matchQuestionUiModel = listOf(
                    MatchQuestionUiModel(
                        id = "",
                        text = "Chennai is capital of which state in India",
                        answerPosition = 3,
                    ),
                    MatchQuestionUiModel(
                        id = "",
                        text = "Kochi",
                        answerPosition = 2,
                    ),
                    MatchQuestionUiModel(
                        id = "",
                        text = "Mumbai",
                        answerPosition = 1,
                    ),
                    MatchQuestionUiModel(
                        id = "",
                        text = "Bangalore",
                        answerPosition = 0,
                    ),
                ),
                matchAnswerUiModel = listOf(
                    MatchAnswerUiModel(
                        id = "",
                        text = "TamilNadu is highest producer of paddy",
                    ),
                    MatchAnswerUiModel(
                        id = "",
                        text = "Kerala",
                    ),
                    MatchAnswerUiModel(
                        id = "",
                        text = "Maharashtra",
                    ),
                    MatchAnswerUiModel(
                        id = "",
                        text = "Karnataka",
                    ),
                ),
            ),
        ),
        onBackPressed = {},
        onPreviewClicked = {},
        onStartQuiz = {},
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StartActivityScreen(
    quizTitle: String,
    sessions: String,
    questions: List<ActivityQuestionsUiModel>,
    onBackPressed: () -> Unit,
    onPreviewClicked: () -> Unit,
    onStartQuiz: () -> Unit,
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
                    AppTopBar(
                        title = quizTitle,
                        onBack = { onBackPressed() },
                        onScheduleClicked = {},
                        onDeleteClicked = {},
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
                            onStartQuiz()
                        },
                    ) {
                        Text(
                            text = "Start Quiz"
                        )
                    }
                }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color(0xFFEFEFEF))
                        .padding(
                            bottom = it.calculateBottomPadding()
                        )
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        PreviewQuizTitle(
                            quizTitle = sessions,
                            isPreview = false,
                            onPreviewClicked = onPreviewClicked,
                        )
                        LazyColumn {
                            items(questions) { question ->
                                PreviewQuestionCard(
                                    questionText = question.questionText,
                                    optionsUiModel = question.optionUiModel,
                                    trueOrFalseUiModel = question.trueOrFalseUiModel,
                                    isMandatory = question.isMandatory,
                                    questionType = question.questionType,
                                    isPreview = false,
                                    answerKeyList = question.answerKeys,
                                    questionTotalMark = question.questionTotalMark,
                                    matchAnswerUiModel = question.matchAnswerUiModel,
                                    matchQuestionUiModel = question.matchQuestionUiModel,
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}