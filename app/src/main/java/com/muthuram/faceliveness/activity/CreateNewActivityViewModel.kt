package com.muthuram.faceliveness.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muthuram.faceliveness.activityUi.ActivityQuestionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CreateNewActivityViewModel : ViewModel() {

    private val viewModelState = MutableStateFlow(
        CreateNewActivityViewModelState()
    )
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

}

data class CreateNewActivityViewModelState(
    val quizTitle : String? = null,
    val isLoading : Boolean = false,
    val isBackDialog : Boolean = false,
) {
    fun toUiState() = CreateNewActivityUiModel(
        quizTitle = quizTitle,
        isLoading = isLoading,
        isBackDialog = isBackDialog,
    )
}

data class CreateNewActivityUiModel(
    val quizTitle : String?,
    val isLoading : Boolean,
    val isBackDialog : Boolean,
)

data class ActivityNewQuestionsUiModel(
    val questionEntered: String,
    val onQuestionEntered: (String) -> Unit,
    val totalMark: Int,
    val sessionName: String,
    val sloSelectedCount: Int,
    val taxonomySelectedCount: Int,
    val questionNumber: Int,
    val isMaximumCharacterSelected: Boolean,
    val isAnswerKeySelected: Boolean,
    val isMandatory: Boolean,
    val options: List<OptionUiModel>,
    val characterCount: Int,
    val questionType: ActivityQuestionType,
)

data class OptionUiModel(
    val id: String,
    val optionText: String,
    var isAnswer: Boolean,
    val isAnswered : Boolean,
)