package com.example.sergey.photogallery.data.pojo

sealed class LoadingDataState

data class LoadingStartState(val showProgress: Boolean = true) : LoadingDataState()

data class LoadingCompleteState<T>(val data: T) : LoadingDataState()

data class ErrorLoadingState(val message: String = "") : LoadingDataState()