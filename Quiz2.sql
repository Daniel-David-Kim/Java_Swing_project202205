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

create table osTBL(
	que varchar(200) not null primary key,
    ans text not null,
    wr1 text not null,
    wr2 text not null,
    wr3 text not null
);
insert into osTBL values('운영체제의 일반적인 역할이 아닌 것은?', ' 실행 가능한 목적(object) 프로그램 생성', '입/출력에 대한 보조역할', '자원의 효과적인 운영을 위한 스케줄링', '사용자들 간의 하드웨어의 공동 사용');
insert into osTBL values('분산처리 시스템에 대한 설명으로 옳지 않은 것은?', '중앙 집중형 시스템에 비해 시스템 개발이 용이하다.', '시스템 자원을 여러 사용자가 공유할 수 있다.', '신뢰성 및 가용성이 증진된다.', '점진적 확장이 용이하다.');
insert into osTBL values('페이지 교체기법 알고리즘 중 각 페이지마다 "Reference Bit"와 "Modified Bit"가 사용되는 것은?', '  NUR', 'LRU', 'LFU', 'FIFO');
insert into osTBL values('UNIX 파일시스템 구조에서 데이터가 저장된 블록의 시작 주소를 확인할 수 있는 블록은?', ' i-node 블록', '부트 블록', '슈퍼 블록', '데이터 블록');
insert into osTBL values('PCB(Process Control Block)가 갖고 있는 정보가 아닌 것은?', '할당되지 않은 주변 장치의 상태 정보', '스케줄링 및 프로세스의 우선순위', '프로세스 고유 식별자', '프로세스의 현재 상태');
insert into osTBL values('RR(Round-Robin) 스케줄링에 대한 설명으로 틀린 것은?', '“(대기시간＋서비스시간)/서비스시간”의 계산으로 우선순위를 처리한다.', '시간 할당이 작아지면 프로세스 문맥 교환이 자주 일어난다.', 'Time Sharing System을 위해 고안된 방식이다.', '시간 할당이 커지면 FCFS 스케줄링과 같은 효과를 얻을 수 있다.');
insert into osTBL values(' UNIX에서 사용자에 대한 파일의 접근을 제한하는데 사용되는 명령어는?', ' chmod', 'du', 'fork', 'cat');
insert into osTBL values('시스템 타이머에서 일정한 시간이 만료된 경우나 오퍼레이터가 콘솔상의 인터 럽트 키를 입력한 경우 발생하는 인터럽트는?', ' 외부 인터럽트', '프로그램 검사 인터럽트', 'SVC 인터럽트', '입·출력 인터럽트');
insert into osTBL values('유닉스의 i-node 에 포함되는 정보가 아닌 것은?', '파일이 처음 사용된 시간', '디스크 상의 물리적 주소', '파일 소유자의 사용자 식별', '파일에 대한 링크 수');
insert into osTBL values('디렉토리 구조 중 가장 간단한 형태로 같은 디렉토리에 시스템에 보관된 모든 파일 정보를 포함하는 구조는?', '  일단계 디렉토리', '트리 구조 디렉토리', '이단계 디렉토리', '비주기 디렉토리');
insert into osTBL values('운영체제에서 커널의 기능이 아닌 것은?', '사용자 인터페이스', '기억 장치 할당, 회수', '프로세스 생성, 종료', '파일 시스템 관리');

create table ctTBL(
	que varchar(200) not null primary key,
    ans text not null,
	wr1 text not null,
    wr2 text not null,
    wr3 text not null
);
insert into ctTBL values('반송파의 진폭과 위상을 상호 변환하여 신호를 전송함으로써 전송 속도를 높이는 변조 방식은?', '  QAM', 'PSK', 'FM', 'ASK');
insert into ctTBL values('OSI-7계층 중 물리 주소를 지정하고 흐름 제어 및 전송 제어를 수행하는 계층은?', '  데이터 링크 계층', '물리 계층', '세션 계층', '응용 계층');
insert into ctTBL values('패킷교환 방식에 대한 설명으로 틀린 것은?', '   노드나 회선의 오류 발생 시 다른 경로를 선택할 수 없어 전송이 중단된다.', '저장-전달 방식을 사용한다.', '전송 데이터가 많은 통신환경에 적합하다.', '패킷길이가 제한된다.');
insert into ctTBL values('공중데이터망에서 패킷형 터미널을 위한 DCE와 DTE사이의 접속규격을 나타내는 것은?', '  X.25', 'X.4', 'X.24', 'X.27');
insert into ctTBL values('HDLC 프레임 구성에서 프레임 검사 시퀸스(FCS) 영역의 기능으로 옳은 것은?', '  전송 오류 검출', ' 데이터 처리', '주소 인식', '정보 저장');
insert into ctTBL values('동기식 문자 지향 프로토콜 프레임에서 전송될 문자의 시작을 나타내는 제어 문자는?', '  STX', 'SYN', 'DLE', 'CRC');
insert into ctTBL values('데이터 전송 중 한 비트에 에러가 발생했을 경우 이를 수신측에서 정정할 목적으로 사용되는 것은?', '  Hamming code', 'Checksum', ' HRC', 'P/F');
insert into ctTBL values('X.25 프로토콜의 계층 구조에 포함되지 않는 것은?', '  네트워크 계층', '패킷 계층', '물리 계층', '링크 계층');
insert into ctTBL values('각 채널별로 타임 슬롯을 사용하나 데이터를 전송하고자 하는 채널에 대해서만 슬롯을 유동적으로 배정하며, 비트블록에 데이터뿐만 아니라 목적지 주소에 대한 정보도 포함하는 다중화방식은?', '  통계적 시분할 다중화방식', '주파수 분할 다중화방식', '코드 분할 다중화방식', '파장 분할 다중화방식');
insert into ctTBL values('HDLC(High-Level Data Link Control) 프레임형식으로 옳은 것은?', '   플래그 | 주소영역 | 제어영역 | 정보영역 | FCS | 플래그', '플래그 | 제어영역 | 주소영역 | 정보영역 | FCS | 플래그', '플래그 | 주소영역 | 정보영역 | 제어영역 | FCS | 플래그', '플래그 | 정보영역 | 제어영역 | 주소영역 | FCS | 플래그');

create table corrects(
	uid varchar(15) not null,
    os bigint not null default 0,
    ct bigint not null default 0,
    primary key(uid),
    foreign key(uid) references members(uid) on delete cascade on update cascade
);
insert into corrects values('admin1', 0, 0);
insert into corrects values('user1', 0, 0);
insert into corrects values('user2', 0, 0);
insert into corrects values('user3', 0, 0);
insert into corrects values('user4', 0, 0);

create table category(
	tname varchar(15) not null primary key,
    cname varchar(50) not null
);
insert into category values('ctTBL', '정보통신개론');
insert into category values('osTBL', '운영체제');