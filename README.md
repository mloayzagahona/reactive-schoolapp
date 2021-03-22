# Reactive School API APP

It's a reactive Spring Boot App.

It uses:
 - Spring Web Flux
 - Spring Data R2DBC 
 - Java 11.
 - H2 Embeded DB

# Endpoints

## Student Controller

- fetchAllStudents: GET /school/student/all
- fetchStudentById: GET /school/student/{id}
- listSubjectsPerStudentPerSemester: GET /school/student//{studentId}/subjects/semester/{semesterId}
- saveStudent: POST /school/student
- updateStudent: PUT /school/student
- enrollStudentToSemester: POST  /school/student/semester

## Record Controller

- fetchAllRecords: GET /school/record/all
- saveRecord: POST /school/record
- findEnrolledStudentsPerSubjectPerSemester: GET /school/record/{subjectId}/semester/{semesterId}/students
- updateRecord: PUT /school/record
- deleteRecord: DELETE /school/record

## Semester Controller

- fetchAllSemesters: GET /school/semester/all
- saveSemeter: POST /school/semester
- updateSemester: PUT /school/semester

## Subject Controller

- fetchAllSubjetcs: GET /school/subject/all
- saveSubject: POST /school/subject
- updateSubject: PUT /school/subject

# ER Model

- The Record entity stores the subject information per semester and per student
- The Student entity stores the student information. Also it stores the current enrolled semester.

![](docs/school_er.jpg?raw=true)

# Unit Testing

The coverage tool for the is OpenClover

Run the following maven command to get the coverga report

```
mvn clean clover:setup test clover:aggregate clover:clover 
```
The report can be find it in the following path: target/site/clover

![](docs/CloverReport.png?raw=true)

# Running the App

To run the application execute the following maven command by skiping the unit testing.

```
mvn -Pnt spring-boot:run
```

To run the the application in debug mode run the following maven command

```
mvn -P nt,debug spring-boot:run
```
or 

```
mvn -Ddebug=true spring-boot:run
```

The app will start in the port 8080. To port for debuging is 7008.


# CI/CD

  The process includes 3 tools:

   ## AWS Pipeline
   
   The Pipelines is configured to listen any change (new commit) in the git repo to start the process. The process only includes AWS CodeBuild
   
   ![](docs/AWS_CodePipeline.jpg?raw=true)

   ## AWS CodeBuild

   The AWS CodeBuild is using the buildspec.yml file where
    - Run the Unit Testing
    - Build the Application
    - Build the Docker container image
    - Deploy the the docker image to the Docker Hub repo - https://hub.docker.com/repository/docker/mloayzagahona/reactive-schoolapp

  ![](docs/AWS_CodeBuild.jpg?raw=true)


  ## Manual Deployment to GCP Kubernetes

  Once there is a new docker image deployed to the Docker Hub, we can restart the service

  - There is a secret who allow us to retrieve the docker image from an external docker registry.
  - There is services who control the deployment of the docker container

  ![](docs/GCP_Kubernetes.jpg?raw=true)

   The services is running in this URL:

   http://35.192.82.242:8080


