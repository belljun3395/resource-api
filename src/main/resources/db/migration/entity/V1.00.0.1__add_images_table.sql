create table images
(
    id   bigint auto_increment
        primary key,
    name varchar(255) not null,
    created_at       datetime(6)  not null
);

alter table images auto_increment = 1000;
alter table images
    add constraint uq_images_name unique (name);