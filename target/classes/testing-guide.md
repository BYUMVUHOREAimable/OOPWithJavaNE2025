# Comprehensive Testing Guide for Payroll Management System

This guide provides step-by-step instructions for testing all the main features of the Payroll Management System after logging in as an admin.

## Prerequisites

1. The application is running
2. You have access to the Swagger UI at: http://localhost:8080/api/swagger-ui.html

## Authentication

### Login as Admin

1. Open the Swagger UI at http://localhost:8080/api/swagger-ui.html
2. Expand the `/api/auth/login` endpoint
3. Click "Try it out"
4. Enter the following JSON in the request body:
   ```json
   {
     "email": "admin@example.com",
     "password": "admin123"
   }
   ```
5. Click "Execute"
6. You should receive a response with a JWT token, user details, and roles
7. Copy the token value (without the quotes)
8. Click the "Authorize" button at the top of the Swagger UI
9. Enter the token with the Bearer prefix: `Bearer your_token_here`
10. Click "Authorize" and close the popup

Now you're authenticated as an admin and can test all features.

## Employee Management

### View All Employees

1. Expand the `/employees` GET endpoint
2. Click "Try it out"
3. Click "Execute"
4. You should see a list of all employees in the system

### View Employee Details

1. Expand the `/employees/{code}` GET endpoint
2. Click "Try it out"
3. Enter an employee code (e.g., "EMP-ADMIN001")
4. Click "Execute"
5. You should see the details of the specified employee

### Create a New Employee

1. Expand the `/employees` POST endpoint
2. Click "Try it out"
3. Enter the following JSON in the request body:
   ```json
   {
     "firstName": "Test",
     "lastName": "User",
     "email": "test.user@example.com",
     "password": "password123",
     "mobile": "1234567890",
     "dateOfBirth": "1990-01-01",
     "roles": ["ROLE_EMPLOYEE"]
   }
   ```
4. Click "Execute"
5. You should receive a success response with the created employee details

### Update an Employee

1. Expand the `/employees/{code}` PUT endpoint
2. Click "Try it out"
3. Enter the employee code you want to update
4. Enter the updated employee details in the request body
5. Click "Execute"
6. You should receive a success response with the updated employee details

### Delete an Employee

1. Expand the `/employees/{code}` DELETE endpoint
2. Click "Try it out"
3. Enter the employee code you want to delete
4. Click "Execute"
5. You should receive a success response indicating the employee was deleted

## Employment Management

### View All Employments

1. Expand the `/employments` GET endpoint
2. Click "Try it out"
3. Click "Execute"
4. You should see a list of all employment records in the system

### View Active Employments

1. Expand the `/employments/active` GET endpoint
2. Click "Try it out"
3. Click "Execute"
4. You should see a list of all active employment records

### View Employment Details

1. Expand the `/employments/{code}` GET endpoint
2. Click "Try it out"
3. Enter an employment code (e.g., "EMPL-ADMIN01")
4. Click "Execute"
5. You should see the details of the specified employment record

### Create a New Employment

1. Expand the `/employments` POST endpoint
2. Click "Try it out"
3. Enter the following JSON in the request body:
   ```json
   {
     "employeeCode": "EMP-XXXX",
     "department": "IT",
     "position": "Developer",
     "baseSalary": 70000,
     "joiningDate": "2023-01-01",
     "status": "ACTIVE"
   }
   ```
   (Replace "EMP-XXXX" with an actual employee code)
4. Click "Execute"
5. You should receive a success response with the created employment details

### Update an Employment

1. Expand the `/employments/{code}` PUT endpoint
2. Click "Try it out"
3. Enter the employment code you want to update
4. Enter the updated employment details in the request body
5. Click "Execute"
6. You should receive a success response with the updated employment details

### Delete an Employment

1. Expand the `/employments/{code}` DELETE endpoint
2. Click "Try it out"
3. Enter the employment code you want to delete
4. Click "Execute"
5. You should receive a success response indicating the employment was deleted

## Deduction Management

### View All Deductions

1. Expand the `/deductions` GET endpoint
2. Click "Try it out"
3. Click "Execute"
4. You should see a list of all deductions in the system

### View Deduction Details

1. Expand the `/deductions/{code}` GET endpoint
2. Click "Try it out"
3. Enter a deduction code
4. Click "Execute"
5. You should see the details of the specified deduction

### Create a New Deduction

1. Expand the `/deductions` POST endpoint
2. Click "Try it out"
3. Enter the following JSON in the request body:
   ```json
   {
     "name": "Income Tax",
     "description": "Government income tax",
     "rate": 15.0,
     "isPercentage": true,
     "isActive": true
   }
   ```
4. Click "Execute"
5. You should receive a success response with the created deduction details

### Update a Deduction

1. Expand the `/deductions/{code}` PUT endpoint
2. Click "Try it out"
3. Enter the deduction code you want to update
4. Enter the updated deduction details in the request body
5. Click "Execute"
6. You should receive a success response with the updated deduction details

### Delete a Deduction

1. Expand the `/deductions/{code}` DELETE endpoint
2. Click "Try it out"
3. Enter the deduction code you want to delete
4. Click "Execute"
5. You should receive a success response indicating the deduction was deleted

## Payroll Management

### View All Pay Slips

1. Expand the `/payroll/payslips` GET endpoint
2. Click "Try it out"
3. Click "Execute"
4. You should see a list of all pay slips in the system

### View Pay Slips by Status

1. Expand the `/payroll/payslips/status/{status}` GET endpoint
2. Click "Try it out"
3. Enter a status (e.g., "PENDING", "APPROVED", "PAID")
4. Click "Execute"
5. You should see a list of pay slips with the specified status

### View Pay Slips by Month and Year

1. Expand the `/payroll/payslips/month/{month}/year/{year}` GET endpoint
2. Click "Try it out"
3. Enter a month (1-12) and year (e.g., 2023)
4. Click "Execute"
5. You should see a list of pay slips for the specified month and year

### Generate Payroll

1. Expand the `/payroll/generate` POST endpoint
2. Click "Try it out"
3. Enter a month (1-12) and year (e.g., 2023)
4. Click "Execute"
5. You should receive a success response with the generated pay slips

### Approve a Pay Slip

1. Expand the `/payroll/approve/{id}` PUT endpoint
2. Click "Try it out"
3. Enter the ID of the pay slip you want to approve
4. Click "Execute"
5. You should receive a success response with the approved pay slip details

### Approve All Pay Slips for a Month

1. Expand the `/payroll/approve/all` PUT endpoint
2. Click "Try it out"
3. Enter a month (1-12) and year (e.g., 2023)
4. Click "Execute"
5. You should receive a success response with all the approved pay slips

## Testing as Different User Roles

To test the application with different user roles:

1. Logout by clearing the authorization (click "Authorize" and then "Logout")
2. Login with different user credentials:
   - Manager: manager@example.com / manager123
   - Employee: employee@example.com / employee123
3. Test the endpoints to verify role-based access control

## Troubleshooting

- If you receive a 401 Unauthorized error, your token may have expired. Try logging in again.
- If you receive a 403 Forbidden error, you don't have the required role to access that resource.
- Make sure to include the "Bearer " prefix in the Authorization header.