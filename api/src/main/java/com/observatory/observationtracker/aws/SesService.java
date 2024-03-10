package com.observatory.observationtracker.aws;

import com.observatory.observationtracker.configuration.services.ServicesConfig;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Service
public class SesService {
    private final ServicesConfig servicesConfig;
    private final String notificationSenderEmail;
    private final SesClient client;

    public SesService(ServicesConfig servicesConfig) {
        this.servicesConfig = servicesConfig;
        this.client = servicesConfig.sesClient();
        this.notificationSenderEmail = servicesConfig.getNotificationSenderEmail();
    }

    public void sendEmail(String subject, String body, String toEmail) {
        // Create email request with custom header
        SendEmailRequest request = SendEmailRequest.builder()
                .destination(Destination.builder().toAddresses(toEmail).build())
                .message(Message.builder()
                        .body(Body.builder()
                                .html(Content.builder().charset("UTF-8").data(body).build())
                                .build())
                        .subject(Content.builder().charset("UTF-8").data(subject).build())
                        .build())
                .source(notificationSenderEmail)
                .build();

        // Send email
        try {
            client.sendEmail(request);
            System.out.println("Email sent successfully!");
        } catch (SesException e) {
            System.out.println("Error sending email: " + e.getMessage());
        }

    }
}
