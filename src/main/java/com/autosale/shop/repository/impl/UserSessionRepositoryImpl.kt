package com.autosale.shop.repository.impl

import com.autosale.shop.model.UserSession
import com.autosale.shop.repository.UserSessionRepository
import com.autosale.shop.util.GenericSerializationUtil
import lombok.SneakyThrows
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.stereotype.Repository

@Repository
class UserSessionRepositoryImpl(
    private val lettuceConnectionFactory: LettuceConnectionFactory,
    @Value("\${jwt.access-token-lifetime}") private val sessionTimeToLive: Int
) : UserSessionRepository {
    @SneakyThrows
    override fun create(key: String, session: UserSession) {
        lettuceConnectionFactory.getConnection().use { connection ->
            connection.stringCommands()
                .setEx(key.toByteArray(), sessionTimeToLive * 60L, GenericSerializationUtil.serialize(session))
        }
    }

    @SneakyThrows
    override fun read(key: String): UserSession {
        lettuceConnectionFactory.getConnection().use { connection ->
            val bytes = connection.stringCommands()[key.toByteArray()]
            return bytes?.let { GenericSerializationUtil.deserialize(it) }!!
        }
    }

    override fun delete(key: String): Boolean? {
        lettuceConnectionFactory.getConnection()
            .use { connection -> return connection.commands().expireAt(key.toByteArray(), 1) }
    }
}
