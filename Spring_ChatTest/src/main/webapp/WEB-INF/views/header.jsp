<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<style>
#uName,#uId{
margin-top:10px;
}
#uId{
margin-left : 10px;
}
.alarmAction{
	color : red;
}
.chatCnt{
    position: absolute;
    color: white;
    border: 1px solid;
    background: red;
    top: -8px;
    right: 7px;
    height: 20px;
    width: 20px;
    border-radius: 20px;
    text-align: center;
    font-size: 12px;
}
.chat-none{
 display: none;
}
</style>
 <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
 <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
 <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.0/jquery.min.js"></script>
<nav class="navbar navbar-expand-sm bg-dark navbar-dark">
	<div class="container-fluid">
		<a class="navbar-brand" href="#">정수의 스터디사이트</a>
		<button class="navbar-toggler" type="button" data-bs-toggle="collapse"
			data-bs-target="#collapsibleNavbar">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="collapse navbar-collapse" id="collapsibleNavbar">
			<ul class="navbar-nav">
				<li class="nav-item"><a class="nav-link" href="/studyhome">스터디모집</a></li>
				<li class="nav-item" style="position: relative;">
					<a class="nav-link" href="/chathome">채팅방</a>
					<div class="chatCnt chat-none"></div>
				</li>
				<li class="nav-item dropdown">
					<a class="nav-link dropdown-toggle" id="alarm" href="#" role="button"data-bs-toggle="dropdown" >알림</a>
					<ul class="dropdown-menu" id="toast">
<!-- 						<li><a class="dropdown-item" href="#">Link</a></li> -->
							<!-- 동적 처리 -->
					</ul>
				</li>
				<li class="nav-item text-muted">
					<p class="text-muted" id="uName">이름 : ${sessionScope.user.userName }</p>
				</li>
				<li class="nav-item">
					<p class="text-muted" id="uId">아이디 : ${sessionScope.user.userId }</p>
				</li>
				<li class="nav-item"><a class="nav-link" href="/logout">로그아웃</a></li>
			</ul>
		</div>
	</div>
</nav>
<script>
// ------------------------------chat에 대한 script 시작------------------------------------------------------------------
// 웹소켓
var websocket = null;
  connect();
 //입장 버튼을 눌렀을 때 호출되는 함수
function connect() {
    // 웹소켓 주소
    var wsUri = "ws://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/websocket/chat.do";
    // 소켓 객체 생성
    websocket = new WebSocket(wsUri);
    //웹 소켓에 이벤트가 발생했을 때 호출될 함수 등록 (오버라이딩)
    websocket.onopen = function(){
    	console.log('info: connection opened.');
    }
}
 
// 채팅방 이외의 공간에 있을 떄 실시간 채팅 개수 알람 받기.
websocket.onmessage = function(evt) {
 	console.log("evt.data : ", evt.data)
 	
    if(evt.data == "reload"){
    	getChatCnt();
    }
 }
// ------------------------------chat에 대한 script 끝------------------------------------------------------------------


// ------------------------------alarm 에 대한 script 시작----------------------------------------------------------------
 // 처음 로드시 실행
  getAlarm();
  getChatCnt();
  
  // 3초마다 반복 실행
  setInterval(() => {
	  getAlarm();
	}, 3000);
  
function getAlarm(){
	$.ajax({
		type : "post",
		url : "/studyHome/getAlarmInfo",
		contentType : "application/json; charset=utf-8",
		success : function(res){
			if(res.length > 0){
				var msg = "";
				for(var i = 0; i < res.length; i++){
					msg += `<li><a class='dropdown-item' id='\${res[i].alarmNo}'href='studyhome'>\${res[i].alarmPrefix}<br>\${res[i].alarmCdate}</a></li>`
				}
				$("#toast").html(msg)
				$("#alarm").attr("style", "color:red;")
			}else{
				$("#toast").html(`<li style='margin-left:20px;'>알람이 없습니다.</li>`)
				$("#alarm").attr("style", "")
			}
		}
	})
}

function getChatCnt(){
	$.ajax({
		type : "post",
		url : "/getChatCnt.do",
		data : {
			"userId" : "${sessionScope.user.userId}"
		},
		success : function(res){
			var chatCnt = $(".chatCnt")
			if(res > 0){
				chatCnt.removeClass("chat-none");
				chatCnt.text(res);
			}else{
				chatCnt.addClass("chat-none");
			}
		}
	})
}
	
	$(document).on("click", "#toast a",function(){
		var alarmNo = $(this).attr("id");
		$.ajax({
			type : "post",
			data : {
				"alarmNo" : alarmNo
				},
			url : "/studyHome/deleteAlarm",
// 			contentType : "application/json; charset=utf-8", // 이렇게 설정하면 JSON.stringify()로 js를 JSON문자열로 변환해서 보내야함
			success : function(res){
				if(res == "SUCCESS"){
					console.log(res)
				}
			}
		})
	})
// ------------------------------------alarm 에 대한 script 끝----------------------------------------------------------------	
</script>