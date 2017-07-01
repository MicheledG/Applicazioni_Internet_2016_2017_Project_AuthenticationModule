package it.polito.ai.auth.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.polito.ai.auth.exception.FailedToAuthenticateException;
import it.polito.ai.auth.exception.FailedToLoginException;
import it.polito.ai.auth.exception.FailedToSignupException;
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
	@ResponseStatus(code = HttpStatus.OK)
	public void login(@Validated @RequestBody LoginCredentials credentials, HttpServletResponse response) throws FailedToLoginException {
		
		// If the login credentials fails validation => 400
		
		// If the login credentials are wrong => 401
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
	@ResponseStatus(code = HttpStatus.OK)
	public void signup(@Validated @RequestBody SignupCredentials credentials, HttpServletResponse response) throws FailedToSignupException {
		
		// If the profile fails validation => 400
		
		// If an account with the same username already exists => 409
		if (!accountService.signup(credentials)) {
			throw new FailedToSignupException();
		}

	}
	
	/**
	 * Activate the account of a new registered user after verifying the token.
	 * 
	 * @param token
	 */
	@RequestMapping(value = "/activate", method = RequestMethod.GET)
	@ResponseStatus(code = HttpStatus.OK)
	public void activate(@RequestParam String token) throws FailedToSignupException {
		
		// If account activation failes => 400
		if (!accountService.activate(token)) {
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
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<RemoteAuthentication> authenticate(@RequestBody Map<String, String> requestBody) throws FailedToAuthenticateException {
		
		String token = requestBody.get("token");
		
		String username = accountService.getUsernameFromToken(token);
		
		// If authentication fails => 401
		if (username == null) {
			throw new FailedToAuthenticateException();
		}
		
		RemoteAuthentication remoteAuthentication = new RemoteAuthentication(username);
		
		return new ResponseEntity<RemoteAuthentication>(remoteAuthentication, HttpStatus.OK);
		
	}
	
	/**
	 * Update the password of the currently logged user.
	 * 
	 * @param password
	 * @throws FailedToUpdatePasswordException
	 */
	@RequestMapping(value = "/password", method = RequestMethod.PUT)
	@ResponseStatus(code = HttpStatus.OK)
	public void updatePassword(@Validated @RequestBody Password password) {
		
		// If the password fails validation => 400
		
		// Get the username of the current logged user
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		
		// Update the password of the current logged user
		accountService.updatePassword(username, password.getPassword());
		
	}
}
