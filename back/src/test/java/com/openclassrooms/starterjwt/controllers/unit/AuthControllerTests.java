package com.openclassrooms.starterjwt.controllers.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

@SpringBootTest
public class AuthControllerTests {

	@Mock
	AuthenticationManager authenticationManager;

	@Mock
	JwtUtils jwtUtils;

	@Mock
	PasswordEncoder passwordEncoder;

	@Mock
	UserRepository userRepository;

	private AuthController authController;

	private final String email = "mail@mail.com";
	private final String password = "password";
	private final String firstName = "first";
	private final String lastName = "last";
	boolean isAdmin = false;

	@BeforeEach
	public void setup() {
		authController = new AuthController(authenticationManager, passwordEncoder, jwtUtils, userRepository);
	}

	@Test
	public void authenticateUser() {
		Long id = 1L;

		UserDetailsImpl user = UserDetailsImpl.builder().username(email).firstName(firstName).lastName(lastName).id(id)
				.password(password).build();

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null);

		when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password)))
				.thenReturn(authentication);
		when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwt");
		when(userRepository.findByEmail(email)).thenReturn(Optional.of(User.builder().id(id).email(email)
				.password(password).firstName(firstName).lastName(lastName).admin(isAdmin).build()));

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail(email);
		loginRequest.setPassword(password);

		ResponseEntity<?> response = authController.authenticateUser(loginRequest);
		JwtResponse responseBody = (JwtResponse) response.getBody();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(email, responseBody.getUsername());
		assertEquals(firstName, responseBody.getFirstName());
		assertEquals(lastName, responseBody.getLastName());
		assertEquals(id, responseBody.getId());
		assertEquals(isAdmin, responseBody.getAdmin());
		assertEquals("Bearer", responseBody.getType());
		assertNotNull(responseBody.getToken());
	}

	@Test
	public void registerUser() {
		when(userRepository.existsByEmail(email)).thenReturn(false);
		when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
		when(userRepository.save(User.builder().email(email).password("encodedPassword").firstName(firstName).lastName(lastName).admin(isAdmin).build())).thenReturn(User.builder().email(email).password("encodedPassword").firstName(firstName).lastName(lastName).admin(isAdmin).build());
		
		SignupRequest signupRequest=new SignupRequest() ;
		signupRequest.setEmail(email);
		signupRequest.setPassword(password);
		signupRequest.setFirstName(firstName);
		signupRequest.setLastName(lastName);
		ResponseEntity<?> response = authController.registerUser(signupRequest);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void registerEmailTaken() {
		when(userRepository.existsByEmail(email)).thenReturn(true);
		
		SignupRequest signupRequest=new SignupRequest() ;
		signupRequest.setEmail(email);
		signupRequest.setPassword(password);
		signupRequest.setFirstName(firstName);
		signupRequest.setLastName(lastName);
		ResponseEntity<?> response = authController.registerUser(signupRequest);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

}
