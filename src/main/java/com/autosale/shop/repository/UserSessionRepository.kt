package com.autosale.shop.repository

import com.autosale.shop.model.UserSession
import java.util.*

//Поліморфізм
//Дає змогу використовувати різні класи у їхній загальній формі (кілька реалізацій через інтерфейс, тощо)
interface UserSessionRepository {
    fun create(key: String, session: UserSession)
    fun read(key: String): Optional<UserSession>
    fun delete(key: String): Boolean?
}
