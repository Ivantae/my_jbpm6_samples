package com.pti.jbpm.examples.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jbpm.process.audit.ProcessInstanceLog;
import org.kie.api.task.model.TaskSummary;

import com.pti.jbpm.examples.service.TaskBean;
import com.pti.jbpm.examples.service.UserAndRoleHelper;
import com.pti.jbpm.examples.service.VoInvoice;

public class TaskServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			processRequest(request, response);
		} catch (ServletException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			processRequest(request, response);
		} catch (ServletException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	private void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, Exception {
		String activity = request.getParameter("activity");
		String user = request.getParameter("user");
		
		ServletContext context = this.getServletContext();
		TaskBean taskBean = new TaskBean(user, user);
		String targetPage = "/task?activity=Tasklist&user=" + user;
		if ("list".equals(activity)) {
			try {
				List<ProcessInstanceLog> processInstances = taskBean
						.getProcessInstances();
				request.setAttribute("processInstances", processInstances);
				targetPage = "/process.jsp";
			} catch (Exception e) {
				throw new ServletException(e);
			}
		} else if ("Tasklist".equals(activity)) {
			List<TaskSummary> taskList = taskBean.retrieveTaskList(user);
			request.setAttribute("taskList", taskList);
			targetPage = "/task.jsp";
		} else if ("Start".equals(activity) || "Process".equals(activity)) {
			long taskId = Long.parseLong(request.getParameter("taskId"));
			String taskName = request.getParameter("taskName");
			
			if ("Start".equals(activity)) {
				taskBean.startTask(user, taskId);
			}
			
			if("CreateInvoice".equals(taskName)) {
				VoInvoice invoice = taskBean.createInvoiceInit(taskId, user);
				request.setAttribute("invoice", invoice);
				request.setAttribute("taskId", taskId);
				request.setAttribute("user", user);
				targetPage = "/createInvoice.jsp";
			} else if ("UpdateInvoice".equals(taskName)) {
				VoInvoice invoice = taskBean.updateInvoiceInit(taskId, user);
				request.setAttribute("invoice", invoice);
				request.setAttribute("taskId", taskId);
				request.setAttribute("user", user);
				targetPage = "/updateInvoice.jsp";
			} else if ("ManualApproval".equals(taskName)) {
				VoInvoice invoice = taskBean.manualApprovalInvoiceInit(taskId, user);
				request.setAttribute("invoice", invoice);
				request.setAttribute("taskId", taskId);
				request.setAttribute("user", user);
				targetPage = "/manualApprovalInvoice.jsp";
			} else if ("BuyerAcceptInvoice".equals(taskName)) {
				VoInvoice invoice = taskBean.buyerAcceptInvoiceInit(taskId, user);
				request.setAttribute("invoice", invoice);
				request.setAttribute("taskId", taskId);
				request.setAttribute("user", user);
				targetPage = "/buyerAcceptInvoice.jsp";
			}
		} else if ("Stop".equals(activity)) {
			long taskId = Long.parseLong(request.getParameter("taskId"));
			taskBean.stopTask(user, taskId);
		} else if ("Claim".equals(activity)) {
			long taskId = Long.parseLong(request.getParameter("taskId"));
			taskBean.claimTask(user, taskId);
		} else if("ReassignInit".equals(activity)) {
			long taskId = Long.parseLong(request.getParameter("taskId"));
			List<String> users = UserAndRoleHelper.getUsers(user);
			request.setAttribute("users", users);
			request.setAttribute("taskId", taskId);
			request.setAttribute("user", user);
			targetPage = "/assignTask.jsp";
		} else if ("Reassign".equals(activity)) {
			String newUser = request.getParameter("newUser");
			long taskId = Long.parseLong(request.getParameter("taskId"));
			taskBean.assignTask(taskId, newUser, user);
		} else if ("Release".equals(activity)) {
			long taskId = Long.parseLong(request.getParameter("taskId"));
			taskBean.releaseTask(user, taskId);
//		} else if ("Status".equals(activity)) {
//		} else if ("ReportStatus".equals(activity)) {
//		} else if ("GraphicalStatus".equals(activity)) {
		} else if ("createInvoiceSubmit".equals(activity)) {
			long taskId = Long.parseLong(request.getParameter("taskId"));
			String createInvoicResult = request
					.getParameter("createInvoicResult");
			VoInvoice invoice = new VoInvoice();
			invoice.setSiteCode(request.getParameter("siteCode"));
			invoice.setCompanyCode(request.getParameter("companyCode"));
			invoice.setUserCode(request.getParameter("userCode"));
			invoice.setInvoiceNo(request.getParameter("invoiceNo"));
			invoice.setInvoiceAmt(Float.valueOf(request
					.getParameter("invoiceAmt")));
			invoice.setInvoiceCcy(request.getParameter("invoiceCcy"));
			invoice.setInvoiceTenor(Integer.valueOf(request
					.getParameter("invoiceTenor")));
			invoice.setInvoiceDate(request.getParameter("invoiceDate"));
			taskBean.createInvoiceSubmit(taskId, user, invoice,
					createInvoicResult);
		} else if ("updateInvoiceSubmit".equals(activity)) {
			long taskId = Long.parseLong(request.getParameter("taskId"));
			String updateInvoiceResult = request
					.getParameter("updateInvoiceResult");
			VoInvoice invoice = new VoInvoice();
			invoice.setInvoiceNo(request.getParameter("invoiceNo"));
			invoice.setInvoiceAmt(Float.valueOf(request
					.getParameter("invoiceAmt")));
			invoice.setInvoiceCcy(request.getParameter("invoiceCcy"));
			invoice.setInvoiceTenor(Integer.valueOf(request
					.getParameter("invoiceTenor")));
			invoice.setInvoiceDate(request.getParameter("invoiceDate"));
			taskBean.updateInvoiceSubmit(taskId, user, invoice,
					updateInvoiceResult);
		} else if ("manualApprovalInvoiceSubmit".equals(activity)) {
			long taskId = Long.parseLong(request.getParameter("taskId"));
			String manualApprovalResult = request
					.getParameter("manualApprovalResult");
			taskBean.manualApprovalInvoiceSubmit(taskId, user,
					manualApprovalResult);
		} else if ("buyerAcceptInvoiceSubmit".equals(activity)) {
			long taskId = Long.parseLong(request.getParameter("taskId"));
			String buyerAcceptResult = request
					.getParameter("buyerAcceptResult");
			taskBean.buyerAcceptInvoiceSubmit(taskId, user,
					buyerAcceptResult);
		}
		RequestDispatcher dispatcher = context.getRequestDispatcher(targetPage);
		dispatcher.forward(request, response);
	}
}