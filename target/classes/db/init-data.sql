-- Initialize database with required data for the Payroll Management System

-- Clear existing data
DELETE FROM pay_slips;
DELETE FROM messages;
DELETE FROM employments;
DELETE FROM employee_roles;
DELETE FROM employees;
DELETE FROM deductions;

-- Create employees
INSERT INTO employees (code, first_name, last_name, email, password, mobile, date_of_birth, status)
VALUES ('EMP-6A4E9A54', 'BYUMVUHORE', 'Aimable', 'aimablebyumvuhore@gmail.com', 
        '$2a$10$wFc5YVzlzHRcqdSwmE6pru63GvB7euNvy5Dhy5.eX7b/Ty6W81WoG', -- Aimable1!
        '0796004898', '2002-11-05', 'ACTIVE');

INSERT INTO employees (code, first_name, last_name, email, password, mobile, date_of_birth, status)
VALUES ('EMP-D56E4F2E', 'IRADUKUNDA', 'Celeverien', 'aimablebyumvuhore2@gmail.com', 
        '$2a$10$wFc5YVzlzHRcqdSwmE6pru63GvB7euNvy5Dhy5.eX7b/Ty6W81WoG', -- Aimable1!
        '0784191775', '2005-12-24', 'ACTIVE');

INSERT INTO employees (code, first_name, last_name, email, password, mobile, date_of_birth, status)
VALUES ('EMP-4BDB1CD7', 'NIYITEGEKA', 'Elizabeth', 'aimablebyumvuhore3@gmail.com', 
        '$2a$10$wFc5YVzlzHRcqdSwmE6pru63GvB7euNvy5Dhy5.eX7b/Ty6W81WoG', -- Aimable1!
        '0784191775', '2005-12-24', 'ACTIVE');

-- Assign roles
INSERT INTO employee_roles (employee_code, role)
VALUES ('EMP-6A4E9A54', 'ROLE_ADMIN');

INSERT INTO employee_roles (employee_code, role)
VALUES ('EMP-D56E4F2E', 'ROLE_MANAGER');

INSERT INTO employee_roles (employee_code, role)
VALUES ('EMP-4BDB1CD7', 'ROLE_EMPLOYEE');

-- Create employments
INSERT INTO employments (code, employee_code, department, position, base_salary, joining_date, status)
VALUES ('EMPL-0CCC66DA', 'EMP-6A4E9A54', 'Administration', 'System Administrator', 100000.00, CURRENT_DATE, 'ACTIVE');

INSERT INTO employments (code, employee_code, department, position, base_salary, joining_date, status)
VALUES ('EMPL-876F7C0D', 'EMP-D56E4F2E', 'Administration', 'Manager', 100000.00, CURRENT_DATE, 'ACTIVE');

INSERT INTO employments (code, employee_code, department, position, base_salary, joining_date, status)
VALUES ('EMPL-DD9AC09B', 'EMP-4BDB1CD7', 'Engineering', 'Software Engineer', 100000.00, CURRENT_DATE, 'ACTIVE');

-- Create deductions
INSERT INTO deductions (code, name, percentage)
VALUES ('DED-AA21F075', 'EmployeeTax', 30.00);

INSERT INTO deductions (code, name, percentage)
VALUES ('DED-2E582A30', 'Pension', 6.00);

INSERT INTO deductions (code, name, percentage)
VALUES ('DED-8A8B83A1', 'MedicalInsurance', 5.00);

INSERT INTO deductions (code, name, percentage)
VALUES ('DED-2B55607F', 'Others', 5.00);

INSERT INTO deductions (code, name, percentage)
VALUES ('DED-065CE821', 'Housing', 14.00);

INSERT INTO deductions (code, name, percentage)
VALUES ('DED-3477B6C4', 'Transport', 14.00);