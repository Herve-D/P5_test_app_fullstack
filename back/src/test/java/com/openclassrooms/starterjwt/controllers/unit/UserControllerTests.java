package com.openclassrooms.starterjwt.controllers.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.UserService;

@SpringBootTest
public class UserControllerTests {

	@Mock
	private UserMapper userMapper;

	@Mock
	private UserService userService;

	@InjectMocks
	private UserController userController;

	@BeforeEach
	public void setup() {
		userController = new UserController(userService, userMapper);
	}

	@Test
	public void getById() {
		Long id = 1L;
		User user = new User();
		user.setId(id);
		UserDto userDto = new UserDto();
		userDto.setId(id);

		when(userService.findById(id)).thenReturn(user);
		when(userMapper.toDto(user)).thenReturn(userDto);

		ResponseEntity<?> response = userController.findById(id.toString());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(response.getBody(), userDto);
	}

	@Test
	public void getByIdNotFound() {
		Long id = 1L;
		when(userService.findById(id)).thenReturn(null);

		ResponseEntity<?> response = userController.findById(id.toString());

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	public void save() {
		Long id = 1L;
		String email = "mail@mail.com";
		String lastName = "last";
		String firstName = "first";
		String password = "password";
		User user = User.builder().id(id).email(email).lastName(lastName).firstName(firstName).password(password)
				.build();

		UserDetailsImpl userDetails = UserDetailsImpl.builder().id(id).username(email).firstName(firstName)
				.lastName(lastName).password(password).admin(true).build();
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
		SecurityContext securityContext = mock(SecurityContext.class);
		SecurityContextHolder.setContext(securityContext);

		when(securityContext.getAuthentication()).thenReturn(authentication);
		when(userService.findById(id)).thenReturn(user);
		doNothing().when(userService).delete(id);

		ResponseEntity<?> response = userController.save(id.toString());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(userService).findById(id);
		verify(userService).delete(id);
	}

	@Test
	public void saveNotFound() {
		Long id = 1L;
		when(userService.findById(id)).thenReturn(null);

		ResponseEntity<?> response = userController.save(id.toString());

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		verify(userService).findById(id);
		verify(userService, never()).delete(id);
	}

}
