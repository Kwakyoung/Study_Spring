package kr.co.smart;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomerController {
	
	// 고객 목록 화면
	@RequestMapping("/list.cu")
	public String list() {
		// 비즈니스 로직
		// 응답화면 연결
		return "customer/list";
	}
}
