package rw.gov.payroll;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Basic test class to verify that the Spring application context loads successfully.
 * This is a smoke test that ensures the application can start without errors.
 */
@SpringBootTest
public class PayrollManagementSystemApplicationTests {

    /**
     * Tests that the application context loads successfully.
     * This test will fail if there are any issues with bean configuration or
     * if required dependencies are missing.
     */
    @Test
    void contextLoads() {
        // This test will pass if the application context loads successfully
        // No assertions needed as the test will fail if the context fails to load
    }
}