
package com.shreyashkadam.bookstore.service;

import com.shreyashkadam.bookstore.model.Order;
import com.shreyashkadam.bookstore.model.OrderItem;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOrderReceipt(String toEmail, Order order, List<OrderItem> items) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("📚 BookStore - Order Receipt #" + order.getId());

        StringBuilder body = new StringBuilder();
        body.append("Thank you for your purchase!\n\n");
        body.append("Order ID: ").append(order.getId()).append("\n\n");

        body.append("Items:\n");
        for (OrderItem item : items) {
            body.append("Book ID: ").append(item.getBookId())
                    .append(" | Quantity: ").append(item.getQuantity())
                    .append(" | Price: ₹").append(item.getPrice())
                    .append("\n");
        }

        body.append("\nTotal Amount: ₹").append(order.getTotalAmount());
        body.append("\n\nHappy Reading!");
        body.append("\nBookStore Team");

        message.setText(body.toString());

        mailSender.send(message);
    }
}
