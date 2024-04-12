package com.example.sportikitochka.data.repositories

import com.example.sportikitochka.data.models.response.auth.LoginResponse
import com.example.sportikitochka.data.models.response.auth.UserType
import com.example.sportikitochka.data.models.response.auth.UserType.Premium.getUserType
import com.example.sportikitochka.domain.repositories.PreferencesRepository
import com.example.sportikitochka.domain.repositories.SessionRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter

class SessionRepositoryImpl(
    private val preferencesRepository: PreferencesRepository,
    moshi: Moshi
) : SessionRepository {

    private companion object {
        const val AUTH_SESSION = "AUTH_SESSION"
        const val PROFILE = "PROFILE"
    }

    @OptIn(ExperimentalStdlibApi::class)
    private val serializationSessionAdapter = moshi.adapter<LoginResponse>()
//    @OptIn(ExperimentalStdlibApi::class)
//    private val serializationProfileAdapter = moshi.adapter<UserProfileResponse>()

    override fun saveSession(authSession: LoginResponse?) {
        if (authSession != null)
            preferencesRepository.putString(AUTH_SESSION, serializationSessionAdapter.toJson(authSession))
        else
            preferencesRepository.remove(AUTH_SESSION)
    }

    override fun getSession(): LoginResponse? =
        preferencesRepository.getStringOrNull(AUTH_SESSION)?.let {
            serializationSessionAdapter.fromJson(it)
        }

    override fun getUserRole(): UserType? {
        return getSession()?.getUserType()
    }

}