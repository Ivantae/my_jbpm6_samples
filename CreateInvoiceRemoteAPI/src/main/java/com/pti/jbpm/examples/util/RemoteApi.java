package com.pti.jbpm.examples.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.jbpm.process.audit.AuditLogService;
import org.kie.api.runtime.KieSession;
import org.kie.api.task.TaskService;
import org.kie.services.client.api.RemoteRestRuntimeEngineFactory;
import org.kie.services.client.api.command.RemoteRuntimeEngine;

public class RemoteApi {
	public static String deploymentId = "com.pti.demo:CreateInvoice:1.1";
	public static String baseUrl = "http://localhost:8080/jbpm-console";
	private RemoteRuntimeEngine runtimeEngine;
	private String user;
	private String pwd;

	public RemoteApi(String user, String pwd) {
		this.user = user;
		this.pwd = pwd;
		this.init();
	}

	private void init() {
		try {
//			RemoteRestRuntimeFactory restSessionFactory = new RemoteRestRuntimeFactory(
//					deploymentId, new URL(baseUrl), user, pwd);
//            RemoteRuntimeEngine runtimeEngine = RemoteRestRuntimeEngineFactory.newBuilder().addUserName(user)
//                    .addPassword(pwd).addUrl(new URL(baseUrl)).build();
            
            RemoteRestRuntimeEngineFactory factory = 
                    RemoteRestRuntimeEngineFactory.newBuilder()
                    .addUserName(user)
                    .addPassword(pwd)
                    .addUrl(new URL(baseUrl))
                    .build();
            runtimeEngine = factory.newRuntimeEngine();
                    
			System.out.println("===RemoteApi: got RemoteRestRuntimeFactory");
//			runtimeEngine = restSessionFactory.newRuntimeEngine();
		} catch (MalformedURLException e) {
			System.out
					.println("===RemoteApi: creating RemoteRuntimeEngine error");
			e.printStackTrace();
		}
	}

	public KieSession getKieSession() {
		System.out.println("===RemoteApi: got KieSession");
		return runtimeEngine.getKieSession();
	}

	public AuditLogService getAuditLogService() {
		System.out.println("===RemoteApi: got AuditLogService");
		return runtimeEngine.getAuditLogService();
	}

	public TaskService getTaskService() {
		System.out.println("===RemoteApi: got TaskService");
		return runtimeEngine.getTaskService();
	}
}
