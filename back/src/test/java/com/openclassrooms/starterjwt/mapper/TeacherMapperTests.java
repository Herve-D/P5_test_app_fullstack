package com.openclassrooms.starterjwt.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;

@SpringBootTest
public class TeacherMapperTests {

	private final TeacherMapper teacherMapper = Mappers.getMapper(TeacherMapper.class);
	private TeacherDto teacherDto;
	private Teacher teacher;

	@BeforeEach
	public void setup() {
		teacherDto = new TeacherDto();
		teacherDto.setFirstName("first");

		teacher = new Teacher();
		teacher.setFirstName("first");
	}

	@Test
	public void toEntity() {
		Teacher teacherEntity = teacherMapper.toEntity(teacherDto);
		assertEquals(teacher, teacherEntity);
	}

	@Test
	public void toDto() {
		TeacherDto teacherDtoEntity = teacherMapper.toDto(teacher);
		assertEquals(teacherDto, teacherDtoEntity);
	}

	@Test
	public void toDtoList() {
		List<Teacher> teachers = new ArrayList<>();
		teachers.add(teacher);
		List<TeacherDto> teacherDtos = new ArrayList<>();
		teacherDtos.add(teacherDto);

		List<TeacherDto> teacherDtosMapped = teacherMapper.toDto(teachers);
		assertEquals(teacherDtosMapped, teacherDtosMapped);
	}

	@Test
	public void toEntityList() {
		List<Teacher> teachers = new ArrayList<>();
		teachers.add(teacher);
		List<TeacherDto> teacherDtos = new ArrayList<>();
		teacherDtos.add(teacherDto);

		List<Teacher> teachersMapped = teacherMapper.toEntity(teacherDtos);
		assertEquals(teachers, teachersMapped);
	}

	@Test
	public void toEntityWhenDtoNull() {
		assertNull(teacherMapper.toEntity((TeacherDto) null));
	}

	@Test
	public void toDtoWhenEntityNull() {
		assertNull(teacherMapper.toDto((Teacher) null));
	}

	@Test
	public void toEntityListWhenDtoNull() {
		assertNull(teacherMapper.toEntity((List<TeacherDto>) null));
	}

	@Test
	public void toDtoListWhenEntityNull() {
		assertNull(teacherMapper.toDto((List<Teacher>) null));
	}

}
