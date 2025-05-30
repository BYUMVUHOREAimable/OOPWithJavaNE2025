# Payroll Management System

## Project Overview

The Payroll Management System is a comprehensive web application designed to manage employee information, employment details, and payroll processing for an organization. It provides a secure, role-based access system with features for managing employees, calculating salaries, generating pay slips, and handling deductions.

### Key Features

- **Employee Management**: Add, update, and manage employee information
- **Employment Management**: Track employment details, positions, and departments
- **Payroll Processing**: Calculate salaries, generate pay slips, and manage deductions
- **Role-Based Access Control**: Different access levels for employees, managers, and administrators
- **Secure Authentication**: JWT-based authentication system
- **API Documentation**: Comprehensive API documentation with Swagger UI

## Technology Stack

- **Backend**: Java 17, Spring Boot 3.x
- **Database**: PostgreSQL (with H2 option for development)
- **Security**: Spring Security with JWT authentication
- **Documentation**: OpenAPI 3.0 (Swagger)
- **Build Tool**: Maven

## Setup and Installation

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- PostgreSQL 12 or higher (or use the embedded H2 database)

### Database Configuration

The application is configured to use PostgreSQL by default. You can modify the database settings in `src/main/resources/application.properties`:

```properties
# PostgreSQL Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/payrolldb
spring.datasource.username=postgres
spring.datasource.password=yourpassword
```

Alternatively, you can use the embedded H2 database by uncommenting the H2 configuration in the properties file.

### Building and Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Build the project:
   ```
   mvn clean install
   ```
4. Run the application:
   ```
   mvn spring-boot:run
   ```
5. The application will be available at `http://localhost:8080/api`

## Accessing Swagger UI

To access the Swagger UI and test the API endpoints:

1. Start the application
2. Open your web browser and navigate to: [http://localhost:8080/api/swagger-ui.html](http://localhost:8080/api/swagger-ui.html)

This will open the Swagger UI interface where you can:
- View all available API endpoints
- Test API operations directly from the browser
- See request/response models and documentation

Alternatively, you can access the raw API documentation in JSON format at:
[http://localhost:8080/api/api-docs](http://localhost:8080/api/api-docs)

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── rw/gov/payroll/
│   │       ├── config/         # Configuration classes
│   │       ├── controller/     # REST controllers
│   │       ├── dto/            # Data Transfer Objects
│   │       ├── model/          # Entity classes
│   │       ├── repository/     # Data repositories
│   │       ├── security/       # Security configuration
│   │       └── service/        # Business logic
│   └── resources/
│       ├── db/                 # Database scripts
│       └── application.properties # Application configuration
└── test/
    └── java/
        └── rw/gov/payroll/     # Test classes
```

## Authentication and Authorization

The Payroll Management System uses JWT (JSON Web Token) based authentication and role-based authorization.

### User Roles

The system has three roles with different access levels:

1. **ROLE_EMPLOYEE**: Basic role for employees who can view their own information
2. **ROLE_MANAGER**: Managers can view and manage employee information
3. **ROLE_ADMIN**: Administrators have full access to the system

### Authentication

To use protected endpoints:
1. First use the `/api/auth/login` endpoint to obtain a JWT token
2. Click the "Authorize" button at the top of the Swagger UI
3. Enter your token in the format: `Bearer your_token_here`
4. Click "Authorize" to apply the token to all subsequent requests

### Testing Security Features

For detailed instructions on testing authentication and authorization:

1. See the security testing guide at `src/main/resources/security-testing-guide.md`
2. Use the sample users SQL script at `src/main/resources/sample-users.sql` to create test users

#### Sample Test Users

- **Admin**: admin@example.com / admin123
- **Manager**: manager@example.com / manager123
- **Employee**: employee@example.com / employee123

### JWT Token Information

The JWT token contains the following claims:
- `sub`: The subject (username/email)
- `roles`: The user's roles
- `iat`: Issued at timestamp
- `exp`: Expiration timestamp

You can decode the JWT token at [jwt.io](https://jwt.io/) to verify the claims.

## API Endpoints

The application provides the following main API endpoints:

### Authentication
- `POST /api/auth/login` - Authenticate a user and get a JWT token
- `POST /api/auth/register` - Register a new user

### Employees
- `GET /api/employees` - Get all employees (requires ADMIN or MANAGER role)
- `GET /api/employees/{code}` - Get employee by code
- `POST /api/employees` - Create a new employee (requires ADMIN role)
- `PUT /api/employees/{code}` - Update an employee (requires ADMIN role)
- `DELETE /api/employees/{code}` - Delete an employee (requires ADMIN role)

### Employments
- `GET /api/employments` - Get all employments (requires ADMIN or MANAGER role)
- `GET /api/employments/{code}` - Get employment by code
- `POST /api/employments` - Create a new employment (requires ADMIN role)
- `PUT /api/employments/{code}` - Update an employment (requires ADMIN role)
- `DELETE /api/employments/{code}` - Delete an employment (requires ADMIN role)

### Payroll
- `GET /api/payroll/payslips` - Get all pay slips (requires ADMIN or MANAGER role)
- `GET /api/payroll/payslips/{code}` - Get pay slip by code
- `POST /api/payroll/generate` - Generate pay slips (requires ADMIN role)
- `PUT /api/payroll/approve/{id}` - Approve pay slips (requires ADMIN role)
- `PUT /api/payroll/approve/all` - Approve pay slips (requires ADMIN role)
- `GET /payroll/payslips/me` - Get your payslip
- `Other APIs`

For a complete list of endpoints and their documentation, refer to the Swagger UI.
