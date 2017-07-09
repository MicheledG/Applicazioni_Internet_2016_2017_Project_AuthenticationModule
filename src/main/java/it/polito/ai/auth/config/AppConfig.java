package it.polito.ai.auth.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan(basePackages = "it.polito.ai")
@EnableWebMvc
@EnableAsync
public class AppConfig {
	
	/**
	 * Configure the application to send email through GMail
	 * 
	 * @return
	 */
	@Bean
	public JavaMailSender getJavaMailSender() {
	    
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost("smtp.gmail.com");
	    mailSender.setPort(587);
	    mailSender.setUsername("cinqueti.ai.polito@gmail.com");
	    mailSender.setPassword("AI20162017CinqueTi");
	     
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");
	     
	    return mailSender;
	}
	
	/**
	 * Set a template for the email to send to new users.
	 * 
	 * @return
	 */
	@Bean
    public SimpleMailMessage templateSimpleMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText("Click on the following link in order to activate your account:\n"
        		+ "http://localhost:63342/InternetApplications_Project_WebClient/#!/activate" + "?token=%s\n");
        return message;
    }

}
