package com.muthuram.faceliveness.activityUi

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import com.muthuram.faceliveness.activity.MatchRowUiModel
import com.muthuram.faceliveness.activity.OptionUiModel
import com.muthuram.faceliveness.activity.TrueOrFalseUiModel

@Preview(showBackground = true)
@Composable
fun IndividualQuizViewResultPreview() {
    IndividualQuizViewResult(
        studentName = "Mohamed Abubakar ",
        userId = "12345678910",
        attendedQuestion = 5,
        totalQuestion = 10,
        overallScoredMark = 25,
        totalMark = 50,
        onShowStudentList = {},
    )
}

@Composable
fun IndividualQuizViewResult(
    studentName: String,
    userId: String,
    attendedQuestion: Int,
    totalQuestion: Int,
    overallScoredMark: Int,
    totalMark: Int,
    onShowStudentList: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = "Select Student",
            fontSize = 12.sp,
            fontFamily = FontFamily(Font(R.font.roboto_regular)),
            fontWeight = FontWeight(400),
            color = Color(0xDE666666),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color(0x1F000000),
                    shape = RoundedCornerShape(size = 8.dp)
                )
                .clickable { onShowStudentList() }
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "$studentName, $userId",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontWeight = FontWeight(400),
                color = Color(0x8A000000),
            )
            IconButton(onClick = { onShowStudentList() }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "arrow",
                    tint = colorResource(id = R.color.grey_new)
                )
            }
        }
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = "Quiz Result",
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.roboto_regular)),
            fontWeight = FontWeight(500),
            color = Color(0xFF374151),
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .border(
                    width = 1.dp,
                    color = Color(0xFF147AFC),
                    shape = RoundedCornerShape(size = 8.dp)
                )
                .background(color = Color(0xFF374151), shape = RoundedCornerShape(size = 8.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Column(
                modifier = Modifier.weight(0.5f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "$attendedQuestion/$totalQuestion",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFFFFFFFF),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = "NO. Of Question",
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFFFFFFFF),
                    textAlign = TextAlign.Center,
                )
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Column(
                modifier = Modifier.weight(0.5f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "$overallScoredMark/$totalMark",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFFFFFFFF),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = "Total mark",
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFFFFFFFF),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IndividualQuestionCardPreview() {
    IndividualQuestionCard(
        questionText = "Describe negative feedback mechanism with an example",
        answerText = "Active transport mechanism moves molecules and ions from lower concentration to higher concentration with the help of energy in the form of ATP.",
        isCorrectlyAnswered = true,
        scoredMark = 5,
        totalQuestionMark = 5,
        optionsUiModel = listOf(),
        trueOrFalseUiModel = listOf(),
        questionType = ActivityQuestionType.SHORT_ANSWER,
        rowUiModel = listOf(),
    )
}

@Preview(showBackground = true)
@Composable
fun IndividualMCQQuestionCardPreview() {
    IndividualQuestionCard(
        questionText = "Describe negative feedback mechanism with an example",
        answerText = "",
        isCorrectlyAnswered = true,
        scoredMark = 5,
        totalQuestionMark = 5,
        optionsUiModel = listOf(
            OptionUiModel(
                id = "",
                optionText = "yes",
                isAnswer = true,
                isAnswered = true,
            ),
            OptionUiModel(
                id = "",
                optionText = "No",
                isAnswer = false,
                isAnswered = false,
            ),
            OptionUiModel(
                id = "",
                optionText = "None of the above",
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
        questionType = ActivityQuestionType.MATCH,
        rowUiModel = listOf(
            MatchRowUiModel(
                id = "",
                text = "Chennai is capital of which state in India",
                answerPosition = 3,
            ),
            MatchRowUiModel(
                id = "",
                text = "Kochi",
                answerPosition = 2,
            ),
            MatchRowUiModel(
                id = "",
                text = "Mumbai",
                answerPosition = 1,
            ),
            MatchRowUiModel(
                id = "",
                text = "Bangalore",
                answerPosition = 0,
            ),
        ),
    )
}

@Composable
fun IndividualQuestionCard(
    questionText: String,
    answerText: String,
    isCorrectlyAnswered: Boolean,
    scoredMark: Int,
    totalQuestionMark: Int,
    optionsUiModel: List<OptionUiModel>,
    trueOrFalseUiModel: List<TrueOrFalseUiModel>,
    rowUiModel: List<MatchRowUiModel>,
    questionType: ActivityQuestionType,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(color = Color.White, shape = RoundedCornerShape(size = 8.dp)),
        backgroundColor = Color.White,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = questionText,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontWeight = FontWeight(400),
                color = Color(0xFF393939),
            )
            Spacer(modifier = Modifier.padding(4.dp))
            when (questionType) {
                ActivityQuestionType.MCQ -> {
                    optionsUiModel.forEach { option ->
                        MCQOptionResultView(
                            isAnswered = option.isAnswered,
                            isCorrectAnswer = option.isAnswer,
                            optionText = option.optionText
                        )
                    }
                }
                ActivityQuestionType.TRUE_OR_FALSE -> {
                    trueOrFalseUiModel.forEach { option ->
                        MCQOptionResultView(
                            isAnswered = option.isAnswered,
                            isCorrectAnswer = option.isAnswer,
                            optionText = option.text
                        )
                    }
                }
                ActivityQuestionType.MATCH -> {
                    rowUiModel.forEachIndexed { index, matchRowUiModel ->
                        MatchQuestionResultView(
                            index = index,
                            text = matchRowUiModel.text,
                            isCorrectingAnswer = false,
                            answerPosition = matchRowUiModel.answerPosition,
                        )
                    }
                }
                else -> {
                    Row(
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = if (isCorrectlyAnswered) Color(0xFF16A34A)
                                else Color(0xFFDC2626),
                                shape = RoundedCornerShape(size = 4.dp)
                            )
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(
                            16.dp,
                            Alignment.CenterHorizontally
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = answerText,
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.roboto_regular)),
                            fontWeight = FontWeight(400),
                            color = if (isCorrectlyAnswered) Color(0xFF16A34A)
                            else Color(0xFFDC2626),
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Divider()
            Spacer(modifier = Modifier.padding(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = if (isCorrectlyAnswered) Icons.Filled.CheckCircle
                    else Icons.Default.Error,
                    contentDescription = "answer",
                    tint = if (isCorrectlyAnswered) Color(0xFF16A34A)
                    else Color(0xFFDC2626)
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = if (isCorrectlyAnswered) "Correct" else "Wrong",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontWeight = FontWeight(500),
                    color = if (isCorrectlyAnswered) Color(0xFF16A34A)
                    else Color(0xFFDC2626),
                )
                Spacer(modifier = Modifier.weight(1f))
                Divider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Mark : ",
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF6B7280),
                )
                Text(
                    text = if (isCorrectlyAnswered) "$scoredMark"
                    else "$scoredMark / $totalQuestionMark",
                    fontSize = 16.sp,
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
fun MCQOptionResultViewPreview() {
    MCQOptionResultView(
        isAnswered = true,
        isCorrectAnswer = true,
        optionText = "Option - 1"
    )
}

@Composable
fun MCQOptionResultView(
    isAnswered: Boolean,
    isCorrectAnswer: Boolean,
    optionText: String,
) {
    val radioButtonColors = RadioButtonDefaults.colors(
        selectedColor = if (isCorrectAnswer) Color(0xFF16A34A)
        else Color(0xFFEF4444),
        unselectedColor = colorResource(id = R.color.hint_color)
    )
    Row(
        modifier = if (isAnswered) {
            Modifier
                .padding(top = 4.dp)
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = if (isCorrectAnswer) Color(0xFF16A34A)
                    else Color(0xFFEF4444),
                    shape = RoundedCornerShape(size = 4.dp)
                )
                .background(
                    color = if (isCorrectAnswer) Color(0xFF16A34A).copy(0.1f)
                    else Color(0xFFFEF2F2),
                    shape = RoundedCornerShape(size = 4.dp)
                )
        } else Modifier
            .padding(top = 4.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        RadioButton(
            selected = isAnswered,
            onClick = {},
            colors = radioButtonColors,
        )
        Text(
            text = optionText,
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.roboto_regular)),
            fontWeight = FontWeight(400),
            color = Color(0xFF374151),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MatchQuestionResultViewPreview() {
    MatchQuestionResultView(
        index = 0,
        text = "chennai",
        answerPosition = 1,
        isCorrectingAnswer = true,
    )
}
@Composable
fun MatchQuestionResultView(
    index : Int,
    text : String,
    answerPosition : Int?,
    isCorrectingAnswer : Boolean,
) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Text(
            text = (index + 97).toChar().uppercase(),
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.roboto_regular)),
            fontWeight = FontWeight(400),
            color = Color(0xFF374151),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier.weight(1f),
            text = text,
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.roboto_regular)),
            fontWeight = FontWeight(400),
            color = Color(0xFF374151),
        )
        MatchAnswerView(
            color = if (!isCorrectingAnswer) if (index % 2 == 0) 0xFFDCFCE7 else 0xFFFEE2E2
            else 0xFFF3F4F6,
            answerPosition = answerPosition,
            onChooseMatchAnswer = {},
            isDropDownSelection = false,
        )
    }
}