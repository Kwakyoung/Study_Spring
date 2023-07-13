package com.hanul.middle;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import hr.EmployeeVO;

@RestController
public class HrController {

	
	
	@Autowired @Qualifier("hr") SqlSession sql;
	@RequestMapping(value = "/select.hr" , produces = "text/html;charset=utf-8")
	public String select() {
		List<EmployeeVO> vo = sql.selectList("hr.select");
		
		return new Gson().toJson(vo);
	}
	
}
