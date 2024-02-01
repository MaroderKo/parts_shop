package com.autosale.shop.model

import kotlin.math.ceil


data class PaginationResponse<T>(
    val content: List<T>,
    val pages: Int,
    val currentPage: Int,
    val pageSize: Int
) {
    constructor(content: List<T>, request: PaginationRequest, itemsAmount: Int) :
            this(
                content,
                ceil(itemsAmount.toDouble() / request.pageSize).toInt(),
                request.currentPage,
                request.pageSize
            )
}



