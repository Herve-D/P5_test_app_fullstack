package com.openclassrooms.starterjwt.controllers.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback(true)
public class SessionControllerIntTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

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
		mockMvc.perform(get("/api/session/2").header("Authorization", "Bearer " + jwt)).andExpect(status().isOk());
	}

	@Test
	public void testFindByIdBadRequest() throws Exception {
		mockMvc.perform(get("/api/session/error").header("Authorization", "Bearer " + jwt))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testFindByIdNotFound() throws Exception {
		mockMvc.perform(get("/api/session/0").header("Authorization", "Bearer " + jwt))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testFindAll() throws Exception {
		mockMvc.perform(get("/api/session").header("Authorization", "Bearer " + jwt)).andExpect(status().isOk());
	}

	@Test
	public void testCreate() throws Exception {
		SessionDto sessionDto = new SessionDto();
		sessionDto.setId(1L);
		sessionDto.setName("name");
		sessionDto.setDescription("test");
		sessionDto.setTeacher_id(1L);
		sessionDto.setDate(Date.valueOf("2024-02-23"));

		mockMvc.perform(MockMvcRequestBuilders.post("/api/session").header("Authorization", "Bearer " + jwt)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(sessionDto)))
				.andExpect(status().isOk());
	}

	@Test
	public void testCreateBadRequest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/api/session").header("Authorization", "Bearer " + jwt)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new SessionDto())))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testUpdate() throws Exception {
		SessionDto sessionDto = new SessionDto();
		sessionDto.setId(1L);
		sessionDto.setName("name");
		sessionDto.setDescription("update");
		sessionDto.setTeacher_id(1L);
		sessionDto.setDate(Date.valueOf("2024-02-23"));

		mockMvc.perform(MockMvcRequestBuilders.put("/api/session/2").header("Authorization", "Bearer " + jwt)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(sessionDto)))
				.andExpect(status().isOk());
	}

	@Test
	public void testUpdateBadRequest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/api/session/error").header("Authorization", "Bearer " + jwt)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new SessionDto())))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testDelete() throws Exception {
		mockMvc.perform(delete("/api/session/2").header("Authorization", "Bearer " + jwt)).andExpect(status().isOk());
	}

	@Test
	public void testDeleteNotFound() throws Exception {
		mockMvc.perform(delete("/api/session/0").header("Authorization", "Bearer " + jwt))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testDeleteBadRequest() throws Exception {
		mockMvc.perform(delete("/api/session/error").header("Authorization", "Bearer " + jwt))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testParticipate() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/session/2/participate/1").header("Authorization", "Bearer " + jwt))
				.andExpect(status().isOk());
	}

	@Test
	public void testParticipateNotFound() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/session/0/participate/0").header("Authorization", "Bearer " + jwt))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testParticipateBadRequest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/api/session/error/participate/error").header("Authorization",
				"Bearer " + jwt)).andExpect(status().isBadRequest());
	}

	@Test
	public void testNoLongerParticipate() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/session/2/participate/1").header("Authorization", "Bearer " + jwt))
				.andExpect(status().isOk());
	}

}
