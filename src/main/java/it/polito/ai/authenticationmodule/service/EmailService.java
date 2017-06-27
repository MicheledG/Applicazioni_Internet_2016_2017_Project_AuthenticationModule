package it.polito.ai.authenticationmodule.service;

import org.springframework.mail.SimpleMailMessage;

public interface EmailService {
	
	void sendSimpleMessage(String to,
            String subject,
            String text);
	
	void sendSimpleMessageUsingTemplate(String to,
	                         String subject,
	                         SimpleMailMessage template,
	                         String ...templateArgs);

}
