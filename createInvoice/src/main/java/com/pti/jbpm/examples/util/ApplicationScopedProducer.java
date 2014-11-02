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

package com.pti.jbpm.examples.util;

import org.jbpm.services.task.identity.DefaultUserInfo;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.manager.RuntimeEnvironment;
import org.kie.api.runtime.manager.RuntimeEnvironmentBuilder;
import org.kie.api.task.UserGroupCallback;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.manager.cdi.qualifier.PerProcessInstance;
import org.kie.internal.runtime.manager.cdi.qualifier.PerRequest;
import org.kie.internal.runtime.manager.cdi.qualifier.Singleton;
import org.kie.internal.task.api.UserInfo;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

@ApplicationScoped
public class ApplicationScopedProducer {
	public static final String PERSISTENCE_UNIT_NAME = "createInvoice_pu";
	public static final String PROCESS_FILE_NAME = "createInvoice.bpmn2";
	public static final String PROCESS_ID = "com.pti.bpm.CreateInvoice";

    @PersistenceUnit(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManagerFactory emf;

    @Produces
    public EntityManagerFactory produceEntityManagerFactory() {
    	System.out.println("ApplicationScopedProducer: creating EntityManagerFactory.");
        if (this.emf == null) {
        	//HHH000284: Error closing connection: java.sql.SQLException: You cannot set auto commit during a managed transaction!
        	//this.emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            try {
				this.emf = (EntityManagerFactory) new javax.naming.InitialContext().lookup("java:/jBPMInvoiceManagerFactory");
			} catch (NamingException e) {
				e.printStackTrace();
			}
        }
        return this.emf;
    }

    @Produces
    @Singleton
    @PerProcessInstance
    @PerRequest
    public RuntimeEnvironment produceEnvironment(EntityManagerFactory emf) {
    	System.out.println("ApplicationScopedProducer: creating produceEnvironment.");
        RuntimeEnvironment environment = RuntimeEnvironmentBuilder.Factory.get()
                .newDefaultBuilder()
                .entityManagerFactory(emf)
                .addAsset(
                        ResourceFactory
                                .newClassPathResource(PROCESS_FILE_NAME),
                        ResourceType.BPMN2).get();
        return environment;
    }

    @Produces
    public UserInfo produceUserInfo() {
    	System.out.println("ApplicationScopedProducer: creating produceUserInfo.");
        // default implementation will load userinfo.properties file on the classpath
        return new DefaultUserInfo(true);
    }

    @Produces
    public UserGroupCallback produceUserGroupCallback() {
        return new CustomUserGroupCallback();
    }

}
