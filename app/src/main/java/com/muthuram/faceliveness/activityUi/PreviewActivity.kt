package com.muthuram.faceliveness.activityUi

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muthuram.faceliveness.R
import com.muthuram.faceliveness.activity.MatchAnswerUiModel
import com.muthuram.faceliveness.activity.MatchQuestionUiModel
import com.muthuram.faceliveness.activity.OptionUiModel
import com.muthuram.faceliveness.activity.TrueOrFalseUiModel

@Preview(showBackground = true)
@Composable
fun PreviewActivityScreenPreview() {
    PreviewActivityScreen(
        quizTitle = "Quiz for Anatomy Basics . L1,L2",
        onSubmitClicked = {},
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
                        isAnswer = true,
                        isAnswered = true,
                    ),
                    OptionUiModel(
                        id = "",
                        optionText = "Kochi",
                        isAnswer = false,
                        isAnswered = false,
                    ),OptionUiModel(
                        id = "",
                        optionText = "Trichy",
                        isAnswer = false,
                        isAnswered = false,
                    ),OptionUiModel(
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
                        isAnswer = true,
                        isAnswered = true,
                    ),
                    TrueOrFalseUiModel(
                        id = "",
                        text = "False",
                        isAnswer = false,
                        isAnswered = false,
                    )
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
                matchAnswerUiModel = listOf(
                    MatchAnswerUiModel(
                        id = "",
                        text = "TamilNadu is highest producer of paddy",
                    ),
                    MatchAnswerUiModel(
                        id = "",
                        text = "Kerala",
                    ),MatchAnswerUiModel(
                        id = "",
                        text = "Maharashtra",
                    ),MatchAnswerUiModel(
                        id = "",
                        text = "Karnataka",
                    ),
                ),
            ),
        ),
    )
}

@Composable
fun PreviewActivityScreen(
    quizTitle : String,
    onSubmitClicked : () -> Unit,
    questions : List<ActivityQuestionsUiModel>
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            PreviewTopBar(
                onClosePreviewSelected = onSubmitClicked
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
                    onSubmitClicked()
                },
            ) {
                Text(
                    text = "Save"
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
                    quizTitle = quizTitle,
                    isPreview = true,
                    onPreviewClicked = {},
                )
                LazyColumn {
                    items(questions) { question ->
                        PreviewQuestionCard(
                            questionText = question.questionText,
                            optionsUiModel = question.optionUiModel,
                            trueOrFalseUiModel = question.trueOrFalseUiModel,
                            matchQuestionUiModel = question.matchQuestionUiModel,
                            matchAnswerUiModel = question.matchAnswerUiModel,
                            isMandatory = question.isMandatory,
                            questionType = question.questionType,
                            isPreview = true,
                            answerKeyList = question.answerKeys,
                            questionTotalMark = question.questionTotalMark,
                        )
                    }
                }
            }

        }
    }
}

enum class ActivityQuestionType(val value : String){
    SHORT_ANSWER("Short Answer"),
    MCQ("Multiple Choice"),
    TRUE_OR_FALSE("True Or False"),
    MATCH("Match")
}

data class ActivityQuestionsUiModel(
    val id : String,
    val questionText : String,
    val questionType : ActivityQuestionType,
    val optionUiModel: List<OptionUiModel>,
    val trueOrFalseUiModel: List<TrueOrFalseUiModel>,
    val matchQuestionUiModel: List<MatchQuestionUiModel>,
    val matchAnswerUiModel: List<MatchAnswerUiModel>,
    val isMandatory : Boolean,
    val answerKeys : ArrayList<String> = arrayListOf(),
    val questionTotalMark : Int,
)

@Preview(showBackground = true)
@Composable
fun PreviewQuizTitlePreview() {
    PreviewQuizTitle(
        quizTitle = "Quiz for Anatomy Basics . L1,L2",
        isPreview = false,
        onPreviewClicked = {},
    )
}

@Composable
fun PreviewQuizTitle(
    quizTitle : String,
    isPreview : Boolean,
    onPreviewClicked : () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = quizTitle,
                color = Color.Black,
                fontSize = 18.sp,
            )
            if (!isPreview) {
                Icon(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .clickable { onPreviewClicked() },
                    imageVector = Icons.Outlined.Visibility,
                    contentDescription = "edit",
                    tint = Color.Black.copy(0.5f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTopBarReview() {
    PreviewTopBar {}
}

@Composable
fun PreviewTopBar(
    onClosePreviewSelected : () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.digi_blue))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Preview",
            color = Color.White,
            fontSize = 18.sp,
            fontFamily = FontFamily(Font(R.font.roboto_bold)),
            fontWeight = FontWeight(500),
        )
        Text(
            modifier = Modifier.clickable { onClosePreviewSelected() },
            text = "CLOSE PREVIEW",
            color = Color.White,
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.roboto_bold)),
            fontWeight = FontWeight(500),
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewQuestionCardPreview() {
    PreviewQuestionCard(
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
                text = "True",
                isAnswer = true,
                isAnswered = false,
            ),
            TrueOrFalseUiModel(
                id = "",
                text = "False",
                isAnswer = false,
                isAnswered = false,
            ),
        ),
        isMandatory = true,
        questionType = ActivityQuestionType.SHORT_ANSWER,
        isPreview = false,
        answerKeyList = listOf(),
        questionTotalMark = 5,
        matchQuestionUiModel = listOf(),
        matchAnswerUiModel = listOf(),
    )
}

