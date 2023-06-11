## 개요
Spring을 이용한 프로젝트를 하기 앞서 프로젝트에 도움을 주고자 해당 시나리오를 작성해 실시간 알림과 채팅을 구현하였습니다.<br><br>
시나리오 대로 구현한 실시간 스터디 채팅 및 실시간 알림에 대한 상세 내용는 <a href="https://silicon-vegetable-8cc.notion.site/WebSocket-29d0826c86ff4433ae21a7df0aa604fb?pvs=4">Notion</a>을 통해 정리하였습니다!
## 웹소켓 기본흐름
1. 클라이언트에서 웹소켓에 접속 (**`onopen메서드`**)
2. 웹소켓 Handler는 `afterConnectionEstablished` 메서드를 통해 접속한 세션을 만들어 놓은 컬렉션에 담는다.
    1. `Map<Integer, ArrayList<WebSocketSession>> roomList` 
        
        → 접속한 유저가 속해있는 채팅방 번호를 key로 하여 비어있는 ArrayList를 value로 채워 넣는다.
        
    2. `private Map<WebSocketSession, String> userSessionMap`
        
        → 해당 key는 onopen한 유저의 WebSocketSession값을 넣고 value는 로그인한 유저의 HttpSession에 담긴 userId를 담는다.
        
    3. `private List<WebSocketSession> sessionList` 
        
        → 로그인한 전체 session을 담는 List
        
3. 클라이언트에서 메시지를 보낸다 (**`send메서드`**)
4. 웹소켓 Handler는 메세지가 수신되면 (handleTextMessage) 메서드를 통해 전체 세션 또는 특정 세션에 메시지를 보내준다.
5. 클라이언트에서 수신된 메시지를 받는다(onmessage메서드)
6. 클라이언트에서 연결이 종료된다.
7. 웹소켓Handler는 연결이 종료되면** `afterConnectionClosed` 메서드에서 해당 세션을 삭제한다.


## 시나리오
### ✅ **실시간 알림 (polling 방식)**

1. 스터디 모집 게시글 작성자가 게시글을 작성하여 DB에 insert 됐을 때 알림 테이블에도 작성자를 제외한 모든 유저에게 새 알림 row가 insert된다.
2. 로그인 시 header에서 alarm 테이블에 해당 유저의 알람을 가져오기 위해 비동기 방식 이용(AJAX)
3. setInterval 을 이용하여 해당 알람을 가져오는 AJAX를 3초 주기로 가져와서 알림에 뿌려 알림 아이콘을 빨간색으로 활성화 시켜준다.
4. 해당 알림을 클릭해 이동하면 해당 알림은 삭제된다. ( DB에 해당 알림 row는 delete 된다.)

### ✅ **실시간 채팅 (websocket 방식)**

1. 스터디 모집 게시글을 작성하면 게시글 작성자는 스터디장이 되며 본인만 있는 채팅방이 생성된다. ( `studygroup` 과 `studymember`이 1:N 관계로 작성자는 자동으로 `studymember`에 해당 `studygroup`번호와 역할이 주어진다)
2. 스터디장을 제외한 누군가 가입신청을하면 해당 `studygroup`에대한 `studymember`에서 스터디원으로 삽입된다.
3. 가입과 동시에 해당 채팅방에 속해진다.
4. 최초 로그인시 websocket이 `onopen` 되어 해당 유저가 속한 채팅방을 `roomList` map에 채운다. (value는 비어있는 list만 채워넣는다)
5. 그리고 해당 session과 실제 userId와 대응하기 위해 `userSessionMap` 도 채우며, 전체 session을 저장하기 위한 `sessionList` 에도 로그인한 session을 저장한다.
6. 채팅방에 입장 하기 전엔 `roomList`에 value에는 빈 List가 들어가 있으며 입장 하면 해당 session이 List에 추가된다. 
7. 닫기 버튼을 누르면 들어가 있던 List에 해당 session을 지우고 다른 방으로 이동하면 이전에 있던 방에 List에 session을 지우고 다시 접속한 새로운 채팅방에 대한 List에 session을 추가한다.
8. 채팅 메시지를 입력 시 해당 읽지 않은 메시지에 대한 메시지 개수가 채팅방 별로 표시되며 상단에 채팅방 버튼에는 전체 안읽은 개수가 실시간으로 표시된다.
9. 안읽은 메시지가 있는 채팅방 접속시 해당 안읽은 메시지 개수가 사라진다.
10. 이미 서로 채팅방에 접속한 상태에서 메시지를 주고 받으면 메시지는 실시간으로 읽음 처리가 되어 있다.
11. 단체 채팅 메시지 별로 읽음 처리횟수를 표시하기 위해 `chatmessage`와 1:N 연결되어 있는 `unreadmember`에는 `chatmessage`에 메시지를 insert할때 마다 해당 테이블에 해당 채팅방에 속해있는 인원 모두에 대한 row가 insert된다. 
12. 해당 메시지를 읽은 유저가 있을 시 `chatmessage`에 `unread_message` 컬럼은 -1씩 되며 해당 `unreadmember`에 있는 해당 방에 유저의 row를 delete 시키므로써 특정 유저가 읽었을 때 중복 읽음 처리가 안되게 하여 채팅 메시지 별로 읽음 처리가 가능하다.

