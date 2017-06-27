package it.polito.ai.authenticationmodule.account.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.polito.ai.authenticationmodule.account.model.Account;
import it.polito.ai.authenticationmodule.account.repository.AccountRepository;
import it.polito.ai.authenticationmodule.security.JWTService;
import it.polito.ai.authenticationmodule.security.LoginCredentials;
import it.polito.ai.authenticationmodule.security.SignupCredentials;

@Service
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	private JWTService jwtService;
	
	@Autowired
	private AccountRepository accountRepository;

	@Override
	public boolean login(LoginCredentials credentials, HttpServletResponse response) {
		
		Account account = accountRepository.findOneByUsername(credentials.getUsername());
		
		// If the username doesn't exist
		if (account == null) {
			return false;
		}
		
		// If the password is wrong
		if (!account.getPassword().equals(credentials.getPassword())) {
			return false;
		}
		
		// If the account is not active (activation is done through a link sent by email)
		if (!account.isEnabled()) {
			return false;
		}
		
		// Generate a token and add it to the response as Authorization header
		try {
			jwtService.addAuthentication(response, account.getUsername());
		} catch (Exception e) {
			return false;
		} 
		
		return true;
	}

	@Override
	public boolean signup(SignupCredentials credentials) {
		
		Account account = accountRepository.findOneByUsername(credentials.getUsername());
		
		// An account with that username already exists
		if (account != null) {
			return false;
		}
		
		// The password or the nickname fields are empty
		if (credentials.getPassword().isEmpty() || credentials.getNickname().isEmpty()) {
			return false;
		}
		
		// Create a new account. For now the account is created already enabled, but later should be
		// created disabled until the user verifies it through a dedicated email.
		Account newAccount = new Account(credentials.getUsername(), credentials.getPassword());
		accountRepository.saveAndFlush(newAccount);
		
		// TODO IN AN OTHER WAY E.G. USING PROFILEMODULE API
		//Profile newProfile = new Profile(credentials.getUsername(), credentials.getNickname());
		//profileService.addProfile(newProfile);
		
		// Here you should generate a unique and temporary token and call an external service for sending 
		// an email to the user in order to activate his/her account. The email will contain a url to click,
		// having the token as query parameter. After having verified that token, the account status will
		// be set to 'enabled'.
		
		return true;
	}
	
	
//	@Override
//	public String getNickname(String username) {
//		//TODO IN AN OTHER WAY E.G. USING PROFILEMODULE API OR DELETING THE NEEDS TO HAVE NICKNAME RIGHT HERE
//		//return profileService.getNickname(username);
//		/*
//		 * DEBUG
//		 */
//		return "ciccio"; 
//		
//		//return null;
//	}
	
	@Override
	public String getUsernameFromToken(String token) {
		String username;
		
		try {
			username = jwtService.getUsername(token);
		} catch(Exception e) {
			return null;
		}
		
		return username;
	}

}
