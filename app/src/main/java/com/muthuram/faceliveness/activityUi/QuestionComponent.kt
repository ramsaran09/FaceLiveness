package com.muthuram.faceliveness.activityUi

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muthuram.faceliveness.R
import com.muthuram.faceliveness.activity.OptionUiModel

@Preview(showBackground = true)
@Composable
fun ActivityItemCardPreview() {
    ActivityItemCard(
        onDragHandleClicked = {},
        onQuestionTypeClicked = {},
        onImageClicked = {},
        totalMark = 5,
        onTotalMarkEntered = {},
        sessionName = "L 01",
        sloSelectedCount = 20,
        taxonomySelectedCount = 20,
        questionEntered = "",
        onQuestionEntered = {},
        questionNumber = 1,
        isMaximumCharacterSelected = false,
        isAnswerKeySelected = true,
        isMandatory = true,
        onAnswerKeyAndFeedbackClicked = {},
        onMandatoryChanged = {},
        questionType = ActivityQuestionType.MCQ,
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
        deleteOption = { _, _ -> },
        onOptionTextChanged = { _, _ -> },
        onSessionDropDownClicked = {},
        onSLODropDownClicked = {},
        onTaxonomyDropDownClicked = {},
        characterCount = 200,
        onMaximumCharacterDropDownClicked = {},
        onCharacterCountEntered = {},
        onAddClicked = {},
        onCopyClicked = {},
        onDeleteClicked = {},
        onMoreClicked = {},
    )
}

@Composable
fun ActivityItemCard(
    questionEntered: String,
    onQuestionEntered: (String) -> Unit,
    totalMark: Int,
    sessionName: String,
    sloSelectedCount: Int,
    taxonomySelectedCount: Int,
    questionNumber: Int,
    isMaximumCharacterSelected: Boolean,
    isAnswerKeySelected: Boolean,
    isMandatory: Boolean,
    options: List<OptionUiModel>,
    characterCount: Int,
    onDragHandleClicked: () -> Unit,
    questionType: ActivityQuestionType,
    onQuestionTypeClicked: () -> Unit,
    onImageClicked: () -> Unit,
    onTotalMarkEntered: (String) -> Unit,
    onAnswerKeyAndFeedbackClicked: () -> Unit,
    onMandatoryChanged: (Boolean) -> Unit,
    deleteOption: (Int, OptionUiModel) -> Unit,
    onOptionTextChanged: (Int, String) -> Unit,
    onSessionDropDownClicked: () -> Unit,
    onSLODropDownClicked: () -> Unit,
    onTaxonomyDropDownClicked: () -> Unit,
    onMaximumCharacterDropDownClicked: () -> Unit,
    onCharacterCountEntered: (String) -> Unit,
    onAddClicked: () -> Unit,
    onCopyClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onMoreClicked: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, colorResource(id = R.color.digi_blue)),
        backgroundColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            IconButton(onClick = { onDragHandleClicked() }) {
                Icon(
                    imageVector = Icons.Default.DragHandle,
                    contentDescription = "drag handle",
                    tint = colorResource(id = R.color.divider_bg)
                )
            }
            QuestionTypeCard(
                questionType = questionType,
                onQuestionTypeClicked = onQuestionTypeClicked,
            )

            QuestionCard(
                questionEntered = questionEntered,
                onQuestionEntered = onQuestionEntered,
                onImageClicked = onImageClicked,
                questionNumber = questionNumber,
            )
            when (questionType) {
                ActivityQuestionType.SHORT_ANSWER -> {
                    CharacterTypeAndLengthCard(
                        count = characterCount,
                        onMaximumCharacterDropDownClicked = onMaximumCharacterDropDownClicked,
                        onCharacterCountEntered = onCharacterCountEntered,
                        isMaximumCharacterSelected = isMaximumCharacterSelected,
                    )
                }

                else -> {
                    options.forEachIndexed { index, optionUiModel ->
                        ActivityAddOptionsAnswer(
                            optionsPos = index,
                            optionItem = optionUiModel,
                            deleteOption = deleteOption,
                            onOptionTextChanged = onOptionTextChanged,
                        )
                    }
                }
            }
            EnterMarkCard(
                totalMark = totalMark,
                onTotalMarkEntered = onTotalMarkEntered,
            )
            TagComponent(
                onSessionDropDownClicked = onSessionDropDownClicked,
                onSLODropDownClicked = onSLODropDownClicked,
                onTaxonomyDropDownClicked = onTaxonomyDropDownClicked,
                sessionName = sessionName,
                sloSelectedCount = sloSelectedCount,
                taxonomySelectedCount = taxonomySelectedCount,
            )
            AnswerKeyFeedbackCard(
                isAnswerKeySelected = isAnswerKeySelected,
                onAnswerKeyAndFeedbackClicked = onAnswerKeyAndFeedbackClicked,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MandatoryToggle(
                    isMandatory = isMandatory,
                    onMandatoryChanged = onMandatoryChanged,
                )
                Spacer(modifier = Modifier.padding(24.dp))
                AddCopyDeleteMoreComponent(
                    onAddClicked = onAddClicked,
                    onCopyClicked = onCopyClicked,
                    onDeleteClicked = onDeleteClicked,
                    onMoreClicked = onMoreClicked,
                )
            }
        }
    }
}

