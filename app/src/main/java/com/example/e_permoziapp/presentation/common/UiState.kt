package com.example.e_permoziapp.presentation.common

import java.util.UUID

sealed class UiState<out T> {
    object Idle : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val message: String, val id: String = UUID.randomUUID().toString()) : UiState<Nothing>()
}