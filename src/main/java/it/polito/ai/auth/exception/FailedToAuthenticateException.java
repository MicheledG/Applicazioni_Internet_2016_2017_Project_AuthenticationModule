package it.polito.ai.auth.exception;

import static java.lang.String.format;

public class FailedToAuthenticateException extends Exception {

	private static final long serialVersionUID = -4054597124848897187L;

	public FailedToAuthenticateException() {
        super(format("Failed to authenticate"));
    }
	
}
