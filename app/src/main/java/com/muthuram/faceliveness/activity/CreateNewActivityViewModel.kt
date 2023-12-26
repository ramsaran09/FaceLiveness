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
    val trueOrFalseUiModel: List<TrueOrFalseUiModel>,
    val rowUiModel: List<MatchRowUiModel>,
    val columnUiModel: List<MatchColumnUiModel>,
    val characterCount: Int,
    val questionType: ActivityQuestionType,
)

data class OptionUiModel(
    val id: String,
    val optionText: String,
    var isAnswer: Boolean,
    val isAnswered : Boolean,
)

data class SessionUiModel(
    val id: String,
    val isSelected : Boolean,
    val sessionName : String,
    val courseName : String,
    val courseCode : String,
)

data class OutcomeUiModel(
    val id : String,
    val cloName : String,
    val isSelected : Boolean,
    val description : String,
)

data class TaxonomyUiModel(
    val id : String,
    val name : String,
    val isSelected : Boolean,
)

data class TrueOrFalseUiModel(
    val id: String,
    val text : String,
    var isAnswer : Boolean,
    val isAnswered : Boolean,
)

data class MatchRowUiModel(
    val id : String,
    val text : String,
    val answerPosition : Int? = null,
)

data class MatchColumnUiModel(
    val id : String,
    val text : String,
    val questionPosition : Int?,
)