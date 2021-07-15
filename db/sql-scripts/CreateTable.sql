CREATE SCHEMA IF NOT EXISTS $DATABASE_SCHEMA;

create table $DATABASE_SCHEMA.ADDRESSES (
   id  bigserial not null,
	HOUSE_NUMBER int4 not null,
	PLOT_NUMBER int4,
	STREET varchar(256) not null,
	primary key (id)
);
    
create table $DATABASE_SCHEMA.USERS (
   id  bigserial not null,
	FIRST_NAME varchar(256) not null,
	LAST_NAME varchar(256) not null,
	PASSWORD varchar(256) not null,
	ROLES varchar(256) not null,
	USER_NAME varchar(256) not null,
	ADDRESS_ID int8 not null,
	primary key (id)
);
    
alter table $DATABASE_SCHEMA.USERS 
   add constraint FKm7q0f31406tqqathdtcxkjv1t 
   foreign key (ADDRESS_ID) 
   references $DATABASE_SCHEMA.ADDRESSES;
