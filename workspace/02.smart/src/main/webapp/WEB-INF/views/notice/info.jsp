<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h3 class="my-4">공지글안내</h3>
<table class="tb-row">
<colgroup><col width="180px"><col>
		<col width="160px"><col width="160px">
		<col width="100px"><col width="100px">
</colgroup>
<tr><th>제목</th>
	<td colspan="5">${vo.title }</td>
</tr>
<tr><th>작성자</th>
	<td>${vo.name }</td>
	<th>작성일자</th>
	<td>${vo.writedate }</td>
	<th>조회수</th>
	<td>${vo.readcnt }</td>
</tr>
<tr><th>내용</th>
	<td colspan="5">${vo.content }</td>
</tr>
<tr><th>첨부파일</th>
	<td colspan="5">${vo.filename }</td>
</tr>
</table>

</body>
</html>