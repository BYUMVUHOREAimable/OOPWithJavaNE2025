-- PostgreSQL Trigger for Message Generation
-- This trigger will insert a record into the message table when a pay slip's status is updated from PENDING to PAID

CREATE OR REPLACE FUNCTION generate_message_on_payslip_approval()
RETURNS TRIGGER AS $$
DECLARE
    employee_firstname VARCHAR;
    employee_code VARCHAR;
    institution_name VARCHAR := 'Rwanda Coding Academy'; -- Default institution name
BEGIN
    -- Only proceed if status is being updated from PENDING to PAID
    IF OLD.status = 'PENDING' AND NEW.status = 'PAID' THEN
        -- Get employee details
        SELECT e.first_name, e.code INTO employee_firstname, employee_code
        FROM employees e
        WHERE e.code = NEW.employee_code;

        -- Format the message
        INSERT INTO messages (employee_code, content, month, year, created_at, sent)
        VALUES (
            NEW.employee_code,
            'Dear ' || employee_firstname || ', Your salary of ' || 
            CASE 
                WHEN NEW.month = 1 THEN 'JANUARY'
                WHEN NEW.month = 2 THEN 'FEBRUARY'
                WHEN NEW.month = 3 THEN 'MARCH'
                WHEN NEW.month = 4 THEN 'APRIL'
                WHEN NEW.month = 5 THEN 'MAY'
                WHEN NEW.month = 6 THEN 'JUNE'
                WHEN NEW.month = 7 THEN 'JULY'
                WHEN NEW.month = 8 THEN 'AUGUST'
                WHEN NEW.month = 9 THEN 'SEPTEMBER'
                WHEN NEW.month = 10 THEN 'OCTOBER'
                WHEN NEW.month = 11 THEN 'NOVEMBER'
                WHEN NEW.month = 12 THEN 'DECEMBER'
            END || '/' || NEW.year || ' from ' || institution_name || ' ' || 
            NEW.net_salary || ' has been credited to your ' || employee_code || ' account successfully.',
            NEW.month,
            NEW.year,
            NOW(),
            FALSE
        );
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Drop the trigger if it already exists
DROP TRIGGER IF EXISTS payslip_approval_trigger ON pay_slips;

-- Create the trigger
CREATE TRIGGER payslip_approval_trigger
AFTER UPDATE ON pay_slips
FOR EACH ROW
EXECUTE FUNCTION generate_message_on_payslip_approval();