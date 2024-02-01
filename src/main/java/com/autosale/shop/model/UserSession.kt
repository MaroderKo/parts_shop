package com.autosale.shop.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class UserSession @JsonCreator constructor(
    @JsonProperty("id") val id: UUID,
    @JsonProperty("user") val user: User
)
