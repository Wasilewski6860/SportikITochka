package com.example.sportikitochka.domain.use_cases.admin_action

import com.example.sportikitochka.data.models.request.admin_action.AdminAction
import com.example.sportikitochka.data.models.request.admin_action.AdminActionRequest
import com.example.sportikitochka.domain.models.SportActivity
import com.example.sportikitochka.domain.repositories.ActivityRepository
import com.example.sportikitochka.domain.repositories.AdminActionRepository

class BlockUserUseCase(private val adminActionRepository: AdminActionRepository) {

    suspend fun execute(userId: String) = adminActionRepository.adminAction(
        AdminActionRequest(
            user_id = userId,
            action = AdminAction.BLOCK.action
        )
    )
}