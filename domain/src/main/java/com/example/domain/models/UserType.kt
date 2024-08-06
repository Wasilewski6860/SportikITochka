package com.example.domain.models

sealed class UserType {
    object Normal: UserType()
    object Premium: UserType()
    object Admin: UserType()

//    fun LoginResponse.getUserType(): UserType {
//        return when(this.role){
//            "normal" -> Normal
//            "premium" -> Premium
//            "admin" -> Admin
//            else -> Normal
//        }
//    }

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