<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>jBPM6 Web Application Sample</title>
<script type="text/javascript">
function submitForm(resultVal){
	document.getElementById("updateInvoiceResult").value = resultVal;
	//alert(document.getElementById("updateInvoiceResult").value);
	document.forms[0].submit();
}
</script>
</head>
<body>
<jsp:include page="topMenu.jsp" flush="true" />
<p>Update Invoice</p>
	<form method="post" action="task">
	<table>
		<tr>
			<td>Invoice No:</td>
			<td><input type="text" name="invoiceNo" value="${invoice.invoiceNo}"></td>
		</tr>
		<tr>
			<td>Currency:</td>
			<td><input type="text" name="invoiceCcy" value="${invoice.invoiceCcy}"><font color="red">E.g. USD or EUR</font></td>
		</tr>
		<tr>
			<td>Amount:</td>
			<td><input type="text" name="invoiceAmt" value="${invoice.invoiceAmt}"></td>
		</tr>
		<tr>
			<td>Invoice Tenor:</td>
			<td><input type="text" name="invoiceTenor" value="${invoice.invoiceTenor}"></td>
		</tr>
		<tr>
			<td>Invoice Date:</td>
			<td><input type="text" name="invoiceDate" value="${invoice.invoiceDate}"><font color="red">E.g. 2014-07-31</font></td>
		</tr>
	</table>
	<br>
	<input type="hidden" name="user" value="${user}">
	<input type="hidden" name="taskId" value="${taskId}">
	<input type="hidden" name="activity" value="updateInvoiceSubmit">
	<input type="hidden" name="updateInvoiceResult" id="updateInvoiceResult">
	<input type="button" name="Submit" value="Submit" onclick="submitForm('updateSubmit');" />&nbsp;&nbsp; 
	<input type="button" name="Save" value="Save" onclick="submitForm('updateSave');"/>&nbsp;&nbsp;
	<input type="button" name="Cancel" value="Cancel" onclick="submitForm('updateCancel');"/>&nbsp;&nbsp;
</form>
</body>
</html>