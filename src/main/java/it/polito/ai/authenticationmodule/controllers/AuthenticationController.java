package it.polito.ai.authenticationmodule.controllers;

import java.io.UnsupportedEncodingException;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import it.polito.ai.authenticationmodule.model.Credentials;

@RestController
public class AuthenticationController {
	
	/*
	 * Simple test method which create a token starting from the provided credentials
	 */
	@RequestMapping(value="/signin", method=RequestMethod.POST)
	public String signin(@RequestBody Credentials credentials){
		
		System.out.println("user: "+credentials.getUsername());
		System.out.println("password: "+credentials.getPassword());
		
		
		Algorithm algorithm;
		String token;
		try {
			algorithm = Algorithm.HMAC256("hellone");
			token = JWT.create()
					.withSubject(credentials.getUsername())
					.sign(algorithm);
		} catch (IllegalArgumentException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			token = e.getMessage();
		}
		
		return token;
	}
	
}
