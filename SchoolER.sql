
/* Drop Tables */

DROP TABLE RECORDS;
DROP TABLE STUDENTS;
DROP TABLE SEMESTERS;
DROP TABLE SUBJECTS;




/* Create Tables */

CREATE TABLE RECORDS
(
	record_id int NOT NULL,
	student_id int NOT NULL UNIQUE,
	subject_id int NOT NULL UNIQUE,
	semester_id int NOT NULL UNIQUE,
	grade float DEFAULT 0.0 NOT NULL,
	PRIMARY KEY (record_id),
	UNIQUE (student_id, subject_id, semester_id)
);


CREATE TABLE SEMESTERS
(
	semester_id int NOT NULL UNIQUE,
	name varchar(50) NOT NULL,
	year int NOT NULL,
	period smallint,
	PRIMARY KEY (semester_id)
);


CREATE TABLE STUDENTS
(
	student_id int NOT NULL,
	last_name varchar(255) NOT NULL,
	first_name varchar(255) NOT NULL,
	nationality varchar(100) NOT NULL,
	current_semester_id int NOT NULL UNIQUE,
	PRIMARY KEY (student_id)
);


CREATE TABLE SUBJECTS
(
	subject_id int NOT NULL,
	code varchar(5) UNIQUE,
	name varchar(50) NOT NULL,
	credits smallint NOT NULL,
	PRIMARY KEY (subject_id)
);



/* Create Foreign Keys */

ALTER TABLE RECORDS
	ADD FOREIGN KEY (semester_id)
	REFERENCES SEMESTERS (semester_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE STUDENTS
	ADD CONSTRAINT currentSemesterRule FOREIGN KEY (current_semester_id)
	REFERENCES SEMESTERS (semester_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE RECORDS
	ADD FOREIGN KEY (student_id)
	REFERENCES STUDENTS (student_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE RECORDS
	ADD FOREIGN KEY (subject_id)
	REFERENCES SUBJECTS (subject_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;



