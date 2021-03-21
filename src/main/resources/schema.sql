/* Drop Tables */

DROP TABLE IF EXISTS RECORDS;
DROP TABLE IF EXISTS STUDENTS;
DROP TABLE IF EXISTS SEMESTERS;
DROP TABLE IF EXISTS SUBJECTS;

/* Create Tables */

CREATE TABLE STUDENTS
(
	student_id SERIAL,
	first_name varchar(255) NOT NULL,
	last_name varchar(255) NOT NULL,
	nationality varchar(100) NOT NULL,
	current_semester_id int,
	total_credits int NOT NULL,
	PRIMARY KEY (student_id)
);

CREATE TABLE SEMESTERS
(
	semester_id SERIAL,
	name varchar(50) NOT NULL,
	year int NOT NULL,
	period smallint,
	PRIMARY KEY (semester_id)
);

CREATE TABLE SUBJECTS
(
	subject_id SERIAL,
	code varchar(5) UNIQUE,
	name varchar(50) NOT NULL,
	credits smallint NOT NULL,
	PRIMARY KEY (subject_id)
);

CREATE TABLE RECORDS
(
	record_id  SERIAL,
	student_id int NOT NULL,
	subject_id int NOT NULL,
	semester_id int NOT NULL,
	grade varchar(5),
	PRIMARY KEY (record_id),
	UNIQUE (student_id, subject_id, semester_id)
);

/* Create Foreign Keys */

ALTER TABLE RECORDS
	ADD FOREIGN KEY (semester_id)
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

ALTER TABLE STUDENTS
	ADD CONSTRAINT currentSemesterRule FOREIGN KEY (current_semester_id)
	REFERENCES SEMESTERS (semester_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;

INSERT INTO 
	STUDENTS(first_name, last_name, nationality, total_credits)
 	VALUES('Gerale', 'Loayza', 'Peru', 0);
INSERT INTO 
	STUDENTS(first_name, last_name, nationality, total_credits) 
 	VALUES('Noellia', 'Loayza', 'Peru', 0);
 	
INSERT INTO 
    SEMESTERS(name, year, period)
    VALUES('2021-I', 2021, 1);

UPDATE STUDENTS SET current_semester_id=1
	WHERE student_id=1;

 	