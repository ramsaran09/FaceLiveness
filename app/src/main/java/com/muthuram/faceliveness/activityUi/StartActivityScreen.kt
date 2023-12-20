package com.muthuram.faceliveness.activityUi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.muthuram.faceliveness.activity.OptionUiModel

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
                        questions.forEach { question ->
                            PreviewQuestionCard(
                                questionText = question.questionText,
                                optionsUiModel = question.optionUiModel,
                                isMandatory = question.isMandatory,
                                questionType = question.questionType,
                                isPreview = false,
                                answerKeyList = question.answerKeys,
                                questionTotalMark = question.questionTotalMark,
                            )
                        }
                    }
                }
            }
        }
    )
}