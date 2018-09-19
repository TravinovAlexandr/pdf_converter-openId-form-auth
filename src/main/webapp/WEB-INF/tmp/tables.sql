# create table role (
#   id bigint(2) not null auto_increment,
#   name varchar(20) not null unique ,
#   primary key (id)) engine = InnoDB;
#
# create table user (
#   id bigint(20) not null auto_increment,
#   nick varchar(25) not null,
#   email varchar(255) not null,
#   password varchar(255) not null,
#   creation_date date not null,
#   creation_time time not null,
#   primary key(id)
#   ) engine = InnoDB;
#
# create table user_roles(
#   user_id bigint(20) not null,
#   role_id bigint(20) not null,
#   key frk_user (user_id),
#   key frk_role (role_id),
#   constraint frk_user foreign key (user_id) references user(id),
#   constraint frk_role foreign key (role_id) references role(id)
#   ) engine = InnoDB;

# alter table role remove unique (id);
# alter table role add unique (id);

# insert into role (name) values ('ROLE_ADMIN');
# insert into role (name) values ('ROLE_P_USER');