@Composable
fun QuestionTypeCard(
    questionType: ActivityQuestionType,
    onQuestionTypeClicked: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 0.dp,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Color(0XFFEFF9FB) //EFF9FB
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = "Question Type",
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = colorResource(id = R.color.hint_color) //#4B5563
            )
            Spacer(modifier = Modifier.padding(16.dp))
            Card(
                shape = RoundedCornerShape(8.dp),
                backgroundColor = Color.Transparent,
                border = BorderStroke(
                    1.dp,
                    Color(0xFF4F46E5)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 12.dp,
                            end = 10.dp,
                            top = 4.dp,
                            bottom = 4.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = if (questionType == ActivityQuestionType.MCQ) "Multiple Choice"
                        else "Short Answers",
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = Color(0xFF4F46E5),
                    )
                    IconButton(onClick = { onQuestionTypeClicked() }) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            imageVector = Icons.Default.ArrowForwardIos,
                            contentDescription = "arrow",
                            tint = colorResource(id = R.color.grey_new)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActivityAddOptionsAnswerPreview() {
    ActivityAddOptionsAnswer(
        optionsPos = 0,
        optionItem = OptionUiModel(
            id = "",
            optionText = "CaCo3 * 2H20",
            isAnswer = false,
            isAnswered = true,
        ),
        deleteOption = { _, _ -> },
        onOptionTextChanged = { _, _ -> }
    )
}

@Composable
fun ActivityAddOptionsAnswer(
    optionsPos: Int,
    optionItem: OptionUiModel,
    deleteOption: (Int, OptionUiModel) -> Unit,
    onOptionTextChanged: (Int, String) -> Unit,
) {
    val radioButtonColors = RadioButtonDefaults.colors(
        selectedColor = colorResource(id = R.color.digi_blue),
        unselectedColor = colorResource(id = R.color.hint_color)
    )
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            colors = radioButtonColors,
            selected = optionItem.isAnswer,
            onClick = {},
        )
        TextField(
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color(0xFF374151),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = Color.Transparent,
                cursorColor = colorResource(R.color.digi_blue)
            ),
            modifier = Modifier
                .weight(1f)
                .background(Color.White),
            value = optionItem.optionText,
            onValueChange = {
                onOptionTextChanged(optionsPos, it)
            },
            placeholder = {
                Text(
                    text = "Option - ${optionsPos + 1}",
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = Color(0xFF6B7280)
                    )
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
        )
        IconButton(onClick = {
            deleteOption(optionsPos, optionItem)
        }) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "close"
            )
        }
    }
}

@Composable
fun QuestionCard(
    questionNumber: Int,
    questionEntered: String,
    onQuestionEntered: (String) -> Unit,
    onImageClicked: () -> Unit,
) {
    TextField(
        value = questionEntered,
        onValueChange = onQuestionEntered,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            focusedIndicatorColor = colorResource(id = R.color.grey_new),
            unfocusedIndicatorColor = colorResource(id = R.color.grey_new),
        ),
        textStyle = TextStyle(
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.roboto_regular)),
            fontWeight = FontWeight(400),
            color = Color(0xFF4B5563),
        ),
        trailingIcon = {
            IconButton(onClick = { onImageClicked() }) {
                Icon(
                    imageVector = Icons.Outlined.Image,
                    contentDescription = "image",
                    tint = Color(0xFF838383) //#838383
                )
            }
        },
        placeholder = {
            Text(
                text = "$questionNumber : Type Question",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontWeight = FontWeight(400),
                color = colorResource(id = R.color.buzz_txt_color),
            )
        }
    )
}

