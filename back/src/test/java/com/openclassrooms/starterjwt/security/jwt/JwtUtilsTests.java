package com.openclassrooms.starterjwt.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

@SpringBootTest
public class JwtUtilsTests {

	@Autowired
	private JwtUtils jwtUtils;

	private UserDetails userDetails;

	@BeforeEach
	public void setup() {
		userDetails = new UserDetailsImpl(null, "user", "first", "last", null, "password");
	}

	@Test
	public void generateJwtToken() {
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
		String token = jwtUtils.generateJwtToken(authentication);

		assertFalse(token.isEmpty());
		assertEquals("user", jwtUtils.getUserNameFromJwtToken(token));
		assertTrue(jwtUtils.validateJwtToken(token));
	}

	@Test
	public void validateJwtToken() {
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
		String token = jwtUtils.generateJwtToken(authentication);

		boolean isValid = jwtUtils.validateJwtToken(token);
		assertTrue(isValid);
	}

	@Test
	public void invalidToken() {
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
		String token = jwtUtils.generateJwtToken(authentication);

		String invalidToken = token + "error";
		boolean isValid = jwtUtils.validateJwtToken(invalidToken);
		assertFalse(isValid);
	}

	@Test
	public void expiredToken() {
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
		ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 0);
		String token = jwtUtils.generateJwtToken(authentication);

		boolean isValid = jwtUtils.validateJwtToken(token);
		assertFalse(isValid);
	}

	@Test
	public void malformedToken() {
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
		String token = jwtUtils.generateJwtToken(authentication);

		String malformedToken = token.substring(0, 10);
		boolean isValid = jwtUtils.validateJwtToken(malformedToken);
		assertFalse(isValid);
	}

	@Test
	public void emptyToken() {
		String emptyToken = "";
		boolean isValid = jwtUtils.validateJwtToken(emptyToken);
		assertFalse(isValid);
	}

}
