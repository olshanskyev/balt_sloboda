with insert_address as (
	INSERT INTO baltsloboda.ADDRESSES (STREET, HOUSE_NUMBER, PLOT_NUMBER) VALUES ('Солнечная', '34', '172') returning id as address_id	
), insert_user as (
	INSERT INTO baltsloboda.USERS 	
	(USER_NAME, PASSWORD, ROLES) VALUES ('olshanskyev@gmail.com', '$2a$10$ofyHZ3LvIfDv.ZFgA0xm/uBe7IEgmtKcid5KslnGUmDuI1rP5cRXW', 'ROLE_USER') 
	returning id as user_id, (select address_id from insert_address)
)
INSERT INTO baltsloboda.RESIDENTS (ADDRESS_ID, USER_ID, FIRST_NAME, LAST_NAME)
select  address_id, user_id, 'Евгений', 'Ольшанский'
from insert_user;


INSERT INTO $DATABASE_SCHEMA.ADDRESSES (STREET, HOUSE_NUMBER, PLOT_NUMBER) VALUES
('Солнечная', '32', '171'),
('Светлая', '4', '168'),
('Светлая', '6', '167')
;
