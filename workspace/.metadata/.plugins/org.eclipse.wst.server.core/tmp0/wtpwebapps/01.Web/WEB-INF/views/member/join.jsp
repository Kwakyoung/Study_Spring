<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div><a href ="<c:url value='/'/>">홈으로</a></div>
	
	<h1>회원가입</h1>
	
	<form  method ="post" action="joinReqest">
	<table border='1'>
	<tr> 
		<th>성명</th> 
		<td>
		<input type ='text' name="name">
		</td> 
	</tr>
	
	<tr> 
		<th>성별</th> 
		<td>
		<input type = 'radio' name="gender" value="남" checked>남 
		<input type = 'radio' name="gender" value="여">여
		</td>
	</tr>
	
	<tr> 
		<th>이메일</th> 
		<td>
		<input type = 'text' name="email">
		</td>
   </tr>
	<tr> 
		<th>나이</th> 
		<td>
		<input type = 'text' name="age">
		</td>
   </tr>
	</table>
	<input type="submit" value="회원가입(HttpServletRequest)">
	<input type="submit" value=@RequestParam onclick="action='joinParam'" >
	<input type="submit" value=데이터객체 onclick="action='join	DataObject'" >
	<input type="submit" value=@PathVariable onclick="go_path( this.form )" > <!-- script 함수 -->
	</form>
	
	<script>
		function go_path( f ){
			f.action = 'joinPath/'+ f.name.value + '/' + f.gender.value + '/' + f.email.value + '/' + f.age.value;
		}
	</script>
	
</body>
</html>