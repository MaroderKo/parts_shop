package com.autosale.shop.model

import com.fasterxml.jackson.annotation.JsonProperty

data class PaginationRequest(
    @JsonProperty("page_size") val pageSize: Int = 10,
    @JsonProperty("current_page") val currentPage: Int = 1
)
