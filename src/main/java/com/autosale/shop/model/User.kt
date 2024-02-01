package com.autosale.shop.model

import com.fasterxml.jackson.annotation.JsonProperty

data class User(
    @JsonProperty("id") val id: Int?,
    @JsonProperty("userName") val userName: String,
    @JsonProperty("password") val password: String,
    @JsonProperty("role") val role: UserRole?
)
