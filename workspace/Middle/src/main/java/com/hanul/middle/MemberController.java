package com.hanul.middle;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

@RestController @RequestMapping("/member")
public class MemberController {
	// 1. 크롬창을 이용해서 url Get방식으로 login 처리가 Y 와 N이 되는지 확인.
	// 2. Android에서 되는지 확인  <- And00_Login 프로젝트를 생성 후 개발환경 직접 구축.
	// 3. Android에서 Edittext를 이용해서 되는지 확인.
	
	
	@RequestMapping(value = "/login" , produces = "text/html;charset=utf-8")
	public String login(String id, String pw) {
		System.out.println(id+pw);
		if(id.equals("admin") && pw.equals("admin1234")) {
			return "Y";
		}else {
			return "N"; 
		}
		
		
	}
	
}

