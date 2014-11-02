package com.sample;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
/**
 * This is a sample file to launch a process.
 */
public class HelloProcessExtension implements WorkItemHandler {

	 public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
			manager.abortWorkItem(workItem.getId());
		  }
	 
	

	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		
		System.out.println(workItem.getParameter("param1") + "\n" 
		                  +workItem.getParameter("param2") + "\n" 
		                  +workItem.getParameter("param3") );
	    manager.completeWorkItem(workItem.getId(), null);
	  }
	
	
}