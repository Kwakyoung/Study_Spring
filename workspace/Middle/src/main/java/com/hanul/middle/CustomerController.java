package com.hanul.middle;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

@RestController
public class CustomerController {
	// 어노테이션 == 주석 ?
	// @영어 <= 어노테이션 == 기계가 해석하는 주석. (Tag)
	// @ ctrl + space 누르면 나오는 모든것들은 어노테이션임.  어노테이션은 밑에 있는 메소드나 또는 변수, 객체등의 역할을 정해주는 기능을 담당한다
	// class ( 어떤 요청을 받기 위한 객체 x ) 
	// @Controller 를 class에 달아주면  어떤 요청을 받는 객체 라고 컴퓨터가 인식 (Spring) org.spring ... 어노테이션 종류 
	
	//json / xml
	// json <= String으로 되어있는데 key와 value가 존재하고 list같은 자료구조도 [] 등으로 표현이 가능한 데이터타입.
	// 요소 하나 (Object , DTO) => 기호:{} , list ==> []
	
	@Autowired @Qualifier("hanul") SqlSession sql;
	@RequestMapping(value = "/list.cu" , produces = "text/html;charset=utf-8")   // < produces 받아오는 걸 utf-8로
	public String list( String param ) {
		System.out.println("누군가가 왔따감" + param);
		List<CustomerVO> vo = sql.selectList("customer.list");
		System.out.println(vo.size());
		
		Gson gson = new Gson();
		return gson.toJson(vo);
	}
	
	
	@RequestMapping(value = "/obj.cu" , produces = "text/html;charset=utf-8")
	public String obj() {
		CustomerVO vo = new CustomerVO();
		vo.setEamil("이메일");
		vo.setName("이름이용");
		return new Gson().toJson(vo);
	}
	
	
	
	
	@Autowired TestBean bean1;
	TestBean bean2;
	
	@RequestMapping("/test.bean")
	public void test() {
		System.out.println(bean1);
		System.out.println(bean2);
	}
	
	
}
