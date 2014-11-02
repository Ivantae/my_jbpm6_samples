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

package com.pti.jbpm.examples.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.process.audit.AuditLogService;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.kie.api.search.SearchCriteria;
import org.kie.api.search.WhereParameter;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.TaskSummary;

import com.pti.jbpm.examples.util.RemoteApi;

public class TaskBean {
	private TaskService taskService = null;
	private AuditLogService logService = null;

	public TaskBean(String user, String pwd) {
		RemoteApi remoteApi = new RemoteApi(user, pwd);
		this.taskService = remoteApi.getTaskService();
		this.logService = remoteApi.getAuditLogService();
	}

	public List<ProcessInstanceLog> getProcessInstances() throws Exception {
		System.out.println("TaskBean getProcessInstances...");
		List<ProcessInstanceLog> list = null;

		try {
            StringBuffer whereStr = new StringBuffer();
            whereStr.append("p.processid=?1 and c.company_code=?2");
            SearchCriteria criteria = new SearchCriteria();
            criteria.setWhereStr(whereStr.toString());
            criteria.setFirstResult(0);
            criteria.setMaxResults(3);

            WhereParameter t = new WhereParameter(1);
            t.addParameterValue("com.pti.bpm.CreateInvoiceSequential");
            criteria.addWhereParameter(t);

            t = new WhereParameter(2);
            t.addParameterValue("SELLER01");
            criteria.addWhereParameter(t);

            list = logService.findProcessInstances(criteria);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return list;
	}
	
	public List<TaskSummary> retrieveTaskList(String actorId) throws Exception {
		System.out.println("TaskBean retrieveTaskList...");
		List<TaskSummary> list;

		try {
		    StringBuffer whereStr = new StringBuffer();
            whereStr.append("(potentialOwners.id=?1 or potentialOwners.id in (?2))").append(" and (potentialOwners.id in (select pot_.entity_id from PeopleAssignments_PotOwners pot_ where t.id=pot_.task_id))")
                    .append(" and (name.language=?3 or (select count(names8_.Task_Names_Id) from I18NText names8_ where t.id=names8_.Task_Names_Id)=0)")
                    .append(" and (subject.language=?3 or (select count(subjects9_.Task_Subjects_Id) from I18NText subjects9_ where t.id=subjects9_.Task_Subjects_Id)=0)")
                    .append(" and (description.language=?3 or (select count(descriptio10_.Task_Descriptions_Id) from I18NText descriptio10_ where t.id=descriptio10_.Task_Descriptions_Id)=0)");
//            if (null != businessAdministrator && !businessAdministrator.isEmpty()) {
//                whereStr.append("(businessAdministrator.id in (?4)) and (businessAdministrator.id in (select ba_.entity_id from peopleAssignments_businessAdministrators ba_ where t.id=ba_.task_id))");
//            }
            
            SearchCriteria criteria = new SearchCriteria();
            criteria.setWhereStr(whereStr.toString());
	        criteria.setFirstResult(0);
	        criteria.setMaxResults(3);
	        
	        WhereParameter t = new WhereParameter(1);
	        t.addParameterValue(actorId);
	        criteria.addWhereParameter(t);
	        
	        t = new WhereParameter(2);
	        t.setNeedIncludeGroupId(true); //backend will get groupIds and fill it in
	        criteria.addWhereParameter(t);
	        
	        t = new WhereParameter(3);
	        t.addParameterValue("en-UK");
	        criteria.addWhereParameter(t);
	        
            list = taskService.getTasks(criteria);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		System.out.println("retrieveTaskList by " + actorId);
        for (TaskSummary task : list) {
            System.out.println(" taskId=" + task.getId() + ", siteCode=" + task.getMoreProperties().get("site_code") + ", companyCode="
                    + task.getMoreProperties().get("company_code"));
        }

		return list;
	}

	public void claimTask(String user, long taskId) {
		System.out.println("claimTask (taskId = " + taskId + ") by " + user);
		try {
			taskService.claim(taskId, user);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void startTask(String user, long taskId) {
		System.out.println("startTask (taskId = " + taskId + ") by " + user);
		try {
			taskService.start(taskId, user);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void releaseTask(String user, long taskId) {
		System.out.println("completeTask (taskId = " + taskId + ") by " + user);
		try {
			taskService.release(taskId, user);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void assignTask(long taskId, String newUser, String userName) {
		System.out.println("assignTask (taskId = " + taskId
				+ ") from old user[" + userName + "] to new user[" + newUser
				+ "]");
		try {
			if (newUser == null) {
				taskService.release(taskId, userName);
			} else {
				taskService.delegate(taskId, userName, newUser);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void stopTask(String user, long taskId) {
		System.out.println("stopTask (taskId = " + taskId + ") by " + user);
		try {
			taskService.stop(taskId, user);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public VoInvoice createInvoiceInit(long taskId, String user) {
		Map<String, Object> taskVars = JbpmAPIHelper.getTaskVariables(
				taskService, taskId);
		VoInvoice invoice = new VoInvoice();
		invoice.setCompanyCode((String) taskVars.get("companyCode_"));
		invoice.setSiteCode((String) taskVars.get("siteCode_"));
		invoice.setUserCode(user);
		return invoice;
	}

	public void createInvoiceSubmit(long taskId, String user,
			VoInvoice invoice, String createInvoicResult) {
		System.out.println("createInvoiceSubmit (user = " + user
				+ "), (taskId = " + taskId + "), (createInvoicResult = "
				+ createInvoicResult + "), (invoice = " + invoice + ")");
		try {
			Map<String, Object> taskVars = JbpmAPIHelper.getTaskVariables(
					taskService, taskId);
			System.out.println("  companyCode_ -> "
					+ taskVars.get("companyCode_"));
			taskVars.put("_createInvoiceResult", createInvoicResult);
			taskVars.put("_invoiceNo", invoice.getInvoiceNo());
			taskVars.put("_invoiceCcy", invoice.getInvoiceCcy());
			taskVars.put("_invoiceAmt", invoice.getInvoiceAmt());
			taskVars.put("_invoiceTenor", invoice.getInvoiceTenor());
			taskVars.put("_invoiceDate", invoice.getInvoiceDate());
			taskVars.put("_invoiceStatus", "C");
			taskService.complete(taskId, user, taskVars);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public VoInvoice updateInvoiceInit(long taskId, String user) {
		Map<String, Object> taskVars = JbpmAPIHelper.getTaskVariables(
				taskService, taskId);
		VoInvoice invoice = new VoInvoice();
		invoice.setInvoiceNo((String) taskVars.get("_invoiceNo"));
		invoice.setInvoiceCcy((String) taskVars.get("_invoiceCcy"));
		invoice.setInvoiceAmt((Float) taskVars.get("_invoiceAmt"));
		invoice.setInvoiceTenor((Integer) taskVars.get("_invoiceTenor"));
		invoice.setInvoiceDate((String)taskVars.get("_invoiceDate"));
		invoice.setUserCode(user);
		return invoice;
	}

	public void updateInvoiceSubmit(long taskId, String user,
			VoInvoice invoice, String updateInvoiceResult) {
		System.out.println("updateInvoiceSubmit (user = " + user
				+ "), (taskId = " + taskId + "), (updateInvoiceResult = "
				+ updateInvoiceResult + "), (invoice = " + invoice + ")");
		try {
			Map<String, Object> taskVars = new HashMap<String, Object>();
			taskVars.put("updateInvoiceResult_", updateInvoiceResult);
			taskVars.put("invoiceNo_", invoice.getInvoiceNo());
			taskVars.put("invoiceCcy_", invoice.getInvoiceCcy());
			taskVars.put("invoiceAmt_", invoice.getInvoiceAmt());
			taskVars.put("invoiceTenor_", invoice.getInvoiceTenor());
			taskVars.put("invoiceDate_", invoice.getInvoiceDate());
			taskService.complete(taskId, user, taskVars);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public VoInvoice manualApprovalInvoiceInit(long taskId, String user) {
		Map<String, Object> taskVars = JbpmAPIHelper.getTaskVariables(
				taskService, taskId);
		VoInvoice invoice = new VoInvoice();
		invoice.setCompanyCode((String) taskVars.get("_companyCode"));
		invoice.setSiteCode((String) taskVars.get("_siteCode"));
		invoice.setInvoiceNo((String) taskVars.get("_invoiceNo"));
		invoice.setInvoiceCcy((String) taskVars.get("_invoiceCcy"));
		invoice.setInvoiceAmt((Float) taskVars.get("_invoiceAmt"));
		invoice.setInvoiceTenor((Integer) taskVars.get("_invoiceTenor"));
		invoice.setInvoiceDate((String)taskVars.get("_invoiceDate"));
		invoice.setUserCode(user);
		return invoice;
	}

	public void manualApprovalInvoiceSubmit(long taskId, String user,
			String manualApprovalResult) {
		System.out.println("manualApprovalInvoiceSubmit (user = " + user
				+ "), (taskId = " + taskId + "), (manualApprovalResult = "
				+ manualApprovalResult + ")");
		try {
			Map<String, Object> taskVars = new HashMap<String, Object>();
			taskVars.put("_manualApprovalResult", manualApprovalResult);
			taskVars.put("_invoiceStatus", "A");
			taskService.complete(taskId, user, taskVars);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public VoInvoice buyerAcceptInvoiceInit(long taskId, String user) {
		Map<String, Object> taskVars = JbpmAPIHelper.getTaskVariables(
				taskService, taskId);
		VoInvoice invoice = new VoInvoice();
		invoice.setCompanyCode((String) taskVars.get("in_companyCode"));
		invoice.setSiteCode((String) taskVars.get("in_siteCode"));
		invoice.setInvoiceNo((String) taskVars.get("in_invoiceNo"));
		invoice.setInvoiceCcy((String) taskVars.get("in_invoiceCcy"));
		invoice.setInvoiceAmt((Float) taskVars.get("in_invoiceAmt"));
		invoice.setInvoiceTenor((Integer) taskVars.get("in_invoiceTenor"));
		invoice.setInvoiceDate((String)taskVars.get("in_invoiceDate"));
		invoice.setUserCode(user);
		return invoice;
	}

	public void buyerAcceptInvoiceSubmit(long taskId, String user,
			String buyerAcceptResult) {
		System.out.println("buyerAcceptInvoiceSubmit (user = " + user
				+ "), (taskId = " + taskId + "), (buyerAcceptResult = "
				+ buyerAcceptResult + ")");
		try {
			Map<String, Object> taskVars = new HashMap<String, Object>();
			taskVars.put("out_buyerAcceptResult", buyerAcceptResult);
			taskService.complete(taskId, user, taskVars);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
