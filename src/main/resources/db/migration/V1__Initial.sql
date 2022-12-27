create table role
(
    id          bigserial
        primary key,
    name        varchar(64)                            not null
        constraint idx_t_role_role_name_unq
            unique,
    description text        default ''::text,
    enabled     boolean     default true,
    deleted     boolean     default false              not null
);

create table "user"
(
    id           bigserial
        primary key,
    email        varchar(512)                           null,
    role_id      bigint                                 not null
        constraint fk_user_role_id
            references role,
    username  varchar(255) null,
    is_confirmed boolean     default false,
    created_at                timestamp null,
    created_by                varchar(255) null,
    updated_at                timestamp null,
    updated_by                varchar(255) null
);

create table permission
(
    id              bigserial
        primary key,
    permission_code varchar(32) not null
        constraint idx_t_system_permission_permission_code_unq
            unique,
    name            varchar(64) not null
        constraint idx_permission_name_unq
            unique,
    description     text    default ''::text,
    enabled         boolean default true
);

create table role_to_permission
(
    id            bigserial
        primary key,
    role_id       bigint not null
        constraint fk_role_to_permission_role_id
            references role,
    permission_id bigint not null
        constraint fk_role_to_permission_permission_id
            references permission
);

INSERT INTO permission (name, permission_code, description) values ('user', 'user_perm', 'Default access to be authorized'),
                                                                     ('admin', 'admin_perm', 'Admin access');


INSERT INTO role (name, description) values ('admin', 'User to access everything'),
                                              ('user', 'Access to get requests');

WITH
    admin as (select id from role where name = 'admin'),
    default_perm as (select id from permission where permission_code = 'user_perm'),
    write_product as (select id from permission where permission_code = 'admin_perm')
INSERT into role_to_permission (role_id, permission_id) VALUES ((table admin), (table default_perm)),
                                                                 ((table admin), (table write_product));


WITH
    userc as (select id from role where name = 'user'),
    default_perm as (select id from permission where permission_code = 'user_perm')
INSERT into role_to_permission (role_id, permission_id) VALUES ((table userc), (table default_perm));


