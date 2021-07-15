INSERT INTO ADDRESSES (id, HOUSE_NUMBER, PLOT_NUMBER, STREET) VALUES
('1', '1', '0', 'Покровская'),
('2', '34', '172', 'Солнечная');
INSERT INTO USERS (ADDRESS_ID, USER_NAME, PASSWORD, ROLES, FIRST_NAME, LAST_NAME) VALUES
('1', 'admin@baltsloboda2.ru', '$2a$10$Y9iceFh92PKUK5d2alIgzOeSy.Gg6kX6/o.Cn32ARH2H9whZaZ17K', 'ROLE_ADMIN', 'Admin', 'Admin'),
('2', 'olshanskyev@gmail.com', '$2a$10$ofyHZ3LvIfDv.ZFgA0xm/uBe7IEgmtKcid5KslnGUmDuI1rP5cRXW', 'ROLE_USER', 'Ольшанский', 'Евгений');