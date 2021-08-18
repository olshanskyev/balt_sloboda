INSERT INTO REQUEST_TYPES (id, DURABLE, NAME, TITLE, ROLES) VALUES
('1', 'true', 'GarbageRemovalRequest', 'Request for Garbage Removal', 'ROLE_USER,ROLE_ADMIN'),
('2', 'false', 'NewUserRequest', 'New user Request', 'ROLE_ADMIN')
;

INSERT INTO REQUEST_PARAMS (REQUEST_TYPE_ID, NAME, TYPE, OPTIONAL, COMMENT) VALUES
('2', 'userName', 'STRING', false, ''),
('2', 'firstName', 'STRING', false, ''),
('2', 'lastName', 'STRING', false, ''),
('2', 'street', 'STRING', false, ''),
('2', 'houseNumber', 'INTEGER', false, ''),
('2', 'plotNumber', 'INTEGER', false, '')
;

INSERT INTO REQUESTS (PARAM_VALUES, STATUS, SUBJECT, LAST_MODIFIED_BY_ID, OWNER_ID, REQUEST_TYPE_ID) VALUES
('{"userName": "olshanskyev@gmail.com"}', 'NEW', 'Create New User', '1', '1', '2')
;


