package it.polito.ai.authenticationmodule.service;

import javax.servlet.http.HttpServletResponse;

import it.polito.ai.authenticationmodule.security.LoginCredentials;
import it.polito.ai.authenticationmodule.security.SignupCredentials;

public interface AccountService {

	public boolean login(LoginCredentials credentials, HttpServletResponse response);

	public boolean signup(SignupCredentials credentials);

	//public String getNickname(String username);
	
	public String getUsernameFromToken(String token);
	
}
