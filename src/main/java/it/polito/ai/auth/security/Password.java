package it.polito.ai.auth.security;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class Password {
	
	@NotEmpty
	@Size(min=8, max=16)
	private String password;
	
	public Password() {
	}
	
	public Password(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
