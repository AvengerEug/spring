CREATE DATABASE mulDatasource;

USE mulDatasource;

CREATE TABLE user(
  user_id INT PRIMARY KEY AUTO_INCREMENT,
  user_name VARCHAR(255) DEFAULT NULL,
  password VARCHAR(155) DEFAULT NULL
);

INSERT INTO user(user_name, password) VALUES
('avenger', 'avenger');


CREATE TABLE student1(
  student_id INT PRIMARY KEY AUTO_INCREMENT,
  student_name VARCHAR(255) DEFAULT NULL
);

CREATE TABLE student2(
  student_id INT PRIMARY KEY AUTO_INCREMENT,
  student_name VARCHAR(255) DEFAULT NULL
);