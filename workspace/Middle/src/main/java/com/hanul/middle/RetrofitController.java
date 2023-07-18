package com.hanul.middle;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import retrofit.RetrofitDAO;
import retrofit.RetrofitVO;

@RestController
public class RetrofitController {
//	@Autowired @Qualifier("hanul") SqlSession sql; -- DAO사용할거라서 바꿔줌
	@Autowired RetrofitDAO dao;
	
	
	@RequestMapping(value = "/list.re" , produces = "text/html;charset=utf-8")
	public String list(RetrofitVO vo) {
		return new Gson().toJson(dao.select());
	}
	
	@RequestMapping(value = "/insert.re" , produces = "text/html;charset=utf-8")
	public String insert(RetrofitVO vo) {
		return new Gson().toJson(dao.insert(vo));
	}
	
	@RequestMapping(value = "/update.re" , produces = "text/html;charset=utf-8")
	public String update(RetrofitVO vo) {
		return new Gson().toJson(dao.update(vo));
	}
	
	@RequestMapping(value = "/delete.re" , produces = "text/html;charset=utf-8")
	public String delete(RetrofitVO vo) {
		return new Gson().toJson(dao.delete(vo));
	}
	
	
	
	
}
