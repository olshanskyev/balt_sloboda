create table IF NOT EXISTS ADDRESSES (
   id  bigserial not null,
	HOUSE_NUMBER int4 not null,
	PLOT_NUMBER int4,
	STREET varchar(256) not null,
	primary key (id)
);

create table IF NOT EXISTS REQUEST_PARAMS (
   id  bigserial not null,
	NAME varchar(64) not null,
	TYPE varchar(64) not null,
	REQUEST_TYPE_ID int8 not null,
	primary key (id)
);

create table IF NOT EXISTS REQUEST_TYPES (
   id  bigserial not null,
	DURABLE boolean not null,
	ROLES varchar(255) not null,
	NAME varchar(64) not null,
	TITLE varchar(256) not null,
	primary key (id)
);

create table IF NOT EXISTS REQUESTS (
   id  bigserial not null,
	COMMENT varchar(512) not null,
	PARAM_VALUES varchar(512),
	CREATION_DATE timestamp,
	LAST_MODIFIED_DATE timestamp,
	STATUS varchar(255) not null,
	SUBJECT varchar(256) not null,
	LAST_MODIFIED_BY_ID int8 not null,
	OWNER_ID int8 not null,
	REQUEST_TYPE_ID int8 not null,
	primary key (id)
);

create table IF NOT EXISTS USERS (
   id  bigserial not null,
	FIRST_NAME varchar(256) not null,
	LAST_NAME varchar(256) not null,
	PASSWORD varchar(256) not null,
	ROLES varchar(256) not null,
	USER_NAME varchar(256) not null,
	ADDRESS_ID int8 not null,
	primary key (id)
);

alter table REQUEST_TYPES
   drop constraint IF EXISTS UK_k1ow2snhc3nl59v3kdwfbnh11;


alter table REQUEST_TYPES
   add constraint IF NOT EXISTS UK_k1ow2snhc3nl59v3kdwfbnh11 unique (NAME);

alter table REQUEST_PARAMS
   add constraint IF NOT EXISTS FKr7o056wpalrxam3mpkb2t1shl
   foreign key (REQUEST_TYPE_ID)
   references REQUEST_TYPES;

alter table REQUESTS
   add constraint IF NOT EXISTS FKdwduqkdgt2ge0nnax41vegc2v
   foreign key (LAST_MODIFIED_BY_ID)
   references USERS;

alter table REQUESTS
   add constraint IF NOT EXISTS FKfeh055doroq6kr7fdvsfcpuxs
   foreign key (OWNER_ID)
   references USERS;

alter table REQUESTS
   add constraint IF NOT EXISTS FKd3u31aw9q2yw4iyma118g79gb
   foreign key (REQUEST_TYPE_ID)
   references REQUEST_TYPES;

alter table USERS
   add constraint IF NOT EXISTS FKm7q0f31406tqqathdtcxkjv1t
   foreign key (ADDRESS_ID)
   references ADDRESSES;