# Security Testing Guide for Payroll Management System

## Authentication and Authorization Overview

The Payroll Management System uses JWT (JSON Web Token) based authentication and role-based authorization. There are three roles in the system:

1. **ROLE_EMPLOYEE**: Basic role for employees who can view their own information
2. **ROLE_MANAGER**: Managers can view and manage employee information
3. **ROLE_ADMIN**: Administrators have full access to the system

## Using Swagger UI with JWT Authentication

The API documentation is available through Swagger UI at `/api/swagger-ui.html`. To use authenticated endpoints:

1. First, use the `/api/auth/login` endpoint to obtain a JWT token
2. Click on the "Authorize" button (lock icon) at the top right of the Swagger UI
3. In the authorization popup, enter your JWT token with the Bearer prefix: `Bearer eyJhbGciOiJIUzUxMiJ9...`
4. Click "Authorize" and close the popup
5. Now you can use the authenticated endpoints in Swagger UI

## Sample Test Users

Here are sample users you can use for testing:

1. **Admin User**
   - Email: admin@example.com
   - Password: admin123
   - Roles: ROLE_ADMIN

2. **Manager User**
   - Email: manager@example.com
   - Password: manager123
   - Roles: ROLE_MANAGER

3. **Employee User**
   - Email: employee@example.com
   - Password: employee123
   - Roles: ROLE_EMPLOYEE

## Testing Authentication

### Step 1: Register a New User

1. Send a POST request to `/api/auth/register` with the following JSON body:

```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "password123",
  "mobile": "1234567890",
  "dateOfBirth": "1990-01-01",
  "roles": ["ROLE_EMPLOYEE"]
}
```

2. You should receive a success response with status code 200.

### Step 2: Login with the Registered User

1. Send a POST request to `/api/auth/login` with the following JSON body:

```json
{
  "email": "john.doe@example.com",
  "password": "password123"
}
```

2. You should receive a response with a JWT token, user details, and roles:

```json
{
  "status": "success",
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "code": "EMP-XXXXXXXX",
    "email": "john.doe@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "roles": ["ROLE_EMPLOYEE"]
  }
}
```

3. Save the token for subsequent requests.

### Step 3: Access Protected Endpoint with Token

1. Send a GET request to `/api/employees/me` with the Authorization header:
   - Header: `Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...`

2. You should receive your employee details in the response.

## Testing Authorization

### Test Case 1: Employee Role Access

1. Login with the employee user credentials:
   - Email: employee@example.com
   - Password: employee123

2. Try to access the following endpoints:
   - GET `/api/employees/me` - Should succeed (own profile)
   - GET `/api/employees/{your-code}` - Should succeed (own profile)
   - GET `/api/employees` - Should fail (403 Forbidden)
   - POST `/api/employees` - Should fail (403 Forbidden)
   - DELETE `/api/employees/{any-code}` - Should fail (403 Forbidden)

### Test Case 2: Manager Role Access

1. Login with the manager user credentials:
   - Email: manager@example.com
   - Password: manager123

2. Try to access the following endpoints:
   - GET `/api/employees` - Should succeed (list all employees)
   - GET `/api/employees/{any-code}` - Should succeed (view any employee)
   - POST `/api/employees` - Should succeed (create employee)
   - PUT `/api/employees/{any-code}` - Should succeed (update any employee)
   - DELETE `/api/employees/{any-code}` - Should fail (403 Forbidden, admin only)

### Test Case 3: Admin Role Access

1. Login with the admin user credentials:
   - Email: admin@example.com
   - Password: admin123

2. Try to access the following endpoints:
   - All endpoints should be accessible
   - DELETE `/api/employees/{any-code}` - Should succeed (admin can delete)

## JWT Token Information

The JWT token contains the following claims:
- `sub`: The subject (username/email)
- `roles`: The user's roles
- `iat`: Issued at timestamp
- `exp`: Expiration timestamp

You can decode the JWT token at [jwt.io](https://jwt.io/) to verify the claims.

## Troubleshooting

- If you receive a 401 Unauthorized error, your token may have expired. Try logging in again.
- If you receive a 403 Forbidden error, you don't have the required role to access that resource.
- Make sure to include the "Bearer " prefix in the Authorization header.
