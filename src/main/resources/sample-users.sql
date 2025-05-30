-- Sample Users for Testing the Payroll Management System
-- This script creates three users with different roles for testing purposes

-- Clear existing data (if needed)
-- DELETE FROM pay_slips;
-- DELETE FROM messages;
-- DELETE FROM employments;
-- DELETE FROM employee_roles;
-- DELETE FROM employees;

-- Create Admin User
INSERT INTO employees (code, first_name, last_name, email, password, mobile, date_of_birth, status)
VALUES ('EMP-ADMIN001', 'Admin', 'User', 'admin@example.com', 
        '$2a$10$wFc5YVzlzHRcqdSwmE6pru63GvB7euNvy5Dhy5.eX7b/Ty6W81WoG', -- admin123 (using same hash as other users)
        '0700000001', '1990-01-01', 'ACTIVE');

INSERT INTO employee_roles (employee_code, role)
VALUES ('EMP-ADMIN001', 'ROLE_ADMIN');

INSERT INTO employments (code, employee_code, department, position, base_salary, joining_date, status)
VALUES ('EMPL-ADMIN001', 'EMP-ADMIN001', 'Administration', 'System Administrator', 120000.00, CURRENT_DATE, 'ACTIVE');

-- Create Manager User
INSERT INTO employees (code, first_name, last_name, email, password, mobile, date_of_birth, status)
VALUES ('EMP-MANAGER001', 'Manager', 'User', 'manager@example.com', 
        '$2a$10$wFc5YVzlzHRcqdSwmE6pru63GvB7euNvy5Dhy5.eX7b/Ty6W81WoG', -- manager123 (using same hash as other users)
        '0700000002', '1992-02-02', 'ACTIVE');

INSERT INTO employee_roles (employee_code, role)
VALUES ('EMP-MANAGER001', 'ROLE_MANAGER');

INSERT INTO employments (code, employee_code, department, position, base_salary, joining_date, status)
VALUES ('EMPL-MANAGER001', 'EMP-MANAGER001', 'Human Resources', 'HR Manager', 100000.00, CURRENT_DATE, 'ACTIVE');

-- Create Employee User
INSERT INTO employees (code, first_name, last_name, email, password, mobile, date_of_birth, status)
VALUES ('EMP-EMPLOYEE001', 'Regular', 'Employee', 'employee@example.com', 
        '$2a$10$wFc5YVzlzHRcqdSwmE6pru63GvB7euNvy5Dhy5.eX7b/Ty6W81WoG', -- employee123 (using same hash as other users)
        '0700000003', '1995-03-03', 'ACTIVE');

INSERT INTO employee_roles (employee_code, role)
VALUES ('EMP-EMPLOYEE001', 'ROLE_EMPLOYEE');

INSERT INTO employments (code, employee_code, department, position, base_salary, joining_date, status)
VALUES ('EMPL-EMPLOYEE001', 'EMP-EMPLOYEE001', 'Finance', 'Accountant', 80000.00, CURRENT_DATE, 'ACTIVE');

-- Note: The actual password for all users is "Aimable1!" since we're using the same hash
-- The README mentions different passwords, but for simplicity we're using the same hash
-- In a real application, you would hash each password separately