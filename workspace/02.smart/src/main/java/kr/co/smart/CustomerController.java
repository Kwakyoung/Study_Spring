package kr.co.smart;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import customer.CustomerServiceImpl;
import customer.CustomerVO;

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
	public String list(HttpSession session, Model model) {
		session.setAttribute("category", "cu");
		
		// 비즈니스 로직 - DB에서 고객목록을 조회한다
		List<CustomerVO> list = service.customer_list();
		model.addAttribute("list", list);
		
		// 응답화면 연결
		return "customer/list";
	}
	
}
