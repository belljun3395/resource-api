create table instance_snapshots
(
    id          bigint auto_increment
        primary key,
    instance_id bigint                                 not null comment 'foreign key referencing instance(id)',
    name        varchar(255)                        not null,
    created_at       datetime(6)  not null
);

alter table instance_snapshots auto_increment = 1000;