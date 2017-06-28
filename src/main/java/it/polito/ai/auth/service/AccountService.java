package it.polito.ai.auth.service;

import javax.servlet.http.HttpServletResponse;

import it.polito.ai.auth.security.LoginCredentials;
import it.polito.ai.auth.security.SignupCredentials;

public interface AccountService {

	public boolean login(LoginCredentials credentials, HttpServletResponse response);

	public boolean signup(SignupCredentials credentials);

	//public String getNickname(String username);
	
	public String getUsernameFromToken(String token);
	
}
