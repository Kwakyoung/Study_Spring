package kr.co.smart;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import smart.board.BoardDAO;
import smart.board.BoardVO;
import smart.common.CommonUtility;
import smart.common.PageVO;
import smart.member.MemberDAO;
import smart.member.MemberVO;

@Controller  @RequestMapping("/board")
public class BoardController {
	@Autowired private BoardDAO service;
	@Autowired private CommonUtility common;
	
	
	
	
	@RequestMapping("/modify")
	public String modify(Model model, int id, PageVO page) {
		// 해당 글 정보를 DB에서 조회해와 수정화면에 출력
		model.addAttribute("vo", service.board_info(id)); 
		model.addAttribute("page", page);
		return "board/modify";
	}
	
	
	
	
	//방명록 신규저장처리 요청
	public String register(BoardVO vo, MultipartFile file[], HttpServletRequest request) {
		vo.setFileList( common.attachedFiles("board", file, request) );
		
		service.board_register(vo);
		
		return "redirect:list";
	}
	
	
	//방명록 신규입력 화면 요청
	@RequestMapping("/new")
	public String board() {
		return "board/new";
	}
	
	
	
	@Autowired private MemberDAO member;
	@Autowired private BCryptPasswordEncoder pw;
	
	//방명록 목록 화면 요청
	@RequestMapping("/list")
	public String list(HttpSession session) {
		// 임시 로그인처리(테스트후 삭제/주석)---------
		String userid = "hong2023";
		String userpw = "Hong2023";
		MemberVO login = member.member_info(userid);
		if( pw.matches( userpw, login.getUserpw()) ) {
			session.setAttribute("loginInfo", login);
		}
		//-------------------------------------
				
		
		session.setAttribute("category", "bo");
		return "board/list";
	}
	
}
