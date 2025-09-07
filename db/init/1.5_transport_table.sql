DROP TABLE IF EXISTS TRANSPORT;

CREATE TABLE TRANSPORT (
    id INT PRIMARY KEY,
    type ENUM('truck', 'car', 'motorcycle') NOT NULL,
    name VARCHAR(100) NOT NULL,
    capacity INT NOT NULL,
    max_capacity INT,
    status ENUM('available', 'in_use', 'maintenance', 'unavailable') NOT NULL,
    carbon_emissions_g_km DECIMAL(10, 2) NOT NULL
);

INSERT INTO TRANSPORT (id, name, type, capacity, max_capacity, status, carbon_emissions_g_km) VALUES
(1, 'T1', 'truck', 0, 5, 'available', 650.00),
(2, 'T2', 'truck', 0, 5, 'available', 630.00),
(3, 'T3', 'truck', 0, 5, 'available', 670.00),
(4, 'C1', 'car', 0, 2, 'available', 150.00),
(5, 'C2', 'car', 0, 2, 'available', 145.00),
(6, 'C3', 'car', 0, 2, 'available', 160.00),
(7, 'M1', 'motorcycle', 0, 1, 'available', 90.00),
(8, 'M2', 'motorcycle', 0, 1, 'available', 85.00),
(9, 'M3', 'motorcycle', 0, 1, 'available', 95.00),
(10, 'T4', 'truck', 0, 5, 'available', 640.00), -- Added for Route 10
(11, 'T5', 'truck', 0, 5, 'available', 660.00), -- Added for Route 11
(12, 'T6', 'truck', 0, 5, 'available', 655.00), -- Added for Route 12
(13, 'T7', 'truck', 0, 5, 'available', 645.00); -- Added for Route 13