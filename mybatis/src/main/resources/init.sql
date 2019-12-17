CREATE DATABASE mybatis;

USE mybatis;

CREATE TABLE user(
  user_id INT PRIMARY KEY AUTO_INCREMENT,
  user_name VARCHAR(255) DEFAULT NULL,
  password VARCHAR(155) DEFAULT NULL
);

INSERT INTO user(user_name, password) VALUES
('eugene', 'eugene1'),
('eugene2', 'eugene2'),
('eugene3', 'eugene3');