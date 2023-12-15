package com.muthuram.faceliveness.activityUi

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.muthuram.faceliveness.R
import com.muthuram.faceliveness.activity.ActivityNewQuestionsUiModel
import com.muthuram.faceliveness.activity.OptionUiModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateActivityScreen(
    isLoading: Boolean,
    quizTitle: String,
    isBackDialog: Boolean,
    onBack: () -> Unit,
    saveAsDraft: () -> Unit,
    onPublish: () -> Unit,
    onEditClick: () -> Unit,
    onPreviewClicked: () -> Unit,
    activityQuestionList : ArrayList<ActivityNewQuestionsUiModel>,
    questionType: ActivityQuestionType,
    onQuestionTypeClicked: () -> Unit,
    onQuestionEntered: (String) -> Unit,
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
    onCreateNewQuestion: () -> Unit,
    onAddQuestionFromBank: () -> Unit,
    onCopyClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onMoreClicked: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackState = remember { SnackbarHostState() }
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    ModalBottomSheetLayout(
        sheetContent = {

        },
        sheetState = sheetState,
        scrimColor = Color.Black.copy(alpha = 0.5f),
        content = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (isBackDialog) {
                        Dialog(
                            onDismissRequest = { saveAsDraft() },
                        ) {
                            ActivityExitAlertDialogue(
                                onEditClick = onBack,
                                onDismissAlertDialog = { saveAsDraft() },
                                tittle = "Are you sure you want to cancel the activity",
                            )
                        }
                    }
                    AppTopBar(
                        onBack = { saveAsDraft() },
                        onScheduleClicked = {},
                        onDeleteClicked = {},
                    )
                    QuizGroupName(
                        quizTitle = quizTitle,
                        onEditClick = onEditClick,
                        onPreviewClicked = onPreviewClicked,
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                        ) {
                            Spacer(modifier = Modifier.padding(top = 8.dp))
                            LazyColumn {
                                itemsIndexed(activityQuestionList) { index, item ->
                                    ActivityItemCard(
                                        questionEntered = item.questionEntered,
                                        onQuestionEntered = onQuestionEntered,
                                        totalMark = item.totalMark,
                                        sessionName = item.sessionName,
                                        sloSelectedCount = item.sloSelectedCount,
                                        taxonomySelectedCount = item.taxonomySelectedCount,
                                        questionNumber = index + 1,
                                        isMaximumCharacterSelected = item.isMaximumCharacterSelected,
                                        isAnswerKeySelected = item.isAnswerKeySelected,
                                        isMandatory = item.isMandatory,
                                        options = item.options,
                                        characterCount = item.characterCount,
                                        onDragHandleClicked = { /*TODO*/ },
                                        questionType = item.questionType,
                                        onQuestionTypeClicked = { /*TODO*/ },
                                        onImageClicked = { /*TODO*/ },
                                        onTotalMarkEntered = onTotalMarkEntered,
                                        onAnswerKeyAndFeedbackClicked = { /*TODO*/ },
                                        onMandatoryChanged = onMandatoryChanged,
                                        deleteOption = {  _, optPosition ->
                                            deleteOption(index, optPosition)
                                        },
                                        onOptionTextChanged = {_,_ ->},
                                        onSessionDropDownClicked = { /*TODO*/ },
                                        onSLODropDownClicked = { /*TODO*/ },
                                        onTaxonomyDropDownClicked = { /*TODO*/ },
                                        onMaximumCharacterDropDownClicked = { /*TODO*/ },
                                        onCharacterCountEntered = {},
                                        onAddClicked = { /*TODO*/ },
                                        onCopyClicked = { /*TODO*/ },
                                        onDeleteClicked = { /*TODO*/ }) {
                                        
                                    }
                                    /*QuestionsItem(
                                        QuestionPos = index,
                                        questionItem = item,
                                        addNewQuestion = addNewQuestion,
                                        addNewOption = { pos, question ->
                                            addNewOption(pos, question)
                                        },
                                        deleteOption = { _, optPosition ->
                                            deleteOption(index, optPosition)
                                        },
                                        onTextChanged = onTextChanged,
                                        onOptionTextChanged = { questionPos, optionsPos, optionText ->
                                            onOptionTextChanged(questionPos, optionsPos, optionText)
                                        },
                                        onChooseAnswerPopUp = { questionItem ->
                                            scope.launch {
                                                onShowAnswers(questionItem, index)
                                            }
                                        },
                                        onAttachmentItem = { questionItem ->
                                            scope.launch {
                                                onShowAttachmentPopup(questionItem, index)
                                                sheetState.show()
                                            }
                                        },
                                        onImagePreview = onImagePreview,
                                        onRemoveAttachment = { onRemoveAttachment(index) },
                                    )*/
                                }
                                item {
                                    Spacer(modifier = Modifier.padding(top = 8.dp))
                                    Column {
                                        ActivityButtons(
                                            icon = Icons.Default.Add,
                                            text = "ADD NEW QUESTIONS",
                                            onButtonClicked = onCreateNewQuestion,
                                        )
                                        ActivityButtons(
                                            icon = Icons.Default.AttachFile,
                                            text = "ADD FROM QUESTION BANK",
                                            onButtonClicked = onAddQuestionFromBank,
                                        )
                                    }
                                    Spacer(modifier = Modifier.padding(30.dp))
                                }
                            }


                        }
                    }
                    ActivityBottomBar(
                        onPublish = {
                            onPublish()
                        },
                        saveAsDraft = {
                            saveAsDraft()
                        },
                    )
                }

                if (isLoading) {
                    CircularProgressIndicator(color = colorResource(R.color.digi_blue))
                }
            }
        }
    )
}