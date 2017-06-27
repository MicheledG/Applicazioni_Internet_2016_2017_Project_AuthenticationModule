package it.polito.ai.authenticationmodule.account.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.polito.ai.authenticationmodule.account.service.AccountService;
import it.polito.ai.authenticationmodule.exception.FailedToLoginException;
import it.polito.ai.authenticationmodule.exception.FailedToSignupException;
import it.polito.ai.authenticationmodule.security.LoginCredentials;
import it.polito.ai.authenticationmodule.security.SignupCredentials;
import it.polito.ai.authenticationmodule.exception.FailedToAuthenticate;

/**
 * RestController for handling accounts: 
 * - user authentication (login); 
 * - user registration (signup);
 * - account activation (verify);
 * - token verification (authenticate).
 */
@RestController
public class AccountController {
	
	@Autowired
	private AccountService accountService;
	
	/**
	 * Authenticate a user by checking his/her credentials (username and password)
	 * and returning a JWT token inside the http response (Authorize header).
	 * 
	 * @param credentials
	 * @param response
	 * @throws FailedToLoginException
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public void login(@RequestBody LoginCredentials credentials, HttpServletResponse response) throws FailedToLoginException {
		
		if (!accountService.login(credentials, response)) {
			throw new FailedToLoginException(credentials.getUsername());
		}

	}
	
	/**
	 * Register a new user into the system.
	 * 
	 * @param credentials
	 * @param response
	 * @throws FailedToSignupException
	 */
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public void signup(@RequestBody SignupCredentials credentials, HttpServletResponse response) throws FailedToSignupException {
		
		if (!accountService.signup(credentials)) {
			throw new FailedToSignupException();
		}

	}
	
	/**
	 * Activate the account of a new registered user after verifying the token.
	 * 
	 * @param token
	 */
	@RequestMapping(value = "verify", method = RequestMethod.GET)
	public void verify(@RequestParam String token) {
		// Verify the token.
		// If valid, enable the relative account.
	}
	
	/**
	 * Given a token, verify it and return the relative username.
	 * 
	 * @param token
	 * @return
	 * @throws FailedToAuthenticate
	 */
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> authenticate(@RequestBody Map<String, String> requestBody) throws FailedToAuthenticate {
		
		String token = requestBody.get("token");
		if (token == null) {
			throw new FailedToAuthenticate();
		}
		
		String username = accountService.getUsernameFromToken(token);
		
		System.err.print("Username is: ");
		System.err.println(username);
		
		if (username == null) {
			throw new FailedToAuthenticate();
		}
		
		Map<String, String> response = new HashMap<>();
		response.put("username", username);
		
		return new ResponseEntity<Map<String,String>>(response, HttpStatus.OK);
	}
	

}
