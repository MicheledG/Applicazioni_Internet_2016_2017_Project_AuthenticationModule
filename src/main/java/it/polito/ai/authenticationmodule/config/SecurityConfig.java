package it.polito.ai.authenticationmodule.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import it.polito.ai.authenticationmodule.security.JWTAuthenticationEntryPoint;
import it.polito.ai.authenticationmodule.security.JWTAuthenticationFilter;
import it.polito.ai.authenticationmodule.security.JWTAuthenticationProvider;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
    private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Autowired
	private JWTAuthenticationFilter jwtAuthenticationFilter;
	
	@Autowired
    private JWTAuthenticationProvider jwtAuthenticationProvider;
	
	@Override
    public void configure(AuthenticationManagerBuilder auth)  throws Exception {
		// Define a custom authentication provider
        auth.authenticationProvider(jwtAuthenticationProvider);
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			// Disable CSRF protection since tokens are immune to it
			.csrf().disable()
			// If the user is not authenticated, returns 401
			.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
			// This is a stateless application, disable sessions
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			// Security policy
			.authorizeRequests()
				// Allow anonymous access to "/" path and static resources
				.antMatchers("/").permitAll()
				.antMatchers("/app/**/*").permitAll()
				.antMatchers("/bower_components/**/*").permitAll()
				.antMatchers("/index.html").permitAll()
				// Allow anonymous access to "/login" and "/signup" (only POST requests)
				.antMatchers(HttpMethod.POST, "/login").permitAll()
				.antMatchers(HttpMethod.POST, "/signup").permitAll()
				//TODO: may be it is better to add authentication of a microservice to use this endpoint
				.antMatchers(HttpMethod.POST, "/authenticate").permitAll()
				// Allow anonymous access to "/verify" (only GET requests)
				.antMatchers(HttpMethod.GET, "/verify").permitAll()
				// Any other request must be authenticated
				.anyRequest().authenticated().and()
			// Custom filter for authenticating users using tokens
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
			// Disable resource caching, enable only if the client app is external to this modoule
			// .headers().cacheControl();
	}	

}
