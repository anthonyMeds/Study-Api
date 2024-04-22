## Study API - 

Welcome to the Study API! This project aims to provide a platform for managing users, courses, and enrollments.

### Technologies Used

The Study API is built using the following technologies:

- **Java 20**: The programming language used for the backend logic.
- **Spring Boot**: A powerful framework for building Java-based applications.
- **JPA**: Java Persistence API for managing relational data in Java applications.
- **Maven**: A build automation tool used for managing dependencies and building the project.
- **MySql**: Relational database used for development.
- **Flyway**: A database migration tool used to manage and version control database schema changes.
- **Docker**: A containerization platform used to deploy the application and database services.
- **Mockito**: unit tests written with Mockito to ensure the functionality of the API endpoints.
- **Swagger**: Api documentation.
- **Git**: Code version.

### Getting Started

To get started with Study API, follow these steps:

1. Clone this repository to your local machine.
2. Ensure you have Docker installed on your machine.
3. Run the following command to start the database in docker :

**docker-compose -f .\compose.yaml up -d**

5. Build and run the application in your IDE.

### Endpoints

Study API provides the following endpoints:

- **User Management**: Create, get an user, get all users.
- **Course Management**: Create, update.
- **Enrollment Management**: Enroll users in courses, view enrollments.
- **Course Rating Management**: Allow users to rate courses and provide feedback.
- **Net Promoter Score (NPS) Generation**: Generate NPS for existing courses based on course ratings.


  - check in swagger : http://localhost:8080/swagger-ui/index.html#/user-controller/createUser
 
    ![image](https://github.com/anthonyMeds/Study-Api/assets/124306076/165e5087-7d59-4868-a355-7554ad0375b5)



## Exception Handlers and Request Data Validation

In the Study API project, specific exception handlers have been implemented to handle various types of exceptions that may occur during the execution of the application. 

### Exception Handlers

1. **MethodArgumentNotValidException Handler**: This handler deals with validation errors that occur when the request data fails validation. It returns a detailed response indicating the specific field(s) that failed validation along with the error message(s).

2. **UnexpectedTypeException Handler**: Handles unexpected type exceptions by providing an appropriate error message and status code in the response.

3. **Other Custom Exception Handlers**: Additional custom exception handlers may be implemented to handle specific business logic errors or unexpected scenarios, ensuring robust error handling throughout the application.

   

### Validations in Endpoints

1. **User Management Endpoints**: Validations are applied to user creation and update endpoints to ensure that required fields are provided and data is in the correct format. For example, username and email uniqueness is enforced, and roles are validated against predefined values.

2. **Course Management Endpoints**: Similar validations are applied to course-related endpoints, such as ensuring unique course codes, validating instructor permissions for course creation, and enforcing constraints on course descriptions.

3. **Enrollment Management Endpoints**: Validations ensure that users can only enroll in active courses and prevent duplicate enrollments for the same user in a course.



