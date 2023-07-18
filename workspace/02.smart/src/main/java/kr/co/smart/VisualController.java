package kr.co.smart;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import smart.visual.VisualDAO;

//@Controller + @ResponseBody
@RestController
@RequestMapping("/visual")
public class VisualController {
	@Autowired private VisualDAO service;
	
	//년도별 채용인원수 조회 요청
	@RequestMapping("/hirement/year")
	public Object hirement_year() {
		return service.hirement_year();
	}
	
	//월별 채용인원수 조회 요청
	@RequestMapping("/hirement/month")
	public Object hirement_month() {
		return service.hirement_month();
	}

	//@ResponseBody 
	@RequestMapping("/department")
	public Object department() {
	//public List<HashMap<String,Object>> department() {
		//DB에서 부서별 사원수를 조회해와 응답한다.
		return service.department();
	}
	
	
}
