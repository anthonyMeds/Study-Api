CREATE TABLE net_score_promoter_course (
                                           id INT AUTO_INCREMENT PRIMARY KEY,
                                           course_id INT,
                                           nps_score FLOAT,
                                           survey_date DATE,
                                           classification VARCHAR(255),
                                           FOREIGN KEY (course_id) REFERENCES course(id)
);
