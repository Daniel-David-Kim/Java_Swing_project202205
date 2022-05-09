create database Quiz;
use Quiz;

create table members(
	uid varchar(15) not null,
    upw varchar(25) not null,
    uname varchar(20) not null,
    ufindq text not null,
    ufinda text not null,
    uclass int not null,
    utel varchar(15) not null,
    primary key(uid)
);

insert into members values('admin1', '01234', '관리쟝', '나는 누구일까?', '관리자', 0, '010-1000-1111');
insert into members values('user1', '1234', '김지원', '1.나는 누구일까?', '첫유저', 1, '010-1111-2222');
insert into members values('user2', '2345', '구준모', '2.나는 누구일까?', '둘유저', 1, '010-2222-3333');
insert into members values('user3', '3456', '이계영', '3.나는 누구일까?', '셋유저', 1, '010-3333-4444');
insert into members values('user4', '4567', '김지원', '4.나는 누구일까?', '넷유저', 1, '010-4444-5555');


select * from members;