package com.ecommerce.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "prices")
@EqualsAndHashCode(exclude="product")
public class Price {
    @Id
    private UUID id;

    private BigDecimal price;

    private String currency;

    @Column(name = "created_at")
    private LocalDateTime createAt;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="product_id", nullable=false)
    private Product product;
}
