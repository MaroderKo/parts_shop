package com.autosale.shop.model

import com.fasterxml.jackson.annotation.JsonProperty

data class JwtTokensDTO(
    @JsonProperty("access_token") val accessToken: String,
    @JsonProperty("refresh_token") val refreshToken: String
)