@Composable
fun CharacterTypeAndLengthCard(
    count: Int,
    onMaximumCharacterDropDownClicked: () -> Unit,
    onCharacterCountEntered: (String) -> Unit,
    isMaximumCharacterSelected: Boolean,
) {
    Column(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(top = 2.dp),
            text = stringResource(id = R.string.str_character_type_length),
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            color = colorResource(id = R.color.buzz_txt_color)
        )
        Card(
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            border = BorderStroke(
                1.dp,
                Color(0xFFE5E7EB)
            )
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .weight(.7f)
                        .padding(4.dp)
                        .clickable { onMaximumCharacterDropDownClicked() },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = if (isMaximumCharacterSelected) "Maximum Character"
                        else "Minimum Character",
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.grey_new),
                    )
                    IconButton(onClick = { onMaximumCharacterDropDownClicked() }) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "arrow",
                            tint = colorResource(id = R.color.grey_new)
                        )
                    }
                }
                Row(
                    modifier = Modifier.weight(.3f),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Divider(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp)
                    )
                    TextField(
                        value = "$count",
                        onValueChange = onCharacterCountEntered,
                        modifier = Modifier
                            .height(48.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.White,
                            textColor = Color(0xFF374151),
                            focusedIndicatorColor = colorResource(id = R.color.grey_new),
                            unfocusedIndicatorColor = colorResource(id = R.color.grey_new),
                        ),
                        placeholder = {
                            Text(
                                text = "Enter Length",
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                                color = Color(0xFF9CA3AF)
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun EnterMarkCard(
    totalMark: Int,
    onTotalMarkEntered: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(top = 2.dp),
            text = "Mark",
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            color = colorResource(id = R.color.buzz_txt_color)
        )
        Card(
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            border = BorderStroke(
                1.dp,
                Color(0xFFE5E7EB)
            ) //#E5E7EB
        ) {
            TextField(
                value = totalMark.toString(),
                onValueChange = onTotalMarkEntered,
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    textColor = Color(0xFF374151),
                    focusedIndicatorColor = colorResource(id = R.color.grey_new),
                    unfocusedIndicatorColor = colorResource(id = R.color.grey_new),
                ),
                placeholder = {
                    Text(
                        text = "Enter Mark",
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = Color(0xFF9CA3AF)
                    )
                }
            )
        }
    }
}

@Composable
fun TagItemCard(
    modifier: Modifier,
    tagCardText: String,
    onTagDropDownClicked: () -> Unit,
    selectedCount: Int,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(
            1.dp,
            Color(0xFFE5E7EB)
        )
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp)
                .clickable { onTagDropDownClicked() },
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = tagCardText,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = colorResource(id = R.color.buzz_txt_color)
            )
            if (selectedCount != 0) {
                Spacer(modifier = Modifier.padding(start = 8.dp))
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            color = Color(0xFF374151),
                            shape = RoundedCornerShape(size = 12.dp)
                        )
                        .padding(2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$selectedCount",
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        fontWeight = FontWeight(400),
                        color = Color.White,
                    )
                }
            }

            IconButton(onClick = { onTagDropDownClicked() }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "arrow",
                    tint = colorResource(id = R.color.grey_new)
                )
            }
        }

    }
}

