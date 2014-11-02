package com.sample;

import java.util.HashMap;
import java.util.Map;

import com.sample.MyWorkItemHandler;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

/**
 * This is a sample file to test a process.
 */
public class ServiceTaskProcessTest {
	public static final void main(String[] args) {
		KieServices ks = KieServices.Factory.get();
		KieContainer kContainer = ks.getKieClasspathContainer();
		KieSession ksession = kContainer.newKieSession("ksession-process");

		ksession.getWorkItemManager().registerWorkItemHandler("MyServiceTask",
				new MyWorkItemHandler());
		System.out.println("before process instance start");
		System.out.println("Session ID ==>" + ksession.getId());

		 Map<String, Object> processParams = new HashMap<String, Object>();
         processParams.put("processVar1", "New York");
		ProcessInstance processInstance = ksession
				.startProcess("com.sample.bpmn.myServiceTask", processParams);
		System.out.println("process instance started:"
				+ processInstance.getProcessName());
		ksession.dispose();
	}

}
