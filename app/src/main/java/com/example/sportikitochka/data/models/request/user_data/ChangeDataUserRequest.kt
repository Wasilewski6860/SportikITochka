package com.example.sportikitochka.data.models.request.user_data

import com.google.gson.annotations.SerializedName

data class ChangeDataUserRequest(
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String,
    @SerializedName("weight") val weight: Int,
    @SerializedName("phone") val phone: String,
    @SerializedName("birthday") val birthday: String
)