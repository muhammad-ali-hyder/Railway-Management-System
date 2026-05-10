-- Railway Management System Database Setup
-- Run this script in MySQL before launching the application

CREATE DATABASE IF NOT EXISTS railway_db;
USE railway_db;

-- Users table (admin + regular users)
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    role ENUM('admin', 'user') DEFAULT 'user',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Trains table
CREATE TABLE IF NOT EXISTS trains (
    id INT AUTO_INCREMENT PRIMARY KEY,
    train_number VARCHAR(20) UNIQUE NOT NULL,
    train_name VARCHAR(100) NOT NULL,
    source VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    departure_time VARCHAR(20) NOT NULL,
    arrival_time VARCHAR(20) NOT NULL,
    total_seats INT NOT NULL,
    available_seats INT NOT NULL,
    fare DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Passengers table
CREATE TABLE IF NOT EXISTS passengers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    gender ENUM('Male', 'Female', 'Other') NOT NULL,
    id_proof VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Bookings table
CREATE TABLE IF NOT EXISTS bookings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    train_id INT NOT NULL,
    passenger_id INT NOT NULL,
    booking_date DATE NOT NULL,
    travel_date DATE NOT NULL,
    seat_number VARCHAR(10),
    status ENUM('Confirmed', 'Cancelled', 'Pending') DEFAULT 'Confirmed',
    total_fare DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (train_id) REFERENCES trains(id),
    FOREIGN KEY (passenger_id) REFERENCES passengers(id)
);

-- Insert default admin account (password: admin123)
INSERT IGNORE INTO users (username, password, email, role)
VALUES ('admin', 'admin123', 'admin@railway.com', 'admin');

-- Insert sample trains
INSERT IGNORE INTO trains (train_number, train_name, source, destination, departure_time, arrival_time, total_seats, available_seats, fare)
VALUES
('TR001', 'Karachi Express', 'Karachi', 'Lahore', '08:00 AM', '08:00 PM', 100, 100, 1500.00),
('TR002', 'Khyber Mail', 'Peshawar', 'Karachi', '06:00 PM', '10:00 AM', 80, 80, 2000.00),
('TR003', 'Tezgam', 'Lahore', 'Islamabad', '07:00 AM', '11:00 AM', 120, 120, 800.00),
('TR004', 'Awam Express', 'Quetta', 'Lahore', '05:00 AM', '11:00 PM', 150, 150, 1800.00),
('TR005', 'Green Line', 'Karachi', 'Islamabad', '09:00 PM', '09:00 AM', 200, 200, 3500.00);

SELECT 'Database setup complete!' AS Status;
