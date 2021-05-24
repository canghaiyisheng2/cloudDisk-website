CREATE DATABASE clouddisk;

CREATE TABLE usrinfo(
    uuid varchar(40),
    usrname varchar(50),
    pwd varchar(20),
    auth varchar(10) DEFAULT 'user',
    avatar varchar(10) DEFAULT NULL,
    primary key(uuid)
)default charset=utf8;

insert into usrinfo VALUES('001','admin','123','admin',null);

CREATE TABLE fileinfo(
    fno numeric(20),
    fname varchar(128),
    fdate datetime,
    fmsg varchar(50),
    fusr varchar(50),
    primary key(fno)
)default charset=utf8;

alter table fileinfo modify fno int auto_increment;