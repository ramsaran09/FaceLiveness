package com.muthuram.faceliveness.activity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.muthuram.faceliveness.activityUi.CreateActivityScreen

@Composable
fun CreateNewActivityRouter(
    viewModel: CreateNewActivityViewModel,
) {

    val uiState by viewModel.uiState.collectAsState()

    CreateActivityScreen(
        isLoading = uiState.isLoading,
        quizTitle = uiState.quizTitle ?: "",
        isBackDialog = uiState.isBackDialog,
        onBack = {},
        saveAsDraft = {},
        onPublish = {},
        onEditClick = {},
        onPreviewClicked = {},
    )
}