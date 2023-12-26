package com.muthuram.faceliveness.activityUi

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muthuram.faceliveness.R
import com.muthuram.faceliveness.activity.MatchRowUiModel
import com.muthuram.faceliveness.activity.OptionUiModel
import com.muthuram.faceliveness.activity.TrueOrFalseUiModel
import com.muthuram.faceliveness.models.ActivityDonutChartData
import com.muthuram.faceliveness.models.ActivityDonutChartDataCollection
import com.muthuram.faceliveness.models.DrawingAngles

@Preview(showBackground = true)
@Composable
fun ActivityResultSummaryScreenPreview() {
    ActivityResultSummaryScreen(
        tabTitles = arrayListOf("Summary", "Individual"),
        onTabSelected = {},
        selectedTab = 1,
        onBackIconPressed = {},
        activityName = "Medicine",
        noOfQuestions = 2,
        courseName = "L1,L2,P1",
        isSystemValidation = false,
        studentName = "Mohamed Abubakar ",
        userId = "12345678910",
        attendedQuestion = 5,
        totalQuestion = 10,
        overallScoredMark = 25,
        totalMark = 50,
        onShowStudentList = {},
        activityResultSummaryList = arrayListOf(
            ActivityResultSummaryUiModel(
                questionText = "What is newton's 3rd law?",
                isMandatory = true,
                questionType = ActivityQuestionType.MCQ,
                noOfResponses = 20,
                correctResponse = 12,
                inCorrectResponse = 6,
                sessions = "L1.1",
                slo = "1.1,1.2",
                taxonomy = "Recall",
                notAttended = 2,
                questionTotalMark = 5,
                id = "",
                answeredOptionUiModel = listOf(
                    AnsweredOptionUiModel(
                        id = "",
                        text = "Yes",
                        answeredCount = 8,
                        totalAnsweredCount = 12,
                    ),
                    AnsweredOptionUiModel(
                        id = "",
                        text = "No",
                        answeredCount = 4,
                        totalAnsweredCount = 12,
                    ),
                    AnsweredOptionUiModel(
                        id = "",
                        text = "None of the above",
                        answeredCount = 3,
                        totalAnsweredCount = 12,
                    ),
                ),
                graphData = ActivityDonutChartDataCollection(
                    items = listOf(
                        ActivityDonutChartData(12.0f, questionAttendanceStatus = "correct"),
                        ActivityDonutChartData(6.0f, questionAttendanceStatus = "inCorrect"),
                        ActivityDonutChartData(2.0f, questionAttendanceStatus = "notAttended")
                    )
                ),
            ),
            ActivityResultSummaryUiModel(
                questionText = "What is newton's 1rd law?",
                isMandatory = false,
                questionType = ActivityQuestionType.SHORT_ANSWER,
                noOfResponses = 20,
                correctResponse = 18,
                inCorrectResponse = 2,
                sessions = "L1.1",
                slo = "1.1,1.2",
                taxonomy = "Recall",
                notAttended = 0,
                questionTotalMark = 5,
                id = "",
                answeredOptionUiModel = listOf(),
                graphData = ActivityDonutChartDataCollection(
                    items = listOf(
                        ActivityDonutChartData(18.0f, questionAttendanceStatus = "correct"),
                        ActivityDonutChartData(2.0f, questionAttendanceStatus = "inCorrect"),
                        ActivityDonutChartData(0.0f, questionAttendanceStatus = "notAttended")
                    )
                ),
            )
        ),
        individualActivityResult = arrayListOf(
            IndividualActivityResultUiModel(
                id = "",
                questionText = "Describe negative feedback mechanism with an example",
                answerText = "",
                isCorrectlyAnswered = true,
                scoredMark = 5,
                questionTotalMark = 5,
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
                questionType = ActivityQuestionType.MCQ,
                trueOrFalseUiModel = listOf(),
                rowUiModel = listOf(),
            ),
            IndividualActivityResultUiModel(
                id = "",
                questionText = "Describe negative feedback mechanism with an example",
                answerText = "",
                isCorrectlyAnswered = false,
                scoredMark = 0,
                questionTotalMark = 5,
                optionsUiModel = listOf(
                    OptionUiModel(
                        id = "",
                        optionText = "yes",
                        isAnswer = true,
                        isAnswered = false,
                    ),
                    OptionUiModel(
                        id = "",
                        optionText = "No",
                        isAnswer = false,
                        isAnswered = true,
                    ),
                    OptionUiModel(
                        id = "",
                        optionText = "None of the above",
                        isAnswer = false,
                        isAnswered = false,
                    ),
                ),
                questionType = ActivityQuestionType.MCQ,
                trueOrFalseUiModel = listOf(),
                rowUiModel = listOf(),
            ),
            IndividualActivityResultUiModel(
                id = "",
                questionText = "Describe negative feedback mechanism with an example",
                answerText = "Active transport mechanism moves molecules and ions from lower concentration to higher concentration with the help of energy in the form of ATP.",
                isCorrectlyAnswered = true,
                scoredMark = 5,
                questionTotalMark = 5,
                optionsUiModel = listOf(),
                trueOrFalseUiModel = listOf(),
                questionType = ActivityQuestionType.SHORT_ANSWER,
                rowUiModel = listOf(),
            ),
            IndividualActivityResultUiModel(
                id = "",
                questionText = "Describe negative feedback mechanism with an example",
                answerText = "Active transport mechanism moves molecules and ions from lower concentration to higher concentration with the help of energy in the form of ATP.",
                isCorrectlyAnswered = false,
                scoredMark = 2,
                questionTotalMark = 5,
                optionsUiModel = listOf(),
                trueOrFalseUiModel = listOf(),
                questionType = ActivityQuestionType.SHORT_ANSWER,
                rowUiModel = listOf(),
            ),
        )
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ActivityResultSummaryScreen(
    tabTitles: ArrayList<String>,
    onTabSelected: (Int) -> Unit,
    selectedTab: Int,
    onBackIconPressed: () -> Unit,
    activityName: String,
    noOfQuestions: Int,
    courseName: String,
    isSystemValidation: Boolean,
    individualActivityResult: ArrayList<IndividualActivityResultUiModel>,
    activityResultSummaryList: ArrayList<ActivityResultSummaryUiModel>,
    studentName: String,
    userId: String,
    attendedQuestion: Int,
    totalQuestion: Int,
    overallScoredMark: Int,
    totalMark: Int,
    onShowStudentList: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    ModalBottomSheetLayout(
        sheetContent = {

        },
        sheetState = sheetState,
        scrimColor = Color.Black.copy(alpha = 0.5f),
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xFFF3F4F6))
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    ActivityViewResultTopBar(
                        tabTitles = tabTitles,
                        onTabSelected = onTabSelected,
                        selectedTab = selectedTab,
                        onBackIconPressed = onBackIconPressed,
                        activityName = activityName,
                        noOfQuestions = noOfQuestions,
                        courseName = courseName,
                    )
                    if (selectedTab == 1) {
                        IndividualQuizViewResult(
                            studentName = studentName,
                            userId = userId,
                            attendedQuestion = attendedQuestion,
                            totalQuestion = totalQuestion,
                            overallScoredMark = overallScoredMark,
                            totalMark = totalMark,
                            onShowStudentList = onShowStudentList,
                        )
                    }
                    if (selectedTab == 0) {
                        LazyColumn {
                            items(activityResultSummaryList) { item ->
                                ActivityViewResultQuestionCard(
                                    questionText = item.questionText,
                                    noOfResponses = item.noOfResponses,
                                    correctResponse = item.correctResponse,
                                    inCorrectResponse = item.inCorrectResponse,
                                    notAttended = item.notAttended,
                                    sessions = item.sessions,
                                    slo = item.slo,
                                    taxonomy = item.taxonomy,
                                    isMandatory = item.isMandatory,
                                    questionType = item.questionType,
                                    isSystemValidation = isSystemValidation,
                                    questionTotalMark = item.questionTotalMark,
                                    answeredOptionUiModel = item.answeredOptionUiModel,
                                    graphData = item.graphData,
                                )
                            }
                        }
                    } else {
                        LazyColumn {
                            items(individualActivityResult) { item ->
                                IndividualQuestionCard(
                                    questionText = item.questionText,
                                    answerText = item.answerText,
                                    isCorrectlyAnswered = item.isCorrectlyAnswered,
                                    scoredMark = item.scoredMark,
                                    totalQuestionMark = item.questionTotalMark,
                                    optionsUiModel = item.optionsUiModel,
                                    trueOrFalseUiModel = item.trueOrFalseUiModel,
                                    questionType = item.questionType,
                                    rowUiModel = item.rowUiModel,
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CourseDetailsTopBarPreview() {
    ActivityViewResultTopBar(
        tabTitles = arrayListOf("Summary", "Individual"),
        onTabSelected = {},
        selectedTab = 0,
        onBackIconPressed = {},
        activityName = "Medicine",
        noOfQuestions = 2,
        courseName = "L1,L2,P1"
    )
}


@Composable
fun ActivityViewResultTopBar(
    tabTitles: ArrayList<String>,
    onTabSelected: (Int) -> Unit,
    selectedTab: Int,
    onBackIconPressed: () -> Unit,
    activityName: String,
    noOfQuestions: Int,
    courseName: String,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(
                max = if (tabTitles.isEmpty()) 100.dp
                    else 150.dp
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_bg_lecture),
            contentDescription = "lecture image",
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            colorResource(id = R.color.topbar_gradientcolor1),
                            colorResource(id = R.color.topbar_gradientcolor2)
                        )
                    )
                ),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()

        ) {
            TopAppBar(
                backgroundColor = Color.Transparent,
                elevation = 0.dp,
                title = {
                    Text(
                        modifier = Modifier
                            .offset(x = (-20).dp),
                        text = activityName,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onBackIconPressed()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back Arrow",
                            tint = Color.White
                        )
                    }
                },
            )
            Row(
                modifier = Modifier.padding(start = 25.dp)

            ) {
                Text(
                    text = "$noOfQuestions Questions",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontWeight = FontWeight(500),
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = courseName,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontWeight = FontWeight(500),
                )
            }

            Tab(
                items = tabTitles,
                onItemSelected = onTabSelected,
                selectedItem = selectedTab
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ActivityViewResultQuestionCardPreview() {
    ActivityViewResultQuestionCard(
        questionText = "What is newton's 3rd law?",
        isMandatory = true,
        questionType = ActivityQuestionType.MCQ,
        noOfResponses = 20,
        correctResponse = 12,
        inCorrectResponse = 6,
        sessions = "L1.1",
        slo = "1.1,1.2",
        taxonomy = "Recall",
        isSystemValidation = false,
        notAttended = 2,
        questionTotalMark = 5,
        answeredOptionUiModel = listOf(
            AnsweredOptionUiModel(
                id = "",
                text = "Yes",
                answeredCount = 8,
                totalAnsweredCount = 12,
            ),
            AnsweredOptionUiModel(
                id = "",
                text = "No",
                answeredCount = 4,
                totalAnsweredCount = 12,
            ),
            AnsweredOptionUiModel(
                id = "",
                text = "None of the above",
                answeredCount = 3,
                totalAnsweredCount = 12,
            ),
        ),
        graphData = ActivityDonutChartDataCollection(
            items = listOf(
                ActivityDonutChartData(25.0f, questionAttendanceStatus = "correct"),
                ActivityDonutChartData(5.0f, questionAttendanceStatus = "inCorrect"),
                ActivityDonutChartData(15.0f, questionAttendanceStatus = "notAttended")
            )
        )
    )
}

@Composable
fun ActivityViewResultQuestionCard(
    questionText: String,
    noOfResponses: Int,
    correctResponse: Int,
    inCorrectResponse: Int,
    notAttended: Int,
    sessions: String,
    slo: String,
    taxonomy: String,
    isMandatory: Boolean,
    questionType: ActivityQuestionType,
    isSystemValidation: Boolean,
    questionTotalMark: Int,
    answeredOptionUiModel: List<AnsweredOptionUiModel>,
    graphData: ActivityDonutChartDataCollection,
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
            Text(
                text = "$noOfResponses responses received • $sessions • SLO $slo • $taxonomy",
                fontSize = 11.sp,
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontWeight = FontWeight(400),
                color = Color(0xFF666666),
            )
            Spacer(modifier = Modifier.padding(8.dp))
            if (questionType != ActivityQuestionType.SHORT_ANSWER) {
                MultipleChoiceAnswerPercentageView(
                    answeredOptionUiModel = answeredOptionUiModel,
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Divider()
                Spacer(modifier = Modifier.padding(8.dp))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$correctResponse • Correct Responded",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF16A34A),
                )
                Text(
                    text = "$inCorrectResponse • Wrong Responded",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFFDC2626),
                )
            }
            Spacer(modifier = Modifier.padding(4.dp))
            if (!isSystemValidation) {
                Text(
                    text = "$notAttended • Not Attended",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF4B5563),
                )
            }
            ActivityDonutChartScreen(
                graphData = graphData
            )
            Divider()
            Spacer(modifier = Modifier.padding(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = questionType.value,
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF9CA3AF),
                )
                Text(
                    text = "Mark : $questionTotalMark",
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF9CA3AF),
                )
            }
        }
    }
}


