create table resume
(
    uuid      char(36) not null
        primary key,
    full_name text     not null
);

create table contact
(
    id          integer,
    type        text     not null,
    value       text     not null,
    resume_uuid char(36) not null
        constraint resume_uuid
            references resume
            on update restrict on delete cascade
);

create index contact_uuid_type_index
    on contact (resume_uuid);

create table section
(
    id          integer,
    type        text     not null,
    value       text     not null,
    resume_uuid char(36) not null
        constraint resume_uuid
            references resume
            on update restrict on delete cascade
);

create index section_uuid_index
    on section (resume_uuid);





