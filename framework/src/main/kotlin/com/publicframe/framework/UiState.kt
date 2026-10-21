package com.publicframe.framework

/** A small UI state interface shared by screens and view models. */
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    object Empty : UiState<Nothing>()
    data class Content<T>(val value: T) : UiState<T>()
    data class Error(val cause: Throwable) : UiState<Nothing>()
}
