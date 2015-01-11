<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="org.kie.services.client.api.builder.RemoteRestRuntimeEngineBuilder,
org.kie.api.runtime.KieSession,
org.kie.api.task.TaskService,
org.kie.api.task.model.TaskSummary,
org.kie.services.client.api.command.RemoteRuntimeEngine,
org.kie.services.client.api.RemoteRestRuntimeEngineFactory,
org.kie.api.runtime.process.ProcessInstance,
java.net.URL,
java.util.HashMap,
java.util.Map, java.util.List" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%!
public String executeProcess(){
	String result = null;
	RemoteRuntimeEngine runtime = null;
		try {
			String user = "MDBANK:MDBANK:ADMINISTRATOR";
            
            // start a new process instance
            runtime = getRuntimeManager(0);
            KieSession ksession = runtime.getKieSession();
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("employee", user);
            params.put("reason", "Yearly performance evaluation");
            ProcessInstance processInstance = ksession.startProcess("evaluation", params);
            long processInstanceId = processInstance.getId();

            // user executes his own performance evaluation
            runtime = getRuntimeManager(processInstanceId);
            TaskService taskService = runtime.getTaskService();
            TaskSummary task1 = getTaskByProcessInstanceId(taskService.getTasksAssignedAsPotentialOwner(user, "en-UK"), processInstanceId);
            printTask(user, task1);
            taskService.start(task1.getId(), user);
            taskService.complete(task1.getId(), user, null);

            // user, part of the "PM" group, executes a performance evaluation
            TaskSummary task2 = getTaskByProcessInstanceId(taskService.getTasksAssignedAsPotentialOwner(user, "en-UK"), processInstanceId);
            printTask(user, task2);
            taskService.claim(task2.getId(), user);
            taskService.start(task2.getId(), user);
            taskService.complete(task2.getId(), user, null);
            
            // user, part of the "HR" group, delegates a performance evaluation
            TaskSummary task3b = getTaskByProcessInstanceId(taskService.getTasksAssignedAsPotentialOwner(user, "en-UK"), processInstanceId);
            printTask(user, task3b);
            taskService.claim(task3b.getId(), user);
            taskService.start(task3b.getId(), user);
            taskService.complete(task3b.getId(), user, null);
			
			runtime = null;
            print("Process instance completed");
    		result = "Process instance completed";
        } catch (Throwable t) {
            t.printStackTrace();
            result = "Process instance error";
        }
        return result;
	}
	
	private RemoteRuntimeEngine getRuntimeManager(long processInstanceId) throws Exception{
        RemoteRestRuntimeEngineBuilder builder = RemoteRestRuntimeEngineFactory.newBuilder();
			builder.addDeploymentId("org.jbpm:Evaluation:1.0").addUserName("wfe")
					.addPassword("wfe").addUrl(new URL("http://ptapp03:9080/scf/wfe")).addTimeout(60);
			if (processInstanceId > 0) {
				builder.addProcessInstanceId(processInstanceId);
			}
			RemoteRuntimeEngine runtime = builder.build();
        return runtime;
    }
    
    private TaskSummary getTaskByProcessInstanceId(List<TaskSummary> tasks, long processInstanceId){
    	if(null != tasks){
    		for(TaskSummary task: tasks){
    			if(processInstanceId == task.getProcessInstanceId()){
    				return task;
    			}
    		}
    		return tasks.get(0);
    	}
    	return null;
    }
    
    private void printTask(String user, TaskSummary task){
    	String msg = user + " of executing HR's task <" + task.getName() + ">: [taskId=" + task.getId() + "; desc=" + task.getDescription() + "; processInstanceId=" + task.getProcessInstanceId()+ "]";
    	print(msg);
    }
    
    private void print(String msg){
    	System.out.println("testKieRemote.jsp >>> "+msg);
    }
%>

<%
long start = System.currentTimeMillis();
String result = ">>> " + executeProcess();
long cost = System.currentTimeMillis() - start;
result += ": Total cost="+cost+" millis seconds";
%>
Executing the test process: evaluation. <%= result%>
</body>
</html>
