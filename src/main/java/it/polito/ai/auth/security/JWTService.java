package it.polito.ai.auth.security;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;

public interface JWTService {

	/**
	 * The JWTAuthenticationProvider calls this method to verify the user authentication.
	 * If the token is not valid, the authentication fails and the request will be refused.
	 * 
	 * @param token	An authentication token to verify.
	 * @return
	 */
	Authentication getAuthentication(String token);

	/**
	 * When a user successfully logs into the application, create a token for that user.
	 * 
	 * @param res		An http response that will be filled with an 'Authentication' header containing the token.
	 * @param username	The username mapped to the user.
	 */
	void addAuthentication(HttpServletResponse res, String username);
	
	/**
	 * Verify a token and returns the relative username.
	 * 
	 * @param token
	 * @return
	 */
	String getUsername(String token);

}