@Composable
fun TagComponent(
    sessionName: String,
    sloSelectedCount: Int,
    taxonomySelectedCount: Int,
    onSessionDropDownClicked: () -> Unit,
    onSLODropDownClicked: () -> Unit,
    onTaxonomyDropDownClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "TAG's",
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            color = colorResource(id = R.color.buzz_txt_color)
        )
        Row(
            modifier = Modifier
                .padding(top = 2.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TagItemCard(
                modifier = Modifier
                    .height(40.dp)
                    .weight(1f),
                tagCardText = sessionName.ifEmpty { "Session" },
                onTagDropDownClicked = onSessionDropDownClicked,
                selectedCount = 0,
            )
            Spacer(modifier = Modifier.padding(start = 8.dp))
            TagItemCard(
                modifier = Modifier
                    .height(40.dp)
                    .weight(1f),
                tagCardText = "SLO",
                onTagDropDownClicked = onSLODropDownClicked,
                selectedCount = sloSelectedCount,
            )
            Spacer(modifier = Modifier.padding(start = 8.dp))
            TagItemCard(
                modifier = Modifier
                    .height(40.dp)
                    .weight(1f),
                tagCardText = "Taxonomy",
                onTagDropDownClicked = onTaxonomyDropDownClicked,
                selectedCount = taxonomySelectedCount,
            )
        }
    }
}

@Composable
fun AnswerKeyFeedbackCard(
    isAnswerKeySelected: Boolean,
    onAnswerKeyAndFeedbackClicked: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(
            1.dp,
            Color(0xFFE5E7EB)
        ) //#E5E7EB
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = if (isAnswerKeySelected) Icons.Filled.CheckCircle
                else Icons.Default.Error,
                contentDescription = "answer key",
                tint = if (isAnswerKeySelected) Color(0xFF16A34A)
                else Color(0xFFDC2626)
            )
            Row(
                modifier = Modifier
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = "AnswerKey & Feedback",
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.grey_new)
                )
                IconButton(onClick = { onAnswerKeyAndFeedbackClicked() }) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = Icons.Default.ArrowForwardIos,
                        contentDescription = "arrow",
                        tint = colorResource(id = R.color.grey_new)
                    )
                }
            }
        }
    }
}

@Composable
fun MandatoryToggle(
    isMandatory: Boolean,
    onMandatoryChanged: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = "Mandatory",
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            color = colorResource(id = R.color.hint_color)
        )
        Switch(
            checked = isMandatory,
            onCheckedChange = onMandatoryChanged,
            modifier = Modifier.scale(1f),
            colors = SwitchDefaults.colors(
                checkedThumbColor = colorResource(id = R.color.digi_blue),
                uncheckedThumbColor = Color.White,
                checkedTrackColor = Color(0xFFC2EBFA),
                uncheckedTrackColor = colorResource(id = R.color.buzz_txt_color),
            )
        )
    }
}

@Composable
fun AddCopyDeleteMoreComponent(
    onAddClicked: () -> Unit,
    onCopyClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onMoreClicked: () -> Unit,
) {
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ToolsItem(
            icon = Icons.Default.Add,
            onIconClicked = onAddClicked
        )
        ToolsItem(
            icon = Icons.Default.ContentCopy,
            onIconClicked = onCopyClicked
        )
        ToolsItem(
            icon = Icons.Default.Delete,
            onIconClicked = onDeleteClicked
        )
        ToolsItem(
            icon = Icons.Default.MoreVert,
            onIconClicked = onMoreClicked
        )
    }
}

@Composable
fun ToolsItem(
    icon: ImageVector,
    onIconClicked: () -> Unit,
) {
    IconButton(onClick = { onIconClicked() }) {
        Icon(
            modifier = Modifier
                .size(16.dp),
            imageVector = icon,
            contentDescription = "icon",
            tint = Color(0XFF6B7280)
        )
    }
}

@Composable
fun ActivityButtons(
    icon: ImageVector,
    text: String,
    onButtonClicked: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(4.dp),
        backgroundColor = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = rememberRipple(bounded = true),
                ) { onButtonClicked() }
                .padding(vertical = 10.dp, horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "add",
                tint = colorResource(id = R.color.digi_blue)
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = text,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                color = colorResource(id = R.color.digi_blue)
            )
        }
    }
}

@Preview
@Composable
fun ActivityButtonsPreview() {
    Column {
        ActivityButtons(
            icon = Icons.Default.Add,
            text = "ADD NEW QUESTIONS",
            onButtonClicked = {}
        )
        ActivityButtons(
            icon = Icons.Default.AttachFile,
            text = "ADD FROM QUESTION BANK",
            onButtonClicked = {}
        )
    }
}