package com.openclassrooms.starterjwt.controllers.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;

@SpringBootTest
public class TeacherControllerTests {

	@Mock
	TeacherMapper teacherMapper;

	@Mock
	TeacherService teacherService;

	@Test
	public void getById() {
		Long id = 1L;
		Teacher teacher = new Teacher();
		teacher.setId(id);
		TeacherDto teacherDto = new TeacherDto();
		teacherDto.setId(id);

		when(teacherService.findById(id)).thenReturn(teacher);
		when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

		TeacherController teacherController = new TeacherController(teacherService, teacherMapper);
		ResponseEntity<?> response = teacherController.findById(id.toString());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(response.getBody(), teacherDto);
	}

	@Test
	public void getAll() {
		List<Teacher> teachers = new ArrayList<>();
		Teacher teacher = new Teacher();
		teacher.setFirstName("first");
		teachers.add(teacher);

		List<TeacherDto> teacherDtos = new ArrayList<>();
		TeacherDto teacherDto = new TeacherDto();
		teacherDto.setFirstName("first");
		teacherDtos.add(teacherDto);

		when(teacherService.findAll()).thenReturn(teachers);
		when(teacherMapper.toDto(teachers)).thenReturn(teacherDtos);

		TeacherController teacherController = new TeacherController(teacherService, teacherMapper);
		ResponseEntity<?> response = teacherController.findAll();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(response.getBody(), teacherDtos);
	}

}
