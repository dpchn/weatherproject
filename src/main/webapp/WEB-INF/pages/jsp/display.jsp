<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<h1 align="center">Weather Report</h1>
<body>
	<div align="center">
		<c:if test="${fn:length(data) == 1}">
		${data.message }
	</c:if>
		<c:if test="${fn:length(data) gt 1}">
			<div>Maximum Temperature : ${data.max } ${data.unit }</div>
			<div>Minimum Temperature : ${data.min } ${data.unit }</div>
			<p>
			<div>
				<a href="/api/forecast/inCelcius">Temperature in Celcius</a>
				</td>
			</div>
			<div>
				<a href="/api/forecast/inFarhenheit">Temperature in Farhenheit</a>
			</div>
		</c:if>

	</div>

</body>
</html>