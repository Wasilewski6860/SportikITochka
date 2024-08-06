package com.example.domain.coroutines

sealed class Response<out T> {
    class Success<T>(val value: T) : Response<T>()
    class Failure(val error: Throwable) : Response<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[value=$value]"
            is Failure -> "Failure[error=$error]"
        }
    }
}

fun <T : Any> Response<T>.getOrNull(): T? = (this as? Response.Success)?.value

fun <T : Any> Response<T>.getOrDefault(default: T): T = getOrNull() ?: default

fun <T : Any> Response<T>.getOrThrow(throwable: Throwable? = null): T {
    return when (this) {
        is Response.Success -> value
        is Response.Failure -> throw throwable ?: error
    }
}
