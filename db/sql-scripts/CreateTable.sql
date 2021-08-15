CREATE SCHEMA IF NOT EXISTS $DATABASE_SCHEMA;

create table $DATABASE_SCHEMA.ADDRESSES (
   id  bigserial not null,
	HOUSE_NUMBER int4 not null,
	PLOT_NUMBER int4,
	STREET varchar(256) not null,
	primary key (id)
);
    
create table $DATABASE_SCHEMA.REQUEST_PARAMS (
   id  bigserial not null,
	NAME varchar(64) not null,
	TYPE varchar(255) not null,
	VALUE varchar(128) not null,
	REQUEST_TYPE_ID int8 not null,
	primary key (id)
);
    
create table $DATABASE_SCHEMA.REQUEST_TYPES (
   id  bigserial not null,
	DURABLE boolean not null,
	SCOPE varchar(255) not null,
	NAME varchar(64) not null,
	TITLE varchar(256) not null,
	primary key (id)
);
    
create table $DATABASE_SCHEMA.REQUESTS (
   id  bigserial not null,
	COMMENT varchar(512) not null,
	CREATION_DATE timestamp,
	LAST_MODIFIED_DATE timestamp,
	STATUS varchar(255) not null,
	SUBJECT varchar(256) not null,
	LAST_MODIFIED_BY_ID int8 not null,
	OWNER_ID int8 not null,
	REQUEST_TYPE_ID int8 not null,
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
       
alter table $DATABASE_SCHEMA.REQUEST_TYPES 
   add constraint UK_k1ow2snhc3nl59v3kdwfbnh11 unique (NAME);
    
alter table $DATABASE_SCHEMA.REQUEST_PARAMS 
   add constraint FKr7o056wpalrxam3mpkb2t1shl 
   foreign key (REQUEST_TYPE_ID) 
   references $DATABASE_SCHEMA.REQUEST_TYPES;
    
alter table $DATABASE_SCHEMA.REQUESTS 
   add constraint FKdwduqkdgt2ge0nnax41vegc2v 
   foreign key (LAST_MODIFIED_BY_ID) 
   references $DATABASE_SCHEMA.USERS;
    
alter table $DATABASE_SCHEMA.REQUESTS 
   add constraint FKfeh055doroq6kr7fdvsfcpuxs 
   foreign key (OWNER_ID) 
   references $DATABASE_SCHEMA.USERS;
    
alter table $DATABASE_SCHEMA.REQUESTS 
   add constraint FKd3u31aw9q2yw4iyma118g79gb 
   foreign key (REQUEST_TYPE_ID) 
   references $DATABASE_SCHEMA.REQUEST_TYPES;
    
alter table $DATABASE_SCHEMA.USERS 
   add constraint FKm7q0f31406tqqathdtcxkjv1t 
   foreign key (ADDRESS_ID) 
   references $DATABASE_SCHEMA.ADDRESSES;