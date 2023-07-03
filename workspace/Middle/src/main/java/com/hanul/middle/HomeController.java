package com.hanul.middle;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {
	// AutoWired : "스프링 빈 객체" 끼리 초기화할때 필요한게 있따면
	// 자동으로 필요한 내용을 주입해서 초기화시키는 과정을 한다.
	// 의존성 자동 주입. : Annotation이 반드시 필요함. (Spring객체)
	// Qualifier : 의존성 자동주입을 할때 키값으로 어떤걸 가져와라 라고 써주는것   bean안에 qualifier에 밸류를 줬음.
	
	
	// SqlSession
	// Service : 
	
	@Autowired @Qualifier("hanul") SqlSession sql;
	
	@Autowired @Qualifier("tt") TestVO vo;
	@Autowired TestDAO dao;
	
	@RequestMapping(value = "/")
	public String home() {
		//int result = sql.selectOne("test.dual");
		//System.out.println(result);
		System.out.println(vo.getField1());
	
		dao.select();
		return "home";
	}
}
