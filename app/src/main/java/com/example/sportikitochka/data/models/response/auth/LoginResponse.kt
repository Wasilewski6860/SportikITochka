package com.example.sportikitochka.data.models.response.auth

import com.google.gson.annotations.SerializedName


data class LoginResponse(
    @SerializedName("success")
    var success: Boolean,
    @SerializedName("access_token")
    var accessToken: String,
    @SerializedName("role")
    var role: String,
    @SerializedName("user_id")
    var userId: Int,
)


sealed class UserType {
    object Normal: UserType()
    object Premium: UserType()
    object Admin: UserType()

    fun LoginResponse.getUserType(): UserType {
        return when(this.role){
            "normal" -> Normal
            "premium" -> Premium
            "admin" -> Admin
            else -> Normal
        }
    }

    override fun toString(): String {
        return when(this){
            is Normal -> "normal"
            is Premium -> "premium"
            is Admin -> "admin"
        }

    }

    fun String.toUserType(): UserType {
        return when(this){
            "normal" -> Normal
            "premium" -> Premium
            "admin" -> Admin
            else -> Normal
        }
    }
}