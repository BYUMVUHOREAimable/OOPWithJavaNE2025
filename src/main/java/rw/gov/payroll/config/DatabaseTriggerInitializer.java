package rw.gov.payroll.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * This class initializes database triggers when the application starts.
 * It reads SQL scripts from the classpath and executes them using JdbcTemplate.
 */
@Component
@Order(2)
public class DatabaseTriggerInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseTriggerInitializer.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        initializeTriggers();
    }

    private void initializeTriggers() {
        try {
            // Read the trigger SQL script
            ClassPathResource resource = new ClassPathResource("db/trigger.sql");
            String triggerSql = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

            // Execute the SQL script
            jdbcTemplate.execute(triggerSql);

            logger.info("Database triggers initialized successfully");
        } catch (IOException e) {
            logger.error("Failed to initialize database triggers", e);
        }
    }
}
