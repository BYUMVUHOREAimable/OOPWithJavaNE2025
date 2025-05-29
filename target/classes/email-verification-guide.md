# Email Verification Guide

This guide explains how to verify that emails are being sent and saved correctly in the Payroll Management System.

## Understanding the Email Process

In the Payroll Management System, emails are:

1. **Generated** when payslips are approved
2. **Saved** to the database with a `sent=false` status
3. **Sent** either immediately or by a scheduled task that runs every hour
4. **Updated** in the database with `sent=true` status after successful delivery

## Verifying Email Configuration

To verify that your email configuration is working correctly:

1. Send a test email using the API endpoint:
   ```
   POST /api/messages/test-email?email=your.email@example.com
   ```

   This will:
   - Create a test message
   - Send it to the specified email address
   - Return a response indicating success or failure

2. Check your email inbox to confirm receipt of the test email

## Checking Saved Messages in the Database

To verify that messages are being saved correctly in the database, use these API endpoints:

1. Get all messages:
   ```
   GET /api/messages
   ```

2. Get messages by sent status (true/false):
   ```
   GET /api/messages/status/false  # Get unsent messages
   GET /api/messages/status/true   # Get sent messages
   ```

3. Get messages for a specific employee:
   ```
   GET /api/messages/employee/{employeeCode}
   ```

4. Get messages for a specific month and year:
   ```
   GET /api/messages/period?month=5&year=2023
   ```

## Forcing Delivery of Unsent Messages

If you want to force the delivery of all unsent messages (instead of waiting for the hourly scheduled task):

```
POST /api/messages/send-unsent
```

This will trigger the sending of all messages with `sent=false` status and update them to `sent=true` after successful delivery.

## Troubleshooting

If emails are not being sent:

1. Check the email configuration in `application.properties`:
   - Verify SMTP host, port, username, and password
   - Ensure authentication and TLS are enabled

2. Check for any exceptions in the application logs

3. Verify that the email account being used to send emails has the necessary permissions and doesn't have security restrictions that might block automated emails

4. If using Gmail, ensure that "Less secure app access" is enabled or use an App Password