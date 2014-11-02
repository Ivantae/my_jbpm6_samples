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

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pti.jbpm.examples.service.ejbImpl.ProcessBean;

public class ProcessServlet extends HttpServlet {
	private static final long serialVersionUID = 6793792544014346133L;

    @EJB
    private ProcessBean processService;

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
		ServletContext context = this.getServletContext();
		String activity = request.getParameter("activity");
		if ("create".equals(activity)) {
			String recipient = request.getParameter("recipient");
			try {
				long processInstanceId = processService.startProcess(recipient);
				System.out.println("process instance (id = " + processInstanceId
						+ ") has been started.");
		        RequestDispatcher dispatcher = context.getRequestDispatcher("/task?activity=list");
		        dispatcher.forward(request, response);
			} catch (Exception e) {
				throw new ServletException(e);
			}
		} else if("stop".equals(activity)){
			String processInstanceId = request.getParameter("processInstanceId");
			try {
				System.out.println("Stopping process instance (id = "
						+ processInstanceId + ").");
				processService.stopProcessInstance(processInstanceId);
		        RequestDispatcher dispatcher = context.getRequestDispatcher("/task?activity=list");
		        dispatcher.forward(request, response);
			} catch (Exception e) {
				throw new ServletException(e);
			}
		}
	}
}