data class AnsweredOptionUiModel(
    val id: String,
    val text: String,
    val answeredCount: Int,
    val totalAnsweredCount: Int,
)

data class ActivityResultSummaryUiModel(
    val id: String,
    val questionText: String,
    val noOfResponses: Int,
    val correctResponse: Int,
    val inCorrectResponse: Int,
    val notAttended: Int,
    val sessions: String,
    val slo: String,
    val taxonomy: String,
    val isMandatory: Boolean,
    val questionType: ActivityQuestionType,
    val questionTotalMark: Int,
    val answeredOptionUiModel: List<AnsweredOptionUiModel>,
    val graphData: ActivityDonutChartDataCollection,
)

data class IndividualActivityResultUiModel(
    val id: String,
    val questionText: String,
    val questionType: ActivityQuestionType,
    val questionTotalMark: Int,
    val answerText: String,
    val isCorrectlyAnswered: Boolean,
    val scoredMark: Int,
    val optionsUiModel: List<OptionUiModel>,
    val trueOrFalseUiModel: List<TrueOrFalseUiModel>,
    val rowUiModel: List<MatchRowUiModel>,
)

@Preview(showBackground = true)
@Composable
fun MultipleChoiceAnswerPercentageViewPreview() {
    MultipleChoiceAnswerPercentageView(
        answeredOptionUiModel = listOf(
            AnsweredOptionUiModel(
                id = "",
                text = "Yes",
                answeredCount = 8,
                totalAnsweredCount = 12,
            ),
            AnsweredOptionUiModel(
                id = "",
                text = "No",
                answeredCount = 4,
                totalAnsweredCount = 12,
            ),
            AnsweredOptionUiModel(
                id = "",
                text = "None of the above",
                answeredCount = 3,
                totalAnsweredCount = 12,
            ),
        )
    )
}

