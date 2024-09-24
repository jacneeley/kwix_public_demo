CREATE TABLE IF NOT EXISTS EMPLOYEE (
	id varchar(50) not null,
	first_name varchar(100) not null,
	last_name varchar(100) not null,
	phone varchar(16) not null,
	pay decimal(19,4) not null,
	active bit not null,
	create_at datetime not null,
	update_at datetime not null,
	PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS PAYSTUB (
	paystub_num varchar(150) not null,
	employee_id varchar(50) not null,
	full_name varchar(100) not null,
	jobsite varchar(50) not null,
	pay decimal (19,4) not null,
	hours_worked decimal (8,2) not null,
	day_worked date not null,
	create_at datetime not null,
	update_at datetime not null,
	PRIMARY KEY(paystub_num)
);


CREATE TABLE IF NOT EXISTS USERS (
	user_id varchar_ignorecase(10) not null,
	user_pw varchar_ignorecase(100) not null,
	user_role varchar_ignorecase(10) not null,
	PRIMARY KEY(user_id)
);