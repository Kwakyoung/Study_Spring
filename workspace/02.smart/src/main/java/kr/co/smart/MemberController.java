package kr.co.smart;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import smart.common.CommonUtility;
import smart.member.MemberDAO;
import smart.member.MemberVO;

@Controller @RequestMapping("/member")
public class MemberController {
	@Autowired private MemberDAO service;
	@Autowired private BCryptPasswordEncoder pwEncoder;
	
	
	
	//아이디 중복확인 처리 요청
	@ResponseBody @RequestMapping("/useridCheck")
	public boolean useridCheck(String userid) {
		//화면에서 입력한 아이디가 DB에 있는지 여부를 확인
		//DB에 없는 아이디이면 사용가능, 있는 아이디이면 사용불가능
		return  service.member_info(userid)==null ? true : false;
	}
	
	
	//회원가입 처리 요청
	@ResponseBody 
	@RequestMapping(value="/register", produces="text/html; charset=utf-8")
	public String join(MemberVO vo, HttpServletRequest request, MultipartFile file) {
		if( ! file.isEmpty() ) { //첨부파일이 있는 경우
			//서버의 물리적인 영역에 파일을 저장하는 처리
			vo.setProfile( common.fileUpload("profile", file, request) );
		}
		//화면에서 입력한 정보로 DB에 신규회원정보저장한 후
		//회원가입 성공 여부를 alert으로 띄운다
		//비밀번호 암호화해서 담기
		vo.setUserpw(  pwEncoder.encode( vo.getUserpw() ) );
		
		StringBuffer msg = new StringBuffer("<script>");
		if( service.member_join(vo)== 1 ) {
			msg.append("alert('회원가입을 축하합니다 ^^'); location='")
			.append( request.getContextPath() )
			.append("' ");
		}else {
			msg.append("alert('회원가입 실패ㅠㅠ'); history.go(-1)");
		}
		msg.append("</script>");
		
		return msg.toString();
	}
	
	
	//회원가입 화면 요청
	@RequestMapping("/join")
	public String join(HttpSession session) {
		session.setAttribute("category", "join");
		return "member/join";
	}
	
	//로그아웃 처리 요청
	@RequestMapping("/logout")
	public String logout(HttpSession session, HttpServletRequest request) {
		MemberVO login = (MemberVO)session.getAttribute("loginInfo");
		session.removeAttribute("loginInfo");
		String social = login.getSocial();
		if( social !=null && social.equals("K") ) {
			//curl -v -X GET "https://kauth.kakao.com/oauth/logout
			//?client_id=${YOUR_REST_API_KEY}
			//&logout_redirect_uri=${YOUR_LOGOUT_REDIRECT_URI}"
			StringBuffer url = new StringBuffer("https://kauth.kakao.com/oauth/logout");
			url.append("?client_id=").append( KAKAO_ID );
			url.append("&logout_redirect_uri=").append( common.appURL(request) );
			return "redirect:" + url.toString();
		}else
			return "redirect:/";
	}
	
	
	//새비밀번호 변경저장 처리 요청
	@ResponseBody @RequestMapping("/updatePassword")
	public boolean update(MemberVO vo) {
		//화면에서 입력한 새 비밀번호가 DB에 변경저장
		vo.setUserpw(  pwEncoder.encode( vo.getUserpw() ) );
		return service.member_resetPassword(vo)==1 ? true : false;
	}
	
	//현재비밀번호 확인 요청
	@ResponseBody @RequestMapping("/confirmPassword")
	public int confirm(String userpw, String userid) {
		//화면에서 입력한 현재 비밀번호가 DB에 있는지 확인
		MemberVO vo = service.member_info(userid);
		return pwEncoder.matches(userpw, vo.getUserpw()) ? 0 : 1;
	}
	
