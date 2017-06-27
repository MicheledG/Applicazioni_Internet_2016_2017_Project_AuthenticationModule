package it.polito.ai.authenticationmodule.security;

import static java.util.Collections.emptyList;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class JWTServiceImpl implements JWTService {
	
	@Override
	public Authentication getAuthentication(String token) {

		// Parse the token, verify it and get the username
		String username = getUsername(token);
		
		if (username != null) {
			// The object that can be read from the SecurityContext by the REST controllers
			// in order to get information about the logged user.
			return new UsernamePasswordAuthenticationToken(username, token, emptyList());
		} else {
			return null;
		}

	}
	
	@Override
	public void addAuthentication(HttpServletResponse res, String username) {
		
		String JWT = JWTHandler.build(username);
		
		res.addHeader(JWTHandler.HEADER_STRING, JWTHandler.TOKEN_PREFIX + " " + JWT);

	}

	@Override
	public String getUsername(String token) {
		// Parse the token, verify it and get the username
		String username = JWTHandler.parse(token);		
		
		return username;
	}

}
