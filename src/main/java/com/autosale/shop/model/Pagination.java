package com.autosale.shop.model;

import lombok.Value;

@Value
public class Pagination {
    int pageSize = 10;
    int currentPage = 1;
}
