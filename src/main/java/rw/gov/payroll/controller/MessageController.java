package rw.gov.payroll.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rw.gov.payroll.dto.ApiResponse;
import rw.gov.payroll.model.Employee;
import rw.gov.payroll.model.Message;
import rw.gov.payroll.repository.EmployeeRepository;
import rw.gov.payroll.repository.MessageRepository;
import rw.gov.payroll.service.MessageService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private MessageService messageService;

    /**
     * Get all messages
     */
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> getAllMessages() {
        List<Message> messages = messageRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success("Messages retrieved successfully", messages));
    }

    /**
     * Get messages by sent status
     */
    @GetMapping("/status/{sent}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> getMessagesBySentStatus(@PathVariable Boolean sent) {
        List<Message> messages = messageRepository.findBySent(sent);
        return ResponseEntity.ok(ApiResponse.success(
                "Messages with sent status " + sent + " retrieved successfully", 
                messages));
    }

    /**
     * Get messages for a specific employee
     */
    @GetMapping("/employee/{employeeCode}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or @securityService.isCurrentUser(#employeeCode)")
    public ResponseEntity<?> getMessagesByEmployee(@PathVariable String employeeCode) {
        Optional<Employee> employeeOpt = employeeRepository.findById(employeeCode);
        if (!employeeOpt.isPresent()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Employee not found"));
        }

        List<Message> messages = messageRepository.findByEmployee(employeeOpt.get());
        return ResponseEntity.ok(ApiResponse.success("Messages for employee retrieved successfully", messages));
    }

    /**
     * Get messages for a specific month and year
     */
    @GetMapping("/period")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> getMessagesByPeriod(
            @RequestParam Integer month,
            @RequestParam Integer year) {
        List<Message> messages = messageRepository.findByMonthAndYear(month, year);
        return ResponseEntity.ok(ApiResponse.success("Messages for period retrieved successfully", messages));
    }

    /**
     * Send a test email to verify email configuration
     */
    @PostMapping("/test-email")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> sendTestEmail(@RequestParam String email) {
        try {
            // Create a test message
            Message testMessage = new Message();
            testMessage.setContent("This is a test email from the Payroll Management System to verify email configuration.");
            testMessage.setMonth(java.time.LocalDate.now().getMonthValue());
            testMessage.setYear(java.time.LocalDate.now().getYear());

            // Find an employee or create a temporary one for the test
            Optional<Employee> employeeOpt = employeeRepository.findByEmail(email);
            Employee employee;

            if (employeeOpt.isPresent()) {
                employee = employeeOpt.get();
            } else {
                // Create a temporary employee object (not saved to DB)
                employee = new Employee();
                employee.setEmail(email);
                employee.setFirstName("Test");
                employee.setLastName("User");
            }

            testMessage.setEmployee(employee);

            // Send the email
            messageService.sendEmailNotification(testMessage);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Test email sent successfully to " + email);
            response.put("emailContent", testMessage.getContent());

            return ResponseEntity.ok(ApiResponse.success("Test email sent", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to send test email: " + e.getMessage()));
        }
    }

    /**
     * Force sending of all unsent messages
     */
    @PostMapping("/send-unsent")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> sendUnsentMessages() {
        try {
            List<Message> unsentMessages = messageRepository.findBySent(false);
            messageService.sendUnsentMessages();

            Map<String, Object> response = new HashMap<>();
            response.put("unsentMessagesCount", unsentMessages.size());

            return ResponseEntity.ok(ApiResponse.success("Triggered sending of unsent messages", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to send unsent messages: " + e.getMessage()));
        }
    }
}
