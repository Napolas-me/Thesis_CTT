INSERT INTO TRIP_TEMPLATE (origin, destination, departure_time, destination_time) VALUES
('Porto', 'Lisboa', '09:00:00', '11:30:00'),      -- ID 1 (2h30m) - Usada como "direta" ou "tempo médio"
('Lisboa', 'Faro', '12:00:00', '15:00:00'),        -- ID 2 (3h00m)
('Porto', 'Faro', '09:30:00', '10:30:00'),         -- ID 3 (1h00m) - Usada como "direta" Porto-Faro
('Porto', 'Coimbra', '09:00:00', '10:00:00'),      -- ID 4 (1h00m)
('Coimbra', 'Lisboa', '10:30:00', '11:30:00'),     -- ID 5 (1h00m)
('Lisboa', 'Beja', '15:00:00', '16:00:00'),        -- ID 6 (1h00m)
('Beja', 'Faro', '16:30:00', '18:00:00'),          -- ID 7 (1h30m)
('Lisboa', 'Setubal', '13:00:00', '14:00:00'),     -- ID 8 (1h00m)
('Setubal', 'Faro', '14:30:00', '17:00:00'),       -- ID 9 (2h30m)
('Lisboa', 'Leiria', '12:00:00', '13:00:00'),      -- ID 10 (1h00m)
('Leiria', 'Faro', '13:30:00', '16:00:00'),        -- ID 11 (2h30m)
('Coimbra', 'Santarém', '10:30:00', '11:30:00'),   -- ID 12 (1h00m)
('Santarém', 'Faro', '11:30:00', '15:00:00'),      -- ID 13 (3h30m)
('Porto', 'Lisboa', '08:00:00', '10:00:00'),      -- ID 14 (2h00m) - Direta Rápida Porto-Lisboa
('Lisboa', 'Porto', '08:30:00', '10:30:00'),      -- ID 15 (2h00m) - Direta Rápida Lisboa-Porto
('Porto', 'Coimbra', '08:30:00', '09:15:00'),     -- ID 16 (0h45m) - Para rota Porto-Lisboa mais rápida via Coimbra
('Coimbra', 'Lisboa', '09:30:00', '10:30:00'),    -- ID 17 (1h00m) - Para rota Porto-Lisboa mais rápida via Coimbra
('Porto', 'Santarém', '09:00:00', '10:30:00'),    -- ID 18 (1h30m) - Para rota Porto-Lisboa mais lenta via Santarém
('Santarém', 'Lisboa', '11:00:00', '12:30:00'),    -- ID 19 (1h30m) - Para rota Porto-Lisboa mais lenta via Santarém
('Lisboa', 'Leiria', '09:00:00', '10:00:00'),     -- ID 20 (1h00m) - Para rota Lisboa-Porto mais rápida via Leiria
('Leiria', 'Porto', '10:30:00', '12:00:00'),      -- ID 21 (1h30m) - Para rota Lisboa-Porto mais rápida via Leiria
('Lisboa', 'Setubal', '08:00:00', '09:00:00'),    -- ID 22 (1h00m) - Para rota Lisboa-Porto mais lenta via Setubal
('Setubal', 'Coimbra', '09:30:00', '11:30:00'),    -- ID 23 (2h00m) - Para rota Lisboa-Porto mais lenta via Setubal
('Coimbra', 'Porto', '12:00:00', '13:30:00'),     -- ID 24 (1h30m) - Para rota Lisboa-Porto mais lenta via Setubal
('Porto', 'Coimbra', '08:00:00', '08:45:00'),     -- ID 25 (0h45m) - Para rota Porto-Faro mais rápida via Coimbra
('Coimbra', 'Faro', '09:00:00', '11:30:00');      -- ID 26 (2h30m) - Para rota Porto-Faro mais rápida via Coimbra


INSERT INTO STOP_TEMPLATE (stop_name, gate_name, arrival_time, departure_time) VALUES
('Lisboa', 'G_L1', '11:30:00', '12:00:00'), -- ID 1
('Lisboa', 'G_L2', '14:30:00', '15:30:00'), -- ID 2
('Porto', 'G_P1', '09:00:00', '09:30:00'),  -- ID 3
('Porto', 'G_P2', '11:00:00', '11:30:00'),  -- ID 4
('Faro', 'G_F1', '15:00:00', '15:30:00'),   -- ID 5
('Faro', 'G_F2', '10:30:00', '11:00:00'),   -- ID 6
('Coimbra', 'G_C1', '10:00:00', '10:30:00'), -- ID 7
('Santarém', 'G_S1', '11:00:00', '11:30:00'), -- ID 8
('Beja', 'G_B1', '14:00:00', '14:30:00'),   -- ID 9
('Leiria', 'G_LR1', '13:00:00', '13:30:00'), -- ID 10
('Setubal', 'G_ST1', '16:00:00', '16:30:00');-- ID 11
