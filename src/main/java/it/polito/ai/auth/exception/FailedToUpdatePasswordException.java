package it.polito.ai.auth.exception;

public class FailedToUpdatePasswordException extends Exception {

	private static final long serialVersionUID = 3411523586343508393L;

	public FailedToUpdatePasswordException() {
        super("Failed to update password");
    }
	
}
