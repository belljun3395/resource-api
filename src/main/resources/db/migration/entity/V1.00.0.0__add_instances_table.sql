create table instances
(
    id           bigint auto_increment  primary key,
    name         varchar(255) not null,
    alias        varchar(255) not null,
    description  varchar(255) not  null default '',
    power_status     varchar(255) not null comment 'application-level enum: PAUSED, SHUTDOWN, CRASHED, NOSTATE, RUNNING, SUSPEN',
    host         varchar(255) not null,
    flavor_id    bigint          not null comment 'foreign key referencing flavor(id)',
    source_type      varchar(255) not null comment 'application-level enum: IMAGE, INSTANCE_SNAPSHOT, BOOTABLE_VOLUME, VOLUME_SNAPSHOT',
    source_target_id bigint not null,
    created_at       datetime(6)  not null,
    updated_at       datetime(6)  not null,
    deleted          bool default false not null
);

alter table instances auto_increment = 1000;
alter table instances add constraint uq_instance_alias unique (alias);
alter table instances add constraint  uq_source unique (source_type, source_target_id);