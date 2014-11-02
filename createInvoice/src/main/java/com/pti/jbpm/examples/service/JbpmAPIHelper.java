package com.pti.jbpm.examples.service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;

import org.drools.compiler.compiler.BPMN2ProcessFactory;
import org.drools.compiler.compiler.ProcessBuilderFactory;
import org.drools.core.base.MapGlobalResolver;
import org.drools.core.marshalling.impl.ProcessMarshallerFactory;
import org.drools.core.runtime.process.ProcessRuntimeFactory;
import org.jbpm.bpmn2.BPMN2ProcessProviderImpl;
import org.jbpm.marshalling.impl.ProcessMarshallerFactoryServiceImpl;
import org.jbpm.process.audit.JPAAuditLogService;
import org.jbpm.process.builder.ProcessBuilderFactoryServiceImpl;
import org.jbpm.process.instance.ProcessRuntimeFactoryServiceImpl;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.Environment;
import org.kie.api.runtime.EnvironmentName;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.persistence.jpa.JPAKnowledgeService;
import org.kie.internal.runtime.StatefulKnowledgeSession;
import org.kie.internal.task.api.InternalTaskService;

import com.pti.jbpm.examples.util.ApplicationScopedProducer;

public class JbpmAPIHelper {
	public static String[] processStateName = { "PENDING", "ACTIVE",
			"COMPLETED", "ABORTED", "SUSPENDED" };
	private static KieBase kbase = null;

	public static void readKnowledgeBase(ArrayList<String> processNames)
			throws Exception {
		System.out.println("Value of processNames : " + processNames);
		try {
			if (kbase == null) {
				ProcessBuilderFactory
						.setProcessBuilderFactoryService(new ProcessBuilderFactoryServiceImpl());
				ProcessMarshallerFactory
						.setProcessMarshallerFactoryService(new ProcessMarshallerFactoryServiceImpl());
				ProcessRuntimeFactory
						.setProcessRuntimeFactoryService(new ProcessRuntimeFactoryServiceImpl());
				BPMN2ProcessFactory
						.setBPMN2ProcessProvider(new BPMN2ProcessProviderImpl());
				KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
						.newKnowledgeBuilder();

				for (String processName : processNames) {
					kbuilder.add(
							ResourceFactory.newClassPathResource(processName),
							ResourceType.BPMN2);
				}
				kbase = kbuilder.newKnowledgeBase();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static KieBase createSimpleKnowledgeBase() {
		KieServices kieServices = KieServices.Factory.get();
		KieContainer kieContainer = kieServices.getKieClasspathContainer();
		return kieContainer.getKieBase();
	}

	public static KieSession createKnowledgeSession() {
		KieServices kieServices = KieServices.Factory.get();
		KieContainer kieContainer = kieServices.getKieClasspathContainer();
		return kieContainer.newKieSession("ksession1");
	}

	public static StatefulKnowledgeSession createSession(
			ArrayList<String> processNames) throws Exception {
		System.out.println("Value of processNames : " + processNames);
		KieSessionConfiguration config = null;
		readKnowledgeBase(processNames);
		EntityManagerFactory emf = getEntityManagerFactory();
		Environment env = KnowledgeBaseFactory.newEnvironment();
		try {
			env.set(EnvironmentName.ENTITY_MANAGER_FACTORY, emf);
			env.set(EnvironmentName.TRANSACTION_MANAGER,
					emf.createEntityManager());
			env.set(EnvironmentName.GLOBALS, new MapGlobalResolver());

			Properties properties = new Properties();
			properties
					.put("drools.processInstanceManagerFactory",
							"org.jbpm.persistence.processinstance.JPAProcessInstanceManagerFactory");
			properties
					.put("drools.processSignalManagerFactory",
							"org.jbpm.persistence.processinstance.JPASignalManagerFactory");
			config = KnowledgeBaseFactory
					.newKnowledgeSessionConfiguration(properties);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JPAKnowledgeService.newStatefulKnowledgeSession(kbase, config,
				env);
	}

	public static EntityManagerFactory getEntityManagerFactory() {
		return new ApplicationScopedProducer().produceEntityManagerFactory();
	}

	public static JPAAuditLogService getJPAAuditLogService() {
		return new JPAAuditLogService(getEntityManagerFactory());
	}
	
	public static Map<String, Object> getTaskVariables(
			InternalTaskService internalTaskService, long taskId) {
		Map<String, Object> taskVars = internalTaskService
				.getTaskContent(taskId);
		for (String key : taskVars.keySet()) {
			System.out.println("    " + key + "=" + taskVars.get(key));
		}
		return (taskVars);
	}
}
