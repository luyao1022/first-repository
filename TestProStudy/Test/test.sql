create table if not exists storage_system3.material
(
    id          int auto_increment
        primary key,
    name        varchar(100) not null,
    description varchar(255) null
);

create table if not exists storage_system3.organization
(
    id          int auto_increment
        primary key,
    name        varchar(100) not null,
    description varchar(255) null
);

create table if not exists storage_system3.warehouse
(
    id              int auto_increment
        primary key,
    name            varchar(100) not null,
    organization_id int          not null,
    has_location    tinyint(1)   null comment '是否分库',
    ready_area      tinyint(1)   null,
    constraint fk_warehouse_organization
        foreign key (organization_id) references storage_system3.organization (id)
);

create table if not exists storage_system3.storage_area
(
    id             int auto_increment
        primary key,
    name           varchar(100) not null,
    warehouse_id   int          not null,
    ready_location tinyint(1)   null comment '判断是否已经分库',
    constraint fk_storage_area_warehouse
        foreign key (warehouse_id) references storage_system3.warehouse (id)
);

create table if not exists storage_system3.storage_location
(
    id          int auto_increment
        primary key,
    name        varchar(100) not null,
    area_id     int          not null,
    material_id int          not null,
    constraint area_id
        unique (area_id, material_id),
    constraint fk_storage_location_area
        foreign key (area_id) references storage_system3.storage_area (id),
    constraint fk_storage_location_material
        foreign key (material_id) references storage_system3.material (id)
);

create table if not exists storage_system3.inventory
(
    id          int auto_increment
        primary key,
    location_id int           not null,
    quantity    int default 0 not null,
    area_id     int           null comment '区域id',
    material_id int           null,
    constraint fk_inventory_location
        foreign key (location_id) references storage_system3.storage_location (id)
);

create table if not exists storage_system3.operation_log
(
    id               int auto_increment
        primary key,
    operation_type   varchar(50)                         not null,
    material_id      int                                 not null,
    from_location_id int                                 null,
    to_location_id   int                                 null,
    quantity         int                                 not null,
    operator         varchar(100)                        not null,
    operation_time   timestamp default CURRENT_TIMESTAMP not null,
    constraint fk_log_from_location
        foreign key (from_location_id) references storage_system3.storage_location (id),
    constraint fk_log_material
        foreign key (material_id) references storage_system3.material (id),
    constraint fk_log_to_location
        foreign key (to_location_id) references storage_system3.storage_location (id)
);

