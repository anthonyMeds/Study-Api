CREATE TABLE Users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255),
                       username VARCHAR(20) UNIQUE,
                       email VARCHAR(255) UNIQUE,
                       password VARCHAR(255),
                       role ENUM('STUDENT', 'INSTRUCTOR', 'ADMIN'),
                       creation_date DATETIME,
                       CONSTRAINT unique_email UNIQUE (email)
);

CREATE TABLE Course (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255),
                        code VARCHAR(10) UNIQUE,
                        instructor VARCHAR(255),
                        description TEXT,
                        status ENUM('ACTIVE', 'INACTIVE'),
                        creation_date DATETIME,
                        inactivation_date DATETIME
);

CREATE TABLE Enrollment (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            user_id INT,
                            course_id INT,
                            enrollment_date DATETIME,
                            FOREIGN KEY (user_id) REFERENCES Users(id),
                            FOREIGN KEY (course_id) REFERENCES Course(id)
);