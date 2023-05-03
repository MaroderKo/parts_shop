package com.autosale.shop.repository.impl;

import com.autosale.shop.model.Product;
import com.autosale.shop.model.ProductStatus;
import com.autosale.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
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
    public List<Product> findAllActive() {
        return dsl.selectFrom(PRODUCT)
                .where(PRODUCT.STATUS.eq(ProductStatus.ON_SALE.toString()))
                .fetchInto(Product.class);
    }

    @Override
    public List<Product> findAll() {
        return dsl.selectFrom(PRODUCT)
                .fetchInto(Product.class);
    }

    @Override
    public List<Product> findAllByUserId(int id) {
        return dsl.selectFrom(PRODUCT)
                .where(PRODUCT.SELLER_ID.eq(id))
                .fetchInto(Product.class);
    }
}
