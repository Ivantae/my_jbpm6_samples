//package defaultPackage;
package com.sample;

import java.util.HashMap;
import java.util.Map;

import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;


public class MyWorkItemHandler implements WorkItemHandler{
    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
        System.out.print("this is my task service");
        String inputParam = (String) workItem.getParameter("input1");
        String output1 = (String) workItem.getParameter("output1");
        System.out.println(" with passed parameter input1 ["+inputParam+"], output1["+output1+"]");
        
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("output1", "The game is over");
        manager.completeWorkItem(workItem.getId(), resultMap);
    }

     public void abortWorkItem (WorkItem workItem, WorkItemManager manager) {
        // Nothing
	System.out.println("Hell0 there...Aborting...");
    	 
    }
}