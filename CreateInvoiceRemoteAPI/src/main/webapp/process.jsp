<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="org.jbpm.process.audit.ProcessInstanceLog"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>jBPM6 Web Application Sample</title>
</head>
<body>
	<jsp:include page="topMenu.jsp" flush="true" />
	<form action="TaskProcessServlet" method="GET">
		<h3>
			<font style="font-weight: bold;"><u>Process Instance List</u></font>
		</h3>
		<table border="2" bordercolor="black">
			<tr>
				<th>Instance ID</th>
				<th>Process ID</th>
				<th>Process Name</th>
				<th>Status</th>
				<!-- th>Action</th-->
				<th>Site</th>
				<th>Company</th>
			</tr>
			<%
				String root = request.getContextPath();	
				List<ProcessInstanceLog> processInstances = (List<ProcessInstanceLog>) (request.getAttribute("processInstances"));
				for (ProcessInstanceLog item: processInstances) {
			%>
				<tr>
					<td><%=item.getProcessInstanceId()%></td>
					<td><%=item.getProcessId()%></td>
					<td><%=item.getProcessName()%></td>
					<%
						int statusTmp = item.getStatus();
						if(statusTmp == 2){
					%>
							<td>COMPLETED</td>
					<%		
						} else if(statusTmp == 1) {
					%>
							<td>ACTIVE</td>
					<%
						} else if(statusTmp == 3){
					%>
							<td>ABORTED</td>
					<%	
						} else {
					%>
							<td><%= statusTmp%></td>
					<% }%>
					<!-- td>
						<%if(statusTmp != 2 && statusTmp != 3){ %>
							<a href="<%=root %>/process?activity=stop&processInstanceId=<%=item.getProcessInstanceId()%>"><b>Stop</b></a>
						<%} else { %>
							N/A
						<%} %>
					</td-->
					<td><%=(null == item.getSiteCode() ? "" : item.getSiteCode()) %></td>
					<td><%=(null == item.getCompanyCode() ? "" : item.getCompanyCode()) %></td>
				</tr>
			<%}%>
		</table>
	</form>
</body>
</html>