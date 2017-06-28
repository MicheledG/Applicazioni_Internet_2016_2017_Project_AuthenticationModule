package it.polito.ai.auth.security;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;

public interface JWTService {

	/**
	 * The JWTAuthenticationProvider calls this method to verify the user authentication.
	 * If the token is not valid, the authentication fails and the request will be refused.
	 * 
	 * @param token	An authentication token to verify.
	 * @return
	 * @throws URISyntaxException 
	 * @throws IOException 
	 */
	Authentication getAuthentication(String token) throws IOException, URISyntaxException;

	/**
	 * When a user successfully logs into the application, create a token for that user.
	 * 
	 * @param res		An http response that will be filled with an 'Authentication' header containing the token.
	 * @param username	The username mapped to the user.
	 * @throws URISyntaxException 
	 * @throws IOException 
	 */
	void addAuthentication(HttpServletResponse res, String username) throws IOException, URISyntaxException;
	
	/**
	 * Verify a token and returns the relative username.
	 * 
	 * @param token
	 * @return
	 */
	String getUsername(String token);

}
