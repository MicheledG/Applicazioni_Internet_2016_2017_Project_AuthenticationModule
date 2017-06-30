package it.polito.ai.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {
	
	@Autowired
    public JavaMailSender emailSender;
	
	@Autowired
	public SimpleMailMessage template;

	@Override
	public void sendEmail(String mail, String token) {
		
		String text = String.format(template.getText(), token);  
		sendSimpleMessage(mail, "CinqueTi Account Activation", text);
		
	}

    public void sendSimpleMessage(String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setTo(to); 
        message.setSubject(subject); 
        message.setText(text);
        emailSender.send(message);

    }

}
