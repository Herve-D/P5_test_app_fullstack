package com.openclassrooms.starterjwt.controllers.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

@SpringBootTest
@AutoConfigureMockMvc
public class TeacherControllerIntTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JwtUtils jwtUtils;
	private String jwt;

	@BeforeEach
	public void setup() {
		UserDetailsImpl userDetails = UserDetailsImpl.builder().username("yoga@studio.com").build();
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
		jwt = jwtUtils.generateJwtToken(authentication);
	}

	@Test
	public void testFindById() throws Exception {
		mockMvc.perform(get("/api/teacher/1").header("Authorization", "Bearer " + jwt)).andExpect(status().isOk());
	}

	@Test
	public void testFindByIdBadRequest() throws Exception {
		mockMvc.perform(get("/api/teacher/error").header("Authorization", "Bearer " + jwt))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testFindByIdNotFound() throws Exception {
		mockMvc.perform(get("/api/teacher/0").header("Authorization", "Bearer " + jwt))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testFindAll() throws Exception {
		mockMvc.perform(get("/api/teacher").header("Authorization", "Bearer " + jwt)).andExpect(status().isOk());
	}

}