	//비밀번호 변경화면 요청
	@RequestMapping("/changePassword")
	public String change(HttpSession session) {
		// 세션에 로그인정보가 없다면 로그인 화면으로 연결하고
		// 세션에 로그인정보가 있으면 비밀번호변경 화면으로 연결
		MemberVO vo = (MemberVO)session.getAttribute("loginInfo");
		if( vo==null ) 	return "redirect:login"; 
		else 			return "member/change";
	}
	
	
	
	//비밀번호 찾기처리 요청
	@ResponseBody 
	@RequestMapping(value="/resetPassword", produces="text/html; charset=utf-8")
	public String reset(MemberVO vo) {
		//화면에서 입력한 아이디/이메일이 일치하는 회원에게 임시 비번을 발급한다
		String name = service.member_userid_email(vo);
		StringBuffer msg = new StringBuffer("<script>");
		if( name == null ) {
			msg.append( "alert('아이디나 이메일이 맞지 않습니다. \\n확인하세요!'); " );
			msg.append( "location='findPassword'" );
		}else {
			vo.setName(name);
			//임시 비번을 생성한 후 DB의 회원정보로 저장 임시비번을 이메일로 보내준다
			String pw = UUID.randomUUID().toString(); // afdlk234-lhluha2432-afdafd
			pw = pw.substring( pw.lastIndexOf("-")+1 ); // afdafd
			vo.setUserpw( pwEncoder.encode(pw) );  // 암호화된 임시비번
			
			if( service.member_resetPassword(vo)==1 
						&& common.sendPassword(vo, pw) ) {
				msg.append("alert('임시 비밀번호가 발급되었습니다. \\n이메일을 확인하세요'); ");
				msg.append("location='login'"); //임시비번으로 로그인하도록 로그인화면 연결
				
			}else {
				msg.append("alert('임시 비밀번호가 발급 실패ㅠㅠ'); ");
				msg.append("history.go(-1)");
			}
		}
		
		msg.append("</script>");
		return msg.toString();
	}
	
	
	//비밀번호 찾기 화면 요청
	@RequestMapping("/findPassword")
	public String find() {
		return "default/member/find";
	}
	
	// 네이버로그인처리 요청
	@RequestMapping("/kakaoLogin")
	public String kakaoLogin(HttpServletRequest request){
		//인가 코드 받기
		//https://kauth.kakao.com/oauth/authorize?response_type=code
		//&client_id=${REST_API_KEY}
		//&redirect_uri=${REDIRECT_URI}
		StringBuffer url = new StringBuffer(
				"https://kauth.kakao.com/oauth/authorize?response_type=code" );
		url.append("&redirect_uri=").append( common.appURL(request) ).append("/member/kakaoCallback");
		url.append("&client_id=").append( KAKAO_ID );
		
		return "redirect:" + url.toString();
	}
	
	
	@RequestMapping("/kakaoCallback")
	public String kakaoCallback(String code, HttpSession session) {
		if( code==null ) return "redirect:/";
		StringBuffer url = new StringBuffer(
				"https://kauth.kakao.com/oauth/token?grant_type=authorization_code");
		url.append("&client_id=").append( KAKAO_ID );
		url.append("&code=").append( code );
		String response = common.requestAPI( url.toString() );
		//문자열 --> JSON 
		JSONObject json = new JSONObject( response );
		String token_type = json.getString("token_type");
		String access_token = json.getString("access_token");
		
//		curl -v -X POST "https://kauth.kakao.com/oauth/token" \
//		 -H "Content-Type: application/x-www-form-urlencoded" \
//		 -d "grant_type=authorization_code" \
//		 -d "client_id=${REST_API_KEY}" \
//		 --data-urlencode "redirect_uri=${REDIRECT_URI}" \
//		 -d "code=${AUTHORIZE_CODE}"
		
		url = new StringBuffer("https://kapi.kakao.com/v2/user/me");
//		common.requestAPI(url.toString(), "Bearer "+ access_token);
		json = new JSONObject( common.requestAPI(url.toString(), token_type + " "+ access_token) );
		
		MemberVO vo = new MemberVO();
		vo.setSocial( "K" );
		vo.setUserid( json.get("id").toString() );
		json = json.getJSONObject("kakao_account");
//		if( json.has("name") ) {
//			vo.setName( json.getString("name") );
//		}
//		vo.setName( json.has("name")? json.getString("name") : "");
		vo.setName( hasKey(json, "name") );
		vo.setEmail( hasKey(json, "email") );
		vo.setGender( hasKey(json, "gender", "female").equals("male") ? "남"  :"여");
		vo.setPhone( hasKey(json, "phone_number") );
		
		json = json.getJSONObject("profile");
		vo.setProfile( hasKey(json, "profile_image_url") );
		if( ! hasKey(json, "nickname").isEmpty()  ) {  //nickname에 값이 있는 경우
			vo.setName( hasKey(json, "nickname", vo.getName() ) );
		}
		//DB에 저장하기 위한 데이터 수집
		
		if( service.member_info( vo.getUserid() ) ==null ) {
			service.member_join(vo);
		}else {
			service.member_update(vo);
		}
		session.setAttribute("loginInfo", vo);
		
		return "redirect:/";
	}
	
