-- 1. Create database if not exists
CREATE DATABASE IF NOT EXISTS dbstore;

USE dbstore;

CREATE TABLE IF NOT EXISTS products (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50), 
    quantity INT NOT NULL,
    price DOUBLE NOT NULL,
    threshold INT DEFAULT 10
);


CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'user',
    email VARCHAR(50),
    isVerified BOOLEAN
);


-- 5. Create new MySQL user for Java connection
CREATE USER IF NOT EXISTS 'testuser'@'localhost' IDENTIFIED BY 'Password@123';

-- 6. Grant privileges on inventorydb to new user
GRANT ALL PRIVILEGES ON dbstore.* TO 'testuser'@'localhost';

-- 7. Apply privileges
FLUSH PRIVILEGES;
