package smart.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import smart.member.MemberVO;

@Service
public class CommonUtility {
	
	
	//파일업로드
	public String fileUpload(String category, MultipartFile file, HttpServletRequest request ) {
		//D:\Study_Spring\Workspace\.metadata\.plugins\org.eclipse.wst.server.core
		// \tmp0\wtpwebapps\02.smart\resources
		//String path = request.getSession().getServletContext().getRealPath("resources");

		// d:\\app\smart
		String path = "D:\\app"+ request.getContextPath(); // /smart
//		String path = "D:/app/smart";

		//String upload = "/upload/profile/2023/06/22/abc.png";
		String upload = "/upload/"+ category 
				+ new SimpleDateFormat("/yyyy/MM/dd").format(new Date());
		path += upload;
		//파일을 저장해둘 폴더가 없으면 폴더 만들기
		File folder = new File( path );
		if( ! folder.exists() ) folder.mkdirs();
		
		String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		try {
			file.transferTo( new File(path, filename) );
		}catch(Exception e) {
		}
		// http://localhost:8080/smart/upload/profile/2023/06/22/abc.png
		return appURL(request) + upload + "/" + filename;
	}
	

	private void emailServerConnect(HtmlEmail email) {
		email.setHostName("smtp.naver.com"); //메일서버지정
		email.setAuthentication("rhkrdudrbs13", "goqxlr13"); //아이디/비번 으로 로그인
		email.setSSLOnConnect(true); // 로그인버튼 클릭
	}
	private String EMAIL_ADDRESS = "rhkrdudrbs13@naver.com";
	
	// 이메일 보내기: 회원가입축하메시지전송
	public void sendWelcome(MemberVO vo, String welcomFile) {
		HtmlEmail email = new HtmlEmail();
		email.setCharset("utf-8");
		email.setDebug(true);
		
		//이메일서버지정
		emailServerConnect(email);
		try {
			email.setFrom( EMAIL_ADDRESS, "스마트 웹&앱 관리자" );
			email.addTo( vo.getEmail(), vo.getName() );
			email.setSubject("한울 스마트 웹&앱 과정 가입 축하");
			
			StringBuffer content = new StringBuffer();
			content.append("<body>");
			content.append("<h3><a target='_blank' href='http://hanuledu.co.kr/'>한울 스마트 웹&앱 과정</a></h3>");
			content.append("<div>우리 과정 가입을 축하합니다</div>");
			content.append("<div>프로젝트까지 마무리하시고 취업에 성공하시길 바랍니다</div>");
			content.append("<div>첨부된 파일을 확인하신후 등교하세요</div>");
			content.append("</body>");
			email.setHtmlMsg( content.toString() );
			
			EmailAttachment file = new EmailAttachment();
			file.setPath( welcomFile ); //파일선택
			email.attach(file);			//선택한 파일 첨부
			
			email.send(); //메일 보내기버튼 클릭
		
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	// 이메일 보내기: 임시비번전송 
	public boolean sendPassword(MemberVO vo, String pw) {
		boolean send = true;
		HtmlEmail email = new HtmlEmail();
		email.setCharset("utf-8");
		email.setDebug(true);  //이메일전송 과정 Console에서 로그로 확인
		
		emailServerConnect(email);
		try {
			email.setFrom( EMAIL_ADDRESS, "스마트 웹&앱 관리자 "); //관리자가 보내는이
			email.addTo( vo.getEmail(), vo.getName() );  // 받는사람 지정
			//email.addTo("박문수") // 받는사람 지정
			
			email.setSubject("스마트 웹&앱 로그인 임시 비밀번호");  // 제목쓰기
			
			StringBuffer content = new StringBuffer();
//			content.append("<h3>임시 비밀번호가 발급되었습니다</h3>");
			content.append("<h3>[").append( vo.getName() ).append("]님 임시 비밀번호가 발급되었습니다</h3>");
			content.append("<div>아이디: ").append( vo.getUserid() ) .append("</div>");
			content.append("<div>임시 비밀번호: <strong>").append( pw ).append("</strong></div>");
			content.append("<div>발급된 임시 비밀번호로 로그인한 후 비밀번호를 변경하세요</div>");
			email.setHtmlMsg( content.toString() ); // 내용쓰기
			email.send();  // 보내기버튼 클릭(전송)
			
		}catch(Exception e) {
			send = false;
		}
		return send;
	}
	
	
	// API 요청
	public String requestAPI( String apiURL ) {
		String response = "";
	    try {
	        URL url = new URL(apiURL);
	        HttpURLConnection con = (HttpURLConnection)url.openConnection();
	        con.setRequestMethod("GET");
	        int responseCode = con.getResponseCode();
	        BufferedReader br;
	        System.out.print("responseCode="+responseCode);
	        if(responseCode==200) { // 정상 호출
	          br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
	        } else {  // 에러 발생
	          br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "utf-8"));
	        }
	        String inputLine;
	        StringBuffer res = new StringBuffer();
	        while ((inputLine = br.readLine()) != null) {
	          res.append(inputLine);
	        }
	        br.close();
	        response = res.toString();
	      } catch (Exception e) {
	        System.out.println(e);
	      }		
		
	    return response;
	}
	
	// API 요청
	public String requestAPI( String apiURL, String property ) {
		String response = "";
	    try {
	        URL url = new URL(apiURL);
	        HttpURLConnection con = (HttpURLConnection)url.openConnection();
	        con.setRequestMethod("GET");
	        //Authorization: {토큰 타입] {접근 토큰]
	        con.setRequestProperty("Authorization", property);
	        int responseCode = con.getResponseCode();
	        BufferedReader br;
	        System.out.print("responseCode="+responseCode);
	        if(responseCode==200) { // 정상 호출
	          br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
	        } else {  // 에러 발생
	          br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "utf-8"));
	        }
	        String inputLine;
	        StringBuffer res = new StringBuffer();
	        while ((inputLine = br.readLine()) != null) {
	          res.append(inputLine);
	        }
	        br.close();
	        response = res.toString();
	      } catch (Exception e) {
	        System.out.println(e);
	      }		
		
	    return response;
	}
	
	
	// context root url 지정
	public String appURL( HttpServletRequest request ) {
		//http://localhost:8080/smart
		StringBuffer url = new StringBuffer("http://");
		//localhost = 127.0.0.1 = 본인ip
		url.append( request.getServerName() ).append(":"); //  http://localhost:
		url.append( request.getServerPort() );  //  http://localhost:8080
		url.append( request.getContextPath() ); //  http://localhost:8080/smart
		return url.toString();
	}
}
