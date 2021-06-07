-- auto-generated definition
create sequence SYS_WEB_LOG_ID

create table SYS_WEB_LOG
(
    ID          NUMBER not null
        primary key,
    BROSWER     VARCHAR2(50 char),
    LOGCONTENT  VARCHAR2(500 char),
    LOGLEVEL    VARCHAR2(50 char),
    IP          VARCHAR2(200 char),
    OPERATETIME VARCHAR2(50 char),
    OPERATETYPE VARCHAR2(50 char),
    LOGINNAME   VARCHAR2(50 char)
)