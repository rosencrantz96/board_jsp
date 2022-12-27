function chkForm() {
	var f = document.frm; // form태그 
	
	// erd 설계 과정에서 title, user_id는 not null 그래서 반드시 체크를 해주어야 한다
	// content는 not null 이 아니라서 굳이 체크를 해주지 않았음 
	if(f.title.value == '') {
		alert("제목을 입력해주십시오.");
		return false;
	}
	if(f.user_id.value == '') {
		alert("아이디를 입력해주십시오.");
		return false;	
	}
	
	f.submit(); // 폼태그 전송 
}

function chkDelete (board_no) {
	const result = confirm("삭제하시겠습니까?");
	
	if(result) {
		const url = location.origin;	
		location.href = url + "/board2_jsp/delete?board_no=" + board_no; // 페이지 이동 
	} else {
		return false;
	}
}