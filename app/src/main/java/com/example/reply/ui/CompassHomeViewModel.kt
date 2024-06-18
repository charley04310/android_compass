/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.reply.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reply.data.PlaceToCompass
import com.example.reply.data.EmailsRepository
import com.example.reply.data.EmailsRepositoryImpl
import com.example.reply.ui.utils.ReplyContentType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ReplyHomeViewModel(private val emailsRepository: EmailsRepository = EmailsRepositoryImpl()) :
    ViewModel() {

    // UI state exposed to the UI
    private val _uiState = MutableStateFlow(ReplyHomeUIState(loading = true))
    val uiState: StateFlow<ReplyHomeUIState> = _uiState

    fun setOpenedEmail(emailId: Long, contentType: ReplyContentType) {
        /**
         * We only set isDetailOnlyOpen to true when it's only single pane layout
         */
        val email = uiState.value.placeToCompasses.find { it.id == emailId }
        _uiState.value = _uiState.value.copy(
            openedPlaceToCompass = email,
            isDetailOnlyOpen = contentType == ReplyContentType.SINGLE_PANE
        )
    }

    fun toggleSelectedEmail(emailId: Long) {
        val currentSelection = uiState.value.selectedEmails
        _uiState.value = _uiState.value.copy(
            selectedEmails = if (currentSelection.contains(emailId))
                currentSelection.minus(emailId) else currentSelection.plus(emailId)
        )
    }

    fun closeDetailScreen() {
        _uiState.value = _uiState
            .value.copy(
                isDetailOnlyOpen = false,
                openedPlaceToCompass = null
            )
    }
}

data class ReplyHomeUIState(
    val placeToCompasses: List<PlaceToCompass> = emptyList(),
    val selectedEmails: Set<Long> = emptySet(),
    val openedPlaceToCompass: PlaceToCompass? = null,
    val isDetailOnlyOpen: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null
)
