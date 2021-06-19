CREATE DATABASE transaction_test;

USE transaction_test;

CREATE TABLE account(
  id VARCHAR(255) PRIMARY KEY,
  amount Decimal NULL
);

INSERT INTO account(id, amount) VALUES
('avengerEug', 100),
('zhangsan', 20);