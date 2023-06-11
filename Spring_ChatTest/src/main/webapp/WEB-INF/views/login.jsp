<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author"
	content="Mark Otto, Jacob Thornton, and Bootstrap contributors">
<meta name="generator" content="Hugo 0.88.1">
<title>Signin Template · Bootstrap v5.1</title>

<link rel="canonical" href="https://getbootstrap.com/docs/5.1/examples/sign-in/">



<!-- Bootstrap core CSS -->
<link href="../resources/assets/dist/css/bootstrap.min.css"	rel="stylesheet">
<link href="../resources/assets/dist/css/signin.css" rel="stylesheet">

<style>
.bd-placeholder-img {
	font-size: 1.125rem;
	text-anchor: middle;
	-webkit-user-select: none;
	-moz-user-select: none;
	user-select: none;
}

@media ( min-width : 768px) {
	.bd-placeholder-img-lg {
		font-size: 3.5rem;
	}
}
</style>


<!-- Custom styles for this template -->
<link href="signin.css" rel="stylesheet">
</head>
<body class="text-center">

	<main class="form-signin">
		<form action="/login" method="post">
			<img class="mb-4" src="../resources/assets/brand/bootstrap-logo.svg" alt=""
				width="72" height="57">
			<h1 class="h3 mb-3 fw-normal">로그인 페이지</h1>

			<div class="form-floating">
				<input type="text" class="form-control" id="floatingInput" name="userId"
					placeholder="아이디 입력"> <label for="floatingInput">아이디</label>
			</div>
			<div class="form-floating">
				<input type="password" class="form-control" id="floatingPassword" name="userPassword"
					placeholder="비밀번호 입력"> <label for="floatingPassword">비밀번호</label>
			</div>

			<div class="checkbox mb-3">
				<label> <input type="checkbox" value="remember-me">
					아이디 저장
				</label>
			</div>
			<button class="w-100 btn btn-lg btn-primary" type="submit">로그인</button>
			<p class="mt-5 mb-3 text-muted">&copy; 2017–2023</p>
		</form>
	</main>
</body>
</html>
