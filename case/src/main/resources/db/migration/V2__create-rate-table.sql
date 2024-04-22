CREATE TABLE IF NOT EXISTS course_rating (
                                             id INT AUTO_INCREMENT PRIMARY KEY,
                                             course_id INT,
                                             user_id INT,
                                             rating INT,
                                             feedback VARCHAR(255),
                                             rating_date DATETIME,
                                             FOREIGN KEY (course_id) REFERENCES course(id),
                                             FOREIGN KEY (user_id) REFERENCES user(id)
);
