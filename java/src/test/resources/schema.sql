
create table IF NOT EXISTS ADDRESSES (
   id  bigserial not null,
	HOUSE_NUMBER int4 not null,
	PLOT_NUMBER int4,
	STREET varchar(256) not null,
	primary key (id)
);

create table IF NOT EXISTS REQUEST_PARAMS (
   id  bigserial not null,
	COMMENT varchar(128),
	DEFAULT_VALUE varchar(256),
	NAME varchar(64) not null,
	OPTIONAL boolean not null,
	TYPE varchar(64) not null,
	REQUEST_TYPE_ID int8 not null,
	primary key (id)
);

create table IF NOT EXISTS REQUEST_TYPES (
   id  bigserial not null,
	DURABLE boolean not null,
	NAME varchar(64) not null,
	ROLES varchar(256) not null,
	TITLE varchar(256) not null,
	DESCRIPTION varchar(512),
	primary key (id)
);

create table IF NOT EXISTS REQUESTS (
   id  bigserial not null,
	COMMENT varchar(512),
	CREATION_DATE timestamp,
	LAST_MODIFIED_DATE timestamp,
	PARAM_VALUES varchar(512),
	STATUS varchar(255) not null,
	SUBJECT varchar(256) not null,
	LAST_MODIFIED_BY_ID int8 not null,
	OWNER_ID int8 not null,
	REQUEST_TYPE_ID int8 not null,
	primary key (id)
);

create table IF NOT EXISTS RESIDENTS (
   id  bigserial not null,
	FIRST_NAME varchar(256) not null,
	LAST_NAME varchar(256) not null,
	ADDRESS_ID int8 not null,
	USER_ID int8 not null,
	primary key (id)
);


create table IF NOT EXISTS USERS (
   id  bigserial not null,
	PASSWORD varchar(256) not null,
	ROLES varchar(256) not null,
	USER_NAME varchar(256) not null,
	PASSWORD_RESET_TOKEN varchar(256),
	primary key (id)
);

alter table REQUEST_TYPES
   add constraint IF NOT EXISTS UK_k1ow2snhc3nl59v3kdwfbnh11 unique (NAME);

alter table USERS
   add constraint IF NOT EXISTS UK_21q8fvry4wix31petp1awxsx9 unique (USER_NAME);

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
   add constraint IF NOT EXISTS FKbnmklf2ehuv88h1ejpsphn8a5
   foreign key (REQUEST_TYPE_ID)
   references REQUEST_TYPES;

alter table RESIDENTS
   add constraint IF NOT EXISTS FKq1ajexn440ss5h4lp3e5nw96j
   foreign key (ADDRESS_ID)
   references ADDRESSES;

alter table RESIDENTS
   add constraint IF NOT EXISTS FKmoksj6gw3xcn8phy205yrkaja
   foreign key (USER_ID)
   references USERS;