<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<select class="form-select" id="sido">
	<option value=''>시도 선택</option>
	<c:forEach items="${list.items.item }" var="vo">
	<option value='${vo.orgCd}'>${vo.orgdownNm}</option>
	</c:forEach>
</select>

<script>
$('#sido').change(function(){
	animal_list( 1 );
})
</script>