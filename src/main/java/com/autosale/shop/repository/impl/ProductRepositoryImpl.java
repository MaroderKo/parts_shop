package com.autosale.shop.repository.impl;

import com.autosale.shop.model.PaginationRequest;
import com.autosale.shop.model.Product;
import com.autosale.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static structure.tables.Product.PRODUCT;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {
    private final DSLContext dsl;

    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    // читання даних не відбувається а номер айді заповює сама бд, тому можна поставити найнижчий рівень ізоляції
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
        int deleted = dsl.deleteFrom(PRODUCT)
                .where(PRODUCT.ID.eq(product.getId()))
                .execute();
        if (deleted == 0) {
            throw new DataAccessException("No record with id " + product.getId() + " found for deletion.");
        }
        dsl.insertInto(PRODUCT)
                .set(dsl.newRecord(PRODUCT, product))
                .execute();
    }

    @Override
    public int deleteById(int id) {
        return dsl.deleteFrom(PRODUCT)
                .where(PRODUCT.ID.eq(id))
                .execute();
    }

    @Override
    public int deleteByIdInArray(List<Integer> ids) {
        return dsl.deleteFrom(PRODUCT)
                .where(PRODUCT.ID.in(ids))
                .execute();
    }

    @Override
    public List<Product> findAllByUserId(int userId) {
        return findAllWithConditions(List.of(PRODUCT.SELLER_ID.eq(userId)));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED) // йде зчитування актуальних даних на момент запиту
    public List<Product> findAllByStatus(PaginationRequest pageRequest, String status) {
        if (status != null) {
            return findAllWithConditionsPageable(pageRequest, List.of(PRODUCT.STATUS.eq(status)));
        } else {
            return findAllWithConditionsPageable(pageRequest, Collections.emptyList());
        }
    }

    @Override
    public int countAllByStatus(PaginationRequest pageRequest, String status) {
        if (status != null) {
            return countAllWithConditions(List.of(PRODUCT.STATUS.eq(status)));
        } else {
            return countAllWithConditions(Collections.emptyList());
        }
    }

    @Override
    public void saveAllIgnoreExistence(List<Product> products) {

        dsl.batchStore(products.stream()
                .map(p -> dsl.newRecord(PRODUCT, p))
                .collect(Collectors.toUnmodifiableList()))
                .execute();
    }

    private List<Product> findAllWithConditions(List<Condition> conditions) {
        return dsl.selectFrom(PRODUCT)
                .where(conditions)
                .fetchInto(Product.class);
    }

    private List<Product> findAllWithConditionsPageable(PaginationRequest paginationRequest, List<Condition> conditions) {
        return dsl.selectFrom(PRODUCT)
                .where(conditions)
                .offset((paginationRequest.getCurrentPage() - 1) * paginationRequest.getPageSize()) // -1 because we start pages from 1
                .limit(paginationRequest.getPageSize())
                .fetchInto(Product.class);
    }

    private int countAllWithConditions(List<Condition> conditions) {
        return dsl.fetchCount(PRODUCT, conditions);
    }

}
