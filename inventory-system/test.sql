DROP DATABASE IF EXISTS Inventory;
CREATE DATABASE Inventory;
USE Inventory;

CREATE TABLE Product(
    id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    category VARCHAR(50) NOT NULL, 
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL
);

INSERT INTO Product (id,name,category,quantity,price)
VALUES
(1,'Laptop','Electronics',10,55000),
(2,'Chair','Furniture',20,2500),
(3,'Pen','Stationary',100,10),
(4,'Phone','Electronics',15,30000),
(5,'Table','Furniture',5,8000);

-- Query 1
SELECT * FROM Product;


-- Query 2
SELECT name,category FROM Product;

-- Query 3
SELECT * FROM Product WHERE  quantity>10;

-- Query 4
SELECT * FROM Product WHERE price<5000;

-- Query 5
SELECT * FROM Product WHERE category="Electronics";

-- Query 6
SELECT * FROM Product ORDER BY price DESC;

-- Query 7
SELECT * FROM Product ORDER BY price DESC LIMIT 3;

-- Query 8
SELECT SUM(quantity) FROM Product;

-- Query 9
SELECT AVG(price) FROM Product;

-- Query 10
SELECT * FROM Product ORDER BY price DESC LIMIT 1;