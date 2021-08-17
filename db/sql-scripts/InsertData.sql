INSERT INTO $DATABASE_SCHEMA.ADDRESSES (id, STREET, HOUSE_NUMBER, PLOT_NUMBER) VALUES
('2', 'Солнечная', '34', '172'),
('3', 'Солнечная', '32', '171'),
('4', 'Светлая', '4', '168'),
('5', 'Светлая', '6', '167')
;

INSERT INTO $DATABASE_SCHEMA.USERS (id, ADDRESS_ID, USER_NAME, PASSWORD, ROLES, FIRST_NAME, LAST_NAME) VALUES 
('2', '2', 'olshanskyev@gmail.com', '$2a$10$ofyHZ3LvIfDv.ZFgA0xm/uBe7IEgmtKcid5KslnGUmDuI1rP5cRXW', 'ROLE_USER', 'Ольшанский', 'Евгений')
;

