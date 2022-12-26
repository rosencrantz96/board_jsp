package controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import DAO.BoardDAO;
import DTO.Board;

@WebServlet("/")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BoardDAO dao;
	private ServletContext ctx;
	
    @Override // init: 맨 처음에 서블릿 객체를 생성하는 메소드 (딱 한 번만 쓰이고 공유될 수 있다) 
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		// init은 서블릿 객체 생성 시 딱 한 번만 실행하므로 객체를 한 번만 생성해 공유할 수 있다. 
		dao = new BoardDAO();
		ctx = getServletContext(); // ServletContext: 웹 프로젝트 하나당 하나의 ServletContext가 생성이 된다. 
		// getServletContext(): 웹 어플리케이션의 자원을 관리하는 ServletContext를 얻어온다. 
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	request.setCharacterEncoding("UTF-8"); // request 객체에 저장된 한글 깨짐 방지 
    	doPro(request, response);
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8"); // request 객체에 저장된 한글 깨짐 방지 
		doPro(request, response);
	}
	
	// doPro의 역할: 라우팅 (길을 찾아주는 역할을 한다) 
	protected void doPro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String context = request.getContextPath();
		String command = request.getServletPath();
		String site = null;
		
		// 경로 라우팅(경로를 찾아줌) 
		switch (command) {
		case "/list": 
			site = getList(request);
			break;
		case "/view": 
			site = getView(request);
			break;
		// 글쓰기를 누르면 write.jsp를 보여주는 것이 맞다 (insert는 기능만!! 등록을 눌렀을 때 request하는 경로일 뿐!!)
		case "/write": // 글쓰기 화면을 보여줌   
			site = "write.jsp";
			break;
		case "/insert": // insert 기능  
			site = insertBoard(request); // 제목, 내용 등 내가 작성한 것들이 request 객체에 저장되어 있다
			break;
		}	
		
		/*
		 * -둘 다 페이지를 이동한다. 
		   redirect: URL의 변화 O, 객체의 재사용 X (request, response 객체)
		   		*DB에 변화가 생기는 요청에 사용 (글쓰기, 회원가입...) insert, update, delete 조심...
		   		
		   forward: URL의 변화 X(보안의 이점), 객체의 재사용 O (request, response 객체)
				*단순 조회(리스트 보기, 검색) select...
		*/
		if (site.startsWith("redirect:/")) { // startsWith(): 이 문자로 시작하는 걸 찾는 거 // redirect
			String rview = site.substring("redirect:/".length());
			System.out.println(rview);
			response.sendRedirect(rview);
		} else { // forward 
			ctx.getRequestDispatcher("/" + site).forward(request, response);
		}
	}
	
	public String getList(HttpServletRequest request) {
		List<Board> list;
		
		// 어디서인가는 꼭 에러 처리를 해줘야한다. (throw를 하더라도... 어디선간 try/catch로 처리를 해주어야 한다) 
		try {
			list = dao.getList();
			request.setAttribute("boardList", list); // ("키", value) = boardList라는 이름으로 밸류를 저장 
		} catch (Exception e) {
			e.printStackTrace();
			ctx.log("게시판 목록 생성 과정에서 문제 발생"); // log(): 기록
			// 나중에 사용자한테 에러메세지를 보여주기 위해 저장
			request.setAttribute("error", "게시판 목록이 정상적으로 처리되지 않았습니다!");
		}
		
		return "index.jsp";
	}

	private String getView(HttpServletRequest request) {
		int board_no = Integer.parseInt(request.getParameter("board_no"));
		
		try {
			dao.updateViews(board_no); // 조회수 증가
			Board b = dao.getView(board_no);
			request.setAttribute("board", b); 
		} catch (Exception e) {
			e.printStackTrace();
			ctx.log("게시글을 가져오는 과정에서 문제 발생"); 
			request.setAttribute("error", "게시글을 정상적으로 가져오지 못했습니다!");
		}
		
		return "view.jsp";
	}
	
	public String insertBoard(HttpServletRequest request) {
		/*
		request.getParameter("user_id");
		request.getParameter("title");
		request.getParameter("content");
		*/
		Board b = new Board();
		/*
		b.setUser_id(request.getParameter("user_id"));
		b.setTitle(request.getParameter("title"));
		b.setContent(request.getParameter("content"));
		*/
		
		try {
			BeanUtils.populate(b, request.getParameterMap());
			dao.insertBoard(b);
		} catch (Exception e) {
			e.printStackTrace();
			ctx.log("추가 과정에서 문제 발생"); 
			request.setAttribute("error", "게시글이 정상적으로 등록되지 않았습니다!");
			return getList(request);
		} 
		
		return "redirect:/list";
		
	}

}
