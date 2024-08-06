package com.example.data.models.response.user_data

import com.google.gson.annotations.SerializedName

data class UserDataResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String,
    @SerializedName("weight") val weight: Float,
    @SerializedName("phone") val phone: String,
    @SerializedName("birthday") val birthday: String
)