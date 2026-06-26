package com.project.compose.core.common.utils.state

sealed class ApiState<out T> {
    data class Success<out T>(val data: T?) : ApiState<T>()
    data class Error<out T>(val throwable: Throwable) : ApiState<T>()
    data object Loading : ApiState<Nothing>()

    fun <R> map(transform: (T) -> R): ApiState<R> {
        return when (this) {
            is Success -> Success(data?.let(transform))
            is Error -> Error(throwable)
            is Loading -> Loading
        }
    }
}
