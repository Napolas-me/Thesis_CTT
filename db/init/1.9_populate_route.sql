-- Desativa temporariamente as verificações de chaves estrangeiras para evitar problemas durante a inserção
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE SEQUENCE_TEMPLATE;
TRUNCATE TABLE ROUTE_TEMPLATE;

-- Rota 1: Porto -> Faro (Direta)
-- Tempo: 09:30:00 - 10:30:00 (1h00m)
INSERT INTO ROUTE_TEMPLATE (name, route_start_name, route_end_name, route_start_time, route_end_time, transport_id)
VALUES ('Porto-Faro (Direta)', 'Porto', 'Faro', '09:30:00', '10:30:00', 1);
SET @route_id = LAST_INSERT_ID();
INSERT INTO SEQUENCE_TEMPLATE (id_template, sequence_order, item_type, item_id) VALUES
(@route_id, 1, 'stop', 3), -- Paragem de Início: Porto (G_P1) - ID 3
(@route_id, 2, 'trip', 3), -- Viagem: Porto -> Faro - ID 3
(@route_id, 3, 'stop', 5); -- Paragem de Fim: Faro (G_F1) - ID 5

-- Rota 2: Porto -> Faro Via Coimbra (Mais Rápida com Paragem)
-- Tempo: 08:00:00 - 11:30:00 (3h30m)
INSERT INTO ROUTE_TEMPLATE (name, route_start_name, route_end_name, route_start_time, route_end_time, transport_id)
VALUES ('Porto-Faro Via Coimbra (Mais Rápida)', 'Porto', 'Faro', '08:00:00', '11:30:00', 2);
SET @route_id = LAST_INSERT_ID();
INSERT INTO SEQUENCE_TEMPLATE (id_template, sequence_order, item_type, item_id) VALUES
(@route_id, 1, 'stop', 3),  -- Paragem de Início: Porto (G_P1) - ID 3
(@route_id, 2, 'trip', 25), -- Viagem 1: Porto -> Coimbra (08:00-08:45) - ID 25
(@route_id, 3, 'stop', 7),  -- Paragem Intermédia: Coimbra (G_C1) - ID 7
(@route_id, 4, 'trip', 26), -- Viagem 2: Coimbra -> Faro (09:00-11:30) - ID 26
(@route_id, 5, 'stop', 5);  -- Paragem de Fim: Faro (G_F1) - ID 5

-- Rota 3: Porto -> Faro Via Lisboa (Mais Lenta com Paragem)
-- Tempo: 09:00:00 - 15:00:00 (6h00m)
INSERT INTO ROUTE_TEMPLATE (name, route_start_name, route_end_name, route_start_time, route_end_time, transport_id)
VALUES ('Porto-Faro Via Lisboa (Mais Lenta)', 'Porto', 'Faro', '09:00:00', '15:00:00', 3);
SET @route_id = LAST_INSERT_ID();
INSERT INTO SEQUENCE_TEMPLATE (id_template, sequence_order, item_type, item_id) VALUES
(@route_id, 1, 'stop', 3), -- Paragem de Início: Porto (G_P1) - ID 3
(@route_id, 2, 'trip', 1), -- Viagem 1: Porto -> Lisboa (09:00-11:30) - ID 1
(@route_id, 3, 'stop', 1), -- Paragem Intermédia: Lisboa (G_L1) - ID 1
(@route_id, 4, 'trip', 2), -- Viagem 2: Lisboa -> Faro (12:00-15:00) - ID 2
(@route_id, 5, 'stop', 5); -- Paragem de Fim: Faro (G_F1) - ID 5


-- -----------------------------------------------------------
-- Rotas Porto <-> Lisboa (3 variações para cada sentido)
-- -----------------------------------------------------------

