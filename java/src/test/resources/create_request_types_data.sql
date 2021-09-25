INSERT INTO REQUEST_TYPES (id, DURABLE, NAME, TITLE, ROLES, ASSIGN_TO_ID, SYSTEM_REQUEST, REQUEST_ID_PREFIX) VALUES
('1', 'true', 'GarbageRemovalRequest', 'Request for Garbage Removal', 'ROLE_USER,ROLE_ADMIN', '1', 'false', 'GRR'),
('2', 'false', 'NewUserRequest', 'New user Request', 'ROLE_ADMIN', '1', 'true', 'NUR'),
('3', 'true', 'GarbageRemovalRequest2', 'Request for Garbage Removal', 'ROLE_USER,ROLE_ADMIN', '1', 'false', 'GR2')
;

INSERT INTO REQUEST_PARAMS (REQUEST_TYPE_ID, NAME, TYPE, OPTIONAL, COMMENT) VALUES
('2', 'userName', 'STRING', false, ''),
('2', 'firstName', 'STRING', false, ''),
('2', 'lastName', 'STRING', false, ''),
('2', 'street', 'STRING', false, ''),
('2', 'houseNumber', 'INTEGER', false, ''),
('2', 'plotNumber', 'INTEGER', false, '')
;

INSERT INTO REQUESTS (PARAM_VALUES, STATUS, LAST_MODIFIED_BY_ID, OWNER_ID, ASSIGNED_TO_ID, REQUEST_TYPE_ID, GENERATED_IDENTIFIER) VALUES
('{"userName": "olshanskyev@gmail.com"}', 'NEW', '1', '1', '1', '2', 'GEN1'),
('{"test": "NEW"}', 'NEW', '2', '2', '1', '1', 'GEN2'),
('{"test": "ACCEPTED"}', 'ACCEPTED', '1', '2', '1', '1', 'GEN3'),
('{"test": "IN_PROGRESS"}', 'IN_PROGRESS', '1', '2', '1', '1', 'GEN4'),
('{"test": "CLOSED"}', 'CLOSED', '1', '2', '1', '1', 'GEN5'),
('{"test": "ACCEPTED"}', 'ACCEPTED', '1', '2', '2', '3', 'GEN6')
;


