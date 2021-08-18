INSERT INTO $DATABASE_SCHEMA.ADDRESSES (id, STREET, HOUSE_NUMBER, PLOT_NUMBER) VALUES
('1', 'Солнечная', '34', '172'),
('2', 'Солнечная', '32', '171'),
('3', 'Светлая', '4', '168'),
('4', 'Светлая', '6', '167')
;

INSERT INTO $DATABASE_SCHEMA.USERS (id, USER_NAME, PASSWORD, ROLES ) VALUES
('2', 'olshanskyev@gmail.com', '$2a$10$ofyHZ3LvIfDv.ZFgA0xm/uBe7IEgmtKcid5KslnGUmDuI1rP5cRXW', 'ROLE_USER');

INSERT INTO $DATABASE_SCHEMA.RESIDENTS (USER_ID, ADDRESS_ID, FIRST_NAME, LAST_NAME) VALUES
('2', '1', 'Ольшанский', 'Евгений');