package com.pti.jbpm.examples.service;

import java.util.Map;

import org.jbpm.services.task.utils.ContentMarshallerHelper;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Content;
import org.kie.api.task.model.Task;
import org.kie.api.task.model.TaskData;

public class JbpmAPIHelper {
	public static String[] processStateName = { "PENDING", "ACTIVE",
			"COMPLETED", "ABORTED", "SUSPENDED" };

	public static Map<String, Object> getTaskVariables(TaskService taskService,
			long taskId) {
		Task userTask = taskService.getTaskById(taskId);
		TaskData taskData = userTask.getTaskData();
		Content content = taskService.getContentById(taskData
				.getDocumentContentId());

		@SuppressWarnings("unchecked")
		Map<String, Object> taskVars = (Map<String, Object>) ContentMarshallerHelper
				.unmarshall(content.getContent(), null);
		return (taskVars);
	}
}
