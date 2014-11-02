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

package com.pti.jbpm.examples.service.ejbImpl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.internal.runtime.manager.cdi.qualifier.Singleton;
import org.kie.internal.runtime.manager.context.EmptyContext;

import com.pti.jbpm.examples.util.ApplicationScopedProducer;
import com.pti.jbpm.examples.util.BuyerProfileWorkItemHandler;
import com.pti.jbpm.examples.util.SellerProfileWorkItemHandler;

@Startup
@javax.ejb.Singleton
@TransactionManagement(TransactionManagementType.BEAN)
public class ProcessBean {
	@Inject
    @Singleton
    private RuntimeManager singletonManager;
	
	@Resource
    private UserTransaction ut;

    @PostConstruct
    public void configure() {
        // use toString to make sure CDI initializes the bean
        // this makes sure that RuntimeManager is started asap,
        // otherwise after server restart complete task won't move process forward 
        singletonManager.toString();
    }

    public long startProcess(String recipient) throws Exception {

        RuntimeEngine runtime = singletonManager.getRuntimeEngine(EmptyContext.get());
        KieSession ksession = runtime.getKieSession();

        long processInstanceId = -1;
        
        ut.begin();

        try {
        	ksession.getWorkItemManager().registerWorkItemHandler("getSellerProfile", 
                    new SellerProfileWorkItemHandler());
        	ksession.getWorkItemManager().registerWorkItemHandler("getBuyerProfile", 
                    new BuyerProfileWorkItemHandler());
        	
            // start a new process instance
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("userCode", recipient);
            params.put("siteCode", "PTBANK");
            params.put("companyCode", "SELLER01");
			ProcessInstance processInstance = ksession.startProcess(ApplicationScopedProducer.PROCESS_ID,
					params);
			ksession.fireAllRules();
            processInstanceId = processInstance.getId();
            
            System.out.println("Process started ... : processInstanceId = "
                    + processInstanceId);
            ut.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (ut.getStatus() == Status.STATUS_ACTIVE) {
                ut.rollback();
            }
            throw e;
        }

        return processInstanceId;
    }
    
	public void stopProcessInstance(String processInstanceId) {
		System.out.println("ProcessBean stopProcessInstance...");
		try {
			RuntimeEngine runtime = singletonManager.getRuntimeEngine(EmptyContext.get());
	        KieSession ksession = runtime.getKieSession();
	        ksession.abortProcessInstance(Long.valueOf(processInstanceId));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}