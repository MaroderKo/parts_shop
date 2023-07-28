package com.autosale.shop.service.impl;

import com.autosale.shop.exception.InvalidOperationException;
import com.autosale.shop.exception.PermissionDeniedException;
import com.autosale.shop.model.PaginationRequest;
import com.autosale.shop.model.PaginationResponse;
import com.autosale.shop.model.Product;
import com.autosale.shop.model.ProductStatus;
import com.autosale.shop.repository.ProductRepository;
import com.autosale.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    // можливі проблеми - при зміні сторінки може дублюватись чи не бути показаний певний елемент (фантомні рядки). Можливі вирішення: зміна ізоляції на SERIALIZABLE, але враховуючи що пошук товарів це один з основних функціоналів у багатокористувацькому магазині це робить дане рішення неефективним, також можливо змінити тип пагінації на keyset-based
    public PaginationResponse<Product> findByStatus(PaginationRequest paginationRequest, ProductStatus productStatus) {
        String status = productStatus == null ? null : productStatus.toString();

        List<Product> products = repository.findAllByStatus(paginationRequest, status);
        int totalAmount = repository.countAllByStatus(paginationRequest, status);

        return new PaginationResponse<>(products, paginationRequest, totalAmount);
    }

    @Override
    public Product findById(int id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Product with id %d not found", id)));
    }

    @Override
    public List<Product> findAllFromCurrentUser() {
        int userId = (int) getContext().getAuthentication().getPrincipal();
        return repository.findAllByUserId(userId);
    }

    @Override
    public List<Product> findAllByUserId(int id) {
        return repository.findAllByUserId(id);
    }

    @Override
    public int create(Product product) {
        ProductStatus status = ProductStatus.ON_SALE;
        if (product.getCost() >= 100) {
            status = ProductStatus.ON_MODERATION;
        }
        int sellerId = product.getSellerId() != null ? product.getSellerId() : (int) getContext().getAuthentication().getPrincipal();
        product = new Product(product.getId(), product.getName(), product.getDescription(), product.getCost(), status, sellerId, product.getBuyerId());
        return repository.save(product).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot save product to database!"));
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    // в межах транзакції зчитується і змінюється багато важливих даних, зміна яких ззовні - неприпустима
    public void edit(Product product) {
        if (getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) || (product.getSellerId().equals(getContext().getAuthentication().getPrincipal()) && !product.getStatus().equals(ProductStatus.BLOCKED))) {
            repository.update(product);
        } else {
            throw new PermissionDeniedException("You don't have permission to do that!");
        }
    }

    @Override
    public int deleteById(int id) {
        if (getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) || findById(id).getSellerId().equals(getContext().getAuthentication().getPrincipal())) {
            return repository.deleteById(id);
        } else {
            throw new PermissionDeniedException("You don't have permission to do that!");
        }
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    // ситуація схожа до апдейту, проте в межах транзакції додатково виконується зчитуваня даних та перевірка їх за певними обмеженями, що дає змогу понизити рівень ізоляції до REPEATABLE_READ
    public void buy(int productId) {
        Product product = findById(productId);
        if (product.getStatus().equals(ProductStatus.ON_SALE) && !product.getSellerId().equals(getContext().getAuthentication().getPrincipal())) {
            product = new Product(product.getId(), product.getName(), product.getDescription(), product.getCost(), ProductStatus.SOLD, product.getSellerId(), (Integer) getContext().getAuthentication().getPrincipal());
            repository.update(product);
        } else {
            throw new InvalidOperationException("You can't buy this product");
        }

    }
}
