package com.ecommerce.web;

import com.ecommerce.dto.ProductResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductsControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldReturnExistentProduct() {
        var response = this.restTemplate
                .getForObject("http://localhost:" + port + "/products?id={id}", ProductResponse.class, "b04e9ded-e857-4677-aee7-c61d327210a6");

        assertThat(response.getId()).isEqualTo(UUID.fromString("b04e9ded-e857-4677-aee7-c61d327210a6"));
        assertThat(response.getName()).isEqualTo("test1");
        assertThat(response.getPrice()).isEqualTo("115.36000000");
        assertThat(response.getCurrency()).isEqualTo("EUR");

    }

    @Test
    public void shouldThrowNotFound() {
        var response = this.restTemplate
                .getForEntity("http://localhost:" + port + "/products?id={id}", String.class, "777777-e857-4677-aee7-c61d327210a6");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("Not Found");

    }
}