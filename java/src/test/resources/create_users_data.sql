INSERT INTO ADDRESSES (id, STREET, HOUSE_NUMBER, PLOT_NUMBER) VALUES
('1', 'Pokrovskaya', '1', '0'),
('2', 'Solnechnaya', '34', '172'),
('3', 'Solnechnaya', '32', '171'),
('4', 'Svetlaya', '4', '168'),
('5', 'Svetlaya', '6', '167')
;
INSERT INTO USERS (id, USER_NAME, PASSWORD, ROLES, PASSWORD_RESET_TOKEN ) VALUES
('1', 'admin@baltsloboda2.ru', '$2a$10$Y9iceFh92PKUK5d2alIgzOeSy.Gg6kX6/o.Cn32ARH2H9whZaZ17K', 'ROLE_ADMIN', null),
('2', 'olshanskyev@gmail.com', '$2a$10$ofyHZ3LvIfDv.ZFgA0xm/uBe7IEgmtKcid5KslnGUmDuI1rP5cRXW', 'ROLE_USER', 'PASSWORD_RESET_TOKEN');

INSERT INTO RESIDENTS (id, USER_ID, ADDRESS_ID, FIRST_NAME, LAST_NAME) VALUES
('1', '2', '2', 'Olshansky', 'Evgeny');