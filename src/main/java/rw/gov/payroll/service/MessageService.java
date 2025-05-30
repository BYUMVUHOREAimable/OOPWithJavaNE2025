package rw.gov.payroll.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.gov.payroll.model.Employee;
import rw.gov.payroll.model.Message;
import rw.gov.payroll.model.PaySlip;
import rw.gov.payroll.repository.MessageRepository;
import rw.gov.payroll.repository.PaySlipRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private PaySlipRepository paySlipRepository;

    @Autowired
    private JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${institution.name:Rwanda Coding Academy}")
    private String institutionName;

    /**
     * Generate a message for a pay slip
     */
    @Transactional
    public Message generateMessage(PaySlip paySlip) {
        Employee employee = paySlip.getEmployee();

        // Format the message
        String messageContent = String.format(
                "Dear %s, Your salary of %s/%s from %s %s has been credited to your %s account successfully.",
                employee.getFirstName(),
                Month.of(paySlip.getMonth()).name(),
                paySlip.getYear(),
                institutionName,
                paySlip.getNetSalary(),
                employee.getCode()
        );

        // Create and save the message
        Message message = new Message();
        message.setEmployee(employee);
        message.setContent(messageContent);
        message.setMonth(paySlip.getMonth());
        message.setYear(paySlip.getYear());
        message.setCreatedAt(LocalDateTime.now());
        message.setSent(false);

        return messageRepository.save(message);
    }

    /**
     * Generate messages for all approved pay slips
     */
    @Transactional
    public void generateMessagesForApprovedPaySlips(List<PaySlip> approvedPaySlips) {
        for (PaySlip paySlip : approvedPaySlips) {
            generateMessage(paySlip);
        }
    }

    /**
     * Send email notification for a message
     */
    public void sendEmailNotification(Message message) {
        try {
            Employee employee = message.getEmployee();

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(fromEmail);
            mailMessage.setTo(employee.getEmail());
            mailMessage.setSubject("Salary Payment Notification");
            mailMessage.setText(message.getContent());

            emailSender.send(mailMessage);

            // Update message as sent
            message.setSent(true);
            messageRepository.save(message);
        } catch (Exception e) {
            // Log the error but don't rethrow it to ensure the transaction completes
            System.err.println("Error sending email notification: " + e.getMessage());
        }
    }

    /**
     * Scheduled task to send unsent messages
     * Runs every hour
     */
    @Scheduled(fixedRate = 3600000) // 1 hour in milliseconds
    @Transactional
    public void sendUnsentMessages() {
        try {
            List<Message> unsentMessages = messageRepository.findBySent(false);
            System.out.println("Found " + unsentMessages.size() + " unsent messages to process");

            for (Message message : unsentMessages) {
                try {
                    System.out.println("Sending message ID: " + message.getId() + " to employee: " + message.getEmployee().getCode());
                    sendEmailNotification(message);
                    System.out.println("Successfully sent message ID: " + message.getId());
                } catch (Exception e) {
                    System.err.println("Error sending message ID: " + message.getId() + " - " + e.getMessage());
                    // Continue with the next message
                }
            }
        } catch (Exception e) {
            System.err.println("Error in sendUnsentMessages: " + e.getMessage());
        }
    }

    /**
     * Get all messages for an employee
     */
    public List<Message> getMessagesByEmployee(Employee employee) {
        return messageRepository.findByEmployee(employee);
    }

    /**
     * Get messages for a specific month and year
     */
    public List<Message> getMessagesByMonthAndYear(Integer month, Integer year) {
        return messageRepository.findByMonthAndYear(month, year);
    }
}
