-- Disable foreign key checks temporarily if needed (though UA has no FKs here)
SET FOREIGN_KEY_CHECKS = 0;

-- Drop and Create UA table with new columns
DROP TABLE IF EXISTS UA;
CREATE TABLE UA (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    type ENUM('EXPRESSO', 'VERDE', 'AZUL', 'NORMAL') NOT NULL, -- Novo campo 'type'
    origin VARCHAR(255) NOT NULL,
    destination VARCHAR(255) NOT NULL,
    max_transfers INT DEFAULT 0, -- Novo campo 'max_transfers'
    deadline DATETIME NOT NULL, -- Renomeado para 'deadline' no DB, corresponde a data_chegada no DTO
    status ENUM('active', 'completed', 'created') DEFAULT 'created'
);

-- Clear existing data from UA table for a clean run
TRUNCATE TABLE UA;

-- Populate UA table based on Entity instances (Updated to include 'type' and 'max_transfers')

-- Original 4 Entities
INSERT INTO UA (name, description, type, origin, destination, max_transfers, deadline) VALUES
('Package Express Porto-Faro', 'High priority package for fastest delivery. No transfers specified.', 'EXPRESSO', 'Porto', 'Faro', 0, '2025-07-21 18:00:00'),
('Package Green Porto-Faro', 'Environmentally conscious package, seeking lowest carbon emissions. No transfers specified.', 'VERDE', 'Porto', 'Faro', 0, '2025-07-21 18:00:00'),
('Package Blue Porto-Faro', 'Priority package for fastest delivery. No transfers specified.', 'AZUL', 'Porto', 'Faro', 0, '2025-07-21 18:00:00'),
('Package Normal Porto-Faro', 'Standard package delivery. No transfers specified.', 'NORMAL', 'Porto', 'Faro', 0, '2025-07-21 18:00:00');

-- New scenarios for comprehensive testing
INSERT INTO UA (name, description, type, origin, destination, max_transfers, deadline) VALUES
('Package Normal Porto-Faro (Max 0 Transfers)', 'Standard package delivery. Max 0 transfers allowed.', 'NORMAL', 'Porto', 'Faro', 0, '2025-07-21 18:00:00'),
('Package Normal Porto-Faro (Max 1 Transfer)', 'Standard package delivery. Max 1 transfer allowed.', 'NORMAL', 'Porto', 'Faro', 1, '2025-07-21 18:00:00'),
('Package Normal Lisboa-Porto (Max 2 Transfers)', 'Standard package delivery. Max 2 transfers allowed.', 'NORMAL', 'Lisboa', 'Porto', 2, '2025-07-21 18:00:00'),
('Package Normal Lisboa-Porto (Max 0 Transfers)', 'Standard package delivery. Max 0 transfers allowed.', 'NORMAL', 'Lisboa', 'Porto', 0, '2025-07-21 18:00:00');

-- Scenario 2: Testing VERDE (Cost/Emissions) for Porto -> Lisboa
INSERT INTO UA (name, description, type, origin, destination, max_transfers, deadline) VALUES
('Package Green Porto-Lisboa', 'Environmentally conscious package, seeking lowest carbon emissions. No transfers specified.', 'VERDE', 'Porto', 'Lisboa', 0, '2025-07-21 18:00:00');

-- Scenario 3: Testing AZUL (Fastest) for Lisboa -> Faro
INSERT INTO UA (name, description, type, origin, destination, max_transfers, deadline) VALUES
('Package Blue Lisboa-Faro', 'Priority package for fastest delivery. No transfers specified.', 'AZUL', 'Lisboa', 'Faro', 0, '2025-07-21 18:00:00');

-- Scenario 4: Testing EXPRESSO (Earliest) for Coimbra -> Faro
INSERT INTO UA (name, description, type, origin, destination, max_transfers, deadline) VALUES
('Package Express Coimbra-Faro', 'High priority package for earliest arrival. No transfers specified.', 'EXPRESSO', 'Coimbra', 'Faro', 0, '2025-07-21 18:00:00');

-- Scenario 5: Testing with tight deadlines
INSERT INTO UA (name, description, type, origin, destination, max_transfers, deadline) VALUES
('Package Normal Porto-Faro (Tight Deadline Early)', 'Standard package delivery with a very early deadline.', 'NORMAL', 'Porto', 'Faro', 0, '2025-07-21 10:00:00'),
('Package Normal Porto-Faro (Tight Deadline Mid)', 'Standard package delivery with a deadline for Route 1.', 'NORMAL', 'Porto', 'Faro', 0, '2025-07-21 10:45:00');

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;
