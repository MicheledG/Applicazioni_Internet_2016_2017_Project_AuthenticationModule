package it.polito.ai.auth.security;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class SignupCredentials {

	@NotEmpty
	@Email
	private String username;
	
	@NotEmpty
	@Size(min=8, max=16)
	private String password;
	
	@NotEmpty
	@Pattern(regexp = "[a-z-A-Z_]*")
	private String nickname;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
}
