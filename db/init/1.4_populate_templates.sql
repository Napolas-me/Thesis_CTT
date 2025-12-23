INSERT INTO TRIP_TEMPLATE (origin, destination, departure_time, destination_time) VALUES
('Porto', 'Faro', '10:00:00', '18:00:00'),          -- ID 1 - Teste 1
('Porto', 'Santarém', '12:00:00', '12:30:00'),      -- ID 2 - Teste 2
('Santarém', 'Lisboa', '12:30:00', '13:00:00'),     -- ID 3 - Teste 2
('Lisboa', 'Faro', '15:00:00', '18:00:00'),         -- ID 4 - Teste 2
('Porto', 'Evora', '12:00:00', '13:00:00'),         -- ID 5 - Teste 3
('Evora', 'Faro', '13:00:00', '18:00:00');          -- ID 6 - Teste 3
--('Beja', 'Faro', '16:30:00', '18:00:00'),          -- ID 7 (1h30m)
--('Lisboa', 'Setubal', '13:00:00', '14:00:00'),     -- ID 8 (1h00m)
--('Setubal', 'Faro', '14:30:00', '17:00:00'),       -- ID 9 (2h30m)
--('Lisboa', 'Leiria', '12:00:00', '13:00:00'),      -- ID 10 (1h00m)
--('Leiria', 'Faro', '13:30:00', '16:00:00'),        -- ID 11 (2h30m)
--('Coimbra', 'Santarém', '10:30:00', '11:30:00'),   -- ID 12 (1h00m)
--('Santarém', 'Faro', '11:30:00', '15:00:00'),      -- ID 13 (3h30m)
--('Porto', 'Lisboa', '08:00:00', '10:00:00'),      -- ID 14 (2h00m) - Direta Rápida Porto-Lisboa
--('Lisboa', 'Porto', '08:30:00', '10:30:00'),      -- ID 15 (2h00m) - Direta Rápida Lisboa-Porto
--('Porto', 'Coimbra', '08:30:00', '09:15:00'),     -- ID 16 (0h45m) - Para rota Porto-Lisboa mais rápida via Coimbra
--('Coimbra', 'Lisboa', '09:30:00', '10:30:00'),    -- ID 17 (1h00m) - Para rota Porto-Lisboa mais rápida via Coimbra
--('Porto', 'Santarém', '09:00:00', '10:30:00'),    -- ID 18 (1h30m) - Para rota Porto-Lisboa mais lenta via Santarém
--('Santarém', 'Lisboa', '11:00:00', '12:30:00'),    -- ID 19 (1h30m) - Para rota Porto-Lisboa mais lenta via Santarém
--('Lisboa', 'Leiria', '09:00:00', '10:00:00'),     -- ID 20 (1h00m) - Para rota Lisboa-Porto mais rápida via Leiria
--('Leiria', 'Porto', '10:30:00', '12:00:00'),      -- ID 21 (1h30m) - Para rota Lisboa-Porto mais rápida via Leiria
--('Lisboa', 'Setubal', '08:00:00', '09:00:00'),    -- ID 22 (1h00m) - Para rota Lisboa-Porto mais lenta via Setubal
--('Setubal', 'Coimbra', '09:30:00', '11:30:00'),    -- ID 23 (2h00m) - Para rota Lisboa-Porto mais lenta via Setubal
--('Coimbra', 'Porto', '12:00:00', '13:30:00'),     -- ID 24 (1h30m) - Para rota Lisboa-Porto mais lenta via Setubal
--('Porto', 'Coimbra', '08:00:00', '08:45:00'),     -- ID 25 (0h45m) - Para rota Porto-Faro mais rápida via Coimbra
--('Coimbra', 'Faro', '09:00:00', '11:30:00');      -- ID 26 (2h30m) - Para rota Porto-Faro mais rápida via Coimbra


INSERT INTO STOP_TEMPLATE (stop_name, gate_name, arrival_time, departure_time) VALUES
('Porto', 'G_P1', '09:30:00', '10:00:00'),      -- ID 1 - Start Test 1
('Faro', 'G_F1', '18:00:00', '00:00:00'),       -- ID 2 - End Test 1
('Porto', 'G_P2', '09:30:00', '10:00:00'),      -- ID 3 - Start Test 2
('Santarém', 'G_S1', '11:00:00', '11:30:00'),   -- ID 4
('Lisboa', 'G_L1', '13:00:00', '15:00:00'),     -- ID 5
('Faro', 'G_F2', '18:00:00', '00:00:00'),       -- ID 6 - End Test 3
('Porto', 'G_P3', '09:30:00', '10:00:00'),      -- ID 7 - Start Test 3
('Evora', 'G_E1', '12:00:00', '13:00:00'),      -- ID 8
('Faro', 'G_F3', '15:00:00', '00:00:00');       -- ID 9 - End Test 3


