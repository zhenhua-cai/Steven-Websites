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
    createDateTime datetime not null,
    enabled tinyint(1) not null,
    `locked` tinyint(1) not null
)engine=InnoDB auto_increment=1 default charset=latin1;

insert into users(username, email, password, enabled, locked, createDateTime) 
values("stevencai","steven.cai1990@hotmail.com","$2a$10$1Y9D9WGMSIOInmW2eCpEgenk89VJNClvQBARyzF3ZBX6z/6bh7Hy6",1,0, now());

drop table if exists roles;
create table roles(
	id int primary key not null auto_increment,
    `title` varchar(50) unique not null
)engine=InnoDB auto_increment=1 default charset = latin1;

insert into roles(title) values("ROLE_ADMIN");
insert into roles(title) values("ROLE_USER");
insert into roles(title) values("ROLE_WRITER");


DROP TABLE IF EXISTS `authorities`;
CREATE TABLE `authorities` (
  `userId` int NOT NULL,
  `roleId` int NOT NULL,
  UNIQUE KEY `authorities_idx_1` (`userId`,`roleId`),
  CONSTRAINT `authorities_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `users` (`id`),
  constraint `authorities_dbfk_2` foreign key (`roleId`) references `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

insert into authorities values(1,1);
insert into authorities values(1,3);

drop table if exists verification_token;
create table verification_token(
	id int primary key not null auto_increment,
    token varchar(256) unique not null,
    userId int not null,
    `type` varchar(20) not null,
    expireDate datetime not null,
    createData datetime not null,
    constraint `verificationToken_idfk_1` foreign key(`userId`) references users(id)
)ENGINE=InnoDB DEFAULT CHARSET=latin1;

drop table if exists article_categories;
create table article_categories(
	id int primary key not null auto_increment,
    category varchar(20) not null
)ENGINE=InnoDB DEFAULT CHARSET=latin1;

drop table if exists article_keywords;
create table article_keywords(
	id int primary key not null auto_increment,
    keyword varchar(30) unique not null
)ENGINE=InnoDB DEFAULT CHARSET=latin1;

drop table if exists article;
create table article(
	id varchar(256) primary key not null,
	title varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci not null,
    summary varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci default null,
    `path` varchar(256) not null,
    createDateTime datetime not null,
    lastModifiedDateTime datetime not null,
    private tinyint(1) default 0,
    userId int not null,
    constraint `user_post_idfk` foreign key(`userId`) references users(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

drop table if exists article_draft;
create table article_draft(
	id varchar(256) primary key not null,
    title varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci default null,
    summary varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci default null,
    `path` varchar(256) not null,
    createDateTime datetime not null,
    lastModifiedDateTime datetime not null,
    userId int not null,
    constraint `user_postDraft_idfk` foreign key(`userId`) references users(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;