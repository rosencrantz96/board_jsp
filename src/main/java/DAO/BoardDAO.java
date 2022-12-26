package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import DTO.Board;

public class BoardDAO {
	final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
	final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521:xe";

	// 리소스 방식?
	// 데이터 베이스와의 연결 수행 메소드
	public Connection open() {
		Connection conn = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(JDBC_URL, "system", "sys1234");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return conn; // Connection: 데이터 베이스의 연결 객체를 리턴
	}

	// 게시판 리스트 가져오기
	public ArrayList<Board> getList() throws Exception {
		Connection conn = open();
		ArrayList<Board> boardList = new ArrayList<Board>(); // Board 객체를 데이터 저장할 arrayList

		String sql = "select board_no, title, user_id, to_char(reg_date, 'yyyy.mm.dd') reg_date, views from board ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		
		/*
		 * String sql =
		 * "select BOARD_NO, TITLE, USER_ID, to_char(REG_DATE, 'yyyy.mm.dd') REG_DATE , VIEWS from BOARD "
		 * ; PreparedStatement pstmt = conn.prepareStatement(sql); // 쿼리문 등록 -> 컴파일
		 * ResultSet rs = pstmt.executeQuery(); // 쿼리문 실행 (실질적으로 실행하는 부분) -> 데이터 베이스 결과
		 * 저장 // ResultSet: 데이터베이스 결과를 저장하는 곳
		 */

		// 리소스 자동 닫기 (try-with-resource 구문)
		/*
		 * 아래는 얘와 같은 방식임 (하지만 굳이 닫아줄 필요가 없다는 점에서 더 편리함) try { } catch (Exception e) { }
		 * finally { conn.close(); pstmt.close(); rs. close(); }
		 */
		try(conn; pstmt; rs) {
			while (rs.next()) {
				Board b = new Board();
				// 데이터 타입 잘 보기!
				b.setBoard_no(rs.getInt(1));
				b.setTitle(rs.getString(2));
				b.setUser_id(rs.getString(3));
				b.setReg_date(rs.getString(4));
				b.setViews(rs.getInt(5));

				boardList.add(b); // 레코드들이 보드 객체에 저장이 되어서 리스트에 쌓인다
			}

			return boardList; // 게시판 목록 리스트 가져오는 메소드 끝
		}
	}

	// 게시판 내용 가져오기
	public Board getView(int board_no) throws Exception {
		Connection conn = open();
		Board b = new Board();

		String sql = "select board_no, title, user_id, to_char(reg_date, 'yyyy.mm.dd') as reg_date, views, content from board where board_no = ? ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, board_no); 
		ResultSet rs = pstmt.executeQuery();
		
		try(conn; pstmt; rs) {
			while (rs.next()) {
				b.setBoard_no(rs.getInt(1));
				b.setTitle(rs.getString(2));
				b.setUser_id(rs.getString(3));
				b.setReg_date(rs.getString(4));
				b.setViews(rs.getInt(5));
				b.setContent(rs.getString(6));
			}
			return b;
		}
	}
	
	// 조회수 증가
	public void updateViews(int board_no) throws Exception {
		Connection conn = open();

		String sql = "update board set views = (views + 1) where board_no = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		try(conn;pstmt) {
			pstmt.setInt(1, board_no);
			pstmt.executeUpdate();
		} 
	}
	
	public void insertBoard(Board b) throws Exception {
		Connection conn = open();
		String sql ="insert into board (board_no, user_id, title, content, reg_date, views) values(board_seq.nextval, ?, ?, ?, sysdate, 0)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		try(conn; pstmt) {
			pstmt.setString(1, b.getUser_id());
			pstmt.setString(2, b.getTitle());
			pstmt.setString(3, b.getContent());
			pstmt.executeUpdate();
		}
	}
}
