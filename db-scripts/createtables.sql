drop database if exists stevenblog;
create database if not exists stevenblog;
use stevenblog;

drop table if exists users;
create table users(
	id int primary key not null auto_increment,
    username varchar(50) unique not null,
	`password` char(68) not null,
	email varchar(128) unique not null,
	firstName varchar(50) default null,
    lastName varchar(50) default null,
    enabled tinyint(1) not null,
    `locked` tinyint(1) not null
)engine=InnoDB auto_increment=1 default charset=latin1;

drop table if exists roles;
create table roles(
	id int primary key not null auto_increment,
    `title` varchar(50) unique not null
)engine=InnoDB auto_increment=1 default charset = latin1;

insert into roles(title) values("ROLE_ADMIN");
insert into roles(title) values("ROLE_USER");

DROP TABLE IF EXISTS `authorities`;
CREATE TABLE `authorities` (
  `userId` int NOT NULL,
  `roleId` int NOT NULL,
  UNIQUE KEY `authorities_idx_1` (`userId`,`roleId`),
  CONSTRAINT `authorities_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `users` (`id`),
  constraint `authorities_dbfk_2` foreign key (`roleId`) references `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

drop table if exists verificationToken;
create table verificationToken(
	id int primary key not null auto_increment,
    token varchar(256) unique not null,
    userId int not null,
    `type` varchar(20) not null,
    expireDate datetime not null,
    createData datetime not null,
    constraint `verificationToken_idfk_1` foreign key(`userId`) references users(id)
)