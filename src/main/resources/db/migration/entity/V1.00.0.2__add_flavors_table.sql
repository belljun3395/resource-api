create table flavors
(
    id bigint auto_increment  primary key,
    name           varchar(255) not null,
    description    varchar(255) not null default '',
    vCPU           float          not null,
    memory         float          not null,
    root_disk_size float          not null,
    created_at       datetime(6)  not null
);

alter table flavors auto_increment = 1000;
alter table flavors
    add constraint uq_flavors_name unique (name);