-- Rota 4: Porto -> Lisboa (Direta)
-- Tempo: 09:00:00 - 11:30:00 (2h30m)
INSERT INTO ROUTE_TEMPLATE (name, route_start_name, route_end_name, route_start_time, route_end_time, transport_id)
VALUES ('Porto-Lisboa (Direta)', 'Porto', 'Lisboa', '09:00:00', '11:30:00', 4);
SET @route_id = LAST_INSERT_ID();
INSERT INTO SEQUENCE_TEMPLATE (id_template, sequence_order, item_type, item_id) VALUES
(@route_id, 1, 'stop', 3), -- Paragem de Início: Porto (G_P1) - ID 3
(@route_id, 2, 'trip', 1), -- Viagem: Porto -> Lisboa - ID 1
(@route_id, 3, 'stop', 1); -- Paragem de Fim: Lisboa (G_L1) - ID 1

-- Rota 5: Porto -> Lisboa Via Coimbra (Mais Rápida com Paragem)
-- Tempo: 08:30:00 - 10:30:00 (2h00m)
INSERT INTO ROUTE_TEMPLATE (name, route_start_name, route_end_name, route_start_time, route_end_time, transport_id)
VALUES ('Porto-Lisboa Via Coimbra (Mais Rápida)', 'Porto', 'Lisboa', '08:30:00', '10:30:00', 5);
SET @route_id = LAST_INSERT_ID();
INSERT INTO SEQUENCE_TEMPLATE (id_template, sequence_order, item_type, item_id) VALUES
(@route_id, 1, 'stop', 3),  -- Paragem de Início: Porto (G_P1) - ID 3
(@route_id, 2, 'trip', 16), -- Viagem 1: Porto -> Coimbra (08:30-09:15) - ID 16
(@route_id, 3, 'stop', 7),  -- Paragem Intermédia: Coimbra (G_C1) - ID 7
(@route_id, 4, 'trip', 17), -- Viagem 2: Coimbra -> Lisboa (09:30-10:30) - ID 17
(@route_id, 5, 'stop', 1);  -- Paragem de Fim: Lisboa (G_L1) - ID 1

-- Rota 6: Porto -> Lisboa Via Santarém (Mais Lenta com Paragem)
-- Tempo: 09:00:00 - 12:30:00 (3h30m)
INSERT INTO ROUTE_TEMPLATE (name, route_start_name, route_end_name, route_start_time, route_end_time, transport_id)
VALUES ('Porto-Lisboa Via Santarém (Mais Lenta)', 'Porto', 'Lisboa', '09:00:00', '12:30:00', 6);
SET @route_id = LAST_INSERT_ID();
INSERT INTO SEQUENCE_TEMPLATE (id_template, sequence_order, item_type, item_id) VALUES
(@route_id, 1, 'stop', 3),  -- Paragem de Início: Porto (G_P1) - ID 3
(@route_id, 2, 'trip', 18), -- Viagem 1: Porto -> Santarém (09:00-10:30) - ID 18
(@route_id, 3, 'stop', 8),  -- Paragem Intermédia: Santarém (G_S1) - ID 8
(@route_id, 4, 'trip', 19), -- Viagem 2: Santarém -> Lisboa (11:00-12:30) - ID 19
(@route_id, 5, 'stop', 1);  -- Paragem de Fim: Lisboa (G_L1) - ID 1

-- Rota 7: Lisboa -> Porto (Direta)
-- Tempo: 08:30:00 - 10:30:00 (2h00m)
INSERT INTO ROUTE_TEMPLATE (name, route_start_name, route_end_name, route_start_time, route_end_time, transport_id)
VALUES ('Lisboa-Porto (Direta)', 'Lisboa', 'Porto', '08:30:00', '10:30:00', 7);
SET @route_id = LAST_INSERT_ID();
INSERT INTO SEQUENCE_TEMPLATE (id_template, sequence_order, item_type, item_id) VALUES
(@route_id, 1, 'stop', 1),  -- Paragem de Início: Lisboa (G_L1) - ID 1
(@route_id, 2, 'trip', 15), -- Viagem: Lisboa -> Porto - ID 15
(@route_id, 3, 'stop', 3);  -- Paragem de Fim: Porto (G_P1) - ID 3

