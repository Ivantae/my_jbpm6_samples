/**
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pti.jbpm.examples.web;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jbpm.process.audit.ProcessInstanceLog;
import org.kie.api.task.model.TaskSummary;

import com.pti.jbpm.examples.service.VoInvoice;
import com.pti.jbpm.examples.service.ejbImpl.TaskBean;

public class TaskServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@EJB
	private TaskBean taskService;

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
		String targetPage = "/task?activity=Tasklist&user=" + user;
		if ("list".equals(activity)) {
			try {
				List<ProcessInstanceLog> processInstances = taskService
						.getProcessInstances();
				request.setAttribute("processInstances", processInstances);
				targetPage = "/process.jsp";
			} catch (Exception e) {
				throw new ServletException(e);
			}
		} else if ("Tasklist".equals(activity)) {
			List<TaskSummary> taskList = taskService.retrieveTaskList(user);
			request.setAttribute("taskList", taskList);
			targetPage = "/task.jsp";
		} else if ("Start".equals(activity) || "Process".equals(activity)) {
			long taskId = Long.parseLong(request.getParameter("taskId"));
			String taskName = request.getParameter("taskName");
			
			if ("Start".equals(activity)) {
				taskService.startTask(user, taskId);
			}
			
			if("CreateInvoice".equals(taskName)) {
				VoInvoice invoice = taskService.createInvoiceInit(taskId, user);
				request.setAttribute("invoice", invoice);
				request.setAttribute("taskId", taskId);
				request.setAttribute("user", user);
				targetPage = "/createInvoice.jsp";
			} else if ("UpdateInvoice".equals(taskName)) {
				VoInvoice invoice = taskService.updateInvoiceInit(taskId, user);
				request.setAttribute("invoice", invoice);
				request.setAttribute("taskId", taskId);
				request.setAttribute("user", user);
				targetPage = "/updateInvoice.jsp";
			} else if ("ManualApproval".equals(taskName)) {
				VoInvoice invoice = taskService.manualApprovalInvoiceInit(taskId, user);
				request.setAttribute("invoice", invoice);
				request.setAttribute("taskId", taskId);
				request.setAttribute("user", user);
				targetPage = "/manualApprovalInvoice.jsp";
			} else if ("BuyerAcceptInvoice".equals(taskName)) {
				VoInvoice invoice = taskService.buyerAcceptInvoiceInit(taskId, user);
				request.setAttribute("invoice", invoice);
				request.setAttribute("taskId", taskId);
				request.setAttribute("user", user);
				targetPage = "/buyerAcceptInvoice.jsp";
			}
		} else if ("Stop".equals(activity)) {
			long taskId = Long.parseLong(request.getParameter("taskId"));
			taskService.stopTask(user, taskId);
		} else if ("Claim".equals(activity)) {
			long taskId = Long.parseLong(request.getParameter("taskId"));
			taskService.claimTask(user, taskId);
		} else if ("Reassign".equals(activity)) {
			String newUser = request.getParameter("newUser");
			long taskId = Long.parseLong(request.getParameter("taskId"));
			taskService.assignTask(taskId, newUser, user);
		} else if ("Release".equals(activity)) {
			long taskId = Long.parseLong(request.getParameter("taskId"));
			taskService.releaseTask(user, taskId);
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
			taskService.createInvoiceSubmit(taskId, user, invoice,
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
			taskService.updateInvoiceSubmit(taskId, user, invoice,
					updateInvoiceResult);
		} else if ("manualApprovalInvoiceSubmit".equals(activity)) {
			long taskId = Long.parseLong(request.getParameter("taskId"));
			String manualApprovalResult = request
					.getParameter("manualApprovalResult");
			taskService.manualApprovalInvoiceSubmit(taskId, user,
					manualApprovalResult);
		} else if ("buyerAcceptInvoiceSubmit".equals(activity)) {
			long taskId = Long.parseLong(request.getParameter("taskId"));
			String buyerAcceptResult = request
					.getParameter("buyerAcceptResult");
			taskService.buyerAcceptInvoiceSubmit(taskId, user,
					buyerAcceptResult);
		}
		RequestDispatcher dispatcher = context.getRequestDispatcher(targetPage);
		dispatcher.forward(request, response);
	}
}