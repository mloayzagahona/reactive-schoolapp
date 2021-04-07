package com.melg.schoolapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.melg.schoolapp.dto.RecordDTO;
import com.melg.schoolapp.model.Student;
import com.melg.schoolapp.service.IStudentService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = StudentController.STUDENT_PATH)
public class StudentController {

	public static final String STUDENT_PATH = "/school/student";

	private IStudentService studentService;

	public StudentController(IStudentService studentService) {
		this.studentService = studentService;
	}

	@GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<Student> fetchAllStudents() {
		return studentService.fetchAllStudents();
	}


	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<Student> fetchStudentById(@PathVariable("id") Long studentId) {

		return studentService.fecthStudentById(studentId)
				.switchIfEmpty(Mono.empty());
	}

	@GetMapping(value = "/{studentId}/subjects/semester/{semesterId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<RecordDTO> listSubjectsPerStudentPerSemester(@PathVariable("studentId") Long studentId,
			@PathVariable("semesterId") Long semesterId) {
		return studentService.findSubjectsPerStudentPerSemester(studentId, semesterId);
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Student> saveStudent(@RequestBody Student student) {
		return studentService.addStudent(student);
	}

	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<Student> updateStudent(@RequestBody Student student) {
		return studentService.updateStudent(student);
	}

	@PostMapping(value = "/semester", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Mono<Void> enrollStudentToSemester(@RequestBody Student student) {
		return studentService.enrollStudentToSemester(student);
	}
}