	private String KAKAO_ID = "f925b9cc77a3e06e488ff8fcf22929ed";
	private String NAVER_ID = "8GSlDw2qebIvWjOWcMge";
	private String NAVER_SECRET = "lJqxHhT2BG";
	
	// 네이버로그인처리 요청
	@RequestMapping("/naverLogin")
	public String naverLogin(HttpSession session, HttpServletRequest request) {
		// 네이버 로그인 연동 URL 생성하기
		// https://nid.naver.com/oauth2.0/authorize?response_type=code
		// &client_id=CLIENT_ID
		// &state=STATE_STRING
		// &redirect_uri=CALLBACK_URL
		String state = UUID.randomUUID().toString();
		session.setAttribute("state", state);
		
		StringBuffer url = new StringBuffer(
				"https://nid.naver.com/oauth2.0/authorize?response_type=code");
		url.append("&client_id=").append( NAVER_ID );
		url.append("&state=").append( state );
		url.append("&redirect_uri=").append( common.appURL(request) ).append( "/member/naverCallback" );
		// http://localhost:8080/smart/member/naverCallback
		return "redirect:" + url.toString();
	}
	
	//네이버 콜백처리
	@RequestMapping("/naverCallback")
	public String naverCallback(String code, String state, HttpSession session) {
		String storedState = (String)session.getAttribute("state");
		if( code==null || ! state.equals(storedState) )  return "redirect:/";
		
		// 접근 토큰 발급 요청 
		// https://nid.naver.com/oauth2.0/token?grant_type=authorization_code
		// &client_id=jyvqXeaVOVmV
		// &client_secret=527300A0_COq1_XV33cf
		// &code=EIc5bFrl4RibFls1
		// &state=9kgsGTfH4j7IyAkg  
		StringBuffer url = new StringBuffer(
				"https://nid.naver.com/oauth2.0/token?grant_type=authorization_code");
		url.append("&client_id=").append(NAVER_ID);
		url.append("&client_secret=").append(NAVER_SECRET);
		url.append("&code=").append(code);
		url.append("&state=").append(state);
		
		String response = common.requestAPI( url.toString() );
		// 문자열 --> JSON
		JSONObject json = new JSONObject( response );
		String token = json.getString("access_token");
		String type = json.getString("token_type");
		
		// 접근 토큰을 이용하여 프로필 API 호출하기
//		curl  -XGET "https://openapi.naver.com/v1/nid/me" 
//	      -H "Authorization: Bearer AAAAPIuf0L+"
		url = new StringBuffer("https://openapi.naver.com/v1/nid/me");
		response = common.requestAPI( url.toString(), type + " " + token );
		json = new JSONObject( response );
		if( json.getString("resultcode").equals("00") ) {
			json = json.getJSONObject("response");
			MemberVO vo = new MemberVO();
			vo.setSocial("N"); //N:네이버, K:카카오
			vo.setUserid(  json.getString("id") );
			// 별칭 이 있으면 별칭을, 없으면 이름을 name 필드에 담자
			vo.setName( hasKey(json, "nickname") );
			if( vo.getName().isEmpty()  ) {
				vo.setName( hasKey(json, "name", "아무개") );
			}
			vo.setEmail( hasKey(json, "email") );
			vo.setProfile( hasKey(json, "profile_image") );
			vo.setGender( hasKey(json, "gender", "M").equals("M") ? "남" : "여" )  ; // M/F --> 남/여
			vo.setPhone( hasKey(json, "mobile") );
			
			// DB에 네이버로그인 정보 저장하기 - 존재여부를 확인하여 신규/변경 저장
			if( service.member_info( vo.getUserid() )==null ) {
				service.member_join(vo);
			}else {
				service.member_update(vo);
			}
			session.setAttribute("loginInfo", vo);
		}
		/*
			{
			  "resultcode": "00",
			  "message": "success",
			  "response": {
			    "email": "openapi@naver.com",
			    "profile_image": "https://ssl.pstatic.net/static/pwe/address/nodata_33x33.gif",
			    "age": "40-49",
			    "gender": "F",
			    "nickname": "",
			    "id": "32742776",
			    "birthday": "10-01"
			  }
			}		  
		 */
		
		
		return "redirect:/";
	}
	
