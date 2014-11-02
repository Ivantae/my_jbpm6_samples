package com.pti.jbpm.examples.util;

import java.util.HashMap;
import java.util.Map;

import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;

public class SellerProfileWorkItemHandler implements WorkItemHandler {
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		System.out.print("This workitem with id["+workItem.getId()+"] is ["+workItem.getName()+"] Task");
		String invoiceCcy = (String) workItem.getParameter("ccy");
		System.out.println(" with passed parameter invoiceCcy [" + invoiceCcy + "]");

		Map<String, Object> resultMap = new HashMap<String, Object>();
		if ("USD".equals(invoiceCcy)) {
			resultMap.put("isAutoApproval", true);
			System.out
					.println("******************set isAutoApproval true for USD");
		} else {
			resultMap.put("isAutoApproval", false);
			System.out
					.println("******************set isAutoApproval false for non-USD");
		}
		manager.completeWorkItem(workItem.getId(), resultMap);
	}

	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		System.out.println("******************getSellerProfile is aborting");
	}
}