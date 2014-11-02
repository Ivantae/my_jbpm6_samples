<%@tag description="User Page template" pageEncoding="UTF-8"%>
<%@ attribute name="username" required="true" %>

<form method="post" action="index.jsp">
	<table>
		<tr>
			<td>Login ID:</td>
			<td> <input type="text" name="userName" value="${pageScope.username}"></td>
		</tr>
		<tr>
			<td>password:</td>
			<td> <input type="password" name="password"></td>
		</tr>
	</table>
	<br> <input type="submit" name="activity" value="Login" />
</form>