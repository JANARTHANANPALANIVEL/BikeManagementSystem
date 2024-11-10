CREATE DATABASE IF NOT EXISTS bike_management;
USE bike_management;

-- Table for user credentials
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    role ENUM('admin', 'user') NOT NULL
);

-- Table for storing bike details
CREATE TABLE IF NOT EXISTS bikes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    model VARCHAR(50) NOT NULL,
    brand VARCHAR(50) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    status ENUM('Available', 'Rented') NOT NULL DEFAULT 'Available'
);

-- Table for managing rentals
CREATE TABLE IF NOT EXISTS rentals (
    rental_id INT AUTO_INCREMENT PRIMARY KEY,
    bike_id INT NOT NULL,
    customer_name VARCHAR(100) NOT NULL,
    rental_duration INT NOT NULL,
    FOREIGN KEY (bike_id) REFERENCES bikes(id)
);

-- Sample data: Insert an admin user
INSERT INTO users (username, password, role) VALUES
('admin', 'admin123', 'admin');
