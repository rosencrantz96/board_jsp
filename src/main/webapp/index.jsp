<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Insert title here</title>
<link rel="stylesheet" href="./css/style.css" />
</head>
<body>
	<div class="wrap">
		<table class="board_list">
			<caption>
				<h1>자유게시판</h1>
			</caption>
			<thead>
				<tr>
					<th>번호</th>
					<th>제목</th>
					<th>글쓴이</th>
					<th>작성일</th>
					<th>조회수</th>
				</tr>
			</thead>
			<tbody>
				<%-- for (Board board: boardList) --%>
				<c:forEach var="board" items="${boardList}" varStatus="status">
					<tr>
						<td>${board.board_no}</td>
						<td class="title"><a href="./view?board_no=${board.board_no}">${board.title}</a></td>
						<%-- 타이틀을 누르면 보드 넘버가 해당 보드 넘버를 가지고 있는 정보 페이지로 이동한다. --%>
						<td>${board.user_id}</td>
						<td>${board.reg_date}</td>
						<td>${board.views}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="bt_wrap bt_list">
			<a href="write">글쓰기</a>
		</div>
		<div class="board_page">
			<a href="#" class="bt first">&lt;&lt;</a> <a href="#" class="bt prev">&lt;</a>
			<a href="#" class="num on">1</a> <a href="#" class="num">2</a> <a
				href="#" class="num">3</a> <a href="#" class="num">4</a> <a href="#"
				class="num">5</a> <a href="#" class="bt next">&gt;</a> <a href="#"
				class="bt last">&gt;&gt;</a>
		</div>
	</div>
	<script>
		// jstl은 디코딩을 하지 않아도 됨 
		<c:if test="${param.error != null}">
			alert("${param.error}")
		</c:if>
		// getAttribute()로 할 때는 여기서 에러 메시지 호출 ? (get방식으로 할 때는 여기서 에러메시지 ?)
		<c:if test="${error != null}">
			alert("${error}")
		</c:if>
	</script>
</body>
</html>