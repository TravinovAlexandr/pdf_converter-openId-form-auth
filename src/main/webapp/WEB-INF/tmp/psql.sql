-- create sequence role_pk;

-- create table role (
--   id   bigint      not null default nextval('role_pk'),
--   name varchar(20) not null,
--   primary key (id)
-- );

-- create sequence user_pk;

-- create table user_table (
--   id            bigint       not null default nextval('user_pk'),
--   nick          varchar(50)  not null,
--   email         varchar(255) not null,
--   password      varchar(255) not null,
--   creation_date date         not null,
--   creation_time time         not null,
--   primary key (id)
-- );

-- create table user_roles (
--   user_id  bigint not null references user_table (id),
--   role_id  bigint not null references role (id)
--   );

-- insert into role (name) values ('ROLE_ADMIN');
-- insert into role (name) values ('ROLE_P_USER');

alter table user add column name character varying(50);
alter table user add column last_name character varying(50);

