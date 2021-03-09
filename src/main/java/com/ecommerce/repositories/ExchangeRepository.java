package com.ecommerce.repositories;

import com.ecommerce.data.ExchangeRates;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExchangeRepository extends JpaRepository<ExchangeRates, UUID> {

    ExchangeRates findTopByCurrencyOrderByCreateAtDesc(String currency);
}
