package com.example.data.repositories

import com.example.domain.models.UserData
import com.example.domain.models.UserSession
import com.example.domain.models.UserType
import com.example.domain.models.UserType.Normal.toUserType
import com.example.domain.repositories.PreferencesRepository
import com.example.domain.repositories.SessionRepository
import com.squareup.moshi.Moshi

class SessionRepositoryImpl(
    private val preferencesRepository: PreferencesRepository,
    moshi: Moshi
) : SessionRepository {

    private companion object {
        const val AUTH_SESSION = "AUTH_SESSION"
        const val USER_DATA = "USER_DATA"
    }

    @OptIn(ExperimentalStdlibApi::class)
    private val serializationSessionAdapter = moshi.adapter<UserSession>(UserSession::class.java)

    @OptIn(ExperimentalStdlibApi::class)
    private val serializationUserDataAdapter = moshi.adapter<UserData>(UserData::class.java)

    override fun saveSession(authSession: UserSession?) {
        if (authSession != null)
            preferencesRepository.putString(
                AUTH_SESSION,
                serializationSessionAdapter.toJson(authSession)
            )
        else
            preferencesRepository.remove(AUTH_SESSION)
    }

    override fun getSession(): UserSession? =
        preferencesRepository.getStringOrNull(AUTH_SESSION)?.let {
            serializationSessionAdapter.fromJson(it)
        }

    override fun getUserRole(): UserType? {
        return getSession()?.toString()?.toUserType()
    }

    override fun saveUserData(userData: UserData?) {
        if (userData != null)
            preferencesRepository.putString(
                USER_DATA,
                serializationUserDataAdapter.toJson(userData)
            )
        else
            preferencesRepository.remove(USER_DATA)
    }

    override fun getUserData(): UserData? {
        return preferencesRepository.getStringOrNull(USER_DATA)?.let {
            serializationUserDataAdapter.fromJson(it)
        }
    }
}