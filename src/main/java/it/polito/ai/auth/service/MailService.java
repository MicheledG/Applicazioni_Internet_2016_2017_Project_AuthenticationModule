package it.polito.ai.auth.service;

import org.springframework.scheduling.annotation.Async;

public interface MailService {

	@Async
	void sendEmail(String mail, String token);

}
