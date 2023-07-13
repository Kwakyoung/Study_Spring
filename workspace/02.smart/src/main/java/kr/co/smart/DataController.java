package kr.co.smart;

import java.net.URLEncoder;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import smart.common.CommonUtility;

@Controller @RequestMapping("/data")
public class DataController {
	private String key
	= "w%2FRKikJbHmvOjhvPOgZXQSMHJTV8oA0xh7uttgtctyxC3QQv2t%2BGqUXSJ2tZrYdPRXyO0%2B%2B%2B%2FGSYlw9aw5bdQA%3D%3D";
	
	private String animalURL = "http://apis.data.go.kr/1543061/abandonmentPublicSrvc/"; 
	
	@Autowired private CommonUtility common;
	
	
	// 유기동물 시도조회 요청
	@RequestMapping("/animal/sido")
	public String animal_sido(Model model) {
		StringBuffer url = new StringBuffer( animalURL );
		url.append("sido?serviceKey=").append(key);
		url.append("&_type=json");
		model.addAttribute("list", common.requestAPIResultInfo(url));
		return "data/animal/animal_sido";
	}
	
	
	
	// 유기동물 조회 요청
	@RequestMapping("/animal/list")
	public Object animal_list(int pageNo, int rows , Model model) {
		StringBuffer url = new StringBuffer( animalURL );
		url.append("abandonmentPublic?serviceKey=").append(key);
		url.append("&_type=json");
		url.append("&pageNo=").append(pageNo);
		url.append("&numOfRows=").append(rows);
		model.addAttribute("list", common.requestAPIResultInfo(url.toString()));
		return "data/animal/animal_list";
	}
//	@ResponseBody @RequestMapping("/animal/list")
//	public Object animal_list(int pageNo, int rows) {
//		StringBuffer url = new StringBuffer( animalURL );
//		url.append("abandonmentPublic?serviceKey=").append(key);
//		url.append("&_type=json");
//		url.append("&pageNo=").append(pageNo);
//		url.append("&numOfRows=").append(rows);
//		return new Gson().fromJson( common.requestAPI(url.toString()), 
//				new TypeToken< HashMap<String, Object>>(){}.getType());
//	}
	
	
	
	
	// 약국목록 조회 요청
//	@ResponseBody @RequestMapping(value = "/pharmacy" , produces = "text/html;charset=utf-8")
//	public String pharmacy_list() {
//		StringBuffer url 
//		= new StringBuffer("http://apis.data.go.kr/B551182/pharmacyInfoService/getParmacyBasisList");
//		url.append("?ServiceKey=").append(key);
//		url.append("&_type=json");
//		return common.requestAPI( url.toString() );
//	}
	@ResponseBody @RequestMapping("/pharmacy")
	public Object pharmacy_list(int pageNo, int rows, String name) throws Exception{
		StringBuffer url 
		= new StringBuffer("http://apis.data.go.kr/B551182/pharmacyInfoService/getParmacyBasisList");
		url.append("?ServiceKey=").append(key);
		url.append("&_type=json");
		url.append("&pageNo=").append( pageNo );
		if( !name.isEmpty() ) url.append("&yadmNm=").append( URLEncoder.encode(name, "utf-8") );
		url.append("&numOfRows=").append( rows );
		HashMap<String, Object> map = new Gson().fromJson( common.requestAPI( url.toString() ) 
								, new TypeToken< HashMap<String, Object> >(){}.getType() );
		return map;
	}
	
	
	
	// 공공데이터 목록 화면
	@RequestMapping("/list")
	public String list(HttpSession session) {
		session.setAttribute("category", "da");
		return "data/list";
	}
}
