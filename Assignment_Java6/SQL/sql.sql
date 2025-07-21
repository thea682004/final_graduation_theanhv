create database sale_website
use sale_website

drop table accounts;
drop table categories;
drop table authorities;
drop table discounts;
drop table feedBacks;
drop table orderDetails;
drop table orders;
drop table products;
drop table roles;


create table categories(
	id int identity(1,1) primary key not null,
	name varchar(50) not null
)

create table products(
	id int identity(1,1) primary key not null,
	name nvarchar(50) not null,
	image nvarchar(30) not null,
	image1 nvarchar(30),
	image2 nvarchar(30),
	image3 nvarchar(30),
	description nvarchar(max),
	price float not null, 
	createDate date not null,
	available bit not null,
	categoryId int not null,
	foreign key(categoryId) references categories(id)
)

create table product_size(
	id int identity(1,1) primary key not null,
	size varchar(5) not null,
	quantity int not null,
	product_id int not null,
	foreign key(product_id) references products(id)
)

create table accounts(
	username varchar(20) primary key not null,
	password nvarchar(50) not null,
	email nvarchar(50) not null,
	fullname nvarchar(50),
	sdt nvarchar(12),
	address nvarchar(100),
	create_date date not null
)

create table orders(
	id bigint identity(1,1) primary key not null,
	username varchar(20) not null,
	create_date datetime not null,
	total_amount float not null,
	pay bit,
	address nvarchar(100) not null,
	phone_number nvarchar(12) not null,
	status varchar(30) not null,
	foreign key(username) references accounts(username)
)

create table orderDetails(
	id bigint identity(1,1) primary key not null,
	order_id bigint not null,
	product_id int not null,
	quantity int not null,
	size varchar(5) not null,
	reviewstatus bit,
	foreign key(order_id) references orders(id),
	foreign key(product_id) references products(id)
)

create table authorities(
	id int identity(1,1) primary key not null,
	username varchar(20) not null,
	role_id nchar(4),
	foreign key (username) references accounts(username),
	foreign key (role_id) references roles(id)
)

create table roles(
	id nchar(4) primary key not null,
	name nvarchar(15) not null
)

create table feedBacks(
	id int identity(1,1) primary key not null,
	star int not null,
	review nvarchar(255),
	reviewdate date,
	product_id int not null,
	username varchar(20) not null,
	foreign key(product_id) references products(id),
	foreign key(username) references accounts(username)
)

create table discounts(
	id int identity(1,1) primary key not null,
	name nvarchar(10) not null,
	discount int not null
)

ALTER TABLE product_size
ADD CONSTRAINT UC_ProductSize_Size_ProductId UNIQUE (size, product_id);
