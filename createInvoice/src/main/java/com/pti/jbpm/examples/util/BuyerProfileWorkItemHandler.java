package com.pti.jbpm.examples.util;

import java.util.HashMap;
import java.util.Map;

import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;

public class BuyerProfileWorkItemHandler implements WorkItemHandler {
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		System.out.print("This workitem with id["+workItem.getId()+"] is ["+workItem.getName()+"] Task");
		Float amt = (Float) workItem.getParameter("invoiceAmt");
		System.out.println(" with passed parameter invoiceAmt [" + amt + "]");

		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (amt > 2000) {
			resultMap.put("buyerAcceptFlag", true);
			System.out
					.println("******************set buyerAcceptFlag true for amount > 2000");
		} else {
			resultMap.put("buyerAcceptFlag", false);
			System.out
					.println("******************set buyerAcceptFlag false for amount <= 2000");
		}
		manager.completeWorkItem(workItem.getId(), resultMap);
	}

	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		System.out.println("******************getBuyerProfile is aborting");
	}
}