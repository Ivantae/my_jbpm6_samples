package com.pti.jbpm.examples.service;

import java.util.HashMap;
import java.util.Map;

import org.kie.api.runtime.process.ProcessInstance;

import com.pti.jbpm.examples.util.RemoteApi;

public class ProcessBean {
	public long startProcess(String recipient, String pwd, String processId) throws Exception {
		long processInstanceId = -1;

		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("userCode", recipient);
			params.put("siteCode", "PTBANK");
			params.put("companyCode", "SELLER01");
			ProcessInstance processInstance = new RemoteApi(recipient, pwd)
					.getKieSession().startProcess(processId, params);
			processInstanceId = processInstance.getId();
			System.out.println("=== Process started ... : processInstanceId = "
					+ processInstanceId);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return processInstanceId;
	}

	public void stopProcessInstance(String processInstanceId, String user,
			String pwd) throws Exception {
		System.out.println("ProcessBean stopProcessInstance...");
		try {
			new RemoteApi(user, pwd).getKieSession().abortProcessInstance(
					Long.valueOf(processInstanceId));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
