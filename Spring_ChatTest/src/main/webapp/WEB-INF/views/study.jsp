<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.0/jquery.min.js"></script>
</head>
<body>
	<%@ include file="header.jsp" %>
	
	<div style="margin-left: 20px;">
		<h2>채팅방 생성</h2>
		<form method="post">
			채팅방 제목 : <input id="stTitle" type="text" name="stTitle">
			<input type="hidden" name="userId" value="${sessionScope.user.userId }">
			<input type="hidden" name="userName" value="${sessionScope.user.userName }">
			<input id="subBtn" type="button" value="생성하기">
		</form>
	</div>
	<hr>
	<div>
		<table border="1">
			<tr>
				<td align="center" width="80">번호</td>
				<td align="center" width="100">작성자</td>
				<td align="center" width="320">제목</td>
				<td align="center" width="180">작성일</td>
				<td align="center" width="100">신청</td>
			</tr>
			<c:choose>
				<c:when test="${empty studyList }">
					<tr>
						<td align="center" colspan="5">조회하실 게시물이 존재하지 않습니다.</td>
					</tr>
				</c:when>
				<c:otherwise>
					<c:forEach items="${studyList }" var="study">
						<tr>
							<td align="center">${study.stNo }</td>
							<td align="center">${study.userName }</td>
							<td align="center">${study.stTitle }</td>
							<td align="center">${study.stDate }</td>
							<td align="center"><input type="button" class="joinBtn" value="가입하기" id="${study.stNo }"></td>
						</tr>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</table>	
	</div>
</body>
<script type="text/javascript">
$(function(){
	var	joinBtn = $(".joinBtn");
	
	joinBtn.on("click", function(){
		var stNo = $(this).attr("id");
		var studyObject = {
			"userId" : "${sessionScope.user.userId}",
			"userName" : "${sessionScope.user.userName}",
			"stNo" : stNo,
			"authRole" : "스터디원"
		}
		console.log(studyObject);
		$.ajax({
			type : "post",
			url : "/joinStudy",
			data : JSON.stringify(studyObject),
			contentType : "application/json; charset=utf-8",
			success : function(res){
				if(res === "SUCCESS"){
					alert("가입에 성공하였습니다.")
				}else{
					alert("가입에 실패하였습니다. 다시 시도해주세요.")
				}
			}
		})
		
	})
	
	$("#subBtn").on("click", function(){
		var stTitle = $("#stTitle").val();
		
		if(stTitle == ""){
			alert("제목을 입력하세요")
			return false;
		}
		
		$("form").submit();
	})
	
})
</script>
</html>