package com.hanul.middle;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.google.gson.Gson;

import hr.EmployeeVO;

@RestController
public class FileControllerController {

	
	@RequestMapping(value = "/file.f" , produces = "text/html;charset=utf-8")   // < produces 받아오는 걸 utf-8로
	public String list(HttpServletRequest req) throws IllegalStateException, IOException { //req(요청에 대한 모든정보) , res
		System.out.println(req.getLocalAddr());			
		System.out.println(req.getLocalPort());
		System.out.println(req.getContextPath()+"/폴더");
//		192.168.0.10
//		8080
//		/middle/폴더
		
		// 파일을 빼오고 저장하기 . 캐스팅!?
		MultipartRequest mReq = (MultipartRequest) req; // file정보가 없는 req = > 있는 mReq
		MultipartFile file = mReq.getFile("file");
		// 파일이 있는 상태의 요청을 받았는지에 따라서 유동적으로 MultiPartRequest로 캐스팅
		if(file != null) {
//			System.out.println(file.getContentType());
			file.transferTo(new File("D:\\Study_Android\\images\\20230720" , "andimg.jpg"));
		}else {
			
		}
		
		// 물리적으로 저장하기.
		// Middle/img/파일명을 크롬으로 요청하면 열리게 하기.
		// 실제 파일은 D:\Android\폴더\..
		return new Gson().toJson("");
	}
	
	
		
	
	

	
}
