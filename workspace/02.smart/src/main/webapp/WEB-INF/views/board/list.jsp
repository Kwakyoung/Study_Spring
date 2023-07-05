<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
.input-group .form-select { flex:initial; width: 100px }
</style>

</head>
<body>
<h3 class="my-4">방명록 목록</h3>

<form>
<div class="row justify-content-between mb-3">
	<div class="col-auto">
		<div class="input-group">
			<select class="form-select" name="search">
				<option value="s1">전체</option>
				<option value="s2">제목</option>
				<option value="s3">내용</option>
				<option value="s4">작성자</option>
				<option value="s5">제목+내용</option>
			</select>
			<input type="text" name="keyword" class="form-control">
			<button class="btn btn-primary px-3">
				<i class="fa-solid fa-magnifying-glass"></i>
			</button>
		</div>
	</div>



	<!-- 로그인된 경우만 글쓰기 가능 -->
	<c:if test="${ ! empty loginInfo }">
	<div class="col-auto">
		<a class="btn btn-primary" href="new">새글쓰기</a>
	</div>
	</c:if>
</div>

<table class="tb-list">
<colgroup><col width="100px"><col><col width="120px"><col width="120px"><col width="100px"></colgroup>
<tr><th>번호</th>
	<th>제목</th>
	<th>작성자</th>
	<th>작성일자</th>
	<th>조회수</th>
</tr> 
<c:if test="${empty page.list}">
<tr><td colspan="5">방명록 글이 없습니다</td></tr>
</c:if>
<c:forEach items="${page.list}" var="vo">
<tr><td>${vo.no }</td>
	<td class="text-start">${vo.title }
		<c:if test="${vo.filecnt gt 0}"><i class="fa-solid fa-paperclip"></i></c:if>
	</td>
	<td>${vo.name }</td>
	<td>${vo.writedate }</td>
	<td>${vo.readcnt }</td>
</tr>
</c:forEach>

</table>
</form>
<jsp:include page="/WEB-INF/views/include/page.jsp"/>
</body>
</html>