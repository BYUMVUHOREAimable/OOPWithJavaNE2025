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
 * This class initializes the database with sample data when the application starts.
 * It runs before the DatabaseTriggerInitializer to ensure data is loaded before triggers are created.
 */
@Component
@Order(1)
public class DatabaseInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        initializeDatabase();
    }

    private void initializeDatabase() {
        try {
            // Read the initialization SQL script
            ClassPathResource resource = new ClassPathResource("db/init-data.sql");
            String initSql = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

            // Execute the SQL script
            jdbcTemplate.execute(initSql);
            
            logger.info("Database initialized with sample data successfully");
        } catch (IOException e) {
            logger.error("Failed to initialize database with sample data", e);
        }
    }
}