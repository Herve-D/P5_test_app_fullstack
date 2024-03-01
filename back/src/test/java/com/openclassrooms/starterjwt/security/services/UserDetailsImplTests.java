package com.openclassrooms.starterjwt.security.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserDetailsImplTests {

	@Test
	public void equals() {
		UserDetailsImpl userDetailsImpl = UserDetailsImpl.builder().build();
		assertEquals(userDetailsImpl, userDetailsImpl);
	}

	@Test
	public void equalsNull() {
		UserDetailsImpl userDetailsImpl = UserDetailsImpl.builder().build();
		assertFalse(userDetailsImpl == null);
	}

	@Test
	public void equalsOtherObject() {
		UserDetailsImpl userDetailsImpl = UserDetailsImpl.builder().build();
		assertFalse(userDetailsImpl.equals(new Object()));
	}

}
