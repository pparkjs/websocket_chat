<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.0/jquery.min.js"></script>
<style>
.container{
	display : flex; width : 1300px; margin-top : 20px;
}
.chatList, .chatRoom{border : 1px solid black; height : 682px; padding-top : 5px}
/* .chatList{flex:2} */
/* .chatRoom{flex:3} */
.room{border : 1px solid black; height : 70px; padding-top:20px;}
.roomTitle{height : 30px;}
.chatMiddle{height : 450px; border : 1px solid black; overflow: auto;}
.display-none{display: none;}
.right{text-align: right; margin-right : 45px;}
.chatMiddle li{list-style: none;}
.sender{font-weight: bold; font-size: 18px;}
.message{margin-top: 8px; margin-bottom: 4px;}
.message .msg{padding : 5px; border: 1px solid rgb(99, 99, 102); color : rgb(99, 99, 102);}
.message .cnt{color:red; font-size: 13px;}
.regDate span{font-size: 12px;}
</style>
</head>
<body>
	<%@ include file="header.jsp" %>
	
	<div class="container">
		<div class="chatList">
			<!-- 채팅방리스트 동적 처리 -->
		</div>
		<div class="chatRoom display-none">
			<div class="chatTop">
				<h3 style="text-align: center">
					<!-- 채팅방 제목 동적 처리 -->
				</h3>
				<button class="close">닫기</button>
				<div style="border:1px solid black; padding-top:5px;">
					<p id="leader" style="margin-left:20px"></p>
					<p id="stMem" style="margin-left:20px"></p>
				</div>
			</div>
			<div class="chatMiddle">
		        <ul>
					<!-- 채팅 메시지 동적 처리 -->
		        </ul>
		    </div>
		    <div class="chatBottom">
				<textarea placeholder="메세지를 입력해 주세요." cols="62" rows="3"></textarea>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
let roomId; // roomId 초기화 입장 버튼 누르면 id 들어오게 설계

 getChatList();

// 채팅방 리스트 가져오기
 function getChatList(){
	$.ajax({
		type : "post",
		url : "/chatList.do",
		dataType : "json",
		success : function(res){
			room = `<h3 style="text-align: center" >채팅방 리스트</h3>`;
			for(var i = 0; i < res.length; i++){
				room += `	<div class="room">`;
				room += `		<div class="name" style="display: inline; padding-left: 20px;">\${res[i].userName }</div>`;
				room += `		<div class="title" style="display: inline; padding-left: 10px;">\${res[i].stTitle }</div>`;
				room += `		<button class="enter" style="margin-right: 10px;" id="\${res[i].stNo }" value="\${res[i].stTitle }" onclick='enterRoom(this)'>입장</button>`
				if(res[i].msgCount > 0){
					room += `	<div class="counter" style="display: inline; color: red; font-weight:bold; font-size: 18px;">\${res[i].msgCount }</div>`;
				}
				room += `	</div>`;
			}
			$(".chatList").html(room);
		}
	})
}

// 채팅방에서 닫기 버튼 누를 시 
$('.close').on('click', function(){
	// 방에 입장 시 해당 정보 서버에 전달
	const roomData = {
		 "stNo" : roomId,
		 "userName" : "${sessionScope.user.userName}",
		 "userId" : "${sessionScope.user.userId}",
		 "type" : "close-room"
	}
	websocket.send(JSON.stringify(roomData));
	
	$('.chatRoom').toggleClass("display-none");
})

// 입장 버튼 누를때 onclick(this)으로 해당 함수 호출
function enterRoom(obj){
	// 입장하면 display-none 클래스 제거
	$('.chatRoom').removeClass("display-none");
	
// 	roomId = $(obj).attr("id"); // obj를 jQuery 객체로 변환해서 가져오기
	roomId = obj.getAttribute("id") // javaScript 객체에서 id 속성값 가져오기
	roomTitle = obj.value // 채팅방 제목 가져오기
	// 방에 입장 시 해당 정보 서버에 전달
	const roomData = {
		 "stNo" : roomId,
		 "userName" : "${sessionScope.user.userName}",
		 "userId" : "${sessionScope.user.userId}",
		 "type" : "enter-room"
	}
	websocket.send(JSON.stringify(roomData));
	
	chatMessageList();
}

