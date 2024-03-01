package com.openclassrooms.starterjwt.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;

@SpringBootTest
public class UserMapperTests {

	private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
	private UserDto userDto;
	private User user;

	@BeforeEach
	public void setup() {
		userDto = new UserDto();
		userDto.setEmail("mail@mail.com");
		userDto.setLastName("last");
		userDto.setFirstName("first");
		userDto.setPassword("password");
		userDto.setId(1L);

		user = new User();
		user.setEmail("mail@mail.com");
		user.setLastName("last");
		user.setFirstName("first");
		user.setPassword("password");
		user.setId(1L);
	}

	@Test
	public void toEntity() {
		User userEntity = userMapper.toEntity(userDto);
		assertEquals(user, userEntity);
	}

	@Test
	public void toDto() {
		UserDto userDtoEntity = userMapper.toDto(user);
		assertEquals(userDto, userDtoEntity);
	}

	@Test
	public void toDtoList() {
		List<User> users = new ArrayList<>();
		users.add(user);
		List<UserDto> userDtos = new ArrayList<>();
		userDtos.add(userDto);

		List<UserDto> userDtosMapped = userMapper.toDto(users);
		assertEquals(userDtos, userDtosMapped);
	}

	@Test
	public void toEntityList() {
		List<User> users = new ArrayList<>();
		users.add(user);
		List<UserDto> userDtos = new ArrayList<>();
		userDtos.add(userDto);

		List<User> usersMapped = userMapper.toEntity(userDtos);
		assertEquals(users, usersMapped);
	}

	@Test
	public void toEntityWhenDtoNull() {
		assertNull(userMapper.toEntity((UserDto) null));
	}

	@Test
	public void toDtoWhenEntityNull() {
		assertNull(userMapper.toDto((User) null));
	}

	@Test
	public void toEntityListWhenDtoNull() {
		assertNull(userMapper.toEntity((List<UserDto>) null));
	}

	@Test
	public void toDtoListWhenEntityNull() {
		assertNull(userMapper.toDto((List<User>) null));
	}

}
