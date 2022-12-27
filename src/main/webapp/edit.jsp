<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Insert title here</title>
<link rel="stylesheet" href="./css/style.css" />
</head>
<body>
	<div class="board_wrap">
		<div class="board_title">
			<strong>자유게시판</strong>
			<p>자유게시판 입니다.</p>
		</div>
		<div class="board_write_wrap">
			<form name="frm" method="post" action="update?board_no=${board.board_no}">
				<div class="board_write">
					<div class="title">
						<dl>
							<dt>제목</dt>
							<dd>
								<%--디비 데이터 크기 잘 고려해서 maxlength를 주어야 합니다. --%>
								<input type="text" value="${board.title}" name="title" maxlength="50" />
							</dd>
						</dl>
					</div>
					<div class="info">
						<dl>
							<dt>글쓴이</dt>
							<dd>
								<input type="text" value="${board.user_id}" name="user_id" maxlength="10" />
							</dd>
						</dl>
					</div>
					<div class="cont">
						<textarea name="content">${board.content}</textarea>
					</div>
				</div>
				<div class="bt_wrap">
					<a onclick="chkForm(); return false;" class="on">수정</a> <a href="list">취소</a>
				</div>
			</form>
		</div>
	</div>
	<script>
	  <c:if test="${error != null}">
	    alert("${error}");
	  </c:if>
	</script>
	<script type="text/javascript" src="./script.js"></script>
</body>
</html>