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

create table if not exists tb_category(
	id_category int not null auto_increment primary key,
    description varchar(255) not null,
    id_user int not null,
    
    constraint FK_category_user foreign key(id_user) references tb_user(id_user)
);

create table if not exists tb_project(
	id_project int not null auto_increment primary key,
    title varchar(255) not null,
    description varchar(255) not null,
    last_modification timestamp not null,
    start_date timestamp not null,
    finish_date timestamp not null,
    stats tinyint not null,
    id_user int not null,
    
	constraint FK_project_user foreign key(id_user) references tb_user(id_user)
);

create table if not exists tb_activity(
	id_activity int not null auto_increment primary key,
    title varchar(255) not null,
    estimated_time varchar(255) not null,
    description varchar(255) not null,
    stats tinyint not null,
    real_time timestamp not null,
    priority tinyint not null,
    last_modification timestamp not null,
    id_user int not null,
    id_project int not null,
    id_category int not null,
    
    constraint FK_activity_user foreign key (id_user) references tb_user(id_user),
    constraint FK_activity_project foreign key(id_project) references tb_project(id_project),
    constraint FK_activity_category foreign key(id_category) references tb_category(id_category)
);

create table if not exists tb_execution_time(
	id_execution_time int not null auto_increment primary key,
    start_date timestamp not null,
    finish_date timestamp not null,
    id_activity int not null,
    
    constraint FK_execution_time_activity foreign key(id_activity) references tb_activity(id_activity)
);
insert into tb_user values(null,'Bruno Cardoso Ambrosio','1998-12-11',null,null,0,null);
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