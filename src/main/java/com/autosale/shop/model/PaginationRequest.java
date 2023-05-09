package com.autosale.shop.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class PaginationRequest {
    int pageSize;
    int currentPage;

    public PaginationRequest(@JsonProperty("page_size") int pageSize, @JsonProperty("current_page") int currentPage) {
        this.pageSize = pageSize;
        this.currentPage = currentPage;
    }

    public PaginationRequest() {
        this.pageSize = 10;
        this.currentPage = 1;
    }
}
