DROP TABLE IF EXISTS ROUTE_TEMPLATE;
DROP TABLE IF EXISTS ROUTE_DAILY;
DROP TABLE IF EXISTS SEQUENCE;
DROP TABLE IF EXISTS SEQUENCE_TEMPLATE;

CREATE TABLE ROUTE_TEMPLATE (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(64) NOT NULL,
    route_start_name VARCHAR(64) NOT NULL,
    route_end_name VARCHAR(64) NOT NULL,
    route_start_time TIME NOT NULL,
    route_end_time TIME NOT NULL,
    transport_id int,
    FOREIGN KEY (transport_id) REFERENCES TRANSPORT(id)
);

CREATE TABLE SEQUENCE_TEMPLATE (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_template INT,
    sequence_order INT,
    item_type ENUM('stop', 'trip'),
    item_id INT,
    FOREIGN KEY (id_template) REFERENCES ROUTE_TEMPLATE(id)
);

CREATE TABLE ROUTE_DAILY (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(64) NOT NULL,
    route_start_name VARCHAR(64) NOT NULL,
    route_end_name VARCHAR(64) NOT NULL,
    route_start_date DATETIME NOT NULL,
    route_end_date DATETIME NOT NULL,
    transport_id INT NOT NULL,
    status ENUM('scheduled','pending', 'in_progress', 'completed', 'canceled'),
    FOREIGN KEY (transport_id) REFERENCES TRANSPORT(id)
);

CREATE TABLE SEQUENCE_DAILY (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_route INT,
    sequence_order INT,
    item_type ENUM('stop', 'trip'),
    item_id INT,
    FOREIGN KEY (id_route) REFERENCES ROUTE_DAILY(id)
);

