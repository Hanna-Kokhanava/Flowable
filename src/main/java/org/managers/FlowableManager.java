package org.managers;

import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created on 31.01.2019
 */
public class FlowableManager {

    private ProcessEngine processEngine;
    private RepositoryService repositoryService;
    private RuntimeService runtimeService;

    private ProcessDefinition processDefinition;
    private ProcessInstance processInstance;

    /**
     * Inits Process Engine and create services
     */
    public void initEngineAndCreateServices() {
        processEngine = getProcessEngineInstance();
        repositoryService = processEngine.getRepositoryService();
        runtimeService = processEngine.getRuntimeService();
    }

    /**
     * Returns singleton instance
     *
     * @return {@link ProcessEngine} instance
     */
    public ProcessEngine getProcessEngineInstance() {
        return Objects.isNull(processEngine)
                ? ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault().buildProcessEngine()
                : processEngine;
    }

    /**
     * Returns model
     *
     * @return {@link BpmnModel} instance
     */
    public BpmnModel getModel() {
        return repositoryService.getBpmnModel(processDefinition.getId());
    }

    /**
     * Creates Deployment
     *
     * @return {@link Deployment} instance
     */
    public Deployment createDeployment() {
        return repositoryService.createDeployment()
                .addClasspathResource("flowable-example.bpmn20.xml")
                .deploy();
    }

    /**
     * Creates Process Definition
     *
     * @param deployment {@link Deployment}
     */
    public void createProcessDefinition(Deployment deployment) {
        processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();
        System.out.println("Found Process definition : " + processDefinition.getName());
    }

    /**
     * Starts {@link ProcessInstance} in the latest version of the process definition with the given key
     */
    public void startProcessInstance() {
        //TODO actually come from form as process variables
        Map<String, Object> vars = new HashMap<>();
        vars.put("employee", "Hanna Kokhanava");
        processInstance = runtimeService.startProcessInstanceByKey(processDefinition.getKey(), vars);
    }

    /**
     * Complete list of 'manager' group tasks with 'approved' status to make it move on
     */
    public void completeActualTasksForGroup(String candidateGroupName) {
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup(candidateGroupName).list();

        if (tasks.isEmpty()) {
            System.out.println("There are no tasks to complete for " + candidateGroupName + " group");
        } else {
            System.out.println("Managers group has " + tasks.size() + " tasks : ");
            Map<String, Object> variables = new HashMap<>();
            variables.put("approved", true);

            for (Task task : tasks) {
                System.out.println("Task name : " + task.getName());
                System.out.println("Employee name : " + taskService.getVariable(task.getId(), "employee"));

                //TODO it triggers next steps after UserTask completion, in this case - Service Task,
                // which is implemented using JavaDelegate and connected via xml configuration
                taskService.complete(task.getId(), variables);
            }
        }
    }
}
