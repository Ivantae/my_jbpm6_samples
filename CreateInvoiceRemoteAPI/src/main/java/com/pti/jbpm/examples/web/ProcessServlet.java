package com.pti.jbpm.examples.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pti.jbpm.examples.service.ProcessBean;

public class ProcessServlet extends HttpServlet {
	private static final long serialVersionUID = 6793792544014346133L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	processRequest(request, response);
    }
    
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	
	private void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("ProcessServlet handles request........");
		ProcessBean processService = null;
		try {
			processService = new ProcessBean();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
		ServletContext context = this.getServletContext();
		String activity = request.getParameter("activity");
		if ("create".equals(activity)) {
			String recipient = request.getParameter("recipient");
			String processId = request.getParameter("processId");
			try {
				long processInstanceId = processService.startProcess(recipient, recipient, processId);
				System.out.println("process instance (id = " + processInstanceId
						+ ") has been started.");
		        RequestDispatcher dispatcher = context.getRequestDispatcher("/task?activity=list&user="+recipient);
		        dispatcher.forward(request, response);
			} catch (Exception e) {
				throw new ServletException(e);
			}
		} else if("stop".equals(activity)){
			String processInstanceId = request.getParameter("processInstanceId");
			String user = (String) request.getSession(false).getAttribute("currentUser");
			try {
				System.out.println("Stopping process instance (id = "
						+ processInstanceId + ").");
				processService.stopProcessInstance(processInstanceId, user, user);
		        RequestDispatcher dispatcher = context.getRequestDispatcher("/task?activity=list&user="+user);
		        dispatcher.forward(request, response);
			} catch (Exception e) {
				throw new ServletException(e);
			}
		}
	}
}