package com.openclassrooms.starterjwt.security.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

@SpringBootTest
public class UserDetailsServiceImplTests {

	@Mock
	private UserRepository userRepository;

	@Test
	public void loadUserByUsername() {
		String username = "user";
		User user = User.builder().id(1L).email("mail@mail.com").firstName("first").lastName("last")
				.password("password").build();

		when(userRepository.findByEmail(username)).thenReturn(Optional.ofNullable(user));

		UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl(userRepository);
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;

		assert userDetailsImpl.getId().equals(user.getId());
	}

	@Test
	public void loadUserByUsernameNotFound() {
		when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
		
		UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl(userRepository);
		
		assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("mail@mail.com"));
	}

}
