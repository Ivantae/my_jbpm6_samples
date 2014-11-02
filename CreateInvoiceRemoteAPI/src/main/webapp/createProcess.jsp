<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
String user = request.getParameter("user");
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>jBPM6 Web Application Sample</title>
</head>
<body>
<jsp:include page="topMenu.jsp" flush="true" />
	<h3>
		<font style="font-weight: bold;"><u>CREATE INVOICE</u></font>
	</h3>
	<form method="post" action="process">
	<table>
		<tr>
			<td>Creating Invoice Process Type:</td>
			<td>
				<select name="processId">
					<option value="com.pti.bpm.CreateInvoiceSequential">Sequential</option>
					<option value="com.pti.bpm.CreateInvoiceParallel">Parallel</option>
				</select>
			</td>
		</tr>
	</table>
	<br>
	<input type="hidden" name="recipient" value="<%=user %>">
	<input type="hidden" name="activity" value="create">
	<input type="submit" name="Submit" value="Submit"/>&nbsp;&nbsp; 
	<input type="reset" name="Cancel" value="Reset"/>&nbsp;&nbsp;
</form>
</body>
</html>