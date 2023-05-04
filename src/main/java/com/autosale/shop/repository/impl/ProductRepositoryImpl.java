package com.autosale.shop.repository.impl;

import com.autosale.shop.model.Pagination;
import com.autosale.shop.model.Product;
import com.autosale.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static structure.tables.Product.PRODUCT;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {
    private final DSLContext dsl;

    @Override
    public Optional<Integer> save(Product product) {
        return dsl.insertInto(PRODUCT)
                .set(dsl.newRecord(PRODUCT, product))
                .returningResult(PRODUCT.ID)
                .fetchOptionalInto(Integer.class);

    }

    @Override
    public Optional<Product> findById(int id) {
        return dsl.selectFrom(PRODUCT)
                .where(PRODUCT.ID.eq(id))
                .fetchOptionalInto(Product.class);
    }

    @Override
    public void update(Product product) {
        dsl.newRecord(PRODUCT, product)
                .update();
    }

    @Override
    public int deleteById(int id) {
        return dsl.deleteFrom(PRODUCT)
                .where(PRODUCT.ID.eq(id))
                .execute();
    }

    @Override
    public List<Product> findAllWithConditions(Pagination pagination, List<Condition> conditions) {
        return dsl.selectFrom(PRODUCT)
                .where(conditions)
                .offset((pagination.getCurrentPage() - 1) * pagination.getPageSize()) // -1 because we start pages from 1
                .fetchSize(pagination.getPageSize())
                .fetchInto(Product.class);
    }

}
