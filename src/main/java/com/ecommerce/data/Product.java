package com.ecommerce.data;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    private UUID id;

    private String name;

    private Boolean valid;

    @Column(name = "created_at")
    private LocalDateTime createAt;

    @OneToMany(mappedBy="product", fetch = FetchType.LAZY)
    private Set<Price> prices;
}
