# Security Testing Guide for Payroll Management System

This guide provides instructions for testing the authentication and authorization features of the Payroll Management System.

## Authentication Testing

### 1. Obtaining a JWT Token

1. Use the `/api/auth/login` endpoint with valid credentials:
   ```json
   {
     "email": "aimablebyumvuhore@gmail.com",
     "password": "Aimable1!"
   }
   ```
2. The response will contain a JWT token:
   ```json
   {
     "token": "eyJhbGciOiJIUzUxMiJ9...",
     "type": "Bearer",
     "email": "aimablebyumvuhore@gmail.com",
     "roles": ["ROLE_ADMIN"]
   }
   ```
3. Copy the token value for use in subsequent requests.

### 2. Using the JWT Token

1. In Swagger UI:
   - Click the "Authorize" button at the top of the page
   - Enter the token in the format: `Bearer eyJhbGciOiJIUzUxMiJ9...`
   - Click "Authorize"

2. In Postman or other API tools:
   - Add an Authorization header: `Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...`

### 3. Testing Token Expiration

1. The token expires after 24 hours (configurable in application.properties)
2. To test expiration, you can:
   - Wait for the token to expire naturally
   - Modify the JWT expiration time in application.properties for testing purposes

## Authorization Testing

### 1. Testing Role-Based Access

Test each endpoint with different user roles to verify proper authorization:

#### Admin User (aimablebyumvuhore@gmail.com)
- Should have access to all endpoints
- Test creating, updating, and deleting resources

#### Manager User (aimablebyumvuhore2@gmail.com)
- Should have access to view all employees and manage their information
- Should not be able to access administrative functions
- Test accessing employee data and payroll information

#### Employee User (aimablebyumvuhore3@gmail.com)
- Should only have access to their own information
- Should not be able to view other employees' data
- Test accessing personal profile and payroll information

### 2. Testing Access Denial

1. Log in as an employee user
2. Try to access manager or admin-only endpoints
3. Verify that a 403 Forbidden response is returned

## Security Vulnerabilities Testing

### 1. CSRF Protection

The application uses stateless JWT authentication, which mitigates CSRF attacks. However, you can test by:
1. Attempting to perform state-changing operations without proper authentication
2. Verifying that such attempts are rejected

### 2. SQL Injection Testing

Test input fields with SQL injection attempts:
1. Try entering SQL commands in search fields
2. Verify that the application properly sanitizes inputs

### 3. XSS Testing

Test input fields with script tags and other potentially malicious content:
1. Try entering `<script>alert('XSS')</script>` in text fields
2. Verify that the application properly escapes or sanitizes the input

## Sample Test Users

Use these credentials for testing different access levels:

1. Admin User:
   - Email: aimablebyumvuhore@gmail.com
   - Password: Aimable1!
   - Role: ROLE_ADMIN

2. Manager User:
   - Email: aimablebyumvuhore2@gmail.com
   - Password: Aimable1!
   - Role: ROLE_MANAGER

3. Employee User:
   - Email: aimablebyumvuhore3@gmail.com
   - Password: Aimable1!
   - Role: ROLE_EMPLOYEE