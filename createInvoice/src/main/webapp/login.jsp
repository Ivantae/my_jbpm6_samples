<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.pti.jbpm.examples.service.UserAndRoleHelper" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>jBPM6 Web Application Sample</title>
</head>
<body>
	<%
		String activity = request.getParameter("activity");
			if("Login".equals(activity)) {
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		System.out.println("userName["+userName + "], password["+password+"]");
		
		if(null == userName || "".equals(userName.trim())) {
		// login screen
	%>
				<t:loginForm username=""></t:loginForm>
			<%
				} else {
						List<String> roles = UserAndRoleHelper.login(userName, password);
						if(null == roles) {//logon fail
			%>
						<span><b><font color="red">The entered user <%=userName%> is not existing.</font></b></span>
						<t:loginForm username="<%=userName%>"></t:loginForm>
					<% } else {
						session.setAttribute("roles", roles);
						session.setAttribute("currentUser", userName);
					%>
						<script>
							location.href="<%=request.getContextPath()%>/task?activity=Tasklist&user=<%=userName%>";
						</script>
					<%}
				}	
		} else if("Logout".equals(activity)){
			session.invalidate();
		%>
			<t:loginForm username=""></t:loginForm>
		<%} else {%>
			<t:loginForm username=""></t:loginForm>
	<%} %>
</body>
</html>