-- Rota 8: Lisboa -> Porto Via Leiria (Mais Rápida com Paragem)
-- Tempo: 09:00:00 - 12:00:00 (3h00m)
INSERT INTO ROUTE_TEMPLATE (name, route_start_name, route_end_name, route_start_time, route_end_time, transport_id)
VALUES ('Lisboa-Porto Via Leiria (Mais Rápida)', 'Lisboa', 'Porto', '09:00:00', '12:00:00', 8);
SET @route_id = LAST_INSERT_ID();
INSERT INTO SEQUENCE_TEMPLATE (id_template, sequence_order, item_type, item_id) VALUES
(@route_id, 1, 'stop', 1),  -- Paragem de Início: Lisboa (G_L1) - ID 1
(@route_id, 2, 'trip', 20), -- Viagem 1: Lisboa -> Leiria (09:00-10:00) - ID 20
(@route_id, 3, 'stop', 10), -- Paragem Intermédia: Leiria (G_LR1) - ID 10
(@route_id, 4, 'trip', 21), -- Viagem 2: Leiria -> Porto (10:30-12:00) - ID 21
(@route_id, 5, 'stop', 3);  -- Paragem de Fim: Porto (G_P1) - ID 3

-- Rota 9: Lisboa -> Porto Via Setúbal e Coimbra (Mais Lenta com Paragem)
-- Tempo: 08:00:00 - 13:30:00 (5h30m)
INSERT INTO ROUTE_TEMPLATE (name, route_start_name, route_end_name, route_start_time, route_end_time, transport_id)
VALUES ('Lisboa-Porto Via Setúbal e Coimbra (Mais Lenta)', 'Lisboa', 'Porto', '08:00:00', '13:30:00', 9);
SET @route_id = LAST_INSERT_ID();
INSERT INTO SEQUENCE_TEMPLATE (id_template, sequence_order, item_type, item_id) VALUES
(@route_id, 1, 'stop', 1),  -- Paragem de Início: Lisboa (G_L1) - ID 1
(@route_id, 2, 'trip', 22), -- Viagem 1: Lisboa -> Setubal (08:00-09:00) - ID 22
(@route_id, 3, 'stop', 11), -- Paragem Intermédia 1: Setubal (G_ST1) - ID 11
(@route_id, 4, 'trip', 23), -- Viagem 2: Setubal -> Coimbra (09:30-11:30) - ID 23
(@route_id, 5, 'stop', 7),  -- Paragem Intermédia 2: Coimbra (G_C1) - ID 7
(@route_id, 6, 'trip', 24), -- Viagem 3: Coimbra -> Porto (12:00-13:30) - ID 24
(@route_id, 7, 'stop', 3);  -- Paragem de Fim: Porto (G_P1) - ID 3


-- -----------------------------------------------------------
-- Outras Rotas (Ajustadas para usar os novos IDs)
-- -----------------------------------------------------------

-- Rota 10: Lisboa -> Beja -> Faro
INSERT INTO ROUTE_TEMPLATE (name, route_start_name, route_end_name, route_start_time, route_end_time, transport_id)
VALUES ('Lisboa-Faro Via Beja', 'Lisboa', 'Faro', '15:00:00', '18:00:00', 10);
SET @route_id = LAST_INSERT_ID();
INSERT INTO SEQUENCE_TEMPLATE (id_template, sequence_order, item_type, item_id) VALUES
(@route_id, 1, 'stop', 1), -- Paragem de Início: Lisboa (G_L1) - ID 1
(@route_id, 2, 'trip', 6), -- Viagem 1: Lisboa -> Beja - ID 6
(@route_id, 3, 'stop', 9), -- Paragem Intermédia: Beja (G_B1) - ID 9
(@route_id, 4, 'trip', 7), -- Viagem 2: Beja -> Faro - ID 7
(@route_id, 5, 'stop', 5); -- Paragem de Fim: Faro (G_F1) - ID 5