	private String hasKey( JSONObject json, String key ) {
		return json.has(key) ? json.getString(key) : "";
	}
	
	// 기본값을 지정해야 하는 경우
	private String hasKey( JSONObject json, String key, String value ) {
		return json.has(key) ? json.getString(key) : value;
	}
	
	
	
	
	
	
	// 로그인처리 요청
	@RequestMapping( value="/smartLogin")
	public String login(String userid, String userpw
			, HttpSession session
			, RedirectAttributes redirect) {
		
		//화면에서 입력한 아이디, 비번이 일치하는 회원정보가 DB에 있는지 확인
		//입력한 아이디에 해당하는 회원정보 조회
		MemberVO vo = service.member_info(userid);
		boolean match = false;
		if( vo !=null ) { //아이디가 일치하는 회원정보가 있고
			match = pwEncoder.matches( userpw, vo.getUserpw() ); // 비번일치여부 확인
		}
		if( match ) {
			session.setAttribute("loginInfo", vo);  //세션에 로그인한 회원정보 담기
			return "redirect:/";
			
		}else {
			redirect.addFlashAttribute("fail", true );
			return "redirect:login"; //로그인화면 다시 요청
		}
	}
	
	/*
	@ResponseBody @RequestMapping( value="/smartLogin", produces="text/html; charset=utf-8" )
	public String login(String userid, String userpw
						, HttpSession session
						, HttpServletRequest request) {
		//화면에서 입력한 아이디, 비번이 일치하는 회원정보가 DB에 있는지 확인
		//입력한 아이디에 해당하는 회원정보 조회
		MemberVO vo = service.member_info(userid);
		boolean match = false;
		if( vo !=null ) { //아이디가 일치하는 회원정보가 있고
			match = pwEncoder.matches( userpw, vo.getUserpw() ); // 비번일치여부 확인
		}
		
		StringBuffer msg = new StringBuffer("<script>");
		if( match ) {
			//로그인되는 경우
			session.setAttribute("loginInfo", vo);  //세션에 로그인한 회원정보 담기
			msg.append( "location='" )
				.append( common.appURL(request) )
				.append( "'" );
		}else {
			//로그인되지 않는 경우
			msg.append(  "alert('아이디나 비밀번호가 일치하지 않습니다!'); history.go(-1); " );
		}
		msg.append("</script>");
		return msg.toString();
	}
	*/
	
	@Autowired private CommonUtility common;
	
	
	// 로그인화면 요청
	@RequestMapping("/login")
	public String login(HttpSession session) {
		session.setAttribute("category", "login");
		return "default/member/login";
	}
}
