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
	<h3 class="my-4">공지글 목록</h3>
	<table class="tb-list">
	 <colgroup>
	 	<col width="100px">
	 	<col>
	 	<col width="120px">
	 	<col width="120px">
		<tr>
			<th>번호</th>
			<th>제목</th>
			<th>작성자</th>
			<th>작성일자</th>
		</tr>
		<c:forEach items="${list}" var="vo">
		<tr>
			<td>${vo.list}</td>
			<td>${vo.title}</td>
			<td>${vo.name}</td>
			<td>${vo.writedate}-06-23</td>
		</tr>
		</c:forEach>
	</table>
</body>
</html>