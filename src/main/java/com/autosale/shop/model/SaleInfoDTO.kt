package com.autosale.shop.model

import java.io.Serializable

data class SaleInfoDTO(
    val buyerId: Int?,
    val productId: Int?
) : Serializable
