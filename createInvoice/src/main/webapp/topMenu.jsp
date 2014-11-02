<%
String root = request.getContextPath();
String user = (String)session.getAttribute("currentUser");
%>
<table>
	<tr>
		<td bgcolor="black">
			<a href="<%=root%>/task?activity=Tasklist&user=<%=user%>" style="text-decoration: none">
				<font size="2" color="white"><b>TASKLIST</b></font>
			</a>&nbsp;&nbsp;|&nbsp;&nbsp;
		</td>
		<td bgcolor="black">
			<a href="<%=root%>/process?activity=create&recipient=<%=user%>" style="text-decoration: none">
				<font size="2" color="white"><b>CREATE INVOICE</b></font>
			</a>&nbsp;&nbsp;|&nbsp;&nbsp;
		</td>
		<td bgcolor="black">
			<a href="<%=root%>/task?activity=list&user=<%=user%>" style="text-decoration: none">
				<font size="2" color="white"><b>LIST PROCESS INSTANCES</b></font>
			</a>&nbsp;&nbsp;|&nbsp;&nbsp;
		</td>
		<!-- td bgcolor="black">
			<a href="<%=root%>/task?activity=Status" style="text-decoration: none">
				<font size="2" color="white"><b>STATUS</b></font>
			</a>&nbsp;&nbsp;|&nbsp;&nbsp;
		</td-->
		<td bgcolor="black">
			<a href="login.jsp?activity=Logout" style="text-decoration: none">
				<font size="2" color="white"><b>LOGOUT</b></font>
			</a>
		</td>
	</tr>
	<tr>
		<td colspan=4 bgcolor="#9A9790">
			<span style="text-decoration: none;text-align:right">
				<jsp:include page="userInfo.jsp" flush="true"></jsp:include>
			</span>
		</td>
	</tr>
</table>