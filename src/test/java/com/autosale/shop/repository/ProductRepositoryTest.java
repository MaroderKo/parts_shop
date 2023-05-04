package com.autosale.shop.repository;

import com.autosale.shop.configuration.TestBeanFactory;
import com.autosale.shop.model.*;
import com.autosale.shop.repository.impl.ProductRepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.autosale.shop.generator.ProductGenerator.generate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static structure.tables.Product.PRODUCT;
import static structure.tables.Users.USERS;


public class ProductRepositoryTest {
    private final ProductRepository repository = new ProductRepositoryImpl(TestBeanFactory.testDSLContext());

    @BeforeEach
    void createUser() {
        TestBeanFactory.testDSLContext().insertInto(USERS)
                .set(TestBeanFactory.testDSLContext().newRecord(USERS, new User(1, "user1", "", UserRole.ADMIN)))
                .execute();
        TestBeanFactory.testDSLContext().insertInto(USERS)
                .set(TestBeanFactory.testDSLContext().newRecord(USERS, new User(2, "user2", "", UserRole.ADMIN)))
                .execute();
    }

    @AfterEach
    @BeforeEach
    void clear() {
        TestBeanFactory.testDSLContext().delete(PRODUCT).execute();
        TestBeanFactory.testDSLContext().delete(USERS).execute();
    }

    @Test
    void findAll() {
        Product product = generate(ProductStatus.ON_SALE, 1, null);
        Product product1 = generate(ProductStatus.ON_SALE, 2, null);
        Product product2 = generate(ProductStatus.ON_SALE, 2, null);
        repository.save(product);
        repository.save(product1);
        repository.save(product2);
        assertThat(repository.findAllWithConditions(new Pagination(), Collections.emptyList()), containsInAnyOrder(product, product1, product2));
    }

    @Test
    void findAllByUserId() {
        Product product1 = generate(ProductStatus.ON_SALE, 1, null);
        Product product2 = generate(ProductStatus.ON_SALE, 1, null);
        Product product3 = generate(ProductStatus.ON_SALE, 2, null);
        Product product4 = generate(ProductStatus.ON_SALE, 2, null);
        repository.save(product1);
        repository.save(product2);
        repository.save(product3);
        repository.save(product4);
        assertThat(repository.findAllWithConditions(new Pagination(), List.of(PRODUCT.SELLER_ID.eq(1))), containsInAnyOrder(product1, product2));
        assertThat(repository.findAllWithConditions(new Pagination(), List.of(PRODUCT.SELLER_ID.eq(2))), containsInAnyOrder(product3, product4));
    }

    @Test
    void findById() {
        Product product = generate(ProductStatus.ON_SALE, 1, null);
        repository.save(product);
        assertThat(repository.findById(product.getId()).isPresent(), is(true));
        assertThat(repository.findById(product.getId()).get(), is(product));

    }

    @Test
    void findByStatus() {
        Product product1 = generate(ProductStatus.ON_SALE, 1, null);
        Product product2 = generate(ProductStatus.SOLD, 1, null);
        Product product3 = generate(ProductStatus.BLOCKED, 1, null);
        Product product4 = generate(ProductStatus.ON_MODERATION, 1, null);
        repository.save(product1);
        repository.save(product2);
        repository.save(product3);
        repository.save(product4);

        List<Product> activeProducts = repository.findAllWithConditions(new Pagination(), List.of(PRODUCT.STATUS.eq(ProductStatus.ON_SALE.toString())));
        assertThat(activeProducts.isEmpty(), is(false));
        assertThat(activeProducts, containsInAnyOrder(product1));
    }

    @Test
    void save() {
        Product product = generate(ProductStatus.ON_SALE, 1, null);
        Optional<Integer> save = repository.save(product);
        assertThat(save.isPresent(), is(true));
        assertThat(repository.findById(save.get()).get(), is(product));
    }

    @Test
    void update() {
        Product product = generate(ProductStatus.ON_SALE, 1, null);
        repository.save(product);
        Product newProduct = new Product(product.getId(), "1234", null, 1f, product.getStatus(), product.getSellerId(), product.getBuyerId());
        repository.update(newProduct);
        assertThat(repository.findById(newProduct.getId()).get(), is(newProduct));
    }

    @Test
    void deleteById() {
        Product product = generate(ProductStatus.ON_SALE, 1, null);
        repository.save(product);
        int i = repository.deleteById(product.getId());
        assertThat(i, is(1));
        assertThat(repository.findById(product.getId()).isPresent(), is(false));
    }
}
