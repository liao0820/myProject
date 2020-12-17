package com.web.oa.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;

import com.web.oa.pojo.BaoxiaoBill;

public interface WorkFlowService {

	void saveNewDeploye(InputStream in, String filename);

	List<Deployment> findDeploymentList();
	
	List<ProcessDefinition> findProcessDefinitionList();
	
	public void saveStartProcess(long baoxiaoId,String username);
	
	public List<Task> findTaskListByName(String name);
	
	public BaoxiaoBill findBaoxiaoBillByTaskId(String taskId);
	
	public List<Comment> findCommentByTaskId(String taskId);
	
	public List<String> findOutComeListByTaskId(String taskId);
	
	public void saveSubmitTask(long id,String taskId,String comemnt,String outcome,String username);

	ProcessDefinition findProcessDefinitionByTaskId(String taskId);

	Map<String, Object> findCoordingByTask(String taskId);

	InputStream findImageInputStream(String deploymentId, String imageName);

	Task findTaskByBussinessKey(String bUSSINESS_KEY);

	List<Comment> findCommentByBaoxiaoBillId(long id);

	void deleteProcessDefinitionByDeploymentId(String deploymentId);

	
}
