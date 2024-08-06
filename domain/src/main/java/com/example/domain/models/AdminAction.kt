package com.example.domain.models

enum class AdminAction(val action: String) {
    GRANT_PREMIUM("grant_premium"),
    REVOKE_PREMIUM("revoke_premium"),
    BLOCK("block"),
    UNBLOCK("unblock")
}
