package com.autosale.shop.model;

import lombok.Value;

import java.util.List;

@Value
public class PaginationResponse<T> {
    List<T> content;
    int pages;
    int currentPage;
    int pageSize;

    public PaginationResponse(List<T> content, PaginationRequest request, int itemsAmount) {
        this.content = content;
        pages = (int) Math.ceil((double) itemsAmount / request.getPageSize());
        currentPage = request.getCurrentPage();
        pageSize = request.getPageSize();
    }
}
