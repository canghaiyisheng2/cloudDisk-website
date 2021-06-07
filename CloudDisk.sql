CREATE DATABASE clouddisk;

CREATE TABLE usrinfo(
    uuid varchar(40),
    usrname varchar(50),
    pwd varchar(20),
    auth varchar(10) DEFAULT 'user',
    avatar varchar(10) DEFAULT NULL,
    dirno numeric(20),
    primary key(uuid)
)default charset=utf8;

insert into usrinfo VALUES('001','admin','123','admin',null,0);

CREATE TABLE fileinfo(
    fno numeric(20),
    fname varchar(128),
    fdate datetime,
    fmsg varchar(50),
    fusr varchar(40),
    fsize numeric(15),
    path numeric(20),
    token varchar(40),
    primary key(fno),
    FOREIGN KEY(fusr) REFERENCES usrinfo(uuid) ON DELETE CASCADE ON UPDATE CASCADE
)default charset=utf8;

alter table fileinfo modify fno int auto_increment;

CREATE TABLE dirinfo(
    dno numeric(20),
    dname varchar(128),
    ddate datetime,
    dusr varchar(40),
    path numeric(20),
    primary key(dno),
    FOREIGN KEY(dusr) REFERENCES usrinfo(uuid) ON DELETE CASCADE ON UPDATE CASCADE
)default charset=utf8;

alter table dirinfo modify dno int auto_increment;

CREATE TABLE friendsinfo(
    id numeric(20),
    uid1 varchar(40),
    uid2 varchar(40),
    FOREIGN KEY(uid1) REFERENCES usrinfo(uuid) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY(uid2) REFERENCES usrinfo(uuid) ON DELETE CASCADE ON UPDATE CASCADE
)default charset=utf8;

alter table friendsinfo modify id int auto_increment;

CREATE TABLE requestinfo(
    id numeric(20),
    uid1 varchar(40),
    uid2 varchar(40),
    FOREIGN KEY(uid1) REFERENCES usrinfo(uuid) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY(uid2) REFERENCES usrinfo(uuid) ON DELETE CASCADE ON UPDATE CASCADE
)default charset=utf8;

alter table requestinfo modify id int auto_increment;

CREATE TABLE recyclebin(
    type varchar(10),
    id numeric(20),
    name varchar(128),
    uploaddate datetime,
    usr varchar(40),
    path numeric(20),
    fsize numeric(15) DEFAULT NULL,
    fmsg varchar(50) DEFAULT NULL,
    token varchar(40) DEFAULT NULL,
    primary key(type,id),
    FOREIGN KEY(usr) REFERENCES usrinfo(uuid) ON DELETE CASCADE ON UPDATE CASCADE
)default charset=utf8;