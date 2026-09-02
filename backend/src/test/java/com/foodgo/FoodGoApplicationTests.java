package com.foodgo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class FoodGoApplicationTests {

    @Test
    void contextLoads() {
        // Verifies that the full Spring application context starts successfully
        // with an in-memory H2 database, confirming that all beans wire up correctly.
    }
}
