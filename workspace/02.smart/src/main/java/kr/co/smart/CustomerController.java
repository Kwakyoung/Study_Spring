package kr.co.smart;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import customer.CustomerServiceImpl;

@Controller
public class CustomerController {
	@Autowired private CustomerServiceImpl service;
	// ↑ ↓
	/*
	public CustomerController ( CustomerServiceImpl service) {
		this.service = service;
	}
	*/
	
	
	// 고객 목록 화면
	@RequestMapping("/list.cu")
	public String list(HttpSession session) {
		session.setAttribute("category", "cu");
		
		// 비즈니스 로직
		service.customer_list();
		
		// 응답화면 연결
		return "customer/list";
	}
	
}
