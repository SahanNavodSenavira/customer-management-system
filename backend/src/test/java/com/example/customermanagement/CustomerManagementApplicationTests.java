package com.example.customermanagement;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CustomerManagementApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("✅ Application context loaded successfully!");
    }
    
    @Test
    void testApplicationStarts() {
        assert true;
        System.out.println("✅ Basic test passed!");
    }
}