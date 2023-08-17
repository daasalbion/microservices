drop table if exists users;
drop sequence if exists users_id_seq;
drop sequence if exists ix_users_username;

drop table if exists roles;
drop sequence if exists roles_id_seq;

drop table if exists users_roles;
drop sequence if exists users_roles_id_seq;
drop index if exists ix_users_roles_user_id;

--truncate flyway_schema_history;