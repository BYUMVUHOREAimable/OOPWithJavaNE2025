# Payroll Management System API Endpoints and Access Roles

This document lists all API endpoints in the Payroll Management System and the roles that can access them.

## Authentication Endpoints

| Method | Endpoint | Access Roles | Description |
|--------|----------|--------------|-------------|
| POST | `/auth/login` | Public (no authentication required) | User login |
| POST | `/auth/register` | Public (no authentication required) | User registration |

## Deduction Endpoints

| Method | Endpoint | Access Roles | Description |
|--------|----------|--------------|-------------|
| GET | `/deductions` | ROLE_ADMIN, ROLE_MANAGER, ROLE_EMPLOYEE | Get all deductions |
| GET | `/deductions/{code}` | ROLE_ADMIN, ROLE_MANAGER, ROLE_EMPLOYEE | Get deduction by code |
| GET | `/deductions/name/{name}` | ROLE_ADMIN, ROLE_MANAGER, ROLE_EMPLOYEE | Get deduction by name |
| POST | `/deductions` | ROLE_ADMIN | Create a new deduction |
| PUT | `/deductions/{code}` | ROLE_ADMIN | Update a deduction |
| DELETE | `/deductions/{code}` | ROLE_ADMIN | Delete a deduction |

## Employee Endpoints

| Method | Endpoint | Access Roles | Description |
|--------|----------|--------------|-------------|
| GET | `/employees` | ROLE_ADMIN, ROLE_MANAGER | Get all employees |
| GET | `/employees/{code}` | ROLE_ADMIN, ROLE_MANAGER, Self (employee's own data) | Get employee by code |
| GET | `/employees/me` | Any authenticated user | Get current employee's data |
| POST | `/employees` | ROLE_ADMIN, ROLE_MANAGER | Create a new employee |
| PUT | `/employees/{code}` | ROLE_ADMIN, ROLE_MANAGER, Self (employee's own data) | Update an employee |
| DELETE | `/employees/{code}` | ROLE_ADMIN | Delete an employee |

## Employment Endpoints

| Method | Endpoint | Access Roles | Description |
|--------|----------|--------------|-------------|
| GET | `/employments` | ROLE_ADMIN, ROLE_MANAGER | Get all employments |
| GET | `/employments/active` | ROLE_ADMIN, ROLE_MANAGER | Get active employments |
| GET | `/employments/{code}` | ROLE_ADMIN, ROLE_MANAGER | Get employment by code |
| GET | `/employments/employee/{employeeCode}` | ROLE_ADMIN, ROLE_MANAGER, Self (employee's own data) | Get employments by employee |
| POST | `/employments` | ROLE_ADMIN | Create a new employment |
| PUT | `/employments/{code}` | ROLE_ADMIN | Update an employment |
| DELETE | `/employments/{code}` | ROLE_ADMIN | Delete an employment |

## Message Endpoints

| Method | Endpoint | Access Roles | Description |
|--------|----------|--------------|-------------|
| GET | `/messages` | ROLE_ADMIN, ROLE_MANAGER | Get all messages |
| GET | `/messages/status/{sent}` | ROLE_ADMIN, ROLE_MANAGER | Get messages by sent status |
| GET | `/messages/employee/{employeeCode}` | ROLE_ADMIN, ROLE_MANAGER, Self (employee's own data) | Get messages by employee |
| GET | `/messages/period` | ROLE_ADMIN, ROLE_MANAGER | Get messages by month and year |
| POST | `/messages/test-email` | ROLE_ADMIN | Send a test email |
| POST | `/messages/send-unsent` | ROLE_ADMIN | Force sending of unsent messages |

## Payroll Endpoints

| Method | Endpoint | Access Roles | Description |
|--------|----------|--------------|-------------|
| GET | `/payroll/payslips` | ROLE_ADMIN, ROLE_MANAGER | Get all payslips |
| GET | `/payroll/payslips/status/{status}` | ROLE_ADMIN, ROLE_MANAGER | Get payslips by status |
| GET | `/payroll/payslips/employee/{employeeCode}` | ROLE_ADMIN, ROLE_MANAGER, Self (employee's own data) | Get payslips by employee |
| GET | `/payroll/payslips/month/{month}/year/{year}` | ROLE_ADMIN, ROLE_MANAGER | Get payslips by month and year |
| GET | `/payroll/payslips/{id}` | ROLE_ADMIN, ROLE_MANAGER, Self (owner of the payslip) | Get payslip by ID |
| GET | `/payroll/payslips/me` | Any authenticated user | Get current employee's payslips |
| POST | `/payroll/generate` | ROLE_ADMIN | Generate payroll for a specific month and year |
| PUT | `/payroll/approve/{id}` | ROLE_ADMIN | Approve a specific payslip |
| PUT | `/payroll/approve/all` | ROLE_ADMIN | Approve all payslips for a specific month and year |

## Other Accessible URLs

| URL Pattern | Access | Description |
|-------------|--------|-------------|
| `/v3/api-docs/**` | Public | Swagger API documentation |
| `/swagger-ui/**` | Public | Swagger UI |
| `/swagger-ui.html` | Public | Swagger UI HTML |
| `/api-docs/**` | Public | API documentation |
| `/h2-console/**` | Public | H2 Database console |

## Role Hierarchy

The system has three roles with the following hierarchy:

1. **ROLE_ADMIN** - Highest level of access, can perform all operations
2. **ROLE_MANAGER** - Can view most data and perform some management operations
3. **ROLE_EMPLOYEE** - Basic access, mostly limited to viewing their own data

In many cases, users can access their own data regardless of their role through custom security checks.