package com.ecommerce.batch.processors;

import com.ecommerce.batch.dto.ProductBatchDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class ProductItemProcessor implements ItemProcessor<ProductBatchDto, ProductBatchDto> {

    private static final Logger log = LoggerFactory.getLogger(ProductItemProcessor.class);

    @Override
    public ProductBatchDto process(final ProductBatchDto product) {

        ProductBatchDto result = new ProductBatchDto(product.getId(), product.getName(), product.getPrice().stripTrailingZeros());

        log.info("Converting (" + product + ") into (" + result + ")");

        return result;
    }

}