@Composable
fun PreviewQuestionCard(
    questionText: String,
    optionsUiModel : List<OptionUiModel>,
    trueOrFalseUiModel : List<TrueOrFalseUiModel>,
    matchQuestionUiModel: List<MatchQuestionUiModel>,
    matchAnswerUiModel: List<MatchAnswerUiModel>,
    isMandatory : Boolean,
    questionType: ActivityQuestionType,
    isPreview : Boolean,
    answerKeyList : List<String>,
    questionTotalMark : Int,
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
                                selected = if (!isPreview) option.isAnswer else false,
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
                                selected = if (!isPreview) option.isAnswer else false,
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
                }
                ActivityQuestionType.MATCH -> {
                    MatchingQuestionView(
                        matchQuestionUiModel = matchQuestionUiModel,
                        matchAnswerUiModel = matchAnswerUiModel,
                        isPreview = isPreview,
                    )
                }
                else -> {
                    Text(
                        text = if (isPreview) "Your Answer"
                        else " Show Answer",
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFF9CA3AF),
                    )
                    Divider()
                    if (!isPreview) {
                        Spacer(modifier = Modifier.padding(4.dp))
                        Text(
                            text = "Answer Key ",
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.roboto_regular)),
                            fontWeight = FontWeight(500),
                            color = Color(0xFF374151),
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        answerKeyList.forEach { answerKey ->
                            Text(
                                text = answerKey,
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                                fontWeight = FontWeight(400),
                                color = Color(0xFF6B7280),
                            )
                        }
                    }
                }
            }
            if (!isPreview) {
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
                            ActivityQuestionType.MATCH -> "Matching"
                            else -> "Short Answer"
                        },
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFF9CA3AF),
                    )
                    Text(
                        text = "Mark :",
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
}

@Preview(showBackground = true)
@Composable
fun MatchingQuestionViewPreview() {
    MatchingQuestionView(
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
        matchAnswerUiModel = listOf(
            MatchAnswerUiModel(
                id = "",
                text = "TamilNadu is highest producer of paddy",
            ),
            MatchAnswerUiModel(
                id = "",
                text = "Kerala",
            ),MatchAnswerUiModel(
                id = "",
                text = "Maharashtra",
            ),MatchAnswerUiModel(
                id = "",
                text = "Karnataka",
            ),
        ),
        isPreview = false,
    )
}
@Composable
fun MatchingQuestionView(
    matchQuestionUiModel: List<MatchQuestionUiModel>,
    matchAnswerUiModel: List<MatchAnswerUiModel>,
    isPreview: Boolean,
) {
    val radioButtonColors = RadioButtonDefaults.colors(
        selectedColor = colorResource(id = R.color.digi_blue),
        unselectedColor = colorResource(id = R.color.hint_color)
    )
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.width(150.dp))
            Row(
                modifier = Modifier.horizontalScroll(scrollState),
            ) {
                matchAnswerUiModel.forEach {
                    Text(
                        modifier = Modifier
                            .width(100.dp)
                            .wrapContentHeight()
                            .padding(4.dp),
                        text = it.text,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        fontWeight = FontWeight(400),
                        color = Color(0xDE000000),
                    )
                }
            }
        }
        matchQuestionUiModel.forEachIndexed { _, matchRowUiModel ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(
                        color = Color(0xFFF3F4F6),
                        shape = RoundedCornerShape(size = 4.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier
                        .width(100.dp)
                        .wrapContentHeight()
                        .padding(4.dp),
                    text = matchRowUiModel.text,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF374151),
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(scrollState),
                ) {
                    matchAnswerUiModel.forEachIndexed { index, _ ->
                        Spacer(modifier = Modifier.padding(4.dp))
                        RadioButton(
                            modifier = Modifier
                                .width(96.dp)
                                .padding(vertical = 4.dp),
                            colors = radioButtonColors,
                            selected = if (!isPreview) matchRowUiModel.answerPosition == index
                            else false,
                            onClick = { },
                        )
                    }
                }
            }
        }
    }
}