<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	String currentUser = (String)session.getAttribute("currentUser");
	System.out.println("Current user is ["+currentUser + "]");
	if(null == currentUser || "".equals(currentUser.trim())) {
%>
		<jsp:forward page="login.jsp"/>
<%
	} else {
%>
		<script>
			location.href="<%=request.getContextPath()%>/task?activity=Tasklist&user=<%=currentUser%>";
		</script>
<%
	}
%>