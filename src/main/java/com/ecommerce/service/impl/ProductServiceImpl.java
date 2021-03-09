package com.ecommerce.service.impl;

import com.ecommerce.dto.ProductResponse;
import com.ecommerce.repositories.ExchangeRepository;
import com.ecommerce.repositories.PriceRepository;
import com.ecommerce.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static com.ecommerce.util.ResponseMessages.INVALID_PRODUCT;
import static com.ecommerce.util.ResponseMessages.PRODUCT_NOT_FOUND;

@Service
public class ProductServiceImpl implements ProductService {

    private final PriceRepository priceRepository;
    private final ExchangeRepository exchangeRepository;

    public ProductServiceImpl(PriceRepository priceRepository, ExchangeRepository exchangeRepository) {
        this.priceRepository = priceRepository;
        this.exchangeRepository = exchangeRepository;
    }

    @Override
    public ProductResponse getProduct(UUID id) {
        var price = priceRepository.findTopByProductIdOrderByCreateAtDesc(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PRODUCT_NOT_FOUND.name()));

        var product = price.getProduct();
        if (!product.getValid()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_PRODUCT.name());

        var rates = exchangeRepository.findTopByCurrencyOrderByCreateAtDesc(price.getCurrency());

        var eurPrice = rates.getRate().multiply(price.getPrice());

        return new ProductResponse(
                product.getId(),
                product.getName(),
                eurPrice,
                "EUR"
        );
    }
}
