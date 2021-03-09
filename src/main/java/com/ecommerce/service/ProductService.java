package com.ecommerce.service;

import com.ecommerce.dto.ProductResponse;

import java.util.UUID;

public interface ProductService {

    ProductResponse getProduct(UUID id);

}
