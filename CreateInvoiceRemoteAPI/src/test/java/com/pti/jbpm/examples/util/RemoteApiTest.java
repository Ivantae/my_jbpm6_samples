package com.pti.jbpm.examples.util;

import java.io.PrintStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Status;
import org.kie.api.task.model.TaskSummary;
import org.kie.services.client.api.RemoteRestRuntimeFactory;

public class RemoteApiTest {

	private PrintStream ps = System.out;
	private String deploymentId = null;
	private String baseUrl = null;
	
	public void setPrintStream(PrintStream printStream) {
		ps = printStream;
	}
	
	public RemoteApiTest(String deploymentId, String baseUrl) {
		this.deploymentId = deploymentId;
		this.baseUrl = baseUrl;
	}
	
	public void startProcess(String processId, String user, String pwd) throws Exception {
		RemoteRestRuntimeFactory restSessionFactory 
		  = new RemoteRestRuntimeFactory(deploymentId, new URL(baseUrl), user, pwd);
		ps.println("=== got RemoteRestRuntimeFactory");

		// Create KieSession and TaskService instances and use them
		RuntimeEngine engine = restSessionFactory.newRuntimeEngine();
		ps.println("=== got RuntimeEngine");
		KieSession ksession = engine.getKieSession();
		ps.println("=== got KieSession");
		
		Map<String, Object> procParams = new HashMap<String, Object>();
		procParams.put("employee", user);
		procParams.put("reason", "Annual review");
		ProcessInstance processInstance = ksession.startProcess(processId, procParams);
		long procInstId = processInstance.getId();
		ps.println("=== started ProcessInstance " + String.valueOf(procInstId));
	}
	
	public void processTask(String processId, String user, String pwd) throws Exception {
		RemoteRestRuntimeFactory restSessionFactory 
		  = new RemoteRestRuntimeFactory(deploymentId, new URL(baseUrl), user, pwd);
		ps.println("=== got RemoteRestRuntimeFactory");

		// Create KieSession and TaskService instances and use them
		RuntimeEngine engine = restSessionFactory.newRuntimeEngine();
		ps.println("=== got RuntimeEngine");
		
		String taskUserId = user;
		TaskService taskService = engine.getTaskService();
		ps.println("=== got TaskService");
		
		List<TaskSummary> tasks = taskService.getTasksAssignedAsPotentialOwner(taskUserId, "en-UK");
		TaskSummary task = null;
		for (TaskSummary t : tasks) {
			if (processId.equals(t.getProcessId())) task = t;
		}
		
		if (task == null) {
			ps.println("=== No task found for process " + processId);
			return;
		}
		
		ps.println("=== got task (id: " + String.valueOf(task.getId()) + "; name: " + task.getName() + ") for process instance (id: " + String.valueOf(task.getProcessInstanceId()) + ";  " + processId + ")");
//		if (task.getStatus().equals(Status.Ready)) {
//			taskService.claim(task.getId(), taskUserId);
//			ps.println("=== claimed Task (id: " + String.valueOf(task.getId()) + "; name: " + task.getName() + ")");
//		}
//		taskService.start(task.getId(), taskUserId);
//		ps.println("=== started Task (id: " + String.valueOf(task.getId()) + "; name: " + task.getName() + ")");
//		
//		Map<String, Object> taskResults = new HashMap<String, Object>();
//		taskResults.put("performance", "Good");
//		taskService.complete(task.getId(), taskUserId, taskResults);
//		ps.println("=== completed Task (id: " + String.valueOf(task.getId()) + "; name: " + task.getName() + ")");
	}
	
	/**
	 * 
	 * @param processId
	 * @throws Exception
	 */
	public void testProcess(String processId, String user, String pwd) throws Exception {
		// Setup the factory class with the necessary information to communicate with the REST services
		RemoteRestRuntimeFactory restSessionFactory 
		  = new RemoteRestRuntimeFactory(deploymentId, new URL(baseUrl), user, pwd);
		ps.println("=== got RemoteRestRuntimeFactory");

		// Create KieSession and TaskService instances and use them
		RuntimeEngine engine = restSessionFactory.newRuntimeEngine();
		ps.println("=== got RuntimeEngine");
		KieSession ksession = engine.getKieSession();
		ps.println("=== got KieSession");
		
		Map<String, Object> procParams = new HashMap<String, Object>();
		procParams.put("employee", user);
		procParams.put("reason", "Annual review");
		ProcessInstance processInstance = ksession.startProcess(processId, procParams);
		long procInstId = processInstance.getId();
		ps.println("=== started ProcessInstance " + String.valueOf(procInstId));
		  
		String taskUserId = user;
		TaskService taskService = engine.getTaskService();
		ps.println("=== got TaskService");
		
		List<TaskSummary> tasks = taskService.getTasksAssignedAsPotentialOwner(taskUserId, "en-UK");
		TaskSummary task = null;
		for (TaskSummary t : tasks) {
			if (t.getProcessInstanceId() == procInstId) task = t;
		}
		
		if (task == null) {
			ps.println("=== No task for the process instance");
			return;
		}
		
		ps.println("=== got task (id: " + String.valueOf(task.getId()) + "; name: " + task.getName() + ") for the process instance");
		if (task.getStatus().equals(Status.Ready)) {
			taskService.claim(task.getId(), taskUserId);
			ps.println("=== claimed Task (id: " + String.valueOf(task.getId()) + "; name: " + task.getName() + ")");
		}
		taskService.start(task.getId(), taskUserId);
		ps.println("=== started Task (id: " + String.valueOf(task.getId()) + "; name: " + task.getName() + ")");
		
		Map<String, Object> taskResults = new HashMap<String, Object>();
		taskResults.put("performance", "Good");
		taskService.complete(task.getId(), taskUserId, taskResults);
		ps.println("=== completed Task (id: " + String.valueOf(task.getId()) + "; name: " + task.getName() + ")");
	}

	public static void main(String[] args) {
		String deploymentId = RemoteApi.deploymentId;
		String baseUrl = "http://localhost:8080/jbpm-console";
		String user = "john";
		String pwd = "john";
		String processId = "com.pti.bpm.CreateInvoiceSequential";
		String action = "startTask";
		for (int i = 0; i < args.length - 1; i += 2) {
			if ("-action".equalsIgnoreCase(args[i])) {
				action = args[i + 1];
			} else if ("-user".equalsIgnoreCase(args[i])) {
				user = args[i + 1];
			} else if ("-password".equalsIgnoreCase(args[i])) {
				pwd = args[i + 1];
			}
		}
		if (!("startTask".equalsIgnoreCase(action)) && !("startProcess".equalsIgnoreCase(action))) action = "startAll";
		System.out.println("Action: " + action + ";  user: " + user + ";  pwd: " + pwd);
		try {
			RemoteApiTest apiTest = new RemoteApiTest(deploymentId, baseUrl);
			if ("startProcess".equalsIgnoreCase(action)) {
				apiTest.startProcess(processId, user, pwd);
			} else if ("startTask".equalsIgnoreCase(action)) {
				apiTest.processTask(processId, user, pwd);
			} else {
				apiTest.testProcess(processId, user, pwd);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
