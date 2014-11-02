<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.kie.api.task.model.TaskSummary"%>
<%@ page import="org.kie.api.task.model.QuickTaskSummary" %>
<%@ page import="org.kie.api.task.model.Status" %>
<%@ page import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>jBPM6 Web Application Sample</title>
</head>
<body>
	<jsp:include page="topMenu.jsp" flush="true" />
	<%
		String root = request.getContextPath();	
		String user = (String) session.getAttribute("currentUser");
		List<TaskSummary> tasks = (List<TaskSummary>) request.getAttribute("taskList");
	%>
	<h3>
		<font style="font-weight: bold;"><u><%=user%>'s Task</u></font>
	</h3>
	<table border="1">
		<tr>
			<th>Task Name</th>
			<th>Task Id</th>
			<th>Process Id</th>
			<th>ProcessInstance Id</th>
			<th>Owner</th>
			<th>Status</th>
			<th>Action</th>
			<th>Site</th>
			<th>Company</th>
		</tr>
		<%
		if(null != tasks) {	
			int i = 0;
			String actualOwner = null;
			for (TaskSummary task : tasks) {
				i++;
				actualOwner = null;
				if(null != task.getActualOwner()) {
					actualOwner = task.getActualOwner().getId();
				}
		%>
		<tr>
			<td><%=task.getName()%></td>
			<td><%=task.getId()%></td>
			<td><%=task.getProcessId()%></td>
			<td><%=task.getProcessInstanceId()%></td>
			<td><%=(null==actualOwner ? "" : actualOwner)%></td>
			<td><%=task.getStatus()%></td>
			<td>
				<% if(Status.Ready.equals(task.getStatus())) {%>
					<a href="task?user=<%=user%>&taskId=<%=task.getId()%>&activity=Claim">Claim</a>
				<%} else if(Status.Reserved.equals(task.getStatus()) && user.equals(actualOwner)) { %>
					<a href="<%=root%>/task?user=<%=user%>&taskId=<%=task.getId()%>&activity=Start&taskName=<%=task.getName()%>">Start</a>&nbsp;&nbsp;
					<a href="<%=root%>/task?user=<%=user%>&taskId=<%=task.getId()%>&activity=Release">Release</a>&nbsp;&nbsp;
					<a href="<%=root%>/task?user=<%=user%>&taskId=<%=task.getId()%>&activity=ReassignInit">Reassign</a>
				<%} else if(Status.InProgress.equals(task.getStatus()) && user.equals(actualOwner)) {%>
					<a href="<%=root%>/task?user=<%=user%>&taskId=<%=task.getId()%>&activity=Process&taskName=<%=task.getName()%>">Process</a>&nbsp;&nbsp;
					<a href="<%=root%>/task?user=<%=user%>&taskId=<%=task.getId()%>&activity=Release">Release</a>&nbsp;&nbsp;
					<a href="<%=root%>/task?user=<%=user%>&taskId=<%=task.getId()%>&activity=Stop">Stop</a>&nbsp;&nbsp;
					<a href="<%=root%>/task?user=<%=user%>&taskId=<%=task.getId()%>&activity=ReassignInit">Reassign</a>
				<%} else {%>
					N/A
				<%}%>
			</td>
			<td><%=(null == task.getMoreProperties().get("site_code") ? "" : task.getMoreProperties().get("site_code")) %></td>
			<td><%=(null == task.getMoreProperties().get("company_code") ? "" : task.getMoreProperties().get("company_code")) %></td>
		</tr>
		<%
			}
		}
		%>
	</table>
</body>
</html>