// 채팅 메시지 가져오기
function chatMessageList(){
	// 해당 html에 추가되었던 동적 태그 전부 지우기
	$('.chatMiddle ul').html("");
	// ajax를 이용하여 해당 방에 메시지 가져오기
	$.ajax({
		url : roomId + ".do",
		data : {
			userId : "${sessionScope.user.userId}"
		},
		dataType : "json",
		success : function(data){
			$(".chatTop h3").text(roomTitle); 
			
			for(var i = 0; i < data.length; i++){
				// 채팅 목록 동적 추가 (내가 썼는지 안썼는지 판단 : 채팅 정렬 정하기)
				CheckLR(data[i]);
			}
			
			$.ajax({
				url : "/studymember.do",
				data : {
					"stNo" : roomId
				},
				dataType : "json",
				success : function(res){
					
					var cnt = 0;
					var member = "스터디원 : ";
					for(var i = 0; i < res.length; i++){
						if(res[i].authRole == '스터디장'){
							$("#leader").html(`스터디장 : \${res[i].userName}`)
						}else if(i == res.length-1){
							member += `\${res[i].userName}`
						}else{
							member += `\${res[i].userName}, `
						}
						cnt += 1;
					}
					if(cnt > 1){
						$("#stMem").html(member);
					}else{
						member += "없음";
						$("#stMem").html(member);
					}
				}
			})
		}
	});
}

 // enter 누르면 메시지 전송
	$(document).on('keydown', '.chatBottom textarea', function(e){
	    if(e.keyCode == 13 && !e.shiftKey) { // enter와 shift키와 동시에 눌리지 않았을 때 동작
	        e.preventDefault(); // 엔터키가 입력되는 것을 막아준다. enter누를 때 줄바꿈 동작 막음
	        const message = $(this).val();  // 현재 입력된 메세지를 담는다.
	          
	        let search3 = $('.chatBottom textarea').val();
	         
	        // 공백 및 공백 문자열을 제거하고, 

	        // 입력된 메시지가 공백 또는 문자열로만 이루어져 있을 때 함수 실행 멈춤
	        if(search3.replace(/\s|  /gi, "").length == 0){
	              return false;
	              $('.chatBottom textarea').focus();
	           }
	        
	        sendMessage(message);
	        // textarea 비우기
	        clearTextarea();
	    }
	});
	
// * 1 메시지 전송
function sendMessage(message){
	const data = {
			 "stNo" : roomId,
			 "userName" : "${sessionScope.user.userName}",
			 "userId" : "${sessionScope.user.userId}",
			 "messageContent" : message,
			 "messageRegdate" : dateFormat(),
			 "type" : "msg"
	 };
	
	// 해당 data 채팅리스트에 세팅
// 	CheckLR(data);
	
	// websocket 서버에 해당 data 전송
	websocket.send(JSON.stringify(data));
}

 // * 2 메세지 수신 (웹소켓 서버로부터 메시지를 수신하는 함수)
 websocket.onmessage = function(evt) {
	 	console.log("evt.data : ", evt.data)
	 	
	    if(evt.data == "reload"){
	    	getChatList();
	    	getChatCnt();
	    	chatMessageList()
	    }else{
		 	let receive = evt.data.split(","); // evt.data 서버에서 전송된 메시지 데이터
	        
	        const data = {
	                "userName" : receive[0],
	                "userId" : receive[1],
	             "messageContent" : receive[2],
	             "messageRegdate" : receive[3],
	             "unreadCount" : receive[4],
	        };
	        CheckLR(data);
	    }
  }

// * 2-1 추가된 메시지가 내가 보낸 것인지, 상대방이 보낸 것인지 확인하는 함수
 function CheckLR(data){
 	// id가 로그인한 회원의 id와 다르면 left , 같으면 right
 	const LR = (data.userId != "${sessionScope.user.userId}") ? "left" : "right";
 	// 메시지 추가 함수 호출
 	appendMessageTag(LR, data.userId, data.messageContent, data.userName, data.messageRegdate, data.unreadCount);
 }
 
// * 3 메시지 태그 append
 function appendMessageTag(LR_className, id, msg, name, rdate, unreadCount){
	
	const chatList = createMessageTag(LR_className, id, msg, name, rdate, unreadCount);
	
	$(".chatMiddle ul").append(chatList);
	
	// 스크롤바 아래 고정
	$(".chatMiddle").scrollTop($(".chatMiddle").prop("scrollHeight")); //선택한 요소의 scrollHeight 속성 값을 가져옴
}

// * 4 메시지 태그 생성
 function createMessageTag(LR_className, id, msg, name, rdate, unreadCount){
	// chatMiddle ul 안에 넣을 메시지 태그 생성
	let chatList = `<li class="\${LR_className}">
					    <div class="sender">
					        <span>\${name}</span>
					    </div>`
	chatList +=	   `<div class="message"> `
	if(unreadCount > 0){
		if((LR_className == "right")){
			chatList +=			`<span class="cnt">\${unreadCount}</span> <span class="msg">\${msg}</span>`
		}else{
			chatList +=			`<span class="msg">\${msg}</span> <span class="cnt">\${unreadCount}</span>`
		}
	}else{
			chatList +=			`<span class="msg">\${msg}</span>`
	}
	chatList +=	   `</div>
					    <div class="regDate">
					        <span>\${rdate}</span>
					    </div>
					 </li>`;
	return chatList;
		
}
//* 5 - 채팅창 비우기
 function clearTextarea(){
	 $('.chatBottom textarea').val("");
	 return false;
 };
 
 // 현재 시각 불러오기
 function dateFormat(){
	 const currentDate = new Date();
	 const year = currentDate.getFullYear();
	 const month = String(currentDate.getMonth() + 1).padStart(2, '0');
	 const day = String(currentDate.getDate()).padStart(2, '0');
	 const hours = String(currentDate.getHours()).padStart(2, '0');
	 const minutes = String(currentDate.getMinutes()).padStart(2, '0');
	 const seconds = String(currentDate.getSeconds()).padStart(2, '0');

	 const formattedDate = `\${year}-\${month}-\${day} \${hours}:\${minutes}:\${seconds}`;
	 return formattedDate;
 } 
 
</script>
</html>