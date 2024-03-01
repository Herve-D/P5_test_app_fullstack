package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

@SpringBootTest
public class UserServiceTests {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;

	@BeforeEach
	public void setup() {
		userService = new UserService(userRepository);
	}

	@Test
	public void findById() {
		Long id = 4L;
		User user = new User();
		user.setId(id);
		when(userRepository.findById(id)).thenReturn(Optional.of(user));

		User foundUser = userService.findById(id);

		assertEquals(user.getId(), foundUser.getId());
	}

	@Test
	public void delete() {
		User user = new User();
		user.setId(4L);
		doNothing().when(userRepository).deleteById(anyLong());

		userService.delete(user.getId());

		verify(userRepository, times(1)).deleteById(user.getId());
	}

}
