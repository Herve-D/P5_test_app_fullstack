package com.openclassrooms.starterjwt.controllers.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;

@SpringBootTest
public class SessionControllerTests {

	@Mock
	SessionMapper sessionMapper;

	@Mock
	SessionService sessionService;

	private SessionController sessionController;
	private Session session;
	private SessionDto sessionDto;

	@BeforeEach
	public void setup() {
		sessionController = new SessionController(sessionService, sessionMapper);

		User user = new User(4L, "mail@mail.com", "last", "first", "password", false, LocalDateTime.now(),
				LocalDateTime.now());
		List<User> users = new ArrayList<>();
		users.add(user);

		Teacher teacher = new Teacher(4L, "last", "first", LocalDateTime.now(), LocalDateTime.now());
		session = new Session(4L, "yoga", new Date(), "super session", teacher, users, LocalDateTime.now(),
				LocalDateTime.now());

		List<Long> usersId = new ArrayList<>();
		usersId.add(user.getId());
		sessionDto = new SessionDto(4L, "yoga", new Date(), 4L, "super session", usersId, LocalDateTime.now(),
				LocalDateTime.now());
	}

	@Test
	public void getById() {
		when(sessionService.getById(4L)).thenReturn(session);
		when(sessionMapper.toDto(session)).thenReturn(sessionDto);
		
		ResponseEntity<?> response = sessionController.findById("4");
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(response.getBody(), sessionDto);
	}

	@Test
	public void getByIdNotFound() {
		when(sessionService.getById(4L)).thenReturn(null);
		
		ResponseEntity<?> response = sessionController.findById("4");
		
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	public void getAll() {
		List<Session> sessions = new ArrayList<>();
		sessions.add(session);
		List<SessionDto> sessionDtos = new ArrayList<>();
		sessionDtos.add(sessionDto);

		when(sessionService.findAll()).thenReturn(sessions);
		when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

		ResponseEntity<?> response = sessionController.findAll();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(response.getBody(), sessionDtos);
	}

	@Test
	public void create() {
		when(sessionService.create(session)).thenReturn(session);
		when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
		when(sessionMapper.toDto(session)).thenReturn(sessionDto);
		
		ResponseEntity<?> response = sessionController.create(sessionDto);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(response.getBody(), sessionDto);
	}

	@Test
	public void update() {
		when(sessionService.update(4L, session)).thenReturn(session);
		when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
		when(sessionMapper.toDto(session)).thenReturn(sessionDto);
		
		ResponseEntity<?> response = sessionController.update("4", sessionDto);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(response.getBody(), sessionDto);
	}

	@Test
	public void delete() {
		when(sessionService.getById(4L)).thenReturn(session);
		
		ResponseEntity<?> response = sessionController.save("4");
		
		verify(sessionService).getById(4L);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void deleteNotFound() {
		when(sessionService.getById(4L)).thenReturn(null);
		
		ResponseEntity<?> responseEntity = sessionController.save("4");
		
		verify(sessionService).getById(4L);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}

	@Test
	public void participate() {
		ResponseEntity<?> response = sessionController.participate("4", "4");

		verify(sessionService).participate(4l, 4L);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void noLongerParticipate() {
		ResponseEntity<?> response = sessionController.noLongerParticipate("4", "4");

		verify(sessionService).noLongerParticipate(4L, 4L);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

}
