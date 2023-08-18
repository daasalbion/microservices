-- V1

CREATE SEQUENCE user_id_seq START 1;

create table users
(
    id        integer default nextval('user_id_seq'::regclass) not null primary key,
    full_name varchar(255)                                     not null,
    username  varchar(50)                                      not null,
    password  varchar(500)                                     not null
);

CREATE UNIQUE INDEX ix_user_username on users (username);