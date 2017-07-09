package it.polito.ai.auth.service;

import org.springframework.scheduling.annotation.Async;

public interface MailService {

	/**
	 * Send an email to a new registered user with a link
	 * to activate his/her account.
	 * 
	 * @param mail
	 * @param token
	 */
	@Async
	void sendEmail(String mail, String token);

}
