package smart.common;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;

import smart.member.MemberVO;

@Service
public class CommonUtility {
	
	private void emailServerConnect(HtmlEmail email) {    // 이메일 로그인 하는 과정
		email.setHostName("smtp.naver.com"); 			// 메일서버지정
		email.setAuthentication("rhkrdudrbs13@naver.com", "goqxlr13");  	// 아이디/비번으로 로그인
		email.setSSLOnConnect(true);					// 로그인 버튼 클릭
	}
	
	
	private String EMAIL_ADDRESS = "rhkrdudrbs13@naver.com";
	
	
	// 이메일 보내기 처리
	public boolean sendPassword(MemberVO vo, String pw) {
		boolean send = true;
		HtmlEmail email = new HtmlEmail();
		email.setCharset("utf-8");
		email.setDebug(true);   // 이메일전송 과정 Console에서 로그로 확인하려고 
		
		emailServerConnect(email);
		
		try {
			email.setFrom(EMAIL_ADDRESS,"관리자");   			// 관리자가 보내는이
			email.addTo(vo.getEmail(), vo.getName());		// 받는사람 지정
			
			email.setSubject("스마트 웹&앱 임시 비밀번호");  // 제목쓰기
			
			StringBuffer content = new StringBuffer();
			content.append("<h3>임시 비밀번호가 발급되었습니다.</h3>");
			content.append("<h3>[").append(vo.getName()).append("]임시 비밀번호가 발급되었습니다.</h3>");
			content.append("<div>아이디: ").append(vo.getUserid()).append("</div>") ;
			content.append("<div>임시 비밀번호: <strong>").append(pw).append("</div>");
			content.append("<div>발급된 임시 비밀번호로 로그인한 후 비밀번호를 변경하세요</div>");
			email.setHtmlMsg(content.toString());   // 내용쓰기
			email.send(); // 보내기버튼 클릭 (전송)
			
		} catch (Exception e) {
			send = false;
		} 
		
		return send;
	}
	
	
	
	// context root url 지정
	public String appURL(HttpServletRequest request) {
		// http://localhost:8080/smart/
		StringBuffer url = new StringBuffer("http://");
		url.append( request.getServerName() ).append(":"); // http://localhost:
		url.append( request.getServerPort() ); 	// http://localhost:8080
		url.append( request.getContextPath() ); // http://localhost:8080/smart
		return url.toString();
	}
	
	
	
}
