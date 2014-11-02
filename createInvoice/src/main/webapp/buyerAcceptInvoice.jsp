<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>jBPM6 Web Application Sample</title>
<script type="text/javascript">
function submitForm(resultVal){
	document.getElementById("buyerAcceptResult").value = resultVal;
	//alert(document.getElementById("buyerAcceptResult").value);
	document.forms[0].submit();
}
</script>
</head>
<body>
<jsp:include page="topMenu.jsp" flush="true" />
<p>Buyer Accept Invoice</p>
	<form method="post" action="task">
	<table>
		<tr>
			<td>Site Code:</td>
			<td><input type="text" name="siteCode" value="${invoice.siteCode}" readonly></td>
		</tr>
		<tr>
			<td>Company Code:</td>
			<td><input type="text" name="companyCode" value="${invoice.companyCode}" readonly></td>
		</tr>		
		<tr>
			<td>Invoice No:</td>
			<td><input type="text" name="invoiceNo" value="${invoice.invoiceNo}" readonly></td>
		</tr>
		<tr>
			<td>Currency:</td>
			<td><input type="text" name="invoiceCcy" value="${invoice.invoiceCcy}" readonly></td>
		</tr>
		<tr>
			<td>Amount:</td>
			<td><input type="text" name="invoiceAmt" value="${invoice.invoiceAmt}" readonly></td>
		</tr>
		<tr>
			<td>Invoice Tenor:</td>
			<td><input type="text" name="invoiceTenor" value="${invoice.invoiceTenor}" readonly></td>
		</tr>
		<tr>
			<td>Invoice Date:</td>
			<td><input type="text" name="invoiceDate" value="${invoice.invoiceDate}" readonly></td>
		</tr>
	</table>
	<br>
	<input type="hidden" name="user" value="${user}">
	<input type="hidden" name="taskId" value="${taskId}">
	<input type="hidden" name="activity" value="buyerAcceptInvoiceSubmit">
	<input type="hidden" name="buyerAcceptResult" id="buyerAcceptResult">
	<input type="button" name="Submit" value="Accept" onclick="submitForm('Accepted');" />&nbsp;&nbsp; 
	<input type="button" name="Cancel" value="Reject" onclick="submitForm('Rejected');"/>&nbsp;&nbsp;
</form>
</body>
</html>