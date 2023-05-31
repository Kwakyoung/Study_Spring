package com.hanul.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller  // 컨트롤러
public class MemberController {

	// 로그인 화면 요청
	@RequestMapping("/login")
	public String login() {
		return "member/login";
	}
		
	
	// 로그인 처리 요청
	@RequestMapping("/loginResult")
	public String login(@RequestParam String id, String pw) {
		// 로그인 성공 : home 화면으로 연결
		// 로그인 실패 : 다시 로그인하도록 로그인화면으로 연결
		// kwak / 1234 인 경우만 로그인 성공으로 간주
		if(id.equals("kwak") && pw.equals("1234")) {
			//return "home"; // forward 방식
			return "redirect:/";
		}else {
			// return "member/login"; // forward 방식
			return "redirect:login"; // redirect 방식
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
