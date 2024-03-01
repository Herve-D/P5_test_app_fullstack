package com.openclassrooms.starterjwt.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;

@SpringBootTest
public class SessionMapperTests {

	private final SessionMapper sessionMapper = Mappers.getMapper(SessionMapper.class);
	private SessionDto sessionDto;
	private Session session;

	@BeforeEach
	public void setup() {
		sessionDto = new SessionDto();
		sessionDto.setDescription("description");
		sessionDto.setName("session");
		sessionDto.setUsers(new ArrayList<>());

		session = new Session();
		session.setDescription("description");
		session.setName("session");
		session.setUsers(new ArrayList<>());
	}

	@Test
	public void toEntity() {
		Session sessionEntity = sessionMapper.toEntity(sessionDto);
		assertEquals(session, sessionEntity);
	}

	@Test
	public void toDto() {
		SessionDto sessionDtoEntity = sessionMapper.toDto(session);
		assertEquals(sessionDto, sessionDtoEntity);
	}

	@Test
	public void toDtoList() {
		List<Session> sessions = new ArrayList<>();
		sessions.add(session);
		List<SessionDto> sessionDtos = new ArrayList<>();
		sessionDtos.add(sessionDto);

		List<SessionDto> sessionDtosMapped = sessionMapper.toDto(sessions);
		assertEquals(sessionDtosMapped, sessionDtosMapped);
	}

	@Test
	public void toEntityList() {
		List<Session> sessions = new ArrayList<>();
		sessions.add(session);
		List<SessionDto> sessionDtos = new ArrayList<>();
		sessionDtos.add(sessionDto);

		List<Session> sessionsMapped = sessionMapper.toEntity(sessionDtos);
		assertEquals(sessions, sessionsMapped);
	}

	@Test
	public void toEntityWhenDtoNull() {
		assertNull(sessionMapper.toEntity((SessionDto) null));
	}

	@Test
	public void toDtoWhenEntityNull() {
		assertNull(sessionMapper.toDto((Session) null));
	}

	@Test
	public void toEntityListWhenDtoNull() {
		assertNull(sessionMapper.toEntity((List<SessionDto>) null));
	}

	@Test
	public void toDtoListWhenEntityNull() {
		assertNull(sessionMapper.toDto((List<Session>) null));
	}

}
