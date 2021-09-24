
create table IF NOT EXISTS ADDRESSES (
   id  bigserial not null,
	HOUSE_NUMBER int4 not null,
	PLOT_NUMBER int4,
	STREET varchar(256) not null,
	primary key (id)
);

create table IF NOT EXISTS CALENDAR_SELECTION (
   id  bigserial not null,
    MONTH_DAYS varchar(512),
    SELECTED_DAYS varchar(512),
    SELECTION_MODE varchar(64) not null,
    WEEK_DAYS varchar(256),
    primary key (id)
);

create table IF NOT EXISTS REQUEST_PARAMS (
   id  bigserial not null,
	COMMENT varchar(128),
	DEFAULT_VALUE varchar(256),
	ENUM_VALUES varchar(512),
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
	ASSIGN_TO_ID int8 not null,
	DISPLAY_OPTIONS varchar(512),
	CALENDAR_SELECTION_ID int8,
	SYSTEM_REQUEST boolean not null,
	REQUEST_ID_PREFIX varchar(3) not null,
	primary key (id)
);

create table IF NOT EXISTS REQUESTS (
   id  bigserial not null,
	COMMENT varchar(512),
	CREATION_DATE timestamp,
	LAST_MODIFIED_DATE timestamp,
	PARAM_VALUES varchar(512),
	STATUS varchar(255) not null,
	CALENDAR_SELECTION_ID int8,
	LAST_MODIFIED_BY_ID int8,
	OWNER_ID int8 not null,
	ASSIGNED_TO_ID int8 not null,
	REQUEST_TYPE_ID int8 not null,
	GENERATED_IDENTIFIER varchar(10) not null,
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

create table IF NOT EXISTS REQUESTS_LOG (
       id  bigserial not null,
        ITEM_NAME varchar(255) not null,
        MODIFIED_DATE timestamp,
        NEW_VALUE varchar(256),
        PREV_VALUE varchar(256),
        MODIFIED_BY_ID int8,
        REQUEST_ID int8 not null,
        primary key (id)
);

alter table REQUESTS_LOG
   add constraint IF NOT EXISTS FKc06gyc8lj1gphpmwbh2714x7b
   foreign key (MODIFIED_BY_ID)
   references USERS;

alter table REQUESTS_LOG
   add constraint IF NOT EXISTS FKbvx4xlk7phbed3wmo2bpnl6cb
   foreign key (REQUEST_ID)
   references REQUESTS;

alter table REQUEST_TYPES
   add constraint IF NOT EXISTS UK_k1ow2snhc3nl59v3kdwfbnh11 unique (NAME);

alter table USERS
   add constraint IF NOT EXISTS UK_21q8fvry4wix31petp1awxsx9 unique (USER_NAME);

alter table REQUEST_PARAMS
   add constraint IF NOT EXISTS FKr7o056wpalrxam3mpkb2t1shl
   foreign key (REQUEST_TYPE_ID)
   references REQUEST_TYPES;

alter table REQUEST_TYPES
   add constraint IF NOT EXISTS FKljexdsmuw47pm59ftv8dlyrhf
   foreign key (ASSIGN_TO_ID)
   references USERS;

alter table REQUEST_TYPES
   add constraint IF NOT EXISTS FKoe6uxgp58h1bql73tq6ffj6te
   foreign key (CALENDAR_SELECTION_ID)
   references CALENDAR_SELECTION;

alter table REQUESTS
   add constraint IF NOT EXISTS FKdwduqkdgt2ge0nnax41vegc2v
   foreign key (LAST_MODIFIED_BY_ID)
   references USERS;

alter table REQUESTS
   add constraint IF NOT EXISTS FKfeh055doroq6kr7fdvsfcpuxs
   foreign key (OWNER_ID)
   references USERS;

alter table REQUESTS
   add constraint IF NOT EXISTS FKd3xrr7siw4p7lj946u5au8wv0
   foreign key (ASSIGNED_TO_ID)
   references USERS;

alter table REQUESTS
   add constraint IF NOT EXISTS FKbnmklf2ehuv88h1ejpsphn8a5
   foreign key (REQUEST_TYPE_ID)
   references REQUEST_TYPES;

alter table REQUESTS
   add constraint IF NOT EXISTS FKh1n8wmnnq3t5189iqifpoakd
   foreign key (CALENDAR_SELECTION_ID)
   references CALENDAR_SELECTION;

alter table RESIDENTS
   add constraint IF NOT EXISTS FKq1ajexn440ss5h4lp3e5nw96j
   foreign key (ADDRESS_ID)
   references ADDRESSES;

alter table RESIDENTS
   add constraint IF NOT EXISTS FKmoksj6gw3xcn8phy205yrkaja
   foreign key (USER_ID)
   references USERS;