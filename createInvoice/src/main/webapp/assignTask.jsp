<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>jBPM6 Web Application Sample</title>
<base target="_self">
</head>
<script type="text/javascript">
	function finalMethod() {
		opener.location.reload(true);
	}

	function confirmBox() {
		var confirmVal = confirm("Are you sure you want to continue?");
		if (confirmVal == true) {
			window.close();
		}
		return confirmVal;
	}
</script>
<body onunload="finalMethod()">
	<form action="task" method="post">
		<%
			String userName = (String) session.getAttribute("currentUser");
			String taskId = request.getParameter("taskId");
		%>

		<center>
			<h3>
				<font style="font-weight: bold;"><u>Assign Task</u></font>
			</h3>

			<table>
				<tr>
					<td>Current Assignee</td>
					<td><input type="text" name="user"
						value="<%=userName%>" readonly="readonly" /></td>
				</tr>
				<tr>
					<td>New Assignee</td>
					<td><input type="text" name="newUser" value="" /></td>
				</tr>
			</table>
			<input type="hidden" name="taskId" value="<%=taskId%>" /> <br>
			<input type="submit" name="activity" value="Reassign"
				onclick="return confirmBox()" />
		</center>
	</form>
</body>
</html>