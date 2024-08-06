package com.example.sportikitochka.common

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

sealed class State<out T>: Parcelable {
    @Parcelize
    class Success<out T>(val value: @RawValue T) : State<T>()
    @Parcelize
    class Error(val error: Throwable) : State<Nothing>()
    @Parcelize
    object Loading : State<Nothing>()

    @Parcelize
    object NotStarted : State<Nothing>()

    fun <T : Any> State<T>.isLoading(): Boolean = this is Loading
    fun <T : Any> State<T>.isSuccess(): Boolean = this is Success<*>
    fun <T : Any> State<T>.isError(): Boolean = this is Error
}
