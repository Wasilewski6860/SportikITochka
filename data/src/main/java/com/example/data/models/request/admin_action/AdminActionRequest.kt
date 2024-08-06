package com.example.data.models.request.admin_action

import com.google.gson.annotations.SerializedName

data class AdminActionRequest(

    @SerializedName("user_id")
    var user_id: String,
    @SerializedName("action")
    var action: String //"grant_premium" // или "revoke_premium" для отмены премиума, "block" для блокировки или "unblock" для снятия блокировки
)

enum class AdminAction(val action: String) {
    GRANT_PREMIUM("grant_premium"),
    REVOKE_PREMIUM("revoke_premium"),
    BLOCK("block"),
    UNBLOCK("unblock")
}