-- Rota 11: Lisboa -> Setúbal -> Faro
INSERT INTO ROUTE_TEMPLATE (name, route_start_name, route_end_name, route_start_time, route_end_time, transport_id)
VALUES ('Lisboa-Faro Via Setúbal', 'Lisboa', 'Faro', '13:00:00', '17:00:00', 11);
SET @route_id = LAST_INSERT_ID();
INSERT INTO SEQUENCE_TEMPLATE (id_template, sequence_order, item_type, item_id) VALUES
(@route_id, 1, 'stop', 1),  -- Paragem de Início: Lisboa (G_L1) - ID 1
(@route_id, 2, 'trip', 8),  -- Viagem 1: Lisboa -> Setubal - ID 8
(@route_id, 3, 'stop', 11), -- Paragem Intermédia: Setubal (G_ST1) - ID 11
(@route_id, 4, 'trip', 9),  -- Viagem 2: Setubal -> Faro - ID 9
(@route_id, 5, 'stop', 5);  -- Paragem de Fim: Faro (G_F1) - ID 5

-- Rota 12: Lisboa -> Leiria -> Faro
INSERT INTO ROUTE_TEMPLATE (name, route_start_name, route_end_name, route_start_time, route_end_time, transport_id)
VALUES ('Lisboa-Faro Via Leiria', 'Lisboa', 'Faro', '12:00:00', '16:00:00', 12);
SET @route_id = LAST_INSERT_ID();
INSERT INTO SEQUENCE_TEMPLATE (id_template, sequence_order, item_type, item_id) VALUES
(@route_id, 1, 'stop', 1),  -- Paragem de Início: Lisboa (G_L1) - ID 1
(@route_id, 2, 'trip', 10), -- Viagem 1: Lisboa -> Leiria - ID 10
(@route_id, 3, 'stop', 10), -- Paragem Intermédia: Leiria (G_LR1) - ID 10
(@route_id, 4, 'trip', 11), -- Viagem 2: Leiria -> Faro - ID 11
(@route_id, 5, 'stop', 5);  -- Paragem de Fim: Faro (G_F1) - ID 5

-- Rota 13: Coimbra -> Santarém -> Faro
INSERT INTO ROUTE_TEMPLATE (name, route_start_name, route_end_name, route_start_time, route_end_time, transport_id)
VALUES ('Coimbra-Faro Via Santarém', 'Coimbra', 'Faro', '10:30:00', '15:00:00', 13);
SET @route_id = LAST_INSERT_ID();
INSERT INTO SEQUENCE_TEMPLATE (id_template, sequence_order, item_type, item_id) VALUES
(@route_id, 1, 'stop', 7),  -- Paragem de Início: Coimbra (G_C1) - ID 7
(@route_id, 2, 'trip', 12), -- Viagem 1: Coimbra -> Santarém - ID 12
(@route_id, 3, 'stop', 8),  -- Paragem Intermédia: Santarém (G_S1) - ID 8
(@route_id, 4, 'trip', 13), -- Viagem 2: Santarém -> Faro - ID 13
(@route_id, 5, 'stop', 5);  -- Paragem de Fim: Faro (G_F1) - ID 5

-- Rota 14: Porto -> Lisboa (Motorcycle Direct) - NEW ROUTE FOR COST METRIC TESTING
-- Tempo: 09:00:00 - 10:30:00 (1h30m) - Assuming a faster, direct motorcycle trip
INSERT INTO ROUTE_TEMPLATE (name, route_start_name, route_end_name, route_start_time, route_end_time, transport_id)
VALUES ('Porto-Lisboa (Motorcycle Direta)', 'Porto', 'Lisboa', '09:00:00', '10:30:00', 7); -- Using Transport ID 7 (M1 - motorcycle)
SET @route_id = LAST_INSERT_ID();
INSERT INTO SEQUENCE_TEMPLATE (id_template, sequence_order, item_type, item_id) VALUES
(@route_id, 1, 'stop', 3), -- Paragem de Início: Porto (G_P1) - ID 3
(@route_id, 2, 'trip', 1), -- Viagem: Porto -> Lisboa (using existing trip ID 1)
(@route_id, 3, 'stop', 1); -- Paragem de Fim: Lisboa (G_L1) - ID 1

-- Reativa as verificações de chaves estrangeiras
SET FOREIGN_KEY_CHECKS = 1;
