DROP TABLE IF EXISTS TRIP_DAILY;
DROP TABLE IF EXISTS TRIP_TEMPLATE;


CREATE TABLE TRIP_TEMPLATE (
    id INT PRIMARY KEY AUTO_INCREMENT,
    origin VARCHAR(64) NOT NULL,
    destination VARCHAR(64) NOT NULL,
    departure_time TIME NOT NULL,
    destination_time TIME NOT NULL
);

CREATE TABLE TRIP_DAILY (
    id INT PRIMARY KEY AUTO_INCREMENT,
    origin VARCHAR(64) NOT NULL,
    destination VARCHAR(64) NOT NULL,
    departure_date DATETIME,
    destination_date DATETIME,
    status ENUM('scheduled', 'in_progress', 'completed', 'cancelled')
);


