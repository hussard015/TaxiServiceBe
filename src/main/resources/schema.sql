CREATE TABLE IF NOT EXISTS hibernate_sequence (
    next_val bigint
);

insert into hibernate_sequence values ( 1 );

create table IF NOT EXISTS user (
    id bigint not null,
    created_date datetime not null,
    modified_date datetime,
    password varchar(255) not null,
    user_type integer not null,
    username varchar(255) not null,
primary key (id));

create table IF NOT EXISTS assignment (
	id bigint not null primary key,
	created_date datetime not null,
	modified_date datetime,
	address varchar(100) not null,
	complete_dt datetime,
	passenger_id bigint not null,
    driver_id bigint,
	request_dt datetime,
	status integer,
	FOREIGN KEY (passenger_id) references user,
	FOREIGN KEY (driver_id) references user
);
