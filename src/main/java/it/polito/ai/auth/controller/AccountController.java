package it.polito.ai.auth.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.polito.ai.auth.exception.FailedToAuthenticateException;
import it.polito.ai.auth.exception.FailedToLoginException;
import it.polito.ai.auth.exception.FailedToSignupException;
import it.polito.ai.auth.exception.FailedToUpdatePasswordException;
import it.polito.ai.auth.security.LoginCredentials;
import it.polito.ai.auth.security.Password;
import it.polito.ai.auth.security.RemoteAuthentication;
import it.polito.ai.auth.security.SignupCredentials;
import it.polito.ai.auth.service.AccountService;

/**
 * RestController for handling accounts: 
 * - user authentication (login); 
 * - user registration (signup);
 * - account activation (verify);
 * - token verification (authenticate).
 */
@RestController
@CrossOrigin(origins="*", exposedHeaders="Authorization")
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
	@RequestMapping(value = "/verify", method = RequestMethod.GET)
	public void verify(@RequestParam String token) throws FailedToSignupException {
		if (!accountService.verify(token)) {
			throw new FailedToSignupException();
		}
	}

	/**
	 * Given a token, verify it and return the remote authentication.
	 * 
	 * @param requestBody
	 * @return
	 * @throws FailedToAuthenticateException
	 */
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<RemoteAuthentication> authenticate(@RequestBody Map<String, String> requestBody) throws FailedToAuthenticateException {
		
		String token = requestBody.get("token");
		
		String username = accountService.getUsernameFromToken(token);
		
		if (username == null) {
			throw new FailedToAuthenticateException();
		}
		
		RemoteAuthentication remoteAuthentication = new RemoteAuthentication(username);
		
		return new ResponseEntity<RemoteAuthentication>(remoteAuthentication, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/password", method = RequestMethod.PUT)
	public void updatePassword(@RequestBody Password password) throws FailedToUpdatePasswordException {
		
		// Get the username of the current logged user
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		
		// Update the password of the current logged user
		if (!accountService.updatePassword(username, password.getPassword())) {
			throw new FailedToUpdatePasswordException();
		}
		
	}
}
