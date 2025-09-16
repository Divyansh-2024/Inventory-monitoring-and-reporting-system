-- 1️⃣ Create database
CREATE DATABASE IF NOT EXISTS inventorydb;

-- 2️⃣ Use the database
USE inventorydb;

-- 3️⃣ Create products table
CREATE TABLE IF NOT EXISTS products (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    price DOUBLE NOT NULL
);

-- 4️⃣ Create user and grant privileges
CREATE USER IF NOT EXISTS 'testuser'@'localhost' IDENTIFIED BY 'password123';
GRANT ALL PRIVILEGES ON inventorydb.* TO 'testuser'@'localhost';
FLUSH PRIVILEGES;