@Composable
fun MultipleChoiceAnswerPercentageView(
    answeredOptionUiModel: List<AnsweredOptionUiModel>
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        answeredOptionUiModel.forEach { option ->
            val percentage =
                ((option.answeredCount.toFloat() / option.totalAnsweredCount.toFloat()) * 100)
            OptionAnsweredPercentage(
                answeredPercentage = percentage.toInt(),
                progressPercentage = (percentage / 100),
                optionText = option.text
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OptionAnsweredPercentagePreview() {
    OptionAnsweredPercentage(
        answeredPercentage = 65,
        progressPercentage = 0.65f,
        optionText = "Yes"
    )
}

@Composable
fun OptionAnsweredPercentage(
    answeredPercentage: Int,
    progressPercentage: Float,
    optionText: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(vertical = 4.dp),
            text = "$answeredPercentage %",
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.roboto_regular)),
            fontWeight = FontWeight(500),
            color = Color(0xFF9CA3AF),
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.padding(4.dp),
                text = optionText,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontWeight = FontWeight(400),
                color = Color(0xFF374151),
            )
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .height(8.dp),
                progress = progressPercentage.coerceIn(0.0f, 1.0f),
                backgroundColor = colorResource(id = R.color.divider_grey),
                color = colorResource(id = R.color.digi_blue),
                strokeCap = StrokeCap.Round
            )
            Spacer(modifier = Modifier.padding(4.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActivityDonutChartScreenPreview() {
    ActivityDonutChartScreen(
        graphData = ActivityDonutChartDataCollection(
            items = listOf(
                ActivityDonutChartData(25.0f, questionAttendanceStatus = "correct"),
                ActivityDonutChartData(5.0f, questionAttendanceStatus = "inCorrect"),
                ActivityDonutChartData(15.0f, questionAttendanceStatus = "notAttended")
            )
        )
    )
}

@Composable
fun ActivityDonutChartScreen(
    graphData: ActivityDonutChartDataCollection
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        if (graphData.items.isNotEmpty()) {
            ActivityDonutChart(
                data = graphData
            )
        }
    }
}

@Composable
fun ActivityDonutChart(
    chartSize: Dp = 200.dp,
    data: ActivityDonutChartDataCollection,
) {

    val anglesList: MutableList<DrawingAngles> = remember { mutableListOf() }

    val correctAnswerColor = Color(0xFF16A34A)
    val wrongAnswerColor = Color(0xFFDC2626)
    val notAttendedColor = Color(0xFF4B5563)

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(
            modifier = Modifier
                .size(chartSize)
                .aspectRatio(1f),
            onDraw = {
                val defaultStrokeWidth = 80.dp.toPx()
                anglesList.clear()
                var lastAngle = 0f

                if (data.items.isEmpty()) {
                    drawArc(
                        color = notAttendedColor,
                        startAngle = lastAngle,
                        sweepAngle = 360F,
                        useCenter = false,
                        topLeft = Offset(defaultStrokeWidth / 2, defaultStrokeWidth / 2),
                        style = Stroke(60F, cap = StrokeCap.Butt),
                        size = Size(
                            size.width - defaultStrokeWidth,
                            size.height - defaultStrokeWidth
                        )
                    )
                }
                data.items.forEachIndexed { ind, item ->
                    val sweepAngle = data.findSweepAngle(ind)
                    anglesList.add(DrawingAngles(lastAngle, sweepAngle))

                    drawArc(
                        color = when (item.questionAttendanceStatus) {
                            "correct" -> correctAnswerColor
                            "inCorrect" -> wrongAnswerColor
                            "notAttended" -> notAttendedColor
                            else -> Color.White
                        },
                        startAngle = lastAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        topLeft = Offset(defaultStrokeWidth / 2, defaultStrokeWidth / 2),
                        style = Stroke(80F, cap = StrokeCap.Butt),
                        size = Size(
                            size.width - defaultStrokeWidth,
                            size.height - defaultStrokeWidth
                        )
                    )
                    lastAngle += sweepAngle
                }
            }
        )
    }
}

private fun ActivityDonutChartDataCollection.findSweepAngle(index: Int): Float {
    val amount = items[index].count
    val total = items.sumOf { it.count.toDouble() }.toFloat()
    return if (total == 0f) 0f else (amount / total) * 360.0f
}