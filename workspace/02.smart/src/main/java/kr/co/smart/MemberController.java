package kr.co.smart;

import java.lang.reflect.Member;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import smart.common.CommonUtility;
import smart.member.MemberDAO;
import smart.member.MemberVO;

@Controller @RequestMapping("/member")
public class MemberController {
	@Autowired private MemberDAO service;
	@Autowired private BCryptPasswordEncoder pwEncoder;   // 주입시 bean 필요
	
	
	
	
	// 비밀번호 찾기처리 요청
	@ResponseBody
	@RequestMapping(value="/resetPassword", produces="text/html; charset=utf-8")
	public String reset(MemberVO vo) {
		// 화면에서 입력한 아이디/이메일이 일치하는 회원에게 임시 비번을 발급한다.
		String name = service.member_userid_email(vo);
		StringBuffer msg = new StringBuffer("<script>");
		if(name==null) {
			msg.append( "alert('아이디 혹은 이메일이 맞지 않습니다. \\n확인하세요!'); " );
			msg.append( "location='findPassword'" );
		}else {
			vo.setName(name);
			// 임시 비번을 생성한 후 DB의 회원정보로 저장  임시 비번을이메일로 보내준다.
			String pw = UUID.randomUUID().toString();  // asdf234-asld-kf123  이러한 형태
			pw = pw.substring(pw.lastIndexOf("-")+1);  // kf123 
			vo.setUserpw(pwEncoder.encode(pw)); 	   // 암호화된 임시비번
			
		if(	service.member_resetPassword(vo) == 1  && common.sendPassword(vo, pw)) {
			
			msg.append("alert('임시 비밀번호가 발급되었습니다.\\n이메일을 확인하세요');");
			msg.append("location='login'"); // 임시비번으로 로그인하도록 로그인화면연결
			
		}else {
			msg.append("alert('임시 비밀번호 발급 실패');");
			msg.append("history.go(-1)");
			}
		}
		msg.append("</script>");
		return msg.toString();
	}
	
	
	
	// 비밀번호 찾기 화면 요청
	@RequestMapping("/findPassword")
	public String find() {
		return "default/member/find";
	}
	
	
	
	// 로그인 처리 요청
	/*
	@RequestMapping(value="/smartLogin")
	public String login(String userid, String userpw, HttpSession session,
						RedirectAttributes redirect) {
		
		MemberVO vo = service.member_info(userid);
		boolean match = false;
		if(vo != null) {	// 아이디가 일치하는 회원정보가 있고
			match = pwEncoder.matches( userpw, vo.getUserpw() );  // 비번 일치여부 확인
		}
		if( match ) {
			return "redirect:/";
		}else {
			redirect.addFlashAttribute("fail", true);
			return "redirect:login";  // 로그인화면 다시 요청
		}
	}
	*/
	
	
	
	// 로그인 처리 요청
	// ↓ 이 자체가 응답입니다~ 의 에노테이션(@)
	@ResponseBody 
	@RequestMapping(value="/smartLogin", produces="text/html; charset=utf-8")
	public String login(String userid, String userpw, HttpSession session,HttpServletRequest request) {
		// 화면에서 입려한 아이디, 비번이 일치하는 회원정보가 DB에 있는지 확인
		// 입력한 아이디에 해당하는 회원정보 조회
		MemberVO vo = service.member_info(userid);
		boolean match = false;
		if(vo != null) {	// 아이디가 일치하는 회원정보가 있고
			match = pwEncoder.matches( userpw, vo.getUserpw() );  // 비번 일치여부 확인
		}
		
		StringBuffer msg = new StringBuffer("<script>");
		if ( match ) {
			// 로그인되는 경우
			session.setAttribute("loginInfo", vo);  // 세션에 로그인한 회원정보 담기
			msg.append("location='").append(common.appURL(request)).append("'");
		}else {
			// 로그인되지않는 경우
//			StringBuffer alert = new StringBuffer("<script>");
			msg.append("alert('아이디나 비밀번호가 일치하지 않습니다!'); history.go(-1); " ); 
//			alert.append("</script>");
		}
		msg.append("</script>");
		return msg.toString();	
	}
	
	
	@Autowired private CommonUtility common;
	
	
	//로그인 화면 요청
	@RequestMapping("/login")
	public String login(HttpSession session) {
		session.setAttribute("category", "login");
		return "default/member/login";
	}
}
