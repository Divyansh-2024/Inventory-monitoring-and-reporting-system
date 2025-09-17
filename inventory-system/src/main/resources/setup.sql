-- 1. Create database if not exists
CREATE DATABASE IF NOT EXISTS inventorydb;

-- 2. Use the database
USE inventorydb;

-- 3. Create products table if not exists
CREATE TABLE IF NOT EXISTS products (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    price DOUBLE NOT NULL
);

-- 4. Insert sample data
INSERT INTO products (id, name, quantity, price)
VALUES
(1,'Laptop',10,55000),
(2,'Mouse',20,1000),
(3,'Keyboard',30,1500);

-- 5. Create new MySQL user for Java connection
CREATE USER IF NOT EXISTS 'testuser'@'localhost' IDENTIFIED BY 'password123';

-- 6. Grant privileges on inventorydb to new user
GRANT ALL PRIVILEGES ON inventorydb.* TO 'testuser'@'localhost';

-- 7. Apply privileges
FLUSH PRIVILEGES;
