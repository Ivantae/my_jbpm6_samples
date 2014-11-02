<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.kie.api.task.model.TaskSummary"%>
<%@ page import="org.kie.api.task.model.Status" %>
<%@ page import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>jBPM6 Web Application Sample</title>
<script type="text/javascript">
function winPopUp(id) {
	var tempId = "task"+id;
	var taskId = document.getElementById(tempId).value;
	var url = "assignTask.jsp?taskId="+taskId;
	window.open(url,'','width=350, height=200');
}
</script>
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
			<th>Status</th>
			<th>Action</th>
		</tr>
		<%
			int i = 0;	
			for (TaskSummary task : tasks) {
				i++;
		%>
		<tr>
			<td><%=task.getName()%></td>
			<td><%=task.getId()%></td>
			<td><%=task.getProcessId()%></td>
			<td><%=task.getProcessInstanceId()%></td>
			<td><%=task.getStatus()%></td>
			<td><input type="hidden" id="task<%=i%>" value="<%=task.getId()%>" />
				<% if(Status.Ready.equals(task.getStatus())) {%>
					<a href="task?user=<%=user%>&taskId=<%=task.getId()%>&activity=Claim">Claim</a>
				<%} else if(Status.Reserved.equals(task.getStatus())) { %>
					<a href="<%=root%>/task?user=<%=user%>&taskId=<%=task.getId()%>&activity=Start&taskName=<%=task.getName()%>">Start</a>&nbsp;&nbsp;
					<a href="<%=root%>/task?user=<%=user%>&taskId=<%=task.getId()%>&activity=Release">Release</a>&nbsp;&nbsp;
					<!-- a href="<%=root%>/task?user=<%=user%>&taskId=<%=task.getId()%>&activity=Stop">Stop</a>&nbsp;&nbsp;
					<a href="javascript:winPopUp(<%=i%>)" onclick="return false"><b>Reassign</b></a-->
				<%} else if(Status.InProgress.equals(task.getStatus())) {%>
					<a href="<%=root%>/task?user=<%=user%>&taskId=<%=task.getId()%>&activity=Process&taskName=<%=task.getName()%>">Process</a>&nbsp;&nbsp;
					<a href="<%=root%>/task?user=<%=user%>&taskId=<%=task.getId()%>&activity=Release">Release</a>&nbsp;&nbsp;
					<!-- a href="<%=root%>/task?user=<%=user%>&taskId=<%=task.getId()%>&activity=Stop">Stop</a>&nbsp;&nbsp;
					<a href="javascript:winPopUp(<%=i%>)" onclick="return false"><b>Reassign</b></a-->
				<%} else if(Status.Completed.equals(task.getStatus())) {%>
				<%}%>
			</td>
		</tr>
		<%
			}
		%>
	</table>
</body>
</html>