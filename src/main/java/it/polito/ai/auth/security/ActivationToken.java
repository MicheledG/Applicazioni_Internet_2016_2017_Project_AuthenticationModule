package it.polito.ai.auth.security;

public class ActivationToken {
	
	private String token;
	
	public ActivationToken() {
	}
	
	public ActivationToken(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
