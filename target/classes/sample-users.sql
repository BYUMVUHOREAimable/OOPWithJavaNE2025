-- Sample users for testing authentication and authorization
-- Note: Passwords are bcrypt-encoded
-- admin123, manager123, employee123

-- Create admin user
INSERT INTO employees (code, first_name, last_name, email, password, mobile, date_of_birth, status)
VALUES ('EMP-ADMIN001', 'Admin', 'User', 'admin@example.com', 
        '$2a$10$OwuE0BSoVpRgTnSNiLHSx.TqwIGFsj/gHC.LUz0O5ta7n5.3LY2Uy', -- admin123
        '1234567890', '1980-01-01', 'ACTIVE')
ON CONFLICT (code) DO NOTHING;

-- Create manager user
INSERT INTO employees (code, first_name, last_name, email, password, mobile, date_of_birth, status)
VALUES ('EMP-MNGR001', 'Manager', 'User', 'manager@example.com', 
        '$2a$10$3Qrx0Ck.XSc8PFt.G/ZzhuC1.0g5jV9q0YYQQe3ZicH3FM1HmPnJO', -- manager123
        '2345678901', '1985-02-15', 'ACTIVE')
ON CONFLICT (code) DO NOTHING;

-- Create employee user
INSERT INTO employees (code, first_name, last_name, email, password, mobile, date_of_birth, status)
VALUES ('EMP-EMPL001', 'Employee', 'User', 'employee@example.com', 
        '$2a$10$lQnG/7Dxr0j4aDfOQxJIB.ZHfyl8yyJ3q9QeCSq3.KRLIQz.9VXJC', -- employee123
        '3456789012', '1990-05-20', 'ACTIVE')
ON CONFLICT (code) DO NOTHING;

-- Assign roles
INSERT INTO employee_roles (employee_code, role)
VALUES ('EMP-ADMIN001', 'ROLE_ADMIN')
ON CONFLICT DO NOTHING;

INSERT INTO employee_roles (employee_code, role)
VALUES ('EMP-MNGR001', 'ROLE_MANAGER')
ON CONFLICT DO NOTHING;

INSERT INTO employee_roles (employee_code, role)
VALUES ('EMP-EMPL001', 'ROLE_EMPLOYEE')
ON CONFLICT DO NOTHING;

-- Create sample employment records
INSERT INTO employments (code, employee_code, department, position, base_salary, joining_date, status)
VALUES ('EMPL-ADMIN01', 'EMP-ADMIN001', 'Administration', 'System Administrator', 100000.00, '2020-01-01', 'ACTIVE')
ON CONFLICT (code) DO NOTHING;

INSERT INTO employments (code, employee_code, department, position, base_salary, joining_date, status)
VALUES ('EMPL-MNGR01', 'EMP-MNGR001', 'Human Resources', 'HR Manager', 80000.00, '2020-02-15', 'ACTIVE')
ON CONFLICT (code) DO NOTHING;

INSERT INTO employments (code, employee_code, department, position, base_salary, joining_date, status)
VALUES ('EMPL-EMPL01', 'EMP-EMPL001', 'Engineering', 'Software Engineer', 60000.00, '2020-05-20', 'ACTIVE')
ON CONFLICT (code) DO NOTHING;