package com.ecommerce.repositories;

import com.ecommerce.data.Price;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PriceRepository extends JpaRepository<Price, UUID> {


    Optional<Price> findTopByProductIdOrderByCreateAtDesc(UUID productId);
}
