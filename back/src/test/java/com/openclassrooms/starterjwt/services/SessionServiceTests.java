package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;

@SpringBootTest
public class SessionServiceTests {

	@Mock
	private SessionRepository sessionRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private SessionService sessionService;

	@BeforeEach
	public void setup() {
		sessionService = new SessionService(sessionRepository, userRepository);
	}

	@Test
	public void create() {
		Session session = new Session();
		session.setId(4L);
		when(sessionRepository.save(any(Session.class))).thenReturn(session);

		Session createdSession = sessionService.create(session);

		assertEquals(session.getId(), createdSession.getId());
	}

	@Test
	public void delete() {
		Session session = new Session();
		session.setId(4L);
		doNothing().when(sessionRepository).deleteById(anyLong());

		sessionService.delete(session.getId());

		verify(sessionRepository, times(1)).deleteById(session.getId());
	}

	@Test
	public void findAll() {
		List<Session> sessions = new ArrayList<>();
		Session session1 = new Session();
		session1.setId(5L);
		Session session2 = new Session();
		session2.setId(6L);
		sessions.add(session1);
		sessions.add(session2);
		when(sessionRepository.findAll()).thenReturn(sessions);

		List<Session> sessionsList = sessionService.findAll();

		assertEquals(2, sessionsList.size());
	}

	@Test
	public void getById() {
		Long id = 1L;
		Session session = new Session();
		session.setId(id);
		when(sessionRepository.findById(id)).thenReturn(Optional.of(session));

		Session sessionFound = sessionService.getById(id);

		assertEquals(sessionFound.getId(), session.getId());
	}

	@Test
	public void update() {
		Long id = 3L;
		Session session = new Session();
		session.setId(7L);
		when(sessionRepository.save(any(Session.class))).thenReturn(session);

		Session updatedSession = sessionService.update(id, session);

		assertEquals(id, updatedSession.getId());
	}

	@Test
	public void participate() {
		Long id = 8L;
		Long userId = 9L;
		Session session = Session.builder().name("test").id(id).users(new ArrayList<>()).build();
		User user = User.builder().id(userId).email("mail@mail.com").lastName("last").firstName("first")
				.password("password").build();
		when(sessionRepository.findById(id)).thenReturn(Optional.of(session));
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		sessionService.participate(id, userId);

		assertEquals(session.getUsers().size(), 1);
		assertEquals(session.getUsers().get(0).getId(), userId);
	}

	@Test
	public void participateNotFound() {
		Long id = null;
		Long userId = null;

		when(sessionRepository.findById(id)).thenReturn(Optional.empty());
		when(userRepository.findById(userId)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> sessionService.participate(id, userId));

		verify(sessionRepository, times(1)).findById(id);
		verify(userRepository, times(1)).findById(userId);
		verify(sessionRepository, never()).save(any(Session.class));
	}

	@Test
	public void participateBadRequest() {
		Long id = 8888L;
		Long userId = 1L;
		List<User> users = new ArrayList<>();
		Session session = new Session();
		User user = new User();
		session.setUsers(users);
		user.setId(userId);
		session.getUsers().add(user);

		when(sessionRepository.findById(id)).thenReturn(Optional.of(session));
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		assertThrows(BadRequestException.class, () -> sessionService.participate(id, userId));

		verify(sessionRepository, times(1)).findById(id);
		verify(userRepository, times(1)).findById(userId);
	}

	@Test
	public void noLongerParticipate() {
		Long id = 8L;
		Long userId = 9L;
		Session session = Session.builder().name("test").id(id).users(new ArrayList<>()).build();
		User user = User.builder().id(userId).email("mail@mail.com").lastName("last").firstName("first")
				.password("password").build();
		when(sessionRepository.findById(id)).thenReturn(Optional.of(session));
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		session.getUsers().add(user);

		sessionService.noLongerParticipate(id, userId);

		assertEquals(session.getUsers().size(), 0);
	}

	@Test
	public void noLongerParticipateNotFound() {
		Long id = null;
		Long userId = 4L;
		when(sessionRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(id, userId));

		verify(sessionRepository, times(1)).findById(id);
		verify(sessionRepository, times(0)).save(any(Session.class));
	}

	@Test
	public void noLongerParticipateBadRequest() {
		Long id = 8L;
		Long userId = 2L;
		Session session = Session.builder().name("test").id(id).users(new ArrayList<>()).build();
		when(sessionRepository.findById(id)).thenReturn(Optional.of(session));

		assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(id, userId));

		verify(sessionRepository, times(1)).findById(id);
	}

}
