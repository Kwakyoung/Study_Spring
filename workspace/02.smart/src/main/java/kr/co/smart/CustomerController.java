package kr.co.smart;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import smart.customer.CustomerServiceImpl;
import smart.customer.CustomerVO;

@Controller
public class CustomerController {
	@Autowired private CustomerServiceImpl service;
	// ↑ ↓
	/*
	public CustomerController ( CustomerServiceImpl service) {
		this.service = service;
	}
	*/
	
	
	// 고객정보 삭제처리 요청
	@RequestMapping("/delete.cu")
	public String delete(int id) {
		// 비즈니스 로직 : 해당 고객 정보를 DB에서 삭제한 후
		service.customer_delete(id);
		
		// 프리젠테이션 로직 : 고객목록 화면으로 응답
		return "redirect:list.cu";
	}
	
	
	
	
	// 고객정보 변경저장 처리 요청
	@RequestMapping("/update.cu")
	public String update(CustomerVO vo) {
		// 화면에서 변경 입력한 정보로 DB에 변경저장(Update)한 후
		service.customer_update(vo);
		
		// 프리젠테이션 로직 : 응답화면 연결 - 정보화면
		return "redirect:info.cu?id="+ vo.getId();  // 추가적용하여 변경된 화면 !?
	}
	
	
	
	
	// 고객정보 수정화면 요청
	@RequestMapping("/modify.cu")
	public String modify(int id, Model model) {
		// 비즈니스 로직 : 선택한 고객정보를 DB에서 조회해와
		// 			   수정화면에 출력할 수 있도록 데이터를 Model객체에 담는다.
		model.addAttribute("vo", service.customer_info(id)) ;
		// 프리젠테이션로직 : 응답화면연결 - 수정화면
		return "customer/modify";
	}
	
	
	
	
	// 고객 상세 정보 화면 요청
	@RequestMapping("/info.cu")
	public String info(Model model, int id) {
		// 비즈니스 로직 : 선택한 고객 정보를 DB에서 조회
		//             화면에 출력할 수 있도록 데이터를 Model객체에 담는다.
		CustomerVO vo = service.customer_info(id);
		model.addAttribute("vo", vo);
		// 응답화면 연결
		return "customer/info";
	}
	
	
	
	
	
	// DML(Insert/Update/Delete) 처리가 되는 요청에 대해서는
	// 반드시 redirect로 화면응답.
	
	// 신규 고객 정보 저장처리 요청
	@RequestMapping("/register.cu")
	public String register(CustomerVO vo) {
		// 비즈니스 로직 - 화면에서 입력한 정보를 DB에 신규 저장한 후
		service.customer_insert(vo);
		// 프리젠테이션로직 (응답화면 연결) : 목록화면
		return "redirect:list.cu";   // 추가된 list.cu 
	}
	
	// 신규 고객 정보 입력화면 요청
	@RequestMapping("/new.cu")
	public String customer () {
		
		return "customer/new";
	}
	
	// 고객 목록 화면
	@RequestMapping("/list.cu")
	public String list(HttpSession session, Model model, String name) {
		// null, "", "곽"
		session.setAttribute("category", "cu");
		
		// 비즈니스 로직 - DB에서 고객목록을 조회한다
//		List<CustomerVO> list = null;
//		if( name == null ) {
//			list = service.customer_list();
//		}else {
//			list = service.customer_list(name);
//		}
		
		List<CustomerVO> list = name == null
								? service.customer_list()
								: service.customer_list(name);
		model.addAttribute("list", list);
		model.addAttribute("name",name);
		
		// 응답화면 연결
		return "customer/list";
	}
	
	
	
}
