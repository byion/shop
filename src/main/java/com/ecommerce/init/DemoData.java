package com.ecommerce.init;

import com.ecommerce.data.Price;
import com.ecommerce.data.Product;
import com.ecommerce.repositories.PriceRepository;
import com.ecommerce.repositories.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;

@Component
@ConditionalOnProperty(prefix = "generate", name = "data", havingValue = "true")
public class DemoData {

    @Autowired
    private final ProductsRepository productsRepository;
    private final PriceRepository priceRepository;

    public DemoData(ProductsRepository productsRepository, PriceRepository priceRepository) {

        this.productsRepository = productsRepository;
        this.priceRepository = priceRepository;
    }

    @EventListener
    public void appReady(ApplicationReadyEvent event) {

        IntStream.range(0, 100_000)
                .forEach(i -> {
                    Product p = new Product();
                    Price price = getPrice(i, p);
                    p.setId(UUID.randomUUID());
                    p.setName("test" + i);
                    p.setValid(true);
                    p.setPrices(Set.of(price));

                    priceRepository.save(price);
                    productsRepository.save(p);
                });
    }

    private Price getPrice(int i, Product p) {
        Price price = new Price();
        price.setPrice(new BigDecimal(i));
        price.setProduct(p);
        price.setId(UUID.randomUUID());
        return price;
    }


}
