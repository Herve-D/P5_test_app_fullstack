package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;

@SpringBootTest
public class TeacherServiceTests {

	@Mock
	private TeacherRepository teacherRepository;

	@InjectMocks
	private TeacherService teacherService;

	@BeforeEach
	public void setup() {
		teacherService = new TeacherService(teacherRepository);
	}

	@Test
	public void findAll() {
		List<Teacher> teachers = new ArrayList<>();
		teachers.add(new Teacher());
		teachers.add(new Teacher());
		teachers.add(new Teacher());
		when(teacherRepository.findAll()).thenReturn(teachers);

		List<Teacher> foundTeachers = teacherService.findAll();

		assertEquals(3, foundTeachers.size());
	}

	@Test
	public void findById() {
		Long id = 4L;
		Teacher teacher = new Teacher();
		teacher.setId(id);
		when(teacherRepository.findById(id)).thenReturn(Optional.of(teacher));

		Teacher foundTeacher = teacherService.findById(id);

		assertEquals(teacher.getId(), foundTeacher.getId());
	}

}
