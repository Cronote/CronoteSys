create database cronote default charset utf8;
use cronote;
create table if not exists tb_user(
	id_user int not null auto_increment primary key,
    complete_name varchar(255) not null,
    birth_date date not null,
    register_date timestamp default now(),
    email_recover varchar(255) default '---',
    stats tinyint default 1,
    avatar_path varchar(255) default '----'
);

create table if not exists tb_login(
	id_login int not null auto_increment primary key,
    email varchar(255) not null,
    passwd varchar(255) not null,
	id_user int not null,
    
    constraint FK_login_user foreign key (id_user) references tb_user(id_user)
);

create table if not exists tb_user_bussiness(
	id_user_bussiness int not null auto_increment primary key,
    id_user int not null,
    
    constraint FK_bussinessuser_user foreign key(id_user) references tb_user(id_user)
);

drop table tb_category;
Create table if not exists tb_Category(
	id_category int not null auto_increment primary key,
    title varchar(50),
    id_user int not null,
    
    constraint fk_categoria_user foreign key(id_user) references tb_user(id_user) 
);

drop table tb_activity;
Create table if not exists tb_activity(
	id_activity int not null auto_increment primary key,
    title varchar(50),
    description varchar(256),
    
    status int,
    Last_modification timestamp default now()

	
);

select * from tb_user;
select * from tb_login;

insert into tb_user values(null,'Bruno Cardoso Ambrosio','1998-12-11',null,null,1,null);
describe tb_login;
insert into tb_login values(null,'a','123',1);

/*select current_date();
select date_format(now(),"%d/%m/%Y") as a_ from tb_user;

select * from tb_user;
select * from tb_login;
describe tb_user;

select id_login as id, email, passwd,u.stats
from tb_login l
inner join tb_user u on u.id_user = l.id_user;


;
drop database cronote;

drop table user_table;
drop table login_table;
drop table user_photo;