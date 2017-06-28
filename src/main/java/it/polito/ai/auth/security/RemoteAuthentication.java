package it.polito.ai.auth.security;

public class RemoteAuthentication {
	
	private String username;
	
	public RemoteAuthentication() {
	}
	
	public RemoteAuthentication(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
