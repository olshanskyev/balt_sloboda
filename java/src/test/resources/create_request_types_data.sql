INSERT INTO REQUEST_TYPES (id, DURABLE, NAME, TITLE, ROLES) VALUES
('1', 'true', 'GarbageRemovalRequest', 'Request for Garbage Removal', 'ROLE_USER,ROLE_ADMIN'),
('2', 'false', 'NewUserRequest', 'New user Request', 'ROLE_ADMIN')
;

INSERT INTO REQUEST_PARAMS (REQUEST_TYPE_ID, NAME, TYPE) VALUES
('2', 'user', 'STRING'),
('2', 'firstName', 'STRING'),
('2', 'lastName', 'STRING'),
('2', 'address.street', 'STRING'),
('2', 'address.houseNumber', 'INTEGER'),
('2', 'address.plotNumber', 'INTEGER')
;
