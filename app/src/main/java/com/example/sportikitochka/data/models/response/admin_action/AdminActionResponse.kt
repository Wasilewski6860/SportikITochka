package com.example.sportikitochka.data.models.response.admin_action

import com.google.gson.annotations.SerializedName

data class AdminActionResponse(

    @SerializedName("success")
    var success: Boolean,
    @SerializedName("action")
    var action: String, //"grant_premium" // или "revoke_premium" для отмены премиума, "block" для блокировки или "unblock" для снятия блокировки
    @SerializedName("timestamp")
    var timestamp: Long
) {
    fun isSuccess(): Boolean = success
}