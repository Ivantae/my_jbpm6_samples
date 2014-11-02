package com.pti.jbpm.examples.service;

import java.io.Serializable;

public class VoInvoice implements Serializable {
	private static final long serialVersionUID = -6750652807592666803L;
	private String siteCode;
	private String companyCode;
	private String userCode;
	private String invoiceNo;
	private float invoiceAmt;
	private String invoiceCcy;
	private int invoiceTenor;
	private String invoiceDate;
	private String invoiceStatus;

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getInvoiceCcy() {
		return invoiceCcy;
	}

	public void setInvoiceCcy(String invoiceCcy) {
		this.invoiceCcy = invoiceCcy;
	}

	public int getInvoiceTenor() {
		return invoiceTenor;
	}

	public void setInvoiceTenor(int invoiceTenor) {
		this.invoiceTenor = invoiceTenor;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public float getInvoiceAmt() {
		return invoiceAmt;
	}

	public void setInvoiceAmt(float invoiceAmt) {
		this.invoiceAmt = invoiceAmt;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("VoInvoice={siteCode[" + siteCode + "],");
		sb.append("companyCode[" + companyCode + "],");
		sb.append("userCode[" + userCode + "],");
		sb.append("invoiceNo[" + invoiceNo + "],");
		sb.append("invoiceAmt[" + invoiceAmt + "],");
		sb.append("invoiceCcy[" + invoiceCcy + "],");
		sb.append("invoiceTenor[" + invoiceTenor + "],");
		sb.append("invoiceDate[" + invoiceDate + "]");
		sb.append("}");
		return sb.toString();
	}
}
