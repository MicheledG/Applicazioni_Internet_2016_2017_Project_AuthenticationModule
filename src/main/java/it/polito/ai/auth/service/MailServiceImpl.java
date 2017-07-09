package it.polito.ai.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
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
		
		System.err.println("Mail: " + mail);
		String text = String.format(template.getText(), token);  
		System.err.println("Text: " + text);
		sendSimpleMessage(mail, "CinqueTi Account Activation", text);
		System.err.println("Mail sent");
	}

	/** 
	 * Send an email.
	 * 
	 * @param to
	 * @param subject
	 * @param text
	 */
    private void sendSimpleMessage(String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setTo(to); 
        message.setSubject(subject); 
        message.setText(text);
        
        try {
			emailSender.send(message);
		} catch (MailException e) {
			System.err.println("Mail failed");
			e.printStackTrace();
		}

    }

}
