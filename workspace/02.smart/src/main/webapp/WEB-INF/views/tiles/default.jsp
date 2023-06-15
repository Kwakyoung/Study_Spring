<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<c:choose>
	<c:when test="${category eq 'login'}"><c:set var="title" value="로그인"/></c:when>
</c:choose>

<html>
<head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="" />
        <meta name="author" content="" />
        <title>스마트 웹&amp;앱 ${title}</title>
        <!-- Favicon-->
        <link rel="icon" type="image/x-icon" href="<c:url value='/img/hanul.ico' />" />
        <!-- Core theme CSS (includes Bootstrap)-->
        <link href="<c:url value='/css/styles.css?<%=new java.util.Date() %>'/>" rel="stylesheet" />
        <link href="<c:url value='/css/common.css?<%=new java.util.Date() %>'/>" rel="stylesheet" />
        
        <!-- cdnjs.com > fontawesome 검색 > styling, javascript 선언문 복사해서 넣음 -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
        <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/js/all.min.js"></script>
        <!-- // -->
        <link rel="stylesheet" href="//code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
        <script src="https://code.jquery.com/jquery-3.6.0.js"></script>
        <script src="https://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>
        <script src="<c:url value='/js/common.js?<%=new java.util.Date() %>'/>"></script>
    </head>
<body class="bg-primary">
	
 <!-- Page content-->
  <div class="container">
      <tiles:insertAttribute name="container"/>
  </div>	
	
 <!-- Bootstrap core JS-->
 <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
 <!-- Core theme JS-->
 <script src="<c:url value="/js/scripts.js"/>"></script>
	
</body>
</html>