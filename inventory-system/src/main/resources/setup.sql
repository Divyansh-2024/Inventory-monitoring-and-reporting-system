-- 1. Create database if not exists
CREATE DATABASE IF NOT EXISTS dbstore;

USE dbstore;

CREATE TABLE IF NOT EXISTS products (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    price DOUBLE NOT NULL
);

-- 5. Create new MySQL user for Java connection
CREATE USER IF NOT EXISTS 'testuser'@'localhost' IDENTIFIED BY 'Password@123';

-- 6. Grant privileges on inventorydb to new user
GRANT ALL PRIVILEGES ON inventorydb.* TO 'testuser'@'localhost';

-- 7. Apply privileges
FLUSH PRIVILEGES;
