# Payroll Management System

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
