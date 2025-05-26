create table instance_active_logs
(
    id          bigint auto_increment
        primary key,
    instance_id bigint                                 not null comment 'foreign key referencing instance(id)',
    log         text                                not null,
    created_at       datetime(6)  not null
);

alter table instance_active_logs auto_increment = 1000;