## DB 설계
### ✅ 해당 시나리오의 ERD 설계도
![websocketERD](https://github.com/pparkjs/websocket_chat/assets/107859870/8f2b057f-b622-4076-a6e5-af7e247ff80c)


### ✅ chatuser, studygroup, studymember, chatmessage, alarm, unreadmember 의 TABLE 설계


### CHATUSER TABLE
```
  CREATE TABLE CHATUSER(
    USER_ID VARCHAR2(36),
    USER_NAME VARCHAR2(100) UNIQUE NOT NULL,
    USER_PASSWORD VARCHAR2(100) NOT NULL,
    USER_PIC VARCHAR2(100) DEFAULT 'default.jpg',
    CONSTRAINT PK_CHATUSER PRIMARY KEY (USER_ID)
  );
```
### STUDYGROUP TABLE
```
CREATE TABLE STUDYGROUP(
    ST_NO NUMBER(8) NOT NULL,
    ST_TITLE VARCHAR2(400) NOT NULL,
    ST_LEADER VARCHAR2(36) NOT NULL,
    ST_DATE DATE NOT NULL,
		USER_NAME VARCHAR2(100) NULL,
    CONSTRAINT PK_STUDYGROUP PRIMARY KEY(ST_NO)
);

CREATE SEQUENCE SEQ_STUDYGROUP INCREMENT BY 1 START WITH 1 NOCACHE;
```
### STUDYMEMBER TABLE
```
CREATE TABLE STUDYMEMBER(
    ST_NO NUMBER(8) NOT NULL,
    USER_ID VARCHAR2(36) NOT NULL,
    USER_NAME VARCHAR2(100) NOT NULL,
		AUTH_ROLE VARCHAR2(50) NULL,
		MSG_COUNT NUMBER DEFAULT 0,
    CONSTRAINT FK_STUDYMEMBER_ST_NO FOREIGN KEY(ST_NO)
        REFERENCES STUDYGROUP(ST_NO)
);
```
### CHATMESSAGE TABLE
```
CREATE TABLE CHATMESSAGE(
    MESSAGE_ID NUMBER,
    ST_NO VARCHAR2(36),
    MESSAGE_CONTENT VARCHAR2(4000),
    MESSAGE_REGDATE DATE DEFAULT SYSDATE,
    USER_ID VARCHAR2(36) NOT NULL,
    UNREAD_COUNT NUMBER,
    CONSTRAINT PK_CHATMESSAGE PRIMARY KEY (MESSAGE_ID),
		CONSTRAINT FK_CHATMESSAGE_ST_NO FOREIGN KEY(ST_NO)
        REFERENCES STUDYGROUP(ST_NO)
);
CREATE SEQUENCE SEQ_CHATMESSAGE INCREMENT BY 1 START WITH 1 NOCACHE;
```
### ALARM TABLE
```
CREATE TABLE ALARM(
    ALARM_NO NUMBER(8) NOT NULL,
    USER_ID VARCHAR2(36) NOT NULL,
    ALARM_TYPE VARCHAR(100) NOT NULL,
    ALARM_CDATE DATE DEFAULT SYSDATE,
    ALARM_PREFIX VARCHAR2(100) NOT NULL,
		ST_NO NUMBER(8) NOT NULL,
    CONSTRAINT PK_ALARM PRIMARY KEY(ALARM_NO)
		CONSTRAINT FK_ALARM_ST_NO FOREIGN KEY(ST_NO)
        REFERENCES STUDYGROUP(ST_NO)
);

CREATE SEQUENCE SEQ_ALARM INCREMENT BY 1 START WITH 1 NOCACHE;
```
### UNREADMEMBER TABLE
```
CREATE TABLE UNREADMEMBER(
    MESSAGE_ID NUMBER(8) NOT NULL,
    USER_ID VARCHAR2(36) NOT NULL,
		ST_NO NUMBER(8) NOT NULL,
    CONSTRAINT FK_UNREADMEMBER_MESSAGE_ID FOREIGN KEY(MESSAGE_ID)
        REFERENCES CHATMESSAGE(MESSAGE_ID)
);
```
## 시연
### 실시간 알림 (Polling 방식)
3초 주기로 알림을 계속 가져온다.<br>
![실시간 알림](https://github.com/pparkjs/websocket_chat/assets/107859870/3f8ad170-f14b-4723-ae13-3c0362839541)


### 실시간 스터디채팅 (Websocket 방식)
실시간 채팅이 가능하며 실시간으로 안읽은 메시지 개수를 가져오고, 메시지 별로 채팅방에 속한 사람에 대한 읽음 처리가 실시간으로 가능하게 구현함<br>
![실시간 채팅](https://github.com/pparkjs/websocket_chat/assets/107859870/ca974d73-a86b-4a3a-91cf-8a330f0fc478)

