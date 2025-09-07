DROP TABLE IF EXISTS STOP_DAILY;
DROP TABLE IF EXISTS STOP_TEMPLATE;

CREATE TABLE STOP_TEMPLATE (
    id INT PRIMARY KEY AUTO_INCREMENT,
    stop_name VARCHAR(64) NOT NULL,
    gate_name VARCHAR(16) NOT NULL,
    arrival_time TIME,
    departure_time TIME
);

CREATE TABLE STOP_DAILY (
    id INT PRIMARY KEY AUTO_INCREMENT,
    stop_name VARCHAR(64) NOT NULL,
    gate_name VARCHAR(16) NOT NULL,
    arrival_date DATETIME,
    departure_date DATETIME,
    status ENUM('scheduled', 'in-progress', 'completed', 'cancelled')
);
