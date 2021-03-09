package com.ecommerce.batch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Getter
final public class ProductBatchDto {

    private UUID id;

    private String name;

    private BigDecimal price;

}
