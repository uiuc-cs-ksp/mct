-- This script file can serve as an example of a minimal self-contained database initializer for MCT.

drop table if exists component_info;
drop table if exists database_identification;
drop table if exists proxy_component_spec;
drop table if exists component_discipline;
drop table if exists component_spec;
drop table if exists mct_users;
drop table if exists disciplines;

commit;

set session storage_engine = InnoDB;

-- database identification

create table database_identification ( 
     name varchar(80) NOT NULL,  
     value varchar(1000) NOT NULL, 
     obj_version int default '0' NOT NULL,
     PRIMARY KEY(name)
);

-- The "values" line below must be on one line so the revision can be
-- determined automatically during the build!
insert into database_identification(name,value)
values ('schema_id', '1');

-- users, disciplines and components -----------------------------------------------

create table disciplines(
    discipline_id varchar(50) NOT NULL,
    description varchar(1000),
    program varchar(50),
    obj_version int default '0' NOT NULL,
    PRIMARY KEY(discipline_id)
);

create table mct_users(
    user_id varchar(20) NOT NULL,
    firstname varchar(50),
    lastname varchar(50),
    discipline_id varchar(50) NOT NULL,
    obj_version int default '0' NOT NULL,
    foreign key(discipline_id) references disciplines(discipline_id),
    PRIMARY KEY (user_id)
);

create table component_spec(
    component_id varchar(32) CHARACTER SET ASCII NOT NULL,
    component_name varchar(200) NOT NULL,
    creator_user_id varchar(20) not null,
    owner varchar(40) NOT NULL,
    date_created TIMESTAMP NULL, 
    external_key varchar(256) CHARACTER SET ascii COLLATE ascii_bin,
    last_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    component_type varchar(150) NOT NULL,
    model_info longtext,
    obj_version int default '0' NOT NULL,
    PRIMARY KEY(component_id)
);

create index component_name_index on component_spec (component_name);
create index last_modified_index on component_spec (last_modified);
create unique index unique_external_key_index on component_spec (external_key,component_type);

create table component_relationship(
    component_id varchar(32) CHARACTER SET ASCII NOT NULL,
    associated_component_id varchar(32) CHARACTER SET ASCII NOT NULL,
    seq_no int not null,
    foreign key (component_id) references component_spec(component_id) ON DELETE CASCADE,
    foreign key(associated_component_id) references component_spec(component_id) ON DELETE CASCADE
);

create table tag(
	tag_id varchar(100) NOT NULL,
	tag_property varchar(200),
	obj_version int default '0' NOT NULL,
	PRIMARY KEY (tag_id)
);

create table tag_association(
	component_id varchar(32) CHARACTER SET ASCII,
	tag_id varchar(100) NOT NULL,
	tag_property varchar(200),
	PRIMARY KEY (component_id, tag_id),
	foreign key (component_id) references component_spec(component_id) ON DELETE CASCADE,
	foreign key (tag_id) references tag(tag_id)
);

create table view_state(
    component_id varchar(32) CHARACTER SET ASCII not null,
    view_type varchar(150) not null,
    view_info longtext not null,
    foreign key(component_id) references component_spec(component_id) ON DELETE CASCADE,
    PRIMARY KEY (component_id, view_type)
);

-- create users -----------------------------------------------
INSERT INTO disciplines VALUES ('admin','MCT Administrator Group','',0),('user','All Users','',0);

insert into mct_users values ('admin','MCT','Administrator','admin',0),('user','','','user',0);

-- create bootstrap tags
insert into tag values('bootstrap:admin', null, 0);
insert into tag values('bootstrap:creator', null, 0);

-- create user Sandbox -----------------------------------------------
insert into component_spec (component_name, component_type, model_info, owner, component_id, creator_user_id, date_created) values ('My Sandbox', 'gov.nasa.arc.mct.core.components.MineTaxonomyComponent', null, 'user', 'dd0d6ad6da604364b98bdbfbcbe07f8b', 'user', NOW());
insert into tag_association (component_id, tag_id) select component_id, 'bootstrap:creator' from component_spec where component_id = 'dd0d6ad6da604364b98bdbfbcbe07f8b';

insert into component_spec (component_name, component_type, model_info, owner, component_id, creator_user_id, date_created) values ('My Sandbox', 'gov.nasa.arc.mct.core.components.MineTaxonomyComponent', null, 'admin', '046fa84851e04cbaa223a97adbe4cbee', 'admin', NOW());
insert into tag_association (component_id, tag_id) select component_id, 'bootstrap:creator' from component_spec where component_id = '046fa84851e04cbaa223a97adbe4cbee';
