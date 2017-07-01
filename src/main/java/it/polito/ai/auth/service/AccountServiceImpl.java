package it.polito.ai.auth.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import it.polito.ai.auth.model.Account;
import it.polito.ai.auth.repository.AccountRepository;
import it.polito.ai.auth.security.JWTService;
import it.polito.ai.auth.security.LoginCredentials;
import it.polito.ai.auth.security.SignupCredentials;

@Service
public class AccountServiceImpl implements AccountService {
	
	private static final String REMOTE_PROFILE_ENDPOINT = "http://localhost:8083/profile";
	
	@Autowired
	private JWTService jwtService;
	
	@Autowired
	private MailService mailService;
	
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
		
		// Create a new account. For now the account is created already enabled, but later should be
		// created disabled until the user verifies it through a dedicated email.
		Account newAccount = new Account(credentials.getUsername(), credentials.getPassword());
		accountRepository.saveAndFlush(newAccount);
		
		// Create a profile for the new user
		if (!createProfile(credentials.getUsername(), credentials.getNickname())) {
			accountRepository.delete(newAccount);
			return false;
		}
		
		// Generate a token for account activation
		String token = jwtService.getToken(credentials.getUsername());
		
		// Send an email with the generated token
		mailService.sendEmail(credentials.getUsername(), token);
		
		return true;
	}
	
	/**
	 * Send a POST request to the Profile Module in order to create
	 * a new profile given a username and a nickname.
	 * 
	 * @param username
	 * @param nickname
	 * @return	True if succeed, false if failed
	 */
	private boolean createProfile(String username, String nickname) {
		
		// Create the request body with username and nickname
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("username", username);
		requestBody.put("nickname", nickname);
		
		// Send the profile creation request to the Profile module
		RestTemplate restTemplate = new RestTemplate();
		
		try {
			// POST request
			restTemplate.postForObject(
					REMOTE_PROFILE_ENDPOINT,
					requestBody,
					String.class
			);
		} catch (Exception e) {
			// Error getting data from the Profile module
			return false;
		}
		
		return true;
	}
	
	@Override
	public String getUsernameFromToken(String token) {
		
		String username;
		
		// The token field is empty
		if (token == null) {
			return null;
		}
		
		try {
			username = jwtService.getUsername(token);
		} catch(Exception e) {
			return null;
		}
		
		return username;
	}

	@Override
	public void updatePassword(String username, String password) {
		Account account = accountRepository.findOneByUsername(username);
		account.setPassword(password);

		accountRepository.save(account);
		
		return;
	}

	@Override
	public boolean activate(String token) {
		// Verify token
		String username = jwtService.getUsername(token);
		
		// Token not valid
		if (username == null) {
			return false;
		}
		
		// If valid, enable account
		Account account = accountRepository.findOneByUsername(username);
		account.setEnabled(true);
		accountRepository.save(account);
		
		return false;
	}

}
