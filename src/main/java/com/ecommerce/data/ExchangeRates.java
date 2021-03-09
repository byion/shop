package com.ecommerce.data;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "exchange_rates")
public class ExchangeRates {
    @Id
    private UUID id;

    private BigDecimal rate;

    private String currency;

    @Column(name = "created_at")
    private LocalDateTime createAt